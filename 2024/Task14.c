#include "ReadTXTFile.h"
#include <string.h>
#include <stdbool.h>

#define WIDTH 101
#define HEIGHT 103

size_t indexOf(const char* s, char search) {
	char c = '\0';
	size_t i = 0;
	while (c != search && i < strlen(s)) {
		c = s[i];
		i++;
	}
	return i < strlen(s) ? i - 1 : ~0;
}

bool contains(int x, int y, int* positions, size_t s) {
	for (size_t i = 0; i < s; i += 2) {
		if (positions[i] == x && positions[i + 1] == y) {
			return true;
		}
	}
	return false;
}

int main() {
	stringArray arr = fileToStringArray("Input14.txt");

	int* positions = (int*)malloc(arr.rows * 2 * sizeof(int));
	if (positions == NULL) {
		freeStringArray(&arr);
		puts("positions is null");
		return 1;
	}
	int* vectors = (int*)malloc(arr.rows * 2 * sizeof(int));
	if (vectors == NULL) {
		freeStringArray(&arr);
		free(positions);
		puts("vectors is null");
		return 1;
	}

	for (size_t i = 0; i < arr.rows; i++) {
		char* r = arr.arr[i];
		stringArray split = divideString(r, " ");
		if (split.rows < 2) {
			freeStringArray(&split);
			return 1;
		}
		stringArray split1 = divideString(split.arr[0], ",");
		stringArray split2 = divideString(split.arr[1], ",");
		if (split1.rows < 2 || split2.rows < 2) {
			freeStringArray(&split);
			freeStringArray(&split1);
			freeStringArray(&split2);
			return 1;
		}
		positions[i * 2] = strtol(split1.arr[0] + 2, NULL, 10);
		positions[i * 2 + 1] = strtol(split1.arr[1], NULL, 10);
		vectors[i * 2] = strtol(split2.arr[0] + 2, NULL, 10);
		vectors[i * 2 + 1] = strtol(split2.arr[1], NULL, 10);
		freeStringArray(&split);
		freeStringArray(&split1);
		freeStringArray(&split2);
	}

	int* newPositions = (int*)malloc(arr.rows * 2 * sizeof(int));	// newPositions are completely redundant LOL
	if (newPositions == NULL) {
		puts("newPositions is null");
		free(positions);
		free(vectors);
		freeStringArray(&arr);
		return 1;
	}

	unsigned long long q1 = 0;
	unsigned long long q2 = 0;
	unsigned long long q3 = 0;
	unsigned long long q4 = 0;

	for (size_t i = 0; i < arr.rows * 2; i += 2) {
		newPositions[i] = (positions[i] + vectors[i] * 100) % WIDTH;
		if (newPositions[i] < 0) {
			newPositions[i] = WIDTH + newPositions[i];
		}
		newPositions[i + 1] = (positions[i + 1] + vectors[i + 1] * 100) % HEIGHT;
		if (newPositions[i + 1] < 0) {
			newPositions[i + 1] = HEIGHT + newPositions[i + 1];
		}
		if (newPositions[i] < WIDTH / 2) {
			if (newPositions[i + 1] < HEIGHT / 2) {
				q1++;
			}
			else if (newPositions[i + 1] > HEIGHT / 2) {
				q3++;
			}
		}
		else if (newPositions[i] > WIDTH / 2) {
			if (newPositions[i + 1] < HEIGHT / 2) {
				q2++;
			}
			else if (newPositions[i + 1] > HEIGHT / 2) {
				q4++;
			}
		}
	}

	printf("Part 1: %llu\n", q1 * q2 * q3 * q4);

	free(newPositions);
	newPositions = NULL;

	unsigned long long i = 0;
	char buffer[HEIGHT][WIDTH + 1];
	while (true) {
		printf("Second %llu:\n", i);
		int inARow = 0;
		int maxInARow = 0;
		for (size_t j = 0; j < HEIGHT; j++) {
			for (size_t k = 0; k < WIDTH; k++) {
				if (contains(k, j, positions, arr.rows * 2)) {
					inARow++;
					buffer[j][k] = '#';
				}
				else {
					if (inARow > maxInARow) {
						maxInARow = inARow;
					}
					inARow = 0;
					buffer[j][k] = '.';
				}
			}
			inARow = 0;
			buffer[j][WIDTH] = '\0';
		}

		for (size_t j = 0; j < arr.rows * 2; j += 2) {
			positions[j] = (positions[j] + vectors[j]) % WIDTH;
			if (positions[j] < 0) {
				positions[j] = WIDTH + positions[j];
			}
			positions[j + 1] = (positions[j + 1] + vectors[j + 1]) % HEIGHT;
			if (positions[j + 1] < 0) {
				positions[j + 1] = HEIGHT + positions[j + 1];
			}
		}

		if (maxInARow >= 10) {	// with a threshold of 10, the tree should be the first picture displayed
			for (size_t j = 0; j < HEIGHT; j++) {
				puts(buffer[j]);
			}

			puts("\n\nNext?");
			char choice = getc(stdin);
			if (choice == 'n') {
				break;
			}
		}
		i++;
	}

	free(positions);
	free(vectors);
	freeStringArray(&arr);
	return 0;
}