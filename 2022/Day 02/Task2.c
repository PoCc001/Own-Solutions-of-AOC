#include "ReadTXTFile.h"

static int Rock = 1;
static int Paper = 2;
static int Scissors = 3;

int getRPS(char c) {
    if (c == 'A' || c == 'X') {
        return Rock;
    } else if (c == 'B' || c == 'Y') {
        return Paper;
    } else {
        return Scissors;
    }
}

int getWin(int rps1, int rps2) {
    if (rps1 == Rock) {
        if (rps2 == Paper) {
            return 6;
        } else if (rps2 == Rock) {
            return 3;
        } else {
            return 0;
        }
    } else if (rps1 == Paper) {
        if (rps2 == Paper) {
            return 3;
        } else if (rps2 == Rock) {
            return 0;
        } else {
            return 6;
        }
    } else {
        if (rps2 == Paper) {
            return 0;
        } else if (rps2 == Rock) {
            return 6;
        } else {
            return 3;
        }
    }
}

int getStrategy(int opponentStrategy, char c) {
    if (opponentStrategy == Rock) {
        if (c == 'X') {
            return Scissors;
        } else if (c == 'Y') {
            return Rock;
        } else {
            return Paper;
        }
    } else if (opponentStrategy == Paper) {
        if (c == 'X') {
            return Rock;
        } else if (c == 'Y') {
            return Paper;
        } else {
            return Scissors;
        }
    } else {
        if (c == 'X') {
            return Paper;
        } else if (c == 'Y') {
            return Scissors;
        } else {
            return Rock;
        }
    }
}

int main() {
    stringArray arr = fileToStringArray("Input2.txt");

    size_t myScore = 0;

    for (size_t i = 0; i < arr.rows; i++) {
        if (arr.arr[i][0] != '\0') {
            size_t addScore = (size_t)(getWin(arr.arr[i][0] - 'A' + 1, arr.arr[i][2] - 'X' + 1)) + (size_t)(getRPS(arr.arr[i][2]));
            myScore += addScore;
        }
    }

    printf("Part 1: %lu\n", myScore);

    myScore = 0;
    
    for (size_t i = 0; i < arr.rows; i++) {
        if (arr.arr[i][0] != '\0') {
            char sc = arr.arr[i][2];
            size_t addScore = (size_t)(sc == 'X' ? 0 : (sc == 'Y' ? 3 : 6));
            addScore += (size_t)(getStrategy(getRPS(arr.arr[i][0]), sc));
            myScore += addScore;
        }
    }

    printf("Part 2: %lu\n", myScore);

    freeStringArray(&arr);
    return 0;
}