#include <iostream>
#include <string>
#include <vector>
#include <algorithm>

#include <map>
#include <unordered_map>

int countConstruct(const std::string &test, const std::vector<std::string> list, std::unordered_map<std::string, int> &memo){
    if(memo.contains(test)){
        return memo[test];
    }
    if(test.size() == 0) return 1;
    int count = 0;
    
    for(int i = 0; i < test.size(); i++){
        const std::string newTest = test.substr(0, i + 1);
        if( std::find(list.begin(), list.end(), newTest) != list.end() ) //sub in list
           count += countConstruct(test.substr(i + 1, test.size() - 1), list, memo);
    }
    memo[test] = count;
    return count;
}

std::vector<std::vector<std::string>> allConstruct(const std::string &test, 
                                                 const std::vector<std::string> list,
                                                 std::unordered_map<std::string, std::vector<std::vector<std::string>> > &memo
                                                 ){
    if(memo.contains(test)){
        return memo[test];
    }
    if(test.size() == 0){
        return std::vector<std::vector<std::string>> {{}};
    }
    std::vector<std::vector<std::string>> res{};
    for(int i=0; i < test.size(); i++){
        const std::string newTest = test.substr(0, i + 1);
        if( std::find(list.begin(), list.end(), newTest) != list.end() ){ //newTest in list
            auto subRes = allConstruct(test.substr(i + 1, test.size() - 1), list, memo);
            for(auto &v : subRes){ // put newTest in front of every subvector
                v.insert(v.begin(), newTest);
            }
            res.insert(res.end(), subRes.begin(), subRes.end());
        }
    }
    memo[test] = res;
    return res;
}


void print2d(std::vector<std::vector<std::string>> p){
    for(auto &row : p){
        for(auto &col : row){
            std::cout << col << ' ';
        }
        std::cout << '\n';
    }
}

void countTest(){
    std::vector<std::string> vect;
    std::unordered_map<std::string, int> mp{};

    mp.clear();
    vect = {"bo", "rd", "ate", "t", "ska", "sk", "boar"};
    std::cout << countConstruct("skateboard", vect, mp) << '\n';
    
    mp.clear();
    vect = {"ab", "abc", "cd", "def", "abcd"};
    std::cout << countConstruct("abcdef", vect, mp) << '\n';
    
    mp.clear();
    vect = {"a", "p", "ent", "enter", "ot", "o", "t"};
    std::cout << countConstruct("enterapotentpot", vect, mp) << '\n';
    
    mp.clear();
    vect = {"purp", "p", "ur", "le", "purpl"};
    std::cout << countConstruct("purple", vect, mp) << '\n';
    
    mp.clear();
    vect = {"e", "ee", "eee", "eeee", "eeeee", "eeeeee"};
    std::cout << countConstruct("eee", vect, mp) << '\n';
}

void allConstructTest(){
    std::unordered_map<std::string, std::vector<std::vector<std::string>> > mp{};
    std::vector<std::string> vect;
    
    mp.clear();
    vect = {"ab", "abc", "cd", "def", "abcd", "ef", "c"};
    print2d(allConstruct("abcdef", vect, mp));
    
    mp.clear();
    vect = {"purp", "p", "ur", "le", "purpl"};
    print2d(allConstruct("purple", vect, mp));
    
    mp.clear();
    vect = {"e", "ee", "eee", "eeee", "eeeee", "eeeeee"};
    print2d(allConstruct("eee", vect, mp));
}


int main() {   
    std::cout<<"Hello World\n";
    countTest();
    allConstructTest();
    return 0;
}
