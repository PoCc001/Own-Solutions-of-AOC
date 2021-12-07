#include "ReadTXTFile.h"
#include <cmath>

std::vector<std::int64_t> lineToIntArray(const std::string& l) {		// parse input string to an array of ints
	std::vector<std::int64_t> v{};

	for (std::size_t i = 0; i < l.size();) {
		std::size_t j = i;

		for (; j < l.size(); j++) {
			if (l.at(j) == ',' || j == l.size() - 1) {
				break;
			}
		}
		v.push_back(std::stoll(l.substr(i, j)));

		i = j + 1;
	}

	return v;
}

std::int64_t getMin(const std::vector<std::int64_t>& arr) {			// get the position of the submarine with the smallest horizontal position
	std::int64_t min = INT64_MAX;

	for (std::size_t i = 0; i < arr.size(); i++) {
		if (arr.at(i) < min) {
			min = arr.at(i);
		}
	}

	return min;
}

std::int64_t getMax(const std::vector<std::int64_t>& arr) {			// get the position of the submarine with the greatest horizontal position
	std::int64_t max = INT64_MIN;

	for (std::size_t i = 0; i < arr.size(); i++) {
		if (arr.at(i) > max) {
			max = arr.at(i);
		}
	}

	return max;
}

inline std::int64_t sumFromOneTo(std::int64_t v) {
	return ((v + 1) * v) / 2;
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input7.txt");		// Preparations
	std::vector<std::int64_t> arr = lineToIntArray(sa.at(0));
	
	std::int64_t min = getMin(arr);			// Part 1
	std::int64_t max = getMax(arr);

	std::int64_t minFuel = INT64_MAX;

	for (std::int64_t m = min; m < max; m++) {			// brute-force the best position by testing every position between the first and the last submarine
		std::int64_t sum = 0;
		for (std::size_t i = 0; i < arr.size(); i++) {
			sum += abs(arr.at(i) - m);
		}

		if (sum < minFuel) {
			minFuel = sum;
		}
	}

	std::cout << "Part 1:" << std::endl;
	std::cout << minFuel << std::endl;

	minFuel = INT64_MAX;			// Part 2

	for (std::int64_t m = min; m < max; m++) {			// brute-force the best position by testing every position between the first and the last submarine
		std::int64_t sum = 0;
		for (std::size_t i = 0; i < arr.size(); i++) {
			sum += sumFromOneTo(abs(arr.at(i) - m));
		}

		if (sum < minFuel) {
			minFuel = sum;
		}
	}

	std::cout << "\nPart 2:" << std::endl;
	std::cout << minFuel << std::endl;

	system("PAUSE");
	return EXIT_SUCCESS;
}