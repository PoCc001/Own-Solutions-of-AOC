#ifndef READTXTFILE_H
#define READTXTFILE_H

#include <fstream>
#include <iostream>
#include <string>
#include <vector>

namespace aoc {
	std::vector<std::string> fileToStringArray(const std::string& name) {
		std::ifstream in(name, std::ios::in);

		if (!in) {
			return std::vector<std::string>{};
		}
		else {
			std::size_t s = 100;
			std::vector<std::string> v{};
			v.reserve(s);
			v.resize(1);

			while (std::getline(in, v[v.size() - 1])) {
				v.resize(v.size() + 1);
			}

			v.resize(v.size() - 1);

			return v;
		}
	}

	typedef std::vector<std::string> stringArray;
}

#endif