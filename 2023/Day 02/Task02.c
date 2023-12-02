#include "AOCUtils.h"
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <math.h>

#define MAX(x, y) ((x) < (y) ? (y) : (x))

size_t indexOf(const char* s, char search) {
    char c = '\0';
    size_t i = 0;
    while (c != search && i < strlen(s)) {
        c = s[i];
        i++;
    }
    return i < strlen(s) ? i - 1 : ~0;
}

int main() {
    stringArray arr = fileToStringArray("/home/johannes/Schreibtisch/AdventOfCode/Inputs/Input02.txt");
    size_t sumOfGameIds = 0;
    size_t sumOfPowers = 0;
    for (size_t i = 0; i < arr.rows; i++) {
        if (strlen(arr.arr[i]) == 0) {
            continue;
        }
        char* gameId = getSubstring(arr.arr[i], 5, indexOf(arr.arr[i], ':'));
        if (gameId == NULL) {
            puts("gameId is NULL!\nOh noo!");
            return 1;
        }
        size_t gameIdInt = strtoull(gameId, NULL, 10);
        free(gameId);
        char* game = getSubstring(arr.arr[i], indexOf(arr.arr[i], ':') + 2, strlen(arr.arr[i]));
        stringArray gameDivided = divideString(game, "; ");
        free(game);
        bool gameIsPossible = true;
        size_t gameWideNums[3] = { 0 };
        for (size_t j = 0; j < gameDivided.rows; j++) {
            stringArray divided = divideString(gameDivided.arr[j], ", ");
            size_t nums[3] = { 0 };
            for (size_t k = 0; k < divided.rows; k++) {
                size_t num = strtoull(getSubstring(divided.arr[k], 0, indexOf(divided.arr[k], ' ')), NULL, 10);
                char color = divided.arr[k][indexOf(divided.arr[k], ' ') + 1];
                if (color == 'r') {
                    nums[0] += num;
                } else if (color == 'g') {
                    nums[1] += num;
                } else {
                    nums[2] += num;
                }
            }
            gameWideNums[0] = MAX(gameWideNums[0], nums[0]);
            gameWideNums[1] = MAX(gameWideNums[1], nums[1]);
            gameWideNums[2] = MAX(gameWideNums[2], nums[2]);
            freeStringArray(&divided);
            gameIsPossible = nums[0] <= 12 && nums[1] <= 13 && nums[2] <= 14 && gameIsPossible;
        }
        sumOfPowers += gameWideNums[0] * gameWideNums[1] * gameWideNums[2];
        freeStringArray(&gameDivided);
        if (gameIsPossible) {
            sumOfGameIds += gameIdInt;
        }
    }

    printf("Part 1: %llu\n", sumOfGameIds);
    printf("Part 2: %llu\n", sumOfPowers);

    freeStringArray(&arr);

    return 0;
}
