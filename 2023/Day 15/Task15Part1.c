#include "ReadTXTFile.h"
#include <string.h>
#include <stdint.h>

int main() {
    stringArray arr = fileToStringArray("Input15.txt");
    stringArray parts = divideString(arr.arr[0], ",");
    freeStringArray(&arr);
    uint64_t sum = 0;
    for (size_t i = 0; i < parts.rows; i++) {
        uint64_t hash = 0;
        for (size_t j = 0; j < strlen(parts.arr[i]); j++) {
            hash += parts.arr[i][j];
            hash *= 17;
            hash &= 255;
        }
        sum += hash;
    }

    printf("Part 1: %llu\n", sum);

    freeStringArray(&parts);

    return 0;
}
