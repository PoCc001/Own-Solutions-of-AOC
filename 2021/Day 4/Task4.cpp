#include "ReadTXTFile.h"

// aliases to avoid overly long typenames
typedef std::vector<std::int64_t> i64Array;
typedef std::vector<i64Array> i642DArray;
typedef std::vector<i642DArray> bingoArray;

typedef std::vector<bool> boolArray;
typedef std::vector<boolArray> bool2DArray;
typedef std::vector<bool2DArray> bool3DArray;

typedef struct board {
	bingoArray bArr;
	bool3DArray checkArr;
	boolArray checkBoardArr;

	void resetChecks() {
		for (std::size_t b = 0; b < checkArr.size(); b++) {
			checkBoardArr.at(b) = false;

			for (std::size_t r = 0; r < checkArr.at(b).size(); r++) {
				for (std::size_t c = 0; c < checkArr.at(b).at(r).size(); c++) {
					checkArr.at(b).at(r).at(c) = false;
				}
			}
		}
	}
} board_t;

i64Array getInputs(const std::string& s) {
	const char* const str = s.c_str();
	std::size_t index = 0;
	std::size_t nextIndex = 0;
	i64Array iArr{};

	for (; index < s.size();) {
		for (nextIndex += 1; nextIndex < s.size(); nextIndex++) {
			if (str[nextIndex] == ',') {
				break;
			}
		}

		iArr.push_back(std::stoll(s.substr(index, nextIndex)));
		index = nextIndex + 1;
	}

	return iArr;
}

// initialize boards
board_t getBingos(const aoc::stringArray& sa) {
	bingoArray bArr{};
	bool3DArray checkArr{};
	std::size_t index = 0;
	std::size_t nextIndex = 0;
	const char* str = nullptr;

	for (std::size_t b = 0; (b * 6 + 2) < sa.size(); b++) {
		bArr.push_back(i642DArray{});
		checkArr.push_back(bool2DArray{});
		for (std::size_t r = 0; r < 5; r++) {
			index = 0;
			bArr.at(b).push_back(i64Array{});
			checkArr.at(b).push_back(boolArray{});
			checkArr.at(b).at(r).resize(5);
			const std::string& current = sa.at(6 * b + 2 + r);
			str = current.c_str();

			for (; index < current.size();) {
				if (index == 0 && str[index] == ' ') {
					index++;
					continue;
				}

				for (nextIndex = index + 1; nextIndex < current.size(); nextIndex++) {
					if (str[nextIndex] == ' ') {
						break;
					}
				}

				if (nextIndex + 1 >= current.size()) {
					bArr.at(b).at(r).push_back(std::stoll(current.substr(index)));
					break;
				}

				bArr.at(b).at(r).push_back(std::stoll(current.substr(index, nextIndex)));

				for (nextIndex += 1; nextIndex < current.size(); nextIndex++) {
					if (str[nextIndex] != ' ') {
						index = nextIndex;
						break;
					}
				}
			}
		}
	}

	boolArray checkBoards{};
	checkBoards.resize(bArr.size());

	return board_t{bArr, checkArr, checkBoards};
}

bool checkRow(const i64Array& row, boolArray& check, std::int64_t input) {
	for (std::size_t i = 0; i < 5; i++) {
		if (check.at(i)) {
			continue;
		}
		else {
			if (row.at(i) == input) {
				check.at(i) = true;
				break;
			}
		}
	}

	for (std::size_t i = 0; i < 5; i++) {
		if (check.at(i) == false) {
			return false;
		}
	}

	return true;
}

bool checkColumn(const i642DArray& columns, std::size_t c, bool2DArray& check, std::int64_t input) {

	for (std::size_t i = 0; i < 5; i++) {
		if (check.at(i).at(c)) {
			continue;
		}
		else {
			if (columns.at(i).at(c) == input) {
				check.at(i).at(c) = true;
				break;
			}
		}
	}

	for (std::size_t i = 0; i < 5; i++) {
		if (check.at(i).at(c) == false) {
			return false;
		}
	}

	return true;
}

bool checkBoard(const i642DArray& board, bool2DArray& check, std::int64_t input) {
	for (std::size_t r = 0; r < 5; r++) {
		if (checkRow(board.at(r), check.at(r), input)) {
			return true;
		}
	}

	for (std::size_t c = 0; c < 5; c++) {
		if (checkColumn(board, c, check, input)) {
			return true;
		}
	}

	return false;
}

bool checkAllBoards(board_t& boards, std::int64_t input, std::size_t& index) {				// returns true, as soon as one board wins
	for (std::size_t i = 0; i < boards.bArr.size(); i++) {
		if (checkBoard(boards.bArr.at(i), boards.checkArr.at(i), input)) {
			index = i;
			return true;
		}
	}

	return false;
}

std::size_t getLastBoard(board_t& boards, const i64Array& inputs, std::size_t& in) {		// returns the index of the last board that wins
	std::size_t unfinishedBoards = boards.bArr.size();
	for (std::size_t index = 0; index < inputs.size(); index++) {
		for (std::size_t i = 0; i < boards.bArr.size(); i++) {
			if (boards.checkBoardArr.at(i)) {
				continue;
			}

			if (checkBoard(boards.bArr.at(i), boards.checkArr.at(i), inputs.at(index))) {
				boards.checkBoardArr.at(i) = true;
				unfinishedBoards--;
			}

			if (unfinishedBoards == 0) {
				in = index;
				return i;
			}
		}
	}

	return ~0ULL;		// something went wrong apparently, but the compiler doesn't lament about a missing return value or so...
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input4.txt");		// Preparations
	i64Array iArr = getInputs(sa.at(0));
	board_t bArr = getBingos(sa);

	std::size_t index = 0;				// Part 1
	std::size_t board = 0;
	for (; index < iArr.size(); index++) {
		if (checkAllBoards(bArr, iArr.at(index), board)) {
			break;
		}
	}

	i642DArray winnerBoard = bArr.bArr.at(board);
	bool2DArray winnerChecks = bArr.checkArr.at(board);

	std::int64_t sum = 0;

	for (std::size_t r = 0; r < 5; r++) {
		for (std::size_t c = 0; c < 5; c++) {
			if (!winnerChecks.at(r).at(c)) {
				sum += winnerBoard.at(r).at(c);
			}
		}
	}

	std::cout << "Part 1:" << std::endl;
	std::cout << "Score: " << (sum * iArr.at(index)) << std::endl;

	bArr.resetChecks();					// Part 2

	board = getLastBoard(bArr, iArr, index);

	winnerBoard = bArr.bArr.at(board);
	winnerChecks = bArr.checkArr.at(board);
	sum = 0;

	for (std::size_t r = 0; r < 5; r++) {
		for (std::size_t c = 0; c < 5; c++) {
			if (!winnerChecks.at(r).at(c)) {
				sum += winnerBoard.at(r).at(c);
			}
		}
	}

	std::cout << "\nPart 2:" << std::endl;
	std::cout << "Score: " << (sum * iArr.at(index)) << std::endl;

	system("PAUSE");
	return EXIT_SUCCESS;
}