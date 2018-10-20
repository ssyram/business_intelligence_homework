//
//  type.h
//  mix_type_simmilarity_calculation
//
//  Created by 潇湘夜雨 on 2018/10/19.
//  Copyright © 2018 ssyram. All rights reserved.
//

#ifndef type_h
#define type_h

#include <optional>
#include <variant>
#include <string>
#include <vector>
#include <cmath>
#include <unordered_map>
#include <iostream>
#include <iomanip>
using std::setw;
using std::cout;
using std::endl;
using std::unordered_map;
using std::vector;
using std::string;
using std::optional;
using std::variant;
using std::visit;
using std::make_optional;

//1. symmetric binary attribute and norminal attribute: 1 - same, 0 - difference.
//2. asymmetric binary attribute: 1 - both yes, 0 - difference, no calculation - both no.
//3. numeric attribute: $\frac {|x_{if} - x_{jf}|} {max(x_f) - min(x_f)}$
//4. ordinary attribute: rank them, give each of them a symbol of $r_{if}$, let $z_{if} = \frac {r_{if} - 1} {M_f - 1}$, where $M_f$ is the number of elements in attribute $f$.

//bool check_for_binary(column_data col) {
//    for (auto data: col)
//        if (data && !visit([](auto&& arg) {
//            using T = std::decay_t<decltype(arg)>;
//            if constexpr (std::is_same_v<T, string>)
//                return false;
//            if constexpr (std::is_same_v<T, double>)
//                if (arg == 0 || arg == 1)
//                    return true;
//                else
//                    return false;
//        }, *data))
//            return false;
//
//    return true;
//}

enum class type {
    symmetric_binary,
    asymmetric_binary,
    norminal,
    numeric,
    ordinary,
};

using data_t = variant<string, double, int>;
using data = optional<data_t>;
using column_data = vector<data>;
using difference = optional<double>;


class meta_data {
protected:
    bool not_ok_one_two(int line_one, int line_two) const {
        return line_one >= col.size() || line_two >= col.size() || !col[line_one] || !col[line_two];
    }
    // d is not empty
    virtual void addData(const string &d) = 0;
    // line_one and line_two is good and at these positions, it's not null
    virtual difference calDif(int line_one, int line_two) const = 0;
    type t;
    column_data col;
public:
    meta_data(type t): t(t) {}
    virtual ~meta_data() = default;
    
    type getType() { return t; }
    void add(const string &d) {
        if (d.empty()) {
            col.push_back(std::nullopt);
            return;
        }
        
        addData(d);
    }
    difference cal(int line_one, int line_two) const {
        if (not_ok_one_two(line_one, line_two)) return std::nullopt;
        
        return calDif(line_one, line_two);
    }
};

template <class T> struct always_false: std::false_type {};

template<class... Ts> struct overloaded : Ts... { using Ts::operator()...; };
template<class... Ts> overloaded(Ts...) -> overloaded<Ts...>;

class norminal_meta_data: public meta_data {
public:
    norminal_meta_data(): meta_data(type::norminal) {}
    void addData(const string &d) override {
        col.push_back(make_optional(d));
    }
    difference calDif(int line_one, int line_two) const override {
        data_t o = *col[line_one], t = *col[line_two];
        return visit(overloaded {
            [] (string &s1, string &s2) { return make_optional((double) (s1 != s2)); },
            [] (auto& s1, auto& s2) { cout << s1 << ", " << s2 << endl; assert(false); return make_optional(1.0); }
        }, o, t);
//        return visit([] (auto&& one, auto&& two) {
//            using T = std::decay_t<decltype(one)>;
//            if constexpr (std::is_same_v<T, string>)
//                return make_optional((double) (one == two));
//            else return std::nullopt;
//        }, *col[line_one], *col[line_two]);
    }
};

class ordinary_meta_data;

class numeric_meta_data: public meta_data {
    friend class ordinary_meta_data;
    bool first = true;
    double max, min;
    
    double string2Double(const string &s) const {
        bool minus = false;
        int decimal = 0;
        double r = 0;
        for (char c: s)
            if (c >= '0' && c <= '9')
                if (!decimal)
                    r = r * 10 + (c - '0');
                else
                    r += (c - '0') * pow(10, decimal--);
            else if (c == '.' && !decimal)
                --decimal;
            else if (c == '-' && s[0] == '-' && !minus)
                minus = true;
            else
                throw string("not a valid numeric value.");
        
        return minus ? -r : r;
    }
    double getValue(const data &d) const {
        // d cannot be nullopt
        return visit(overloaded {
            [] (const double &d) { return d; },
            [] (const auto &) { return (double)0; },
        }, *d);
    }
public:
    numeric_meta_data(): meta_data(type::ordinary) {}
    void addData(const string &d) override {
        double nd = string2Double(d);
        
        if (first) {
            max = min = nd;
            first = false;
        }
        else {
            if (max < nd) max = nd;
            if (min > nd) min = nd;
        }
        
        col.push_back(make_optional(nd));
    }
    difference calDif(int line_one, int line_two) const override {
        if (max == min) return make_optional(0);
        double o = getValue(col[line_one]), t = getValue(col[line_two]);
        return make_optional(abs(o - t) / (max - min));
    }
};

class ordinary_meta_data: public meta_data {
    mutable bool first = true;
    mutable vector<optional<double>> v; // the true value
    int string2Int(const string &s) {
        bool minus = false;
        int r = 0;
        for (char c: s)
            if (c >= '0' && c <= '9')
                r = r * 10 + (c - '0');
            else if (c == '-' && s[0] == '-' && !minus)
                minus = true;
            else throw string("not a valid ordinary number.");
        
        return minus ? -r : r;
    }
    void fillValueVector() const {
        // firstly, m is a set
        unordered_map<int, int> m;
        vector<int> l;
        for (data i: col)
            if (i)
                visit(overloaded {
                    [&m] (int &i) { m.insert(std::make_pair(i, 0)); },
                    [] (auto &) {}
                }, *i);
        for (std::pair<const int, int> &p: m)
            l.push_back(p.first);
        std::sort(l.begin(), l.end());
        size_t max = l.size() - 1;
        // here m records the exact position of numbers in sorted l
        for (int i = 0; i < l.size(); ++i)
            m[l[i]] = i;
        
        for (data d: col)
            if (d)
                visit(overloaded {
                    [&m, this, max] (int i) {
                        v.push_back(make_optional((double)m[i] / max));
                        cout << "m[" << i << "]: " << m[i] << endl;
                        cout << "max: " << max <<endl;
                    },
                    [] (auto &) {}
                }, *d);
            else v.push_back(std::nullopt);
    }
public:
    ordinary_meta_data(): meta_data(type::ordinary) {}
    void addData(const string &d) override {
        int i = string2Int(d);
        
        col.push_back(make_optional(i));
    }
    difference calDif(int line_one, int line_two) const override {
        if (first) {
            fillValueVector();
            first = false;
        }
        
        return abs(*v[line_one] - *v[line_two]);
    }
};

class symmetric_binary_meta_data: public meta_data {
protected:
    int getData(const data& d) const {
        return visit([](auto&& arg) {
            using T = std::decay_t<decltype(arg)>;
            if constexpr (std::is_same_v<T, string>)
                return -1;
            else if constexpr (std::is_same_v<T, double>)
                return -1;
            else if constexpr (std::is_same_v<T, int>)
                return arg;
            else {
                static_assert(always_false<T>::value, "this will not happened.");
                return -1;
            }
        }, *d);
    }
    symmetric_binary_meta_data(type t): meta_data(t) {}
public:
    symmetric_binary_meta_data(): meta_data(type::symmetric_binary) {}
    void addData(const string &d) override {
        if (d.size() != 1 || !(d[0] == '0' || d[0] == '1'))
            return;
//        if (d && !visit([](auto&& arg) {
//            using T = std::decay_t<decltype(arg)>;
//            if constexpr (std::is_same_v<T, string> || std::is_same_v<T, double>)
//                return false;
//            else
//                if (arg == 0 || arg == 1)
//                    return true;
//                else
//                    return false;
//        }, *d))
//            return;
        
        col.push_back(make_optional((int)(d[0] - '0')));
    }
    difference calDif(int line_one, int line_two) const override {
        return (double) (getData(col[line_one]) != getData(col[line_two]));
    }
};

class asymmetric_binary_meta_data: public symmetric_binary_meta_data {
public:
    asymmetric_binary_meta_data(): symmetric_binary_meta_data(type::asymmetric_binary) {}
    difference calDif(int line_one, int line_two) const override {
        int o = getData(col[line_one]), t = getData(col[line_two]);
        if (o == 0 && t == 0)
            return std::nullopt;
        return (double) (o != t);
    }
};

#endif /* type_h */
