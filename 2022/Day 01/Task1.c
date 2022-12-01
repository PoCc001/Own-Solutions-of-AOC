#include "ReadTXTFile.h"        // is in the root directory of the repository

int main() {
    stringArray str = fileToStringArray("Input1.txt");

    size_t numOfElves = 1;

    for (size_t i = 0; i < str.rows; i++) {
        if (str.arr[i][0] == '\0') {
            numOfElves++;
        }
    }

    numOfElves--;

    size_t *elves = (size_t*)(calloc(numOfElves, sizeof(size_t)));
    
    if (elves == NULL) {
        puts("Elves are null!");
        freeStringArray(&str);
        return 1;
    }

    size_t elfIndex = 0;
    for (size_t i = 0; i < str.rows; i++) {
        if (str.arr[i][0] == '\0') {
            elfIndex++;
            continue;
        }

        size_t load = strtol(str.arr[i], NULL, 10);
        elves[elfIndex] += load;
    }

    size_t maxLoad = 0;

    for (size_t i = 0; i < numOfElves; i++) {
        if (elves[i] > maxLoad) {
            maxLoad = elves[i];
        }
    }

    printf("Part 1: %lu\n", maxLoad);

    size_t secondMaxLoad = 0;
    for (size_t i = 0; i < numOfElves; i++) {
        if (elves[i] > secondMaxLoad && elves[i] != maxLoad) {
            secondMaxLoad = elves[i];
        }
    }

    size_t thirdMaxLoad = 0;
    for (size_t i = 0; i < numOfElves; i++) {
        if (elves[i] > thirdMaxLoad && elves[i] != maxLoad && elves[i] != secondMaxLoad) {
            thirdMaxLoad = elves[i];
        }
    }

    printf("Part 2: %lu\n", maxLoad + secondMaxLoad + thirdMaxLoad);

    freeStringArray(&str);
    free(elves);

    return 0;
}
