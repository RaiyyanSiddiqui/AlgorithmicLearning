#include "Interpreter.h"
#include <iostream> // for debug printing

Interpreter::Interpreter(InputType inputT) {
	inputType = inputT;
	if (inputType == InputType::INPUT_STRING) {
		std::cout << "Input String: ";
		std::cin >> inputString;
	}
}

void Interpreter::printDebug() {
	std::cout << "\nInputSize: " << inputString.size() << '\n';
	std::cout << "Pointer: " << pointer << '\n';
	std::cout << "Depth: " << depth << '\n';
	std::cout << "Mode: " << (int) mode << '\n';
	std::cout << "\nMEMORY:\n";
	for(auto val : memory){
		std::cout << (unsigned int)val << " | ";
	}
	std::cout << "\n\nSTARTCODE: \n" << codeText << "ENDCODE" << '\n'; // flushing cause it might be massive, and bottleneck
}
void Interpreter::interpret(const char& character) {
	codeText += character;
	while(programCounter < codeText.size())
		eval();
}

void Interpreter::gotoPreviousBrace() {
	int64_t i; // full uint32_t plus negatives
	depth = 0;
	for (i = programCounter; i >= 0; i--) {
		//std::cout << codeText[(unsigned int)i] << ' ' << depth;
		if (codeText[(unsigned int)i] == ']') { depth++; } else if (codeText[(unsigned int)i] == '[') {
			if (--depth == 0)
				break;//found start
		} else { continue; }
	}
	if (i < 0) {
		std::cout << "CurrentLocation: " << programCounter << '\n' << codeText;
		std::cout << "\nERROR, MALFORMED CODE: NON MATCHING BRACES";
	} else { 
		// set programCounter to i
		programCounter = (uint32_t)i;
	}
}

void Interpreter::eval() {
	//std::cout << '\n' << character << "--" << (int)mode <<" BB\n";
	//std::cout << std::endl;
	//std::cin;
	/*if(inputString.size() == 0)
		printDebug();*/
	std::string inp; // temporary variable for input
	if (mode == Mode::READ_CHAR) {
			memory[pointer] = (char)codeText[programCounter];
			mode = Mode::NORMAL; 
			programCounter++;
			return;
	} else if (mode == Mode::SKIP_BRACKET) {
		switch (codeText[programCounter]) {
			case '[':
				depth++;
				break;
			case ']':
				if (--depth == 0) mode = Mode::NORMAL;
				break;
			default:
				break;
		}
		programCounter++; return;
	} else if (mode == Mode::END_PROGRAM) {
		// don't increment here
		return; // program has ended; probably with @
	}
	switch (codeText[programCounter]) {
		case '<':
			if (pointer != 0) pointer--;
			break;
		case '>':
			pointer++;
			if(pointer == memory.size())
				memory.push_back(0);
			break;
		case '+':
			memory[pointer]++;
			break;
		case '-':
			memory[pointer]--;
			break;
		case '.':
			std::cout << (char)memory[pointer];
			break;
		case ',':
			//printDebug();
			if(inputType == InputType::CONSOLE_PROMPT){
				std::cout << "\nInput: ";
				std::cin >> inp;
				memory[pointer] = (char)(inp[0]);
			} else if(inputType == InputType::READ_SOURCE){
				mode = Mode::READ_CHAR;
			} else if(inputType == InputType::INPUT_STRING){
				if (inputString.size() > 0) {
					memory[pointer] = inputString[0];
					inputString = inputString.substr(1, std::string::npos);
				} else {
					memory[pointer] = 0;
				}
			}
			break;
		case '[':
			if (memory[pointer] == 0){
				mode = Mode::SKIP_BRACKET;
				depth = 1;
			}
			break;
		case ']':
			if(memory[pointer] != 0)
				gotoPreviousBrace();
			break; // even if it goes to previous brace [, still goto next instruction after
		
		// EXTENSION I
		case '#':      // NONSTANDARD, EVEN FOR EXTENSION I, just used for temporary debug
			printDebug();
			break;
		case '@':
			mode = Mode::END_PROGRAM;
			break;
		case '$':
			memory[pointer] = pointer;
			break;
		case '!':
			pointer = memory[pointer];
			break;
		case '}':
			pointer = pointer >> 1;
			break;
		case '{':
			pointer = pointer << 1;
			break;
		case '~':
			pointer = ~ pointer;
			break;
		case '^':
			pointer = pointer ^ memory[pointer];
			break;
		case '&':
			pointer = pointer & memory[pointer];
			break;
		case '|':
			pointer = pointer | memory[pointer];
			break;
		default:
			break;
	}
	programCounter++;
	return;
}