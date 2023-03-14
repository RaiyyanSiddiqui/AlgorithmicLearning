#ifndef __BRAINFUCK_INTERPRETER_RAIYYAN_SIDDIQUI__
#define __BRAINFUCK_INTERPRETER_RAIYYAN_SIDDIQUI__

#include <vector>
#include <string>
#include <cstdint>

enum class Mode {
	NORMAL, READ_CHAR, SKIP_BRACKET, END_PROGRAM
};
enum class InputType {
	CONSOLE_PROMPT, READ_SOURCE, INPUT_STRING
};

class Interpreter {
	InputType inputType{InputType::CONSOLE_PROMPT};
	Mode mode{Mode::NORMAL};
	std::string codeText{""}, inputString{""};
	std::vector<uint32_t> memory{{0},};
	uint32_t pointer{0}, depth{0}, programCounter{0}; //pointer is cellptr, depth is used internally for matching braces

	// actually evaluates character, assumes codeText is up to date
	void eval();
	void gotoPreviousBrace();
public:
	void printDebug();
	void interpret(const char &character);

	Interpreter() = default;
	Interpreter(InputType inputT);
	Interpreter(const Interpreter &rf): 
										mode{ rf.mode },
										inputType{ rf.inputType },
										memory{std::vector<uint32_t>(rf.memory)},
										pointer{rf.pointer}, depth{rf.depth},
										codeText{std::string(rf.codeText)},
										inputString{std::string(rf.inputString)},
										programCounter{rf.programCounter}
										{}
	~Interpreter() = default;

};

#endif//__BRAINFUCK_INTERPRETER_RAIYYAN_SIDDIQUI__