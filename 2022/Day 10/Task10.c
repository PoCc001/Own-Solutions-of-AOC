#include "ReadTXTFile.h"

static size_t cycle = 0;
static long long x = 1;
static long long signalStrengths = 0;
static char **screen;

void drawSprite() {
    size_t xPos = cycle % 40;
    size_t yPos = cycle / 40;

    if (x == xPos || x + 1 == xPos || x - 1 == xPos) {
        screen[yPos][xPos] = '#';
    }
}

void addx(long long v) {
    cycle++;

    if (cycle == 20 || cycle == 60 || cycle == 100 || cycle == 140 || cycle == 180 || cycle == 220) {
        signalStrengths += x * cycle;
    }

    cycle++;

    if (cycle == 20 || cycle == 60 || cycle == 100 || cycle == 140 || cycle == 180 || cycle == 220) {
        signalStrengths += x * cycle;
    }

    x += v;
}

void noop() {
    cycle++;

    if (cycle == 20 || cycle == 60 || cycle == 100 || cycle == 140 || cycle == 180 || cycle == 220) {
        signalStrengths += x * cycle;
    }
}

void addx2(long long v) {
    drawSprite();
    cycle++;
    drawSprite();
    cycle++;
    x += v;
}

void noop2() {
    drawSprite();
    cycle++;
}

int main() {
    stringArray arr = fileToStringArray("Input10.txt");

    for (size_t i = 0; i < arr.rows; i++) {
        if (arr.arr[i][0] == 'a') {
            addx(strtoll(arr.arr[i] + 5, NULL, 10));
        } else {
            noop();
        }
    }

    printf("Part 1: %lli\n", signalStrengths);

    screen = (char**)(calloc(6, sizeof(char*)));

    if (screen == NULL) {
        puts("Screen is null!\nAbort!");
        freeStringArray(&arr);
        return 1;
    }

    cycle = 0;
    x = 1;

    for (size_t i = 0; i < 6; i++) {
        screen[i] = (char*)(calloc(41, sizeof(char)));

        if (screen[i] == NULL) {
            for (size_t j = 0; j < i; j++) {
                free(screen[j]);
            }

            free(screen);
            freeStringArray(&arr);
            return 1;
        }

        for (size_t j = 0; j < 40; j++) {
            screen[i][j] = '.';
        }
    }

    for (size_t i = 0; i < arr.rows; i++) {
        if (arr.arr[i][0] == 'a') {
            addx2(strtoll(arr.arr[i] + 5, NULL, 10));
        } else {
            noop2();
        }
    }

    puts("\nPart 2:");

    for (size_t i = 0; i < 6; i++) {
        puts(screen[i]);
    }

    for (size_t i = 0; i < 6; i++) {
        free(screen[i]);
    }

    free(screen);

    freeStringArray(&arr);
    return 0;
}