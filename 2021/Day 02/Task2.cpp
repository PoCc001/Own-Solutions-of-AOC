#include "ReadTXTFile.h"

int main() {
	const aoc::stringArray sa = aoc::fileToStringArray("Input2.txt");

	std::int64_t h = 0;		// horizontal value
	std::int64_t v = 0;		// vertical value

	for (std::size_t i = 0; i < sa.size(); i++) {
		std::string str = sa.at(i);
		char c = str.c_str()[0];
		if (c == 'u') {		// up
			v -= std::stoll(str.substr(3, str.length() - 1));
		}
		else if (c == 'd') {	// down
			v += std::stoll(str.substr(5, str.length() - 1));
		}
		else {		// forward
			h += std::stoll(str.substr(8, str.length() - 1));
		}
	}

	std::cout << "Part 1:" << std::endl;
	std::cout << "\tHorizontal: " << h << std::endl;
	std::cout << "\tVertical: " << v << std::endl;
	std::cout << "\tProduct: " << (h * v) << std::endl;

	h = 0;
	v = 0;
	std::int64_t aim = 0;
	std::int64_t forward = 0;

	for (std::size_t i = 0; i < sa.size(); i++) {
		std::string str = sa.at(i);
		char c = str.c_str()[0];
		if (c == 'u') {
			aim -= std::stoll(str.substr(3, str.length() - 1));
		}
		else if (c == 'd') {
			aim += std::stoll(str.substr(5, str.length() - 1));
		}
		else {
			forward = std::stoll(str.substr(8, str.length() - 1));
			h += forward;
			v += (forward * aim);
		}
	}

	std::cout << "\nPart2:" << std::endl;
	std::cout << "\tHorizontal: " << h << std::endl;
	std::cout << "\tVertical: " << v << std::endl;
	std::cout << "\tProduct: " << (h * v) << std::endl;

	system("PAUSE");

	return EXIT_SUCCESS;
}