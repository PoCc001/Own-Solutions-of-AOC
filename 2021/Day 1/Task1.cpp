#include "ReadTXTFile.h"		// is in the root directory of the repository

typedef std::vector<std::int64_t> i64Array;

i64Array stringsToInts(const aoc::stringArray& s) {
	i64Array i{};
	i.resize(s.size());
	

	for (std::size_t index = 0; index < s.size(); index++) {
		i.at(index) = std::stoll(s.at(index));
	}

	return i;
}

unsigned int countIncreases(const i64Array& data) {
	unsigned int count = 0;
	for (size_t i = 1; i < data.size(); i++) {
		if (data.at(i - 1) < data.at(i)) {
			count++;
		}
	}

	return count;
}

void fillThreeMeasurements(const i64Array& data, i64Array& t) {
	for (std::size_t i = 0; i < t.size(); i++) {
		t.at(i) = data.at(i) + data.at(i + 1) + data.at(i + 2);
	}
}

// only for debugging
void printData(const i64Array& data) {
	for (std::size_t i = 0; i < data.size(); i++) {
		std::cout << data.at(i) << std::endl;
	}
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input1.txt");	// write numbers in the input file to an array of strings
	i64Array data = stringsToInts(sa);							// convert strings to 64-bit integers

	unsigned int count1 = countIncreases(data);					// Part 1
	std::cout << "Part 1: " << count1 << std::endl;

	i64Array threeMeasurements{};								// Part 2
	threeMeasurements.resize(sa.size() - 2);

	fillThreeMeasurements(data, threeMeasurements);

	unsigned int count2 = countIncreases(threeMeasurements);
	std::cout << "Part 2: " << count2 << std::endl;

	return 0;
}