#include "ReadTXTFile.h"

bool allDifferent(char c1, char c2, char c3, char c4) {
    if (c1 == c2 || c2 == c3 || c3 == c4 || c1 == c3 || c2 == c4 || c1 == c4) {
        return false;
    }

    return true;
}

bool allDifferent(const std::vector<char>& chars) {
    for (std::size_t i = 0; i < chars.size(); i++) {
        for (std::size_t j = 0; j < chars.size(); j++) {
            if (i != j) {
                if (chars.at(i) == chars.at(j)) {
                    return false;
                }
            }
        }
    }

    return true;
}

int main() {
    aoc::stringArray arr = aoc::fileToStringArray("Input6.txt");

    std::size_t index = 0;

    for (std::size_t i = 0; i < arr.at(0).size() - 4; i++) {
        char c1 = arr.at(0).at(i);
        char c2 = arr.at(0).at(i + 1);
        char c3 = arr.at(0).at(i + 2);
        char c4 = arr.at(0).at(i + 3);

        if (allDifferent(c1, c2, c3, c4)) {
            index = i + 4;
            break;
        }
    }

    std::cout << "Part 1: " << index << std::endl;

    index = 0;

    std::vector<char> charArr{};
    charArr.resize(14);

    for (std::size_t i = 0; i < arr.at(0).size() - 14; i++) {
        for (std::size_t j = 0; j < 14; j++) {
            charArr.at(j) = arr.at(0).at(i + j);
        }

        if (allDifferent(charArr)) {
            index = i + 14;
            break;
        }
    }

    std::cout << "Part 2: " << index << std::endl;

    return 0;
}
