#include "ReadTXTFile.h"
#include <stdio.h>
#include <stdbool.h>
#include <string.h>

bool attemptUp(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y - 1][x] == '#') {
		return false;
	}
	else if (arr->arr[y - 1][x] == 'O') {
		int i = y - 2;
		for (; i > 0; i--) {
			if (arr->arr[i][x] == '#') {
				return false;
			}
			else if (arr->arr[i][x] == '.') {
				arr->arr[i][x] = 'O';
				arr->arr[y - 1][x] = '.';
				return true;
			}
		}
		return false;
	}

	return true;
}

bool attemptRight(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y][x + 1] == '#') {
		return false;
	}
	else if (arr->arr[y][x + 1] == 'O') {
		int i = x + 2;
		for (; i < strlen(arr->arr[y]); i++) {
			if (arr->arr[y][i] == '#') {
				return false;
			}
			else if (arr->arr[y][i] == '.') {
				arr->arr[y][i] = 'O';
				arr->arr[y][x + 1] = '.';
				return true;
			}
		}
		return false;
	}

	return true;
}

bool attemptDown(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y + 1][x] == '#') {
		return false;
	}
	else if (arr->arr[y + 1][x] == 'O') {
		int i = y + 2;
		for (; i < length; i++) {
			if (arr->arr[i][x] == '#') {
				return false;
			}
			else if (arr->arr[i][x] == '.') {
				arr->arr[i][x] = 'O';
				arr->arr[y + 1][x] = '.';
				return true;
			}
		}
		return false;
	}

	return true;
}

bool attemptLeft(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y][x - 1] == '#') {
		return false;
	}
	else if (arr->arr[y][x - 1] == 'O') {
		int i = x - 2;
		for (; i > 0; i--) {
			if (arr->arr[y][i] == '#') {
				return false;
			}
			else if (arr->arr[y][i] == '.') {
				arr->arr[y][i] = 'O';
				arr->arr[y][x - 1] = '.';
				return true;
			}
		}
		return false;
	}

	return true;
}

bool tryUp2(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y - 1][x] == '#' || arr->arr[y - 1][x + 1] == '#') {
		return false;
	}
	else if (arr->arr[y - 1][x] == '[') {
		return tryUp2(x, y - 1, arr, length);
	}
	else if (arr->arr[y - 1][x] == ']') {
		bool r = tryUp2(x - 1, y - 1, arr, length);
		if (arr->arr[y - 1][x + 1] == '[') {
			return r && tryUp2(x + 1, y - 1, arr, length);
		}
		else {
			return r;
		}
	}
	if (arr->arr[y - 1][x + 1] == '[') {
		return tryUp2(x + 1, y - 1, arr, length);
	}
	return true;
}

void moveUp2(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y - 1][x] == '[') {
		moveUp2(x, y - 1, arr, length);
	}
	else if (arr->arr[y - 1][x] == ']') {
		moveUp2(x - 1, y - 1, arr, length);
	}
	if (arr->arr[y - 1][x + 1] == '[') {
		moveUp2(x + 1, y - 1, arr, length);
	}
	arr->arr[y][x] = '.';
	arr->arr[y][x + 1] = '.';
	arr->arr[y - 1][x] = '[';
	arr->arr[y - 1][x + 1] = ']';
}

bool attemptUp2(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y - 1][x] == '#') {
		return false;
	}
	else if (arr->arr[y - 1][x] == '[') {
		if (tryUp2(x, y - 1, arr, length)) {
			moveUp2(x, y - 1, arr, length);
			return true;
		}
		return false;
	}
	else if (arr->arr[y - 1][x] == ']') {
		if (tryUp2(x - 1, y - 1, arr, length)) {
			moveUp2(x - 1, y - 1, arr, length);
			return true;
		}
		return false;
	}

	return true;
}

bool attemptRight2(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y][x + 1] == '#') {
		return false;
	}
	else if (arr->arr[y][x + 1] == '[') {
		int i = x + 2;
		for (; i < strlen(arr->arr[y]); i++) {
			if (arr->arr[y][i] == '#') {
				return false;
			}
			else if (arr->arr[y][i] == '.') {
				break;
			}
		}
		arr->arr[y][x + 1] = '.';
		for (int j = x + 2; j < i; j += 2) {
			arr->arr[y][j] = '[';
			arr->arr[y][j + 1] = ']';
		}
		return true;
	}

	return true;
}

bool tryDown2(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y + 1][x] == '#' || arr->arr[y + 1][x + 1] == '#') {
		return false;
	}
	else if (arr->arr[y + 1][x] == '[') {
		return tryDown2(x, y + 1, arr, length);
	}
	else if (arr->arr[y + 1][x] == ']') {
		bool r = tryDown2(x - 1, y + 1, arr, length);
		if (arr->arr[y + 1][x + 1] == '[') {
			return r && tryDown2(x + 1, y + 1, arr, length);
		}
		else {
			return r;
		}
	}
	if (arr->arr[y + 1][x + 1] == '[') {
		return tryDown2(x + 1, y + 1, arr, length);
	}
	return true;
}

void moveDown2(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y + 1][x] == '[') {
		moveDown2(x, y + 1, arr, length);
	}
	else if (arr->arr[y + 1][x] == ']') {
		moveDown2(x - 1, y + 1, arr, length);
	}
	if (arr->arr[y + 1][x + 1] == '[') {
		moveDown2(x + 1, y + 1, arr, length);
	}
	arr->arr[y][x] = '.';
	arr->arr[y][x + 1] = '.';
	arr->arr[y + 1][x] = '[';
	arr->arr[y + 1][x + 1] = ']';
}

bool attemptDown2(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y + 1][x] == '#') {
		return false;
	}
	else if (arr->arr[y + 1][x] == '[') {
		if (tryDown2(x, y + 1, arr, length)) {
			moveDown2(x, y + 1, arr, length);
			return true;
		}
		return false;
	}
	else if (arr->arr[y + 1][x] == ']') {
		if (tryDown2(x - 1, y + 1, arr, length)) {
			moveDown2(x - 1, y + 1, arr, length);
			return true;
		}
		return false;
	}

	return true;
}

bool attemptLeft2(int x, int y, const stringArray* arr, size_t length) {
	if (arr->arr[y][x - 1] == '#') {
		return false;
	}
	else if (arr->arr[y][x - 1] == ']') {
		int i = x - 2;
		for (; i > 0; i--) {
			if (arr->arr[y][i] == '#') {
				return false;
			}
			else if (arr->arr[y][i] == '.') {
				break;
			}
		}
		arr->arr[y][x - 1] = '.';
		for (int j = i; j < x - 1; j += 2) {
			arr->arr[y][j] = '[';
			arr->arr[y][j + 1] = ']';
		}
		return true;
	}

	return true;
}

int main() {
	stringArray arr = fileToStringArray("Input15.txt");
	size_t splitIndex = 0;

	for (size_t i = 0; i < arr.rows; i++) {
		if (arr.arr[i][0] == '\0') {
			splitIndex = i;
			break;
		}
	}

	// work for part 2
	stringArray arr2;
	arr2.rows = splitIndex;
	arr2.arr = (char**)calloc(splitIndex, sizeof(char*));
	if (arr2.arr == NULL) {
		puts("arr2.arr is null");
		freeStringArray(&arr);
		return 1;
	}
	int robotX2 = 0;
	int robotY2 = 0;
	for (size_t i = 0; i < splitIndex; i++) {
		arr2.arr[i] = (char*)malloc(strlen(arr.arr[i]) * 2 + 1);
		if (arr2.arr[i] == NULL) {
			printf("arr2.arr[%llu] is null", i);
			freeStringArray(&arr2);
			freeStringArray(&arr);
			return 1;
		}
		for (size_t j = 0; j < strlen(arr.arr[i]); j++) {
			if (arr.arr[i][j] == '.') {
				arr2.arr[i][j * 2] = '.';
				arr2.arr[i][j * 2 + 1] = '.';
			}
			else if (arr.arr[i][j] == 'O') {
				arr2.arr[i][j * 2] = '[';
				arr2.arr[i][j * 2 + 1] = ']';
			}
			else if (arr.arr[i][j] == '#') {
				arr2.arr[i][j * 2] = '#';
				arr2.arr[i][j * 2 + 1] = '#';
			}
			else {
				arr2.arr[i][j * 2] = '.';
				arr2.arr[i][j * 2 + 1] = '.';
				robotX2 = j * 2;
				robotY2 = i;
			}
		}
		arr2.arr[i][strlen(arr.arr[i]) * 2] = '\0';
	}
	// end work for part 2

	size_t movementSize = 1;
	for (size_t i = splitIndex + 1; i < arr.rows; i++) {
		movementSize += strlen(arr.arr[i]);
	}

	char* movementRules = (char*)malloc(movementSize);
	if (movementRules == NULL) {
		puts("movementRules is null");
		freeStringArray(&arr);
		freeStringArray(&arr2);
		return 1;
	}

	size_t movementIndex = 0;
	for (size_t i = splitIndex + 1; i < arr.rows; i++) {
		strcpy(movementRules + movementIndex, arr.arr[i]);
		movementIndex += strlen(arr.arr[i]);
	}
	movementRules[movementSize - 1] = '\0';

	int robotX = 0;
	int robotY = 0;
	for (size_t i = 0; i < splitIndex; i++) {
		for (size_t j = 0; j < strlen(arr.arr[i]); j++) {
			if (arr.arr[i][j] == '@') {
				robotX = j;
				robotY = i;
				arr.arr[i][j] = '.';
				break;
			}
		}
		if (robotX != 0 || robotY != 0) {
			break;
		}
	}

	for (size_t i = 0; i < movementSize - 1; i++) {
		switch (movementRules[i]) {
		case '^': {
			if (attemptUp(robotX, robotY, &arr, splitIndex)) {
				robotY--;
			}
			break;
		}
		case '>': {
			if (attemptRight(robotX, robotY, &arr, splitIndex)) {
				robotX++;
			}
			break;
		}
		case 'v': {
			if (attemptDown(robotX, robotY, &arr, splitIndex)) {
				robotY++;
			}
			break;
		}
		case '<': {
			if (attemptLeft(robotX, robotY, &arr, splitIndex)) {
				robotX--;
			}
			break;
		}
		default: {
			puts("Illegal character!");
			free(movementRules);
			freeStringArray(&arr);
			freeStringArray(&arr2);
			return 1;
		}
		}
	}

	unsigned long long gps = 0;
	for (size_t i = 0; i < splitIndex; i++) {
		for (size_t j = 0; j < strlen(arr.arr[i]); j++) {
			if (arr.arr[i][j] == 'O') {
				gps += 100 * i + j;
			}
		}
	}

	printf("Part 1: %llu\n", gps);

	freeStringArray(&arr);

	for (size_t i = 0; i < movementSize - 1; i++) {
		switch (movementRules[i]) {
		case '^': {
			if (attemptUp2(robotX2, robotY2, &arr2, splitIndex)) {
				robotY2--;
			}
			break;
		}
		case '>': {
			if (attemptRight2(robotX2, robotY2, &arr2, splitIndex)) {
				robotX2++;
			}
			break;
		}
		case 'v': {
			if (attemptDown2(robotX2, robotY2, &arr2, splitIndex)) {
				robotY2++;
			}
			break;
		}
		case '<': {
			if (attemptLeft2(robotX2, robotY2, &arr2, splitIndex)) {
				robotX2--;
			}
			break;
		}
		default: {
			puts("Illegal character!");
			free(movementRules);
			freeStringArray(&arr2);
			return 1;
		}
		}
	}

	gps = 0;
	for (size_t i = 0; i < splitIndex; i++) {
		for (size_t j = 0; j < strlen(arr2.arr[i]); j++) {
			if (arr2.arr[i][j] == '[') {
				gps += 100 * i + j;
			}
		}
	}

	printf("Part 2: %llu\n", gps);

	freeStringArray(&arr2);
	free(movementRules);
	return 0;
}