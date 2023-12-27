#include "ReadTXTFile.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct {
    int x;
    int y;
    size_t dist;
} coord_t;

typedef struct {
    coord_t* coords;
    size_t length;
} fringe_t;

#define STEPS1 64
coord_t visitedCoords[4 * STEPS1 * STEPS1];
coord_t fringeCoords[4 * STEPS1 * STEPS1];
size_t visitedCoordsCount = 0;

size_t coordinateIsInArray(coord_t*, coord_t*, size_t);
void initFringe(fringe_t*);
void addToFringe(fringe_t*, coord_t*);
coord_t removeFromFringe(fringe_t*);
void freeFringe(fringe_t*);
coord_t up(coord_t*);
coord_t right(coord_t*);
coord_t down(coord_t*);
coord_t left(coord_t*);

int main() {
    stringArray arr = fileToStringArray("Input21.txt");
    size_t wallCount = 0;
    for (size_t i = 0; i < arr.rows; i++) {
        for (size_t j = 0; j < strlen(arr.arr[i]); j++) {
            if (arr.arr[i][j] == '#') {
                wallCount++;
            }
        }
    }

    coord_t* walls = malloc(sizeof(coord_t) * wallCount);
    if (walls == NULL) {
        puts("walls is NULL!\nAbort!");
        return 1;
    }
    fringe_t fringe;
    coord_t start = {-1, -1, 0};
    initFringe(&fringe);
    size_t wallIndex = 0;
    for (size_t i = 0; i < arr.rows; i++) {
        for (size_t j = 0; j < strlen(arr.arr[i]); j++) {
            if (arr.arr[i][j] == '#') {
                coord_t wall = {j, i, 0};
                walls[wallIndex] = wall;
                wallIndex++;
            } else if (arr.arr[i][j] == 'S') {
                coord_t startCoord = {j, i, 0};
                start = startCoord;
                addToFringe(&fringe, &start);
            }
        }
    }

    while (fringe.length != 0) {
        coord_t current = removeFromFringe(&fringe);
        if (coordinateIsInArray(&current, visitedCoords, visitedCoordsCount) == ~(size_t)0) {
            visitedCoords[visitedCoordsCount] = current;
            visitedCoordsCount++;
        }
        coord_t u = up(&current);
        if (u.dist <= STEPS1 && coordinateIsInArray(&u, visitedCoords, visitedCoordsCount) == ~(size_t)0 && coordinateIsInArray(&u, walls, wallCount) == ~(size_t)0) {
            addToFringe(&fringe, &u);
        }

        coord_t r = right(&current);
        if (r.dist <= STEPS1 && coordinateIsInArray(&r, visitedCoords, visitedCoordsCount) == ~(size_t)0 && coordinateIsInArray(&r, walls, wallCount) == ~(size_t)0) {
            addToFringe(&fringe, &r);
        }

        coord_t d = down(&current);
        if (d.dist <= STEPS1 && coordinateIsInArray(&d, visitedCoords, visitedCoordsCount) == ~(size_t)0 && coordinateIsInArray(&d, walls, wallCount) == ~(size_t)0) {
            addToFringe(&fringe, &d);
        }

        coord_t l = left(&current);
        if (l.dist<= STEPS1 && coordinateIsInArray(&l, visitedCoords, visitedCoordsCount) == ~(size_t)0 && coordinateIsInArray(&l, walls, wallCount) == ~(size_t)0) {
            addToFringe(&fringe, &l);
        }
    }

    size_t result = 0;

    for (size_t i = 0; i < visitedCoordsCount; i++) {
        if ((visitedCoords[i].x + visitedCoords[i].y - start.x - start.y) % 2 == 0) {
            result++;
        }
    }

    printf("Part 1: %llu\n", result);

    free(walls);
    freeStringArray(&arr);

    return 0;
}

size_t coordinateIsInArray(coord_t* coordToFind, coord_t* arr, size_t arrSize) {
    for (size_t i = 0; i < arrSize; i++) {
        if (coordToFind->x == arr[i].x && coordToFind->y == arr[i].y) {
            return i;
        }
    }
    return ~(size_t)0;
}

void initFringe(fringe_t* fringe) {
    fringe->length = 0;
    fringe->coords = fringeCoords;
    if (fringe->coords == NULL) {
        puts("fringe->coords is NULL!\nAbort!");
        exit(1);
    }
}

void addToFringe(fringe_t* fringe, coord_t* coord) {
    size_t index = coordinateIsInArray(coord, fringe->coords, fringe->length);
    if (index != ~(size_t)0) {
        if (fringe->coords[index].dist > coord->dist) {
            fringe->coords[index] = *coord;
        }
    } else {
        fringe->coords[fringe->length] = *coord;
        fringe->length++;
    }
}

coord_t removeFromFringe(fringe_t* fringe) {
    if (fringe->length == 0) {
        puts("Fringe is empty!\nAbort!");
        exit(1);
    }
    coord_t c = fringe->coords[0];
    fringe->length--;
    memmove(fringe->coords, fringe->coords + 1, fringe->length * sizeof(coord_t));
    return c;
}

coord_t up(coord_t* origin) {
    coord_t u = {origin->x, origin->y - 1, origin->dist + 1};
    return u;
}

coord_t right(coord_t* origin) {
    coord_t r = {origin->x + 1, origin->y, origin->dist + 1};
    return r;
}

coord_t down(coord_t* origin) {
    coord_t d = {origin->x, origin->y + 1, origin->dist + 1};
    return d;
}

coord_t left(coord_t* origin) {
    coord_t l = {origin->x - 1, origin->y, origin->dist + 1};
    return l;
}
