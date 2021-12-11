#include "ReadTXTFile.h"

typedef std::vector<std::vector<uint32_t>> matrix_t;
typedef std::vector<std::vector<bool>> lockmatrix_t;		// all octopusses that have already flashed in one specific step cannot flash again (=are locked)

matrix_t getMatrix(const aoc::stringArray& sa) {
	matrix_t m{};
	m.reserve(sa.size());
	for (std::size_t i = 0; i < sa.size(); i++) {
		std::string s = sa.at(i);
		m.push_back(std::vector<std::uint32_t>{});
		m.at(i).reserve(s.size());
		for (std::size_t j = 0; j < s.size(); j++) {
			m.at(i).push_back(std::stoi(s.substr(j, 1)));
		}
	}

	return m;
}

void increaseLevels(matrix_t& m, std::size_t l, std::size_t c) {
	if (l > 0) {
		m.at(l - 1).at(c)++;

		if (c > 0) {
			m.at(l - 1).at(c - 1)++;
		}

		if (c < m.at(l - 1).size() - 1) {
			m.at(l - 1).at(c + 1)++;
		}
	}

	if (l < m.size() - 1) {
		m.at(l + 1).at(c)++;

		if (c > 0) {
			m.at(l + 1).at(c - 1)++;
		}

		if (c < m.at(l + 1).size() - 1) {
			m.at(l + 1).at(c + 1)++;
		}
	}

	if (c > 0) {
		m.at(l).at(c - 1)++;
	}

	if (c < m.at(l).size() - 1) {
		m.at(l).at(c + 1)++;
	}
}

bool hasFlashing(const matrix_t& m, const lockmatrix_t& l) {		// matrix contains flashing octopusses?
	for (std::size_t i = 0; i < m.size(); i++) {
		for (std::size_t j = 0; j < m.at(i).size(); j++) {
			if (m.at(i).at(j) > 9 && !l.at(i).at(j)) {
				return true;
			}
		}
	}

	return false;
}

std::uint64_t doStep(matrix_t& m) {
	lockmatrix_t l{};
	l.resize(m.size());

	for (std::size_t i = 0; i < l.size(); i++) {
		l.at(i).resize(m.at(i).size());
	}

	for (std::size_t i = 0; i < m.size(); i++) {
		for (std::size_t j = 0; j < m.at(i).size(); j++) {
			m.at(i).at(j)++;
		}
	}

	for (std::size_t i = 0; i < m.size(); i++) {
		for (std::size_t j = 0; j < m.at(i).size(); j++) {
			if (m.at(i).at(j) > 9) {
				l.at(i).at(j) = true;
				increaseLevels(m, i, j);
			}
		}
	}

	while (hasFlashing(m, l)) {
		for (std::size_t i = 0; i < m.size(); i++) {
			for (std::size_t j = 0; j < m.at(i).size(); j++) {
				if (m.at(i).at(j) > 9 && !l.at(i).at(j)) {
					l.at(i).at(j) = true;
					increaseLevels(m, i, j);
				}
			}
		}
	}

	std::uint64_t flashes = 0;
	for (std::size_t i = 0; i < l.size(); i++) {
		for (std::size_t j = 0; j < l.at(i).size(); j++) {
			if (l.at(i).at(j)) {
				m.at(i).at(j) = 0;
				flashes++;
			}
		}
	}

	return flashes;
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input11.txt");		// Preparations
	matrix_t m = getMatrix(sa);

	matrix_t m1{ m };

	std::uint64_t f = 0;		// Part 1
	for (std::size_t i = 0; i < 100; i++) {
		f += doStep(m1);
	}

	std::cout << "Part 1:" << std::endl;
	std::cout << f << std::endl;

	std::size_t count = 0;		// Part 2
	while (doStep(m) < (m.size() * m.at(0).size())) { count++; }	// the step before all flash
	count++;

	std::cout << "\nPart 2:" << std::endl;
	std::cout << count << std::endl;

	system("PAUSE");
	return EXIT_SUCCESS;
}