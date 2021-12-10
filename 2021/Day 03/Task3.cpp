#include "ReadTXTFile.h"
#include <list>		// use list for more efficient deletion of elements in the middle of a sequence

typedef std::vector<std::uint64_t> i64Array;
typedef std::list<std::uint64_t> i64List;

i64Array stringsToBinaryInts(const aoc::stringArray& sa) {
	i64Array iArr{};
	iArr.resize(sa.size());

	for (std::size_t i = 0; i < iArr.size(); i++) {
		iArr.at(i) = std::stoull(sa.at(i), nullptr, 2);
	}

	return iArr;
}

i64Array calcGammaEpsilonValue(const i64Array& iArr, std::size_t bitlength) {
	i64Array gammaEpsilon{};
	gammaEpsilon.resize(2);
	std::uint64_t bitmask = 1;
	for (std::size_t i = 0; i < bitlength; i++) {
		std::size_t countOneBits = 0;
		for (std::size_t j = 0; j < iArr.size(); j++) {
			if ((iArr.at(j) & bitmask) == bitmask) {
				countOneBits++;
			}
		}

		gammaEpsilon.at((((countOneBits << 1) - iArr.size()) >> 63) & 1) |= bitmask;		// index 0 for gamma and index 1 for epsilon value

		bitmask <<= 1;
	}

	return gammaEpsilon;
}

void vectorToList(i64List& iList, const i64Array& iArr) {
	for (std::size_t i = 0; i < iArr.size(); i++) {
		iList.push_back(iArr.at(i));
	}
}

std::uint64_t calcOxygen(const i64Array& iArr, std::size_t bitlength) {
	i64List tmp{};			// vector would also be fine, but not as efficient with deleting elements from anywhere in a sequence
	vectorToList(tmp, iArr);

	std::size_t oneCount = 0;
	std::size_t zeroCount = 0;
	std::uint64_t bitmask = 1ULL << (bitlength - 1);

	while (tmp.size() > 1 && bitmask != 0) {
		for (auto i = tmp.begin(); i != tmp.end(); i++) {
			if (((*i) & bitmask) != 0) {
				oneCount++;
			}
			else {
				zeroCount++;
			}
		}

		if (oneCount >= zeroCount) {
			for (auto iterator = tmp.begin(); iterator != tmp.end();) {
				if (((*iterator) & bitmask) == 0) {
					iterator = tmp.erase(iterator);
				}
				else {
					iterator++;
				}
			}
		}
		else {
			for (auto iterator = tmp.begin(); iterator != tmp.end();) {
				if (((*iterator) & bitmask) != 0) {
					iterator = tmp.erase(iterator);
				}
				else {
					iterator++;
				}
			}
		}

		oneCount = 0;
		zeroCount = 0;
		bitmask >>= 1;
	}

	return *tmp.begin();
}

std::uint64_t calcCO2(const i64Array& iArr, std::size_t bitlength) {
	i64List tmp{};
	vectorToList(tmp, iArr);

	std::size_t zeroCount = 0;
	std::size_t oneCount = 0;
	std::uint64_t bitmask = 1ULL << (bitlength - 1);

	while (tmp.size() > 1 && bitmask != 0) {
		for (auto i = tmp.begin(); i != tmp.end(); i++) {
			if (((*i) & bitmask) != 0) {
				oneCount++;
			}
			else {
				zeroCount++;
			}
		}

		i64Array tmp2{};
		if (zeroCount <= oneCount) {
			for (auto iterator = tmp.begin(); iterator != tmp.end();) {
				if (((*iterator) & bitmask) != 0) {
					iterator = tmp.erase(iterator);
				}
				else {
					iterator++;
				}
			}
		}
		else {
			for (auto iterator = tmp.begin(); iterator != tmp.end();) {
				if (((*iterator) & bitmask) == 0) {
					iterator = tmp.erase(iterator);
				}
				else {
					iterator++;
				}
			}
		}
		oneCount = 0;
		zeroCount = 0;
		bitmask >>= 1;
	}

	return *tmp.begin();
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input3.txt");
	std::size_t bitlength = sa.at(0).size();

	i64Array iArr = stringsToBinaryInts(sa);
	i64Array gammaEpsilonValue = calcGammaEpsilonValue(iArr, bitlength);

	std::cout << "Part 1:" << std::endl;
	std::cout << (gammaEpsilonValue[0] * gammaEpsilonValue[1]) << std::endl;

	std::uint64_t oxygen = calcOxygen(iArr, bitlength);
	std::uint64_t co2 = calcCO2(iArr, bitlength);

	std::cout << "\nPart 2:" << std::endl;
	std::cout << (oxygen * co2) << std::endl;

	system("PAUSE");
	return EXIT_SUCCESS;
}