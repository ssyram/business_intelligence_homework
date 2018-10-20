//
//  caller.h
//  mix_type_simmilarity_calculation
//
//  Created by 潇湘夜雨 on 2018/10/19.
//  Copyright © 2018 ssyram. All rights reserved.
//

#ifndef caller_h
#define caller_h

#include "type_calculation.h"
#include <iostream>
#include <memory>
using std::shared_ptr;
using std::cout;
using std::cin;
using std::getline;
using std::endl;
using std::pair;

vector<string> split(const string &s, const string &divider) {
    vector<string> r;
    size_t b = 0, e = 0;
    for (; (e = s.find(divider, b)) != string::npos; b = e + divider.length())
        r.push_back(s.substr(b, e - b));
    r.push_back(s.substr(b, -1));
    return r;
}

string &trimSpace(string &s) {
    size_t b = 0, e = s.length();
    while (s[b] == ' ') ++b;
    while (e > b && s[e - 1] == ' ') --e;
    s = s.substr(b, e - b);
    
    return s;
}

vector<shared_ptr<meta_data>> getHeadInfoVector() {
    vector<shared_ptr<meta_data>> hv;
    string s;
    cout << "Please input the type of each line: (divided by ',' )" << endl;
    cout << "Choices are: symmetric binary(/0), asymmetric binary(/1), numeric(/2), norminal(/3), ordinary(/4)" << endl;
    getline(cin, s);
    auto r = split(s, "|");
    for (string &s: r) {
        trimSpace(s);
        if (s == "symmetric binary" || s == "0")
            hv.push_back(shared_ptr<symmetric_binary_meta_data>(new symmetric_binary_meta_data()));
        else if (s == "asymmetric binary" || s == "1")
            hv.push_back(shared_ptr<asymmetric_binary_meta_data>(new asymmetric_binary_meta_data()));
        else if (s == "numeric" || s == "2")
            hv.push_back(shared_ptr<numeric_meta_data>(new numeric_meta_data()));
        else if (s == "norminal" || s == "3")
            hv.push_back(shared_ptr<norminal_meta_data>(new norminal_meta_data()));
        else if (s == "ordinary" || s == "4")
            hv.push_back(shared_ptr<ordinary_meta_data>(new ordinary_meta_data()));
        else
            throw string("Please input correct head info.");
    }
    
    return hv;
}

pair<double**, int> calculate(const vector<shared_ptr<meta_data>> &hv, int n) {
    double** r = new double*[n];
    r[0] = new double[n * (n - 1) / 2];
    for (int i = 1; i < n; ++i)
        r[i] = r[i - 1] + (i - 1);
    for (int i = 1; i < n; ++i)
        for (int j = 0; j < i; ++j) {
            double dif = 0;
            int count = 0;
            for (auto &d: hv) {
                difference td = d->cal(i, j);
                if (td) {
                    dif += *td;
                    ++count;
                }
            }
            if (count == 0)
                r[i][j] = 1;
            else
                r[i][j] = dif / count;
        }
    
    return std::make_pair(r, n);
}

void run() {
    auto hv = getHeadInfoVector();
    string s;
    int lineNum = 0;
    cout << "Please input data, which should be divided by ','. (If it's binary data, please input 0 / 1 only)" << endl;
    
    cout << "Line " << ++lineNum << ": ";
    getline(cin, s);
    
    while (!s.empty()) {
        vector<string> r = split(s, "|");
        if (r.size() != hv.size())
            throw string("data in a line should have the same amount with head info.");
        for (int i = 0; i < hv.size(); ++i)
            hv[i]->add(trimSpace(r[i]));
        
        cout << "Line " << ++lineNum;
        getline(cin, s);
    }
    if (lineNum < 3)
        throw string("Please input at least two lines of data.");
    
    auto rpair = calculate(hv, lineNum - 1);
    size_t length = rpair.second;
    auto r = rpair.first;
    for (int i = 0; i < length; ++i) {
        cout << "| ";
        for (int j = 0; j < length; ++j) {
            if (i == j)
                cout << setw(8) << "1";
            else if (i > j)
                cout << setw(8) << (1 - r[i][j]);
            else
                cout << setw(8) << (1 - r[j][i]);
            cout << " ";
        }
        cout << " |" << endl;
    }
            
}


#endif /* caller_h */
