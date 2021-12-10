#include "ReadTXTFile.h"
#include <array>
#include <list>

typedef std::vector<std::vector<std::uint8_t>> matrix_t;
typedef std::array<std::size_t, 2> coordinate_t;

matrix_t getMatrix(const aoc::stringArray& arr) {
	matrix_t m{};
	m.reserve(arr.size());
	for (std::size_t i = 0; i < arr.size(); i++) {
		std::string s = arr.at(i);
		m.push_back(std::vector<std::uint8_t>{});
		m.at(i).reserve(s.size());
		for (std::size_t j = 0; j < s.size(); j++) {
			m.at(i).push_back(std::stoi(s.substr(j, 1)));
		}
	}

	return m;
}

coordinate_t getRecursiveLowCoord(const matrix_t& m, coordinate_t c) {
	coordinate_t lowestNeighbour{c.at(0), c.at(1)};
	std::uint8_t currVal = m.at(c.at(1)).at(c.at(0));
	std::uint8_t minVal = currVal;

	if (c.at(1) > 0) {
		if (minVal > m.at(c.at(1) - 1).at(c.at(0))) {
			minVal = m.at(c.at(1) - 1).at(c.at(0));
			lowestNeighbour.at(0) = c.at(0);
			lowestNeighbour.at(1) = c.at(1) - 1;
		}
	}
	
	if (c.at(1) < m.size() - 1) {
		if (minVal > m.at(c.at(1) + 1).at(c.at(0))) {
			minVal = m.at(c.at(1) + 1).at(c.at(0));
			lowestNeighbour.at(0) = c.at(0);
			lowestNeighbour.at(1) = c.at(1) + 1;
		}
	}

	if (c.at(0) > 0) {
		if (minVal > m.at(c.at(1)).at(c.at(0) - 1)) {
			minVal = m.at(c.at(1)).at(c.at(0) - 1);
			lowestNeighbour.at(0) = c.at(0) - 1;
			lowestNeighbour.at(1) = c.at(1);
		}
	}

	if (c.at(0) < m.at(0).size() - 1) {
		if (minVal > m.at(c.at(1)).at(c.at(0) + 1)) {
			minVal = m.at(c.at(1)).at(c.at(0) + 1);
			lowestNeighbour.at(0) = c.at(0) + 1;
			lowestNeighbour.at(1) = c.at(1);
		}
	}

	if (minVal == currVal) {
		return c;
	}
	else {
		coordinate_t coord = getRecursiveLowCoord(m, lowestNeighbour);
		return coord;
	}
}

std::list<coordinate_t> getLowCoords(const matrix_t& m) {
	std::list<coordinate_t> cv{};

	for (std::size_t i = 0; i < m.size(); i++) {
		for (std::size_t j = 0; j < m.at(i).size(); j++) {
			coordinate_t c = getRecursiveLowCoord(m, coordinate_t{ j, i });
			if (m.at(c.at(1)).at(c.at(0)) != 9) {	// points with height 9 cannot be low points
				cv.push_back(c);
			}
		}
	}

	return cv;
}

void removeDuplicateCoords(std::list<coordinate_t>& c) {		// low points may be found several times each
	for (auto i = c.begin(); i != c.end(); i++) {
		for (auto it = c.begin(); it != c.end();) {
			if (i != it && *i == *it) {
				it = c.erase(it);
			}
			else {
				it++;
			}
		}
	}
}

std::vector<std::uint8_t> getValues(const matrix_t& m, const std::list<coordinate_t>& c) {
	std::vector<std::uint8_t> v{};
	v.reserve(c.size());

	for (auto i = c.begin(); i != c.end(); i++) {
		v.push_back(m.at((*i).at(1)).at((*i).at(0)));
	}

	return v;
}

bool existsInList(const coordinate_t& c, const std::vector<coordinate_t>& basinCoords) {		// spot duplicate coordinates
	for (auto it = basinCoords.begin(); it != basinCoords.end(); it++) {
		if (c.at(0) == (*it).at(0) && c.at(1) == (*it).at(1)) {
			return true;
		}
	}

	return false;
}

void getRecursiveBasin(const matrix_t& m, const coordinate_t& c, std::vector<coordinate_t>& basinCoords) {		// get the coordinates of all points in a basin
	basinCoords.push_back(c);

	if (c.at(1) > 0) {
		if (m.at(c.at(1) - 1).at(c.at(0)) != 9) {
			if (!existsInList(coordinate_t{ c.at(0), c.at(1) - 1 }, basinCoords)) {
				getRecursiveBasin(m, coordinate_t{ c.at(0), c.at(1) - 1 }, basinCoords);
			}
		}
	}

	if (c.at(1) < m.size() - 1) {
		if (m.at(c.at(1) + 1).at(c.at(0)) != 9) {
			if (!existsInList(coordinate_t{ c.at(0), c.at(1) + 1 }, basinCoords)) {
				getRecursiveBasin(m, coordinate_t{ c.at(0), c.at(1) + 1 }, basinCoords);
			}
		}
	}

	if (c.at(0) > 0) {
		if (m.at(c.at(1)).at(c.at(0) - 1) != 9) {
			if (!existsInList(coordinate_t{ c.at(0) - 1, c.at(1) }, basinCoords)) {
				getRecursiveBasin(m, coordinate_t{ c.at(0) - 1, c.at(1) }, basinCoords);
			}
		}
	}

	if (c.at(0) < m.at(0).size() - 1) {
		if (m.at(c.at(1)).at(c.at(0) + 1) != 9) {
			if (!existsInList(coordinate_t{ c.at(0) + 1, c.at(1) }, basinCoords)) {
				getRecursiveBasin(m, coordinate_t{ c.at(0) + 1, c.at(1) }, basinCoords);
			}
		}
	}
}

std::vector<std::size_t> getBasinSizes(const matrix_t& m, const std::list<coordinate_t>& l) {
	std::vector<std::size_t> v{};
	v.reserve(l.size());

	for (auto it = l.begin(); it != l.end(); it++) {
		std::vector<coordinate_t> vc{};
		getRecursiveBasin(m, *it, vc);
		v.push_back(vc.size());
	}

	return v;
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input9.txt");		// Preparations
	matrix_t m = getMatrix(sa);

	std::list<coordinate_t> l = getLowCoords(m);		// Part 1
	removeDuplicateCoords(l);
	std::vector<std::uint8_t> v = getValues(m, l);

	std::uint64_t sum = 0;
	for (std::size_t i = 0; i < v.size(); i++) {
		sum += (std::uint64_t)(v.at(i)) + 1;
	}

	std::cout << "Part 1:" << std::endl;
	std::cout << sum << std::endl;

	std::vector<std::size_t> sizes = getBasinSizes(m, l);		// Part 2

	std::list<std::size_t> sizeList{};

	for (std::size_t i = 0; i < sizes.size(); i++) {
		sizeList.push_back(sizes.at(i));
	}

	std::size_t largest = 0;

	for (auto it = sizeList.begin(); it != sizeList.end();) {
		if (largest < *it) {
			largest = *it;
		}
		else {
			it++;
		}
	}

	for (auto it = sizeList.begin(); it != sizeList.end(); it++) {
		if (largest == *it) {
			it = sizeList.erase(it);
			break;
		}
	}

	std::size_t secondLargest = 0;

	for (auto it = sizeList.begin(); it != sizeList.end();) {
		if (secondLargest < *it) {
			secondLargest = *it;
		}
		else {
			it++;
		}
	}

	for (auto it = sizeList.begin(); it != sizeList.end(); it++) {
		if (secondLargest == *it) {
			it = sizeList.erase(it);
			break;
		}
	}

	std::size_t thirdLargest = 0;

	for (auto it = sizeList.begin(); it != sizeList.end();) {
		if (thirdLargest < *it) {
			thirdLargest = *it;
		}
		else {
			it++;
		}
	}

	for (auto it = sizeList.begin(); it != sizeList.end(); it++) {
		if (thirdLargest == *it) {
			it = sizeList.erase(it);
			break;
		}
	}

	std::cout << "\nPart 2:" << std::endl;
	std::cout << (largest * secondLargest * thirdLargest) << std::endl;

	system("PAUSE");
	return EXIT_SUCCESS;
}