#ifndef READTXTFILE_H
#define READTXTFILE_H

#ifdef __cplusplus
#include <fstream>
#include <iostream>
#include <string>
#include <vector>

namespace aoc {
    typedef std::vector<std::string> stringArray;
    
    stringArray fileToStringArray(const std::string& name) {
        std::ifstream in(name, std::ios::in);
        
        if (!in) {
            std::cout << "File \"" << name << "\" doesn't exist!\nAbort!" << std::endl;
            in.close();
            exit(1);
        } else {
            std::size_t s = 100;
            stringArray v{};
            v.reserve(s);
            v.resize(1);
            
            while (std::getline(in, v[v.size() - 1])) {
                v.resize(v.size() + 1);
            }
            
            in.close();
            v.resize(v.size() - 1);
            return v;
        }
    }
    
    std::string substrIndizes(const std::string& str, std::size_t begin, std::size_t end) {
        if (end < begin) {
            std::cout << "Second bound is smaller than the first one!" << std::endl;
            exit(1);
        }
        
        return str.substr(begin, end - begin);
    }

    stringArray divideString(const std::string& str, const std::string& del) {
        stringArray arr{};
        std::string sub = str;
        std::size_t p = sub.find_first_of(del);
        while (p != std::string::npos) {
            arr.push_back(substrIndizes(sub, 0, p));
            sub = sub.substr(p + del.length());
            p = sub.find_first_of(del);
        }

        if (sub.length() > 0) {
            arr.push_back(sub);
        }

        return arr;
    }
}
#else
#include <stdio.h>
#include <stdlib.h>

#if defined(_MSC_VER)
#pragma warning(disable : 4996)
#endif

typedef struct stringArrayType {
    size_t rows;
    char** arr;
} stringArray;

void freeStringArray(stringArray* arr) {
    if (arr != NULL) {
        if (arr->arr != NULL) {
            for (size_t i = 0; i < arr->rows; i++) {
                if (arr->arr[i] != NULL) {
                    free(arr->arr[i]);
                }
            }
    
            free(arr->arr);
        }
        arr->arr = NULL;
        arr->rows = 0;
    }
}

stringArray fileToStringArray(const char* name) {
    FILE* file = fopen(name, "r");
    
    if (file == NULL) {
        printf("File \"%s\" doesn't exist!\nAbort!\n", name);
        exit(1);
    } else {
        size_t byteSize = 0;
        size_t lines = 1;
        int c = fgetc(file);
        
        while (c != EOF) {
            byteSize++;
            
            if ((char)(c) == '\n') {
                lines++;
            }
            
            c = fgetc(file);
        }
        
        if (fseek(file, 0, SEEK_SET)) {
            puts("Seeking unsuccessful!\nAbort!\n");
            fclose(file);
            exit(1);
        }
        
        char* arr = (char*)(calloc(byteSize, sizeof(char)));

        if (arr == NULL) {
            puts("Memory allocation failed!\nAbort!\n");
            fclose(file);
            exit(1);
        }
        
        c = fgetc(file);
        size_t i = 0;
        
        while (c != EOF && i < byteSize) {
            arr[i] = (char)(c);
            i++;
            c = fgetc(file);
        }
        
        fclose(file);
        
        if (c != EOF || i < byteSize) {
            puts("Was the file changed during reading?\nThat shouldn't happen anyways, right? - Right?\nAbort!\n");
            free(arr);
            exit(1);
        }
        
        stringArray strArr = {lines, (char**)(calloc(lines, sizeof(char*)))};

        if (strArr.arr == NULL) {
            puts("Memory allocation failed!\nAbort!\n");
            free(arr);
            exit(1);
        }
        
        size_t gi = 0;
        size_t rowSize = 0;
        for (i = 0; i < lines; i++) {
            rowSize = 0;
            while (arr[gi] != '\n' && gi < byteSize) {
                rowSize++;
                gi++;
            }

            gi++;

            strArr.arr[i] = (char*)(calloc(rowSize + 1, sizeof(char)));
        }
        
        gi = 0;
        for (i = 0; i < lines; i++) {
            size_t col = 0;
            while (arr[gi] != '\n' && gi < byteSize) {
                if (strArr.arr[i] == NULL) {
                    puts("Memory allocation failed!\nAbort!\n");
                    freeStringArray(&strArr);
                    free(arr);
                    exit(1);
                }

                strArr.arr[i][col] = arr[gi];
                col++;
                gi++;
            }

            gi++;
        }

        free(arr);
        
        return strArr;
    }
}

char* getString(const stringArray* strArr, size_t r) {
    if (strArr == NULL) {
        return NULL;
    }

    if (r >= strArr->rows) {
        return NULL;
    } else {
        return strArr->arr[r];
    }
}

char* getSubstring(const char* str, size_t begin, size_t end) {
    if (str == NULL || begin > end) {
        return NULL;
    } else {
        size_t i = 0;
        while (str[i] != '\0' && i < begin) {
            i++;
        }

        if (str[i] == '\0') {
            return NULL;
        }

        while (str[i] != '\0' && i < end) {
            i++;
        }

        char* substr = (char*)(calloc(i - begin + 2, sizeof(char)));

        for (size_t j = 0; j < i - begin + 1; j++) {
            substr[j] = str[begin + j];
        }

        return substr;
    }
}

stringArray divideString(const char* str, const char* del) {    
        size_t dellength = 0;
        while (del[dellength] != '\0') {
            dellength++;
        }

        size_t stringCount = 1;
        size_t i = 0;
        while (str[i] != '\0') {
            if (str[i] == del[0]) {
                stringCount++;
                i += dellength;
            }
            i++;
        }

        stringArray arr = {stringCount, (char**)(calloc(stringCount, sizeof(char*)))};

        size_t gi = 0;
        for (i = 0; i < stringCount; i++) {
            size_t wordSize = 0;
            while (str[gi] != del[0] && str[gi] != '\0') {
                wordSize++;
                gi++;
            }

            gi += dellength;

            arr.arr[i] = (char*)(calloc(wordSize + 1, sizeof(char)));
        }

        gi = 0;

        for (i = 0; i < stringCount; i++) {
            size_t wordIndex = 0;
            while (str[gi] != del[0] && str[gi] != '\0') {
                arr.arr[i][wordIndex] = str[gi];
                wordIndex++;
                gi++;
            }

            gi += dellength;
        }

        return arr;
    }
#endif

#endif
