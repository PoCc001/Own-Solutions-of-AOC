#include "ReadTXTFile.h"
#include <array>

std::vector<std::uint32_t> lineToIntArray(const std::string& l) {
	std::vector<std::uint32_t> v{};

	for (std::size_t i = 0; i < l.size(); i++) {
		v.push_back(std::stoul(&l.at(i)));
		if (i < l.size() - 1) {
			if (l.at(i + 1) == ',') {
				i++;
			}
		}
	}

	return v;
}

void updatePopulation(std::vector<std::uint32_t>& v) {		// more inefficient than updateCompressedPopulation
	std::size_t s = v.size();
	for (std::size_t i = 0; i < s; i++) {
		if (v.at(i) == 0) {
			v.at(i) = 6;
			v.push_back(8);
		}
		else {
			v.at(i)--;
		}
	}
}

std::array<std::uint64_t, 9> compressData(const std::vector<std::uint32_t>& v) {		// make handling the numbers less memory-consuming
	std::array<std::uint64_t, 9> arr{0};

	for (std::size_t i = 0; i < v.size(); i++) {
		arr.at(v.at(i))++;
	}

	return arr;
}

void updateCompressedPopulation(std::array<std::uint64_t, 9>& arr) {
	std::array<std::uint64_t, 9> tmp{0};

	for (std::size_t i = 8; i > 0; i--) {
		tmp.at(i - 1) = arr.at(i);
	}

	tmp.at(8) = arr.at(0);
	tmp.at(6) += arr.at(0);

	for (std::size_t i = 0; i < 9; i++) {
		arr.at(i) = tmp.at(i);
	}
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input6.txt");		// Preparation
	std::vector<std::uint32_t> v = lineToIntArray(sa.at(0));
	std::vector<std::uint32_t> v1{ v };

	for (std::size_t i = 0; i < 80; i++) {			// Part 1
		updatePopulation(v1);
	}

	std::cout << "Part 1:" << std::endl;
	std::cout << v.size() << std::endl;

	std::array<std::uint64_t, 9> arr = compressData(v);		// Part 2

	for (std::size_t i = 0; i < 256; i++) {
		updateCompressedPopulation(arr);
	}

	std::uint64_t sum = 0;

	for (std::size_t i = 0; i < 9; i++) {
		sum += arr.at(i);
	}

	std::cout << "\nPart 2:" << std::endl;
	std::cout << sum << std::endl;

	system("PAUSE");
	return EXIT_SUCCESS;
}