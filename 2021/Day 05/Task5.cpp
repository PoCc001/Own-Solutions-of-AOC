#include "ReadTXTFile.h"
#include <list>
#include <cmath>

// WARNING: QUITE INEFFICIENT!!

class point {			// helper class
	std::int64_t x;
	std::int64_t y;

public:
	point() {
		x = 0;
		y = 0;
	}

	point(std::int64_t xPos, std::int64_t yPos) noexcept {
		x = xPos;
		y = yPos;
	}

	point& operator=(const point& other) noexcept {
		x = other.x;
		y = other.y;

		return *this;
	}

	inline std::int64_t getX() const noexcept {
		return x;
	}

	inline std::int64_t getY() const noexcept {
		return y;
	}

	inline bool operator==(const point& other) const noexcept {
		return x == other.x && y == other.y;
	}

	inline bool isLine(const point& other) const noexcept {
		return x == other.x || y == other.y;
	}

	inline bool isGLine(const point& other) const noexcept {
		return isLine(other) || (abs(x - other.x) == abs(y - other.y));
	}
};

class line {			// "specialized" line - only horizontal and vertical lines can be represented
	point a;
	point b;

public:
	line() noexcept {}

	line(const point& p1, const point& p2) {
		if (!p1.isLine(p2)) {
			throw "Invalid line for Part 1!";
		}
		else {
			if (p1.getY() < p2.getY() || p1.getX() < p2.getX()) {
				a = p1;
				b = p2;
			}
			else {
				a = p2;
				b = p1;
			}
		}
	}

	point getFirstPoint() const noexcept {
		return a;
	}

	point getLastPoint() const noexcept {
		return b;
	}

	std::vector<point> getAllPoints() const {
		if (a.getX() == b.getX()) {
			std::vector<point> pArr{};
			pArr.resize(abs(b.getY() - a.getY()) + 1);

			for (std::size_t i = 0; i < pArr.size(); i++) {
				pArr.at(i) = point{a.getX(), a.getY() + (std::int64_t)(i)};
			}

			return pArr;
		}
		else {
			std::vector<point> pArr{};
			pArr.resize(abs(b.getX() - a.getX()) + 1);

			for (std::size_t i = 0; i < pArr.size(); i++) {
				pArr.at(i) = point{ a.getX() + (std::int64_t)(i), a.getY()};
			}

			return pArr;
		}
	}

	std::list<point> intersections(const line& other) const {
		std::vector<point> l1 = getAllPoints();
		std::vector<point> l2 = other.getAllPoints();
		std::list<point> intersections{};

		for (std::size_t i = 0; i < l1.size(); i++) {
			for (std::size_t j = 0; j < l2.size(); j++) {
				if (l1.at(i) == l2.at(j)) {
					intersections.push_back(l1.at(i));
				}
			}
		}

		return intersections;
	}
};

point parsePoint(const std::string& s) {
	std::size_t index = s.find_first_of(',');
	std::int64_t x = std::stoll(s.substr(0, index));
	std::int64_t y = std::stoll(s.substr(index + 1));
	return point{ x, y };
}

line parseLine(const std::string& s) {
	std::size_t index = s.find_first_of(' ');
	point p1{ parsePoint(s.substr(0, index)) };
	point p2{ parsePoint(s.substr(index + 4)) };

	return line{ p1, p2 };
}

class gLine {		// "generalized" line - horizontal, vertical and diagonal
	point a;
	point b;

public:
	gLine() {}

	gLine(const point& p1, const point& p2) {
		if (!p1.isGLine(p2)) {
			throw "Invalid line for Part 2!";
		}
		else {
			if (p1.getY() < p2.getY()) {
				a = p1;
				b = p2;
			}
			else {
				a = p2;
				b = p1;
			}
		}
	}

	gLine(const line& other) {
		a = other.getFirstPoint();
		b = other.getLastPoint();
	}

	std::vector<point> getAllPoints() const {
		if (a.getX() == b.getX()) {
			std::vector<point> pArr{};
			pArr.resize(abs(b.getY() - a.getY()) + 1);

			for (std::size_t i = 0; i < pArr.size(); i++) {
				pArr.at(i) = point{ a.getX(), a.getY() + (std::int64_t)(i) };
			}

			return pArr;
		}
		else if (a.getY() == b.getY()) {
			std::vector<point> pArr{};
			pArr.resize(abs(b.getX() - a.getX()) + 1);

			if (a.getX() < b.getX()) {
				for (std::size_t i = 0; i < pArr.size(); i++) {
					pArr.at(i) = point{ a.getX() + (std::int64_t)(i), a.getY() };
				}
			}
			else {
				for (std::size_t i = 0; i < pArr.size(); i++) {
					pArr.at(i) = point{ a.getX() - (std::int64_t)(i), a.getY() };
				}
			}

			return pArr;
		}
		else {
			std::vector<point> pArr{};
			pArr.resize(abs(a.getX() - b.getX()) + 1);

			if (a.getX() < b.getX()) {
				for (std::size_t i = 0; i < pArr.size(); i++) {
					pArr.at(i) = point{ a.getX() + (std::int64_t)(i), a.getY() + (std::int64_t)(i) };
				}
			}
			else {
				for (std::size_t i = 0; i < pArr.size(); i++) {
					pArr.at(i) = point{ a.getX() - (std::int64_t)(i), a.getY() + (std::int64_t)(i) };
				}
			}

			return pArr;
		}
	}

	std::list<point> intersections(const gLine& other) const {
		std::vector<point> l1 = getAllPoints();
		std::vector<point> l2 = other.getAllPoints();
		std::list<point> intersections{};

		for (std::size_t i = 0; i < l1.size(); i++) {
			for (std::size_t j = 0; j < l2.size(); j++) {
				if (l1.at(i) == l2.at(j)) {
					intersections.push_back(l1.at(i));
				}
			}
		}

		return intersections;
	}
};

gLine parseGLine(const std::string& s) {
	std::size_t index = s.find_first_of(' ');
	point p1{ parsePoint(s.substr(0, index)) };
	point p2{ parsePoint(s.substr(index + 4)) };

	return gLine{p1, p2};
}

int main() {
	aoc::stringArray sa = aoc::fileToStringArray("Input5.txt");		// Preparations
	std::vector<line> lineArr{};				// Part 1

	for (std::size_t i = 0; i < sa.size(); i++) {
		line l{};
		try {
			l = parseLine(sa.at(i));
		}
		catch (...) {
			continue;
		}
		lineArr.push_back(l);
	}

	std::list<point> pArr{};

	for (std::size_t i = 0; i < lineArr.size(); i++) {
		for (std::size_t j = 0; j < lineArr.size(); j++) {
			if (i != j) {
				std::list<point> tmp{ lineArr.at(i).intersections(lineArr.at(j)) };
				for (auto k = tmp.begin(); k != tmp.end(); k++) {
					pArr.push_back(*k);
				}
			}
		}
	}

	for (auto i = pArr.begin(); i != pArr.end(); i++) {
		for (auto j = pArr.begin(); j != pArr.end();) {
			if (i != j) {
				if (*i == *j) {
					j = pArr.erase(j);
				}
				else {
					j++;
				}
			}
			else {
				j++;
			}
		}
	}

	std::cout << "Part 1:" << std::endl;
	std::cout << pArr.size() << std::endl;

	std::vector<gLine> gLineArr{};			// Part 2

	for (std::size_t i = 0; i < sa.size(); i++) {
		gLine l{};
		try {
			l = parseGLine(sa.at(i));
		}
		catch (...) {
			continue;
		}
		gLineArr.push_back(l);
	}

	pArr.clear();

	for (std::size_t i = 0; i < gLineArr.size(); i++) {
		for (std::size_t j = 0; j < gLineArr.size(); j++) {
			if (i != j) {
				std::list<point> tmp{ gLineArr.at(i).intersections(gLineArr.at(j)) };
				for (auto k = tmp.begin(); k != tmp.end(); k++) {
					pArr.push_back(*k);
				}
			}
		}
	}

	for (auto i = pArr.begin(); i != pArr.end(); i++) {
		for (auto j = pArr.begin(); j != pArr.end();) {
			if (i != j) {
				if (*i == *j) {
					j = pArr.erase(j);
				}
				else {
					j++;
				}
			}
			else {
				j++;
			}
		}
	}

	std::cout << "\nPart 2:" << std::endl;
	std::cout << pArr.size() << std::endl;

	system("PAUSE");
	return EXIT_SUCCESS;
}