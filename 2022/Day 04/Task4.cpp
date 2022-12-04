#include "ReadTXTFile.h"

bool isContained(unsigned int e1L, unsigned int e1H, unsigned int e2L, unsigned int e2H) {
    return (e1L <= e2L && e1H >= e2H) || (e1L >= e2L && e1H <= e2H);
}

bool isOverlapped(unsigned int e1L, unsigned int e1H, unsigned int e2L, unsigned int e2H) {
    return (e1L <= e2L && e1H >= e2L) || (e2L <= e1L && e2H >= e1L);
}

bool isContained(const std::string& str) {              // didn't use aoc::divideString() because I thought it was bugged (when it probably wasn't).
    std::string s1 = str.substr(0, str.find_first_of('-'));
    std::string s2 = str.substr(str.find_first_of('-') + 1);
    s2 = s2.substr(0, s2.find_first_of(','));
    std::string s3 = str.substr(str.find_first_of(',') + 1);
    std::string s4 = s3.substr(s3.find_first_of('-') + 1);
    s3 = s3.substr(0, s3.find_first_of('-'));

    unsigned int elf1Low = (unsigned int)(std::stoul(s1));
    unsigned int elf1High = (unsigned int)(std::stoul(s2));
    unsigned int elf2Low = (unsigned int)(std::stoul(s3));
    unsigned int elf2High = (unsigned int)(std::stoul(s4));

    return isContained(elf1Low, elf1High, elf2Low, elf2High);
}

bool isOverlapped(const std::string& str) {             // didn't use aoc::divideString() because I thought it was bugged (when it probably wasn't).
    std::string s1 = str.substr(0, str.find_first_of('-'));
    std::string s2 = str.substr(str.find_first_of('-') + 1);
    s2 = s2.substr(0, s2.find_first_of(','));
    std::string s3 = str.substr(str.find_first_of(',') + 1);
    std::string s4 = s3.substr(s3.find_first_of('-') + 1);
    s3 = s3.substr(0, s3.find_first_of('-'));

    unsigned int elf1Low = (unsigned int)(std::stoul(s1));
    unsigned int elf1High = (unsigned int)(std::stoul(s2));
    unsigned int elf2Low = (unsigned int)(std::stoul(s3));
    unsigned int elf2High = (unsigned int)(std::stoul(s4));

    return isOverlapped(elf1Low, elf1High, elf2Low, elf2High);
}

int main() {
    aoc::stringArray arr = aoc::fileToStringArray("Input4.txt");

    std::size_t count = 0;

    for (std::size_t i = 0; i < arr.size(); i++) {
        if (isContained(arr.at(i))) {
            count++;
        }
    }

    std::cout << "Part 1: " << count << std::endl;

    count = 0;

    for (std::size_t i = 0; i < arr.size(); i++) {
        if (isOverlapped(arr.at(i))) {
            count++;
        }
    }

    std::cout << "Part 2: " << count << std::endl;

    aoc::stringArray s = aoc::divideString(arr.at(0), ",");

    return 0;
}
