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
			std::cout << "File \"" << name << "\" doesn't exist!\nAbort!" << std::endl;
			std::system("PAUSE");
			exit(1);
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

	std::string substrIndizes(const std::string& str, std::size_t begin, std::size_t end) {
		if (end < begin) {
			std::cout << "Second bound is smaller than the first one!" << std::endl;
			std::system("PAUSE");
			exit(1);
		}

		return str.substr(begin, end - begin);
	}

	typedef std::vector<std::string> stringArray;
}

#endif
