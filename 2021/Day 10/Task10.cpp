#include "ReadTXTFile.h"
#include <list>

class chunk {
	std::vector<char> subChunks{};		// 0th char is the bracket of the chunk, 1st char is the bracket of last unclosed sub-chunk, and so on...
	std::vector<char>::iterator last;

public:
	chunk() {};

	void createSubChunk(char b) {
		subChunks.push_back(b);
		auto it = subChunks.begin();
		for (; it != subChunks.end(); it++) {}
		it--;
		last = it;
	}

	bool matchClosingBracket(char b) {
		if (b == ')' || b == ']' || b == '}' || b == '>') {		// useless if-Statement, but why not
			bool match = false;
			if (*last == '(') {
				match = (b == ')');
			}
			else if (*last == '[') {
				match = (b == ']');
			}
			else if (*last == '{') {
				match = (b == '}');
			}
			else {
				match = (b == '>');
			}

			last = subChunks.erase(last);
			if (!subChunks.empty()) {
				last--;
			}

			return match;
		}
		else {
			throw "Illegal bracket symbol!";
		}
	}

	bool isClosed() {
		return subChunks.empty();
	}

	std::uint64_t doInsertionAndGetScore() {
		std::uint64_t score = 0;
		if (*last == '(') {
			score = 1;
		}
		else if (*last == '[') {
			score = 2;
		}
		else if (*last == '{') {
			score = 3;
		}
		else {
			score = 4;
		}

		last = subChunks.erase(last);
		if (!subChunks.empty()) {
			last--;
		}

		return score;
	}
};

bool isOpeningBracket(char b) {
	if (b == '(' || b == '[' || b == '{' || b == '<') {
		return true;
	}
	else {
		return false;
	}
}

std::list<uint64_t> getSortedList(std::list<uint64_t>& l) {		// look away, if you're allergic to inefficient/hacky sorting algorithms!
	std::list<uint64_t> sorted{};
	while (!l.empty()) {
		std::uint64_t max = 0;
		auto it = l.begin();
		for (; it != l.end(); it++) {
			if (max < *it) {
				max = *it;
			}
		}

		sorted.push_back(max);

		it = l.begin();
		for (; it != l.end(); it++) {
			if (max == *it) {
				break;
			}
		}
		l.erase(it);
	}

	return sorted;
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input10.txt");		// Preparation

	std::size_t errorScore = 0;		// Part 1
	aoc::stringArray sl{};		// used in Part 2
	for (std::size_t i = 0; i < sa.size(); i++) {
		chunk c{};
		std::string s = sa.at(i);
		bool m = true;
		for (std::size_t j = 0; j < s.size(); j++) {
			char b = s.at(j);
			if (isOpeningBracket(b)) {
				c.createSubChunk(b);
			}
			else {
				m = c.matchClosingBracket(b);

				if (!m) {
					if (b == ')') {
						errorScore += 3;
					}
					else if (b == ']') {
						errorScore += 57;
					}
					else if (b == '}') {
						errorScore += 1197;
					}
					else {
						errorScore += 25137;
					}
					
					break;
				}
			}
		}

		if (m) {
			sl.push_back(s);		// for Part 2
		}
	}

	std::cout << "Part 1:" << std::endl;
	std::cout << errorScore << std::endl;

	std::list<std::uint64_t> scores{};		// Part 2
	scores.resize(sl.size());

	auto it = scores.begin();
	for (std::size_t i = 0; i < sl.size(); i++) {
		std::string s = sl.at(i);
		chunk c{};
		for (std::size_t j = 0; j < s.size(); j++) {
			char b = s.at(j);
			if (isOpeningBracket(b)) {
				c.createSubChunk(b);
			}
			else {
				c.matchClosingBracket(b);
			}
		}

		while (!c.isClosed()) {
			*it *= 5ULL;
			*it += c.doInsertionAndGetScore();
		}

		it++;
	}

	std::list<uint64_t> sorted = getSortedList(scores);

	it = sorted.begin();
	for (std::size_t i = 0; i < sorted.size() / 2; i++) {
		it++;
	}

	std::cout << "\nPart 2:" << std::endl;
	std::cout << *it << std::endl;

	system("PAUSE");
	return EXIT_SUCCESS;
}
