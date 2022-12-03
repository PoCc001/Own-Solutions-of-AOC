#include "ReadTXTFile.h"

unsigned int getPriority(char c) {
    if (c >= 'A' && c <= 'Z') {
        return (unsigned int)(c - 'A') + 27;
    } else {
        return (unsigned int)(c - 'a') + 1;
    }
}

int main() {
    aoc::stringArray arr = aoc::fileToStringArray("Input3.txt");

    std::vector<unsigned int> prioritiesOfDoubleItems{};
    prioritiesOfDoubleItems.resize(arr.size());

    for (std::size_t i = 0; i < arr.size(); i++) {
        for (std::size_t j = 0; j < (arr.at(i).size() / 2); j++) {
            for (std::size_t k = arr.at(i).size() / 2; k < arr.at(i).size(); k++) {
                if (arr.at(i).at(j) == arr.at(i).at(k)) {
                    prioritiesOfDoubleItems.at(i) = getPriority(arr.at(i).at(j));
                }
            }
        }
    }

    unsigned int priorities = 0;

    for (std::size_t i = 0; i < arr.size(); i++) {
        priorities += prioritiesOfDoubleItems.at(i);
    }

    std::cout << "Part 1: " << priorities << std::endl;

    priorities = 0;

    for (std::size_t i = 0; i < arr.size(); i += 3) {
        unsigned int priority = 0;
        for (std::size_t j = 0; j < arr.at(i).size(); j++) {
            for (std::size_t k = 0; k < arr.at(i + 1).size(); k++) {
                if (arr.at(i).at(j) == arr.at(i + 1).at(k)) {
                    for (std::size_t l = 0; l < arr.at(i + 2).size(); l++) {
                        if (arr.at(i).at(j) == arr.at(i + 2).at(l)) {
                            priority = getPriority(arr.at(i).at(j));
                        }
                    }
                }
            }
        }

        priorities += priority;
    }

    std::cout << "Part 2: " << priorities << std::endl;

    return 0;
}