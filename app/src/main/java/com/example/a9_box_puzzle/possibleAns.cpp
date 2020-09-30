#include <bits/stdc++.h>
using namespace std;
void print(string &s) {
    for(int i = 1; i < 10; ++i) {
        if(s[i] == '9') cout << "  ";
        else cout << s[i] << " ";
        if(i%3 == 0) cout << endl;
    }
    cout << endl;
}

int main() {
    int i, n;
    string s, t;
    char c;
    queue<string> q;
    map<string, pair<string, int>> mp;
    q.push("0123456789");
    mp["0123456789"] = {"0123456789", 0};
    while(!q.empty()) {
        s = q.front();
        q.pop();
        n = mp[s].second;
        for(i = 1; s[i] != '9'; ++i);
        if(i > 3) {
            t = s;
            c = t[i];
            t[i] = t[i-3];
            t[i-3] = c;
            if(mp.find(t) == mp.end()) {
                q.push(t);
                mp[t] = {s, n+1};
            }
        }
        if(i < 7) {
            t = s;
            c = t[i];
            t[i] = t[i+3];
            t[i+3] = c;
            if(mp.find(t) == mp.end()) {
                q.push(t);
                mp[t] = {s, n+1};
            }
        }
        if(i%3 != 1) {
            t = s;
            c = t[i];
            t[i] = t[i-1];
            t[i-1] = c;
            if(mp.find(t) == mp.end()) {
                q.push(t);
                mp[t] = {s, n+1};
            }
        }
        if(i%3 != 0) {
            t = s;
            c = t[i];
            t[i] = t[i+1];
            t[i+1] = c;
            if(mp.find(t) == mp.end()) {
                q.push(t);
                mp[t] = {s, n+1};
            }
        }
    }
    cin >> s;
    s = '0' + s;
    if(mp.find(s) == mp.end())
        cout << "Invalid Case";
    else {
        cout << "Minimum Moves: " << mp[s].second << endl;
        while(s != mp[s].first) {
            print(s);
            s = mp[s].first;
        }
        print(s);
    }
    return 0;
}