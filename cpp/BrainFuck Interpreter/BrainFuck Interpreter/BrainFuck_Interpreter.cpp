// BrainFuck Interpreter.cpp : This file contains the 'main' function. Program execution begins and ends there.
//
#include "Interpreter.h"
#include <iostream>
#include <cstdio> // for simple file io, and C languages: f file commands
/*
    USES EXTENDED TYPE I BRAINFUCK
    REF: https://esolangs.org/wiki/Extended_Brainfuck
*/

void runFile(std::string str) {
    // Test.bf
    Interpreter it{ InputType::INPUT_STRING }; // console input{true} or file text input {false} character after ',' eg. ,A
    FILE *file = fopen(str.c_str(), "r");
    int temp;
    if (file == NULL) exit(-1);
    
    while ((temp = fgetc(file)) != EOF) {
        it.interpret((char)temp);
    }
    //it.printDebug();
    fclose(file);
}

int main(int argc, char *argv[]){
    if (argc > 1) {
        for (int i = 1; i < argc; i++){
            std::cout << "Running code for program: " << i << ", " << argv[i] << '\n';
            runFile(argv[i]);
        }
    } else {
        runFile("Test.bf");
    }
    std::cout << "\nProgram End\n";
    return (0);
}