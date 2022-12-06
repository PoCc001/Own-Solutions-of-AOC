#include "ReadTXTFile.h"
#include <stack>

typedef std::vector<std::stack<std::string>> stackArray;

int main() {
    aoc::stringArray arr = aoc::fileToStringArray("Input5.txt");

    std::size_t i = 0;
    while (arr.at(i).at(1) != '1') {
        i++;
    }

    std::size_t numOfStacks = (arr.at(i).size() >> 2) + ((arr.at(i).size() & 3) != 0 ? 1 : 0);

    stackArray stacks{};
    stacks.resize(numOfStacks);

    for (int j = (int)(i - 1); j >= 0; j--) {
        std::string str = arr.at(j);
        for (std::size_t k = 0; k < numOfStacks; k++) {
            if (str.at(0) == ' ') {
                if (str.size() < 4) {
                    break;
                }

                str = str.substr(4);
                continue;
            }
            
            stacks.at(k).push(str.substr(0, 3));

            if (str.size() < 4) {
                break;
            }

            str = str.substr(4);
        }
    }

    stackArray stacksP2{};

    for (std::size_t j = 0; j < stacks.size(); j++) {
        std::stack<std::string> stack{stacks.at(j)};
        stacksP2.push_back(stack);
    }

    i += 2;

    for (std::size_t j = i; j < arr.size(); j++) {
        if (arr.at(j).size() == 0) {
            continue;
        }

        std::size_t strIndex = 5;
        std::string str = arr.at(j).substr(strIndex);

        std::size_t moveNum = std::stoul(str.substr(0, str.find_first_of(' ')));
        str = str.substr(str.find_first_of(' ') + 6);
        std::size_t fromStack = std::stoul(str.substr(0, str.find_first_of(' '))) - 1;
        str = str.substr(str.find_first_of(' ') + 4);
        std::size_t toStack = str.find_first_of(' ') != std::string::npos ? std::stoul(str.substr(0, str.find_first_of(' '))) : std::stoul(str);
        toStack--;

        for (std::size_t k = 0; k < moveNum; k++) {
            std::string s = stacks.at(fromStack).top();
            stacks.at(fromStack).pop();
            stacks.at(toStack).push(s);
        }
    }

    std::string part1String = "";

        for (std::size_t j = 0; j < stacks.size(); j++) {
            part1String += stacks.at(j).top().substr(1, 1);
        }

    std::cout << "Part 1: " << part1String << std::endl;

    for (std::size_t j = i; j < arr.size(); j++) {
        if (arr.at(j).size() == 0) {
            continue;
        }

        std::size_t strIndex = 5;
        std::string str = arr.at(j).substr(strIndex);

        std::size_t moveNum = std::stoul(str.substr(0, str.find_first_of(' ')));
        str = str.substr(str.find_first_of(' ') + 6);
        std::size_t fromStack = std::stoul(str.substr(0, str.find_first_of(' '))) - 1;
        str = str.substr(str.find_first_of(' ') + 4);
        std::size_t toStack = str.find_first_of(' ') != std::string::npos ? std::stoul(str.substr(0, str.find_first_of(' '))) : std::stoul(str);
        toStack--;

        std::stack<std::string> stack{};

        for (std::size_t k = 0; k < moveNum; k++) {
            std::string s = stacksP2.at(fromStack).top();
            stack.push(s);
            stacksP2.at(fromStack).pop();
        }

        for (std::size_t k = 0; k < moveNum; k++) {
            std::string s = stack.top();
            stacksP2.at(toStack).push(s);
            stack.pop();
        }
    }

    std::string part2String = "";

        for (std::size_t j = 0; j < stacksP2.size(); j++) {
            part2String += stacksP2.at(j).top().substr(1, 1);
        }

    std::cout << "Part 2: " << part2String << std::endl;

    return 0;
}
