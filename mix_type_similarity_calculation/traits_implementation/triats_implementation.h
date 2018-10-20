//
//  triats_implementation.h
//  mix_type_simmilarity_calculation
//
//  Created by 潇湘夜雨 on 2018/10/20.
//  Copyright © 2018 ssyram. All rights reserved.
//

#ifndef triats_implementation_h
#define triats_implementation_h

#define debug
#ifdef debug
#include <iostream>
#endif
#include <variant>
#include <optional>
#include <string>
#include <vector>
#include <cmath>
#include <unordered_map>
using std::unordered_map;
using std::variant;
using std::optional;
using std::make_optional;
using std::nullopt;
using std::string;
using std::vector;
using std::visit;

using data_t = variant<int, double, string>;
using data = optional<data_t>;
using column_data = vector<data>;
using difference = optional<double>;

struct function_table_for_meta_data {
    void *(*move_constructor)(void *);
    data (*constructData)(void *, const string &);
    void (*preprocess)(void *, const column_data &);
    difference (*calNotNullDif)(void *, const data_t &, const data_t &);
    void (*destructor)(void *);
};

template <typename T>
function_table_for_meta_data real_table = {
    [] (void *obj) -> void * {
        return new T(*static_cast<T*>(obj));
    },
    [] (void *obj, const string &s) -> data {
        return static_cast<T*>(obj)->constructData(s);
    },
    [] (void *obj, const column_data &col) {
        static_cast<T*>(obj)->preprocess(col);
    },
    [] (void *obj, const data_t &data_one, const data_t &data_two) {
        return static_cast<T*>(obj)->calNotNullDif(data_one, data_two);
    },
    [] (void *obj) {
        delete static_cast<T*>(obj);
    }
};

class meta_data {
    void *obj;
    function_table_for_meta_data *table;
    column_data col;
public:
    template <typename T>
    meta_data(const T& obj): obj(new T(obj)), table(&real_table<T>) {}
    template <typename T>
    meta_data(T&& obj): obj(new T(obj)), table(&real_table<T>) {}
    ~meta_data() {
        table->destructor(obj);
    }
    
    void addData(const string &s) {
        if (s.empty()) col.push_back(nullopt);
        
        col.push_back(table->constructData(obj, s));
    }
    difference calDif(size_t line_one, size_t line_two) {
        if (line_one >= col.size() || line_two >= col.size())
            throw string("line number exceeded the boundary of data.");
        if (!(col[line_one] && col[line_two])) return nullopt;
        
        table->preprocess(obj, col);
        
        return table->calNotNullDif(obj, *col[line_one], *col[line_two]);
    }
};

template <class T> struct always_false: std::false_type {};

template<class... Ts> struct overloaded : Ts... { using Ts::operator()...; };
template<class... Ts> overloaded(Ts...) -> overloaded<Ts...>;

class norminal_meta_data {
public:
    data constructData(const string &s) {
        return make_optional(s);
    }
    void preprocess(const column_data&) {}
    difference calNotNullDif(const data_t &data_one, const data_t &data_two) {
        return visit(overloaded {
            [] (string &s1, string &s2) {
                return make_optional((double) (s1 != s2));
            },
            [] (auto &, auto &) {
                assert(false);
                return make_optional(1.0);
            }
        }, data_one, data_two);
    }
};

class numeric_meta_data {
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
    double getValue(const data_t &d) const {
        // d cannot be nullopt
        return visit(overloaded {
            [] (const double &d) { return d; },
            [] (const auto &) { return (double)0; },
        }, d);
    }
public:
     data constructData(const string &s) {
         double nd = string2Double(s);
         
         if (first) {
             max = min = nd;
             first = false;
         }
         else {
             if (max < nd) max = nd;
             if (min > nd) min = nd;
         }
         
         return make_optional(nd);
     }
     void preprocess(const column_data &col) {
     }
     difference calNotNullDif(const data_t &data_one, const data_t &data_two) {
         if (max == min) return make_optional(0);
         return make_optional(abs(getValue(data_one) - getValue(data_two)) - (max - min));
     }
};

class ordinary_meta_data {
    mutable bool first = true;
    mutable unordered_map<int, double> m;
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
    void fillValueMap(const column_data &col) const {
        // firstly, m is a set
        vector<int> l;
        for (data i: col)
            if (i)
                visit(overloaded {
                    [this] (int &i) { m.insert(std::make_pair(i, 0)); },
                    [] (auto &) {}
                }, *i);
        for (std::pair<const int, double> &p: m)
            l.push_back(p.first);
        std::sort(l.begin(), l.end());
        size_t max = l.size() - 1;
        // here m records the exact position of numbers in sorted l
        for (int i = 0; i < l.size(); ++i)
            m[l[i]] = (double) i / max;
        
//        for (data d: col)
//            if (d)
//                visit(overloaded {
//                    [&m, this, max] (int i) {
//                        v.push_back(make_optional((double)m[i] / max));
////                        cout << "m[" << i << "]: " << m[i] << endl;
////                        cout << "max: " << max <<endl;
//                    },
//                    [] (auto &) {}
//                }, *d);
//            else v.push_back(std::nullopt);
    }
public:
     data constructData(const string &s) {
         int i = string2Int(s);
         
         return make_optional(i);
     }
     void preprocess(const column_data &col) {
         if (!first) return;
         
         fillValueMap(col);
         first = false;
     }
     difference calNotNullDif(const data_t &data_one, const data_t &data_two) {
         return visit(overloaded {
             [this] (int i1, int i2) {
                 return make_optional(abs(m[i1] - m[i2]));
             },
             [] (auto, auto) {assert(false); return make_optional(1.0);}
         }, data_one, data_two);
     }
};

class symmetric_binary_meta_data {
protected:
    int getValue(const data_t &d) const {
        return visit(overloaded {
            [] (int &i) { return i; },
            [] (auto &) { assert(false); return -1; }
        }, d);
    }
public:
     data constructData(const string &s) {
         if (s.size() != 1 || !(s[0] == '0' || s[0] == '1'))
             throw string("not a valid value for binary data.");
         
         return make_optional((int) (s[0] - '0'));
     }
     void preprocess(const column_data &col) {
     }
     virtual difference calNotNullDif(const data_t &data_one, const data_t &data_two) const {
         return make_optional((double) (getValue(data_one) != getValue(data_two)));
     }
};

class asymmetric_binary_meta_data: public symmetric_binary_meta_data {
public:
     difference calNotNullDif(const data_t &data_one, const data_t &data_two) const override {
         int o = getValue(data_one), t = getValue(data_two);
         
         if (o == t && o == 0)
             return nullopt;
         
         return (double) (o != t);
     }
};

#endif /* triats_implementation_h */
