#include "ReadTXTFile.h"
#include <array>

typedef std::array<char, 7> charArray;

aoc::stringArray getDigits(const aoc::stringArray& sa) {			// extract the part after the delimiter
	aoc::stringArray digits{};
	for (std::size_t l = 0; l < sa.size(); l++) {
		std::string substr = sa.at(l).substr(sa.at(l).find_first_of('|') + 2);
		for (std::size_t i = 0; i < substr.size();) {
			std::size_t j = i;

			for (; j < substr.size(); j++) {
				if (substr.at(j) == ' ' || j == substr.size() - 1) {
					break;
				}
			}

			if (j < substr.size() - 1) {
				digits.push_back(substr.substr(i, j - i));
			}
			else {
				digits.push_back(substr.substr(i));
			}

			i = j + 1;
		}
	}

	return digits;
}

std::size_t numberOfSimpleDigits(const aoc::stringArray& arr) {
	std::size_t number = 0;
	for (std::size_t i = 0; i < arr.size(); i++) {
		if (arr.at(i).size() >= 2 && arr.at(i).size() <= 4 || arr.at(i).size() == 7) {
			number++;
		}
	}

	return number;
}

aoc::stringArray getCombinationLine(const std::string& l) {		// extract the part before the delimiter
	aoc::stringArray v{};

	std::string sub = l.substr(0, l.find_first_of('|') - 1);

	for (std::size_t i = 0; i < sub.size();) {
		std::size_t j = i;

		for (; j < sub.size(); j++) {
			if (sub.at(j) == ' ') {
				break;
			}
		}
		v.push_back(sub.substr(i, j - i));

		i = j + 1;
	}

	return v;
}

bool stringContains(const std::string& s, char c) {
	for (std::size_t i = 0; i < s.size(); i++) {
		if (s.at(i) == c) {
			return true;
		}
	}

	return false;
}

bool charArrayContains(const charArray& s, char c) {
	for (std::size_t i = 0; i < s.size(); i++) {
		if (s.at(i) == c) {
			return true;
		}
	}

	return false;
}

std::vector<char> stringMinusOther(const std::string& arg1, const std::string& arg2) {		// ignoring the order of the letters
	std::vector<char> v{};
	if (arg1.size() > arg2.size()) {
		for (std::size_t i = 0; i < arg1.size(); i++) {
			if (!stringContains(arg2, arg1.at(i))) {
				v.push_back(arg1.at(i));
			}
		}
	}
	else {
		for (std::size_t i = 0; i < arg2.size(); i++) {
			if (!stringContains(arg1, arg2.at(i))) {
				v.push_back(arg2.at(i));
			}
		}
	}

	return v;
}

charArray matchCombination(const aoc::stringArray& c) {		// find out which letter belongs to which segment
	charArray arr{};
	std::string one, four, seven, eight;

	for (std::size_t i = 0; i < c.size(); i++) {
		if (c.at(i).size() == 2) {
			one = c.at(i);
		}
		else if (c.at(i).size() == 3) {
			seven = c.at(i);
		}
		else if (c.at(i).size() == 4) {
			four = c.at(i);
		}
		else if (c.at(i).size() == 7) {
			eight = c.at(i);
		}
	}

	arr.at(0) = stringContains(one, seven.at(0)) ? (stringContains(one, seven.at(1)) ? seven.at(2) : seven.at(1)) : seven.at(0);
	char c36 = one.at(0);
	char c63 = one.at(1);

	std::string six;
	bool containsC36 = false;
	bool containsC63 = false;
	for (std::size_t i = 0; i < c.size(); i++) {
		containsC36 = stringContains(c.at(i), c36);
		containsC63 = stringContains(c.at(i), c63);
		if (c.at(i).size() == 6 && (!containsC36 || !containsC63)) {
			six = c.at(i);
			break;
		}
	}

	if (containsC36) {
		arr.at(2) = c63;
		arr.at(5) = c36;
	}
	else {
		arr.at(2) = c36;
		arr.at(5) = c63;
	}

	std::vector<char> vc24 = stringMinusOther(four, one);

	std::string nine = "";
	std::string zero = "";
	for (std::size_t i = 0; i < c.size(); i++) {
		if (c.at(i).size() == 6 && c.at(i) != six) {
			if (stringContains(c.at(i), vc24.at(0)) && stringContains(c.at(i), vc24.at(1))) {
				nine = c.at(i);
			}
			else {
				zero = c.at(i);
			}
		}
	}

	if (stringContains(zero, vc24.at(0))) {
		arr.at(1) = vc24.at(0);
		arr.at(3) = vc24.at(1);
	}
	else {
		arr.at(1) = vc24.at(1);
		arr.at(3) = vc24.at(0);
	}

	for (std::size_t i = 0; i < 7; i++) {
		if (!stringContains(nine, eight.at(i))) {
			arr.at(4) = eight.at(i);
			break;
		}
	}

	for (std::size_t i = 0; i < 7; i++) {
		if (!charArrayContains(arr, eight.at(i))) {
			arr.at(6) = eight.at(i);
			break;
		}
	}

	return arr;
}

std::uint8_t getDigit(const std::string& encrypt, const charArray& key) {		// translate a string to a digit
	if (encrypt.size() == 2) {
		return 1;
	}
	else if (encrypt.size() == 3) {
		return 7;
	}
	else if (encrypt.size() == 4) {
		return 4;
	}
	else if (encrypt.size() == 7) {
		return 8;
	}
	else if (encrypt.size() == 5) {
		if (stringContains(encrypt, key.at(4))) {
			return 2;
		}
		else {
			if (stringContains(encrypt, key.at(2))) {
				return 3;
			}
			else {
				return 5;
			}
		}
	}
	else {
		if (!stringContains(encrypt, key.at(3))) {
			return 0;
		}
		else if (!stringContains(encrypt, key.at(2))) {
			return 6;
		}
		else {
			return 9;
		}
	}
}

std::uint32_t composeNumber(const std::vector<std::uint8_t>& v, std::size_t start) {		// v0*10^3+v1*10^2+v2*10^1+v3*10^0
	std::uint32_t p = 1;
	std::uint32_t num = 0;

	for (std::size_t i = 0; i < 4; i++) {
		num += (v.at(start + 3 - i) * p);
		p *= 10;
	}

	return num;
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input8.txt");		// Preparations
	aoc::stringArray digits = getDigits(sa);
	
	std::size_t number = numberOfSimpleDigits(digits);		// Part 1

	std::cout << "Part 1:" << std::endl;
	std::cout << number << std::endl;

	std::vector<std::uint8_t> intDigits{};			// Part 2
	aoc::stringArray combinations{};

	charArray key{};
	for (std::size_t i = 0; i < digits.size(); i++) {
		if (i % 4 == 0) {
			combinations = getCombinationLine(sa.at(i / 4));
			key = matchCombination(combinations);
		}
		intDigits.push_back(getDigit(digits.at(i), key));
	}

	std::vector<std::uint32_t> numberArray{};

	for (std::size_t i = 0; i < intDigits.size(); i += 4) {
		numberArray.push_back(composeNumber(intDigits, i));
	}

	std::uint64_t sum = 0;

	for (std::size_t i = 0; i < numberArray.size(); i++) {
		sum += (std::uint64_t)(numberArray.at(i));
	}

	std::cout << "\nPart 2:" << std::endl;
	std::cout << sum << std::endl;

	system("PAUSE");
	return EXIT_SUCCESS;
}