package aocjava;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task10 {
    private record Coordinate(int x, int y) {
        Coordinate up() {
            return new Coordinate(x, y - 1);
        }

        Coordinate right() {
            return new Coordinate(x + 1, y);
        }

        Coordinate down() {
            return new Coordinate(x, y + 1);
        }

        Coordinate left() {
            return new Coordinate(x - 1, y);
        }
    }
    static char getUpper(List<String> list, int x, int y) {
        if (y == 0) {
            return '.';
        } else {
            return list.get(y - 1).charAt(x);
        }
    }

    static char getRight(List<String> list, int x, int y) {
        if (x == list.get(y).length() - 1) {
            return '.';
        } else {
            return list.get(y).charAt(x + 1);
        }
    }

    static char getBelow(List<String> list, int x, int y) {
        if (y == list.size() - 1) {
            return '.';
        } else {
            return list.get(y + 1).charAt(x);
        }
    }

    static char getLeft(List<String> list, int x, int y) {
        if (x == 0) {
            return '.';
        } else {
            return list.get(y).charAt(x - 1);
        }
    }

    static void traverse(List<String> list, int[] indices, int[] prevIndices, char c, List<int[]> coords) {
        char next = c;
        while (next != 'S') {
            switch (next) {
                case '|' -> {
                    if (prevIndices[1] < indices[1]) {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[1]++;
                    } else {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[1]--;
                    }
                }
                case '-' -> {
                    if (prevIndices[0] < indices[0]) {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[0]++;
                    } else {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[0]--;
                    }
                }
                case '7' -> {
                    if (prevIndices[0] != indices[0]) {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[1]++;
                    } else {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[0]--;
                    }
                }
                case 'F' -> {
                    if (prevIndices[0] != indices[0]) {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[1]++;
                    } else {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[0]++;
                    }
                }
                case 'J' -> {
                    if (prevIndices[0] != indices[0]) {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[1]--;
                    } else {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[0]--;
                    }
                }
                case 'L' -> {
                    if (prevIndices[0] != indices[0]) {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[1]--;
                    } else {
                        prevIndices = Arrays.copyOf(indices, 2);
                        indices[0]++;
                    }
                }
            }
            next = list.get(indices[1]).charAt(indices[0]);
            coords.add(Arrays.copyOf(indices, 2));
        }
    }

    static List<int[]> parseCoordinates(List<String> stringList, int[] indices) {
        List<int[]> coords = new ArrayList<>();
        coords.add(indices);

        char next = getUpper(stringList, indices[0], indices[1]);
        switch (next) {
            case '|', 'F', '7' -> {
                indices[1]--;
                coords.add(Arrays.copyOf(indices, 2));
                traverse(stringList, indices, new int[] {indices[0], indices[1] + 1}, next, coords);
            }
            default -> {
                next = getRight(stringList, indices[0], indices[1]);
                switch (next) {
                    case '-', '7', 'J' -> {
                        indices[0]++;
                        coords.add(Arrays.copyOf(indices, 2));
                        traverse(stringList, indices, new int[] {indices[0] - 1, indices[1]}, next, coords);
                    }
                    default -> {
                        next = getBelow(stringList, indices[0], indices[1]);
                        switch (next) {
                            case '|', 'J', 'L' -> {
                                indices[1]++;
                                coords.add(Arrays.copyOf(indices, 2));
                                traverse(stringList, indices, new int[] {indices[0], indices[1] - 1}, next, coords);
                            }
                            default -> {
                                next = getLeft(stringList, indices[0], indices[1]);
                                switch (next) {
                                    case '-', 'J', '7' -> {
                                        indices[0]--;
                                        coords.add(Arrays.copyOf(indices, 2));
                                        traverse(stringList, indices, new int[] {indices[0] - 1, indices[1]}, next, coords);
                                    }
                                    default -> throw new IllegalStateException();
                                }
                            }
                        }
                    }
                }
            }
        }
        return coords;
    }

    static boolean tryToEscape(Coordinate c, Set<Coordinate> pipes, int maxX, int maxY) {
        Set<Coordinate> visited = new HashSet<>(pipes);
        visited.add(c);
        Queue<Coordinate> next = new ArrayDeque<>(Set.of(c.up(), c.right(), c.down(), c.left()));
        next.removeIf(visited::contains);
        while (!next.isEmpty()) {
            Coordinate nextCoordinate = next.poll();
            if (nextCoordinate.x == 0 || nextCoordinate.y == 0 || nextCoordinate.x == maxX || nextCoordinate.y == maxY) {
                return true;
            }
            visited.add(nextCoordinate);
            next.addAll(Stream.of(nextCoordinate.up(), nextCoordinate.right(), nextCoordinate.down(), nextCoordinate.left()).filter(co -> !visited.contains(co)).toList());
            next = next.stream().distinct().collect(Collectors.toCollection(ArrayDeque::new));
        }
        return false;
    }

    static List<String> widen(List<String> list) {
        List<String> widened = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            widened.add("");
            widened.add("");
            widened.add("");
            for (int j = 0; j < list.get(i).length(); j++) {
                switch (list.get(i).charAt(j)) {
                    case '.' -> {
                        widened.set(i * 3, widened.get(i * 3) + "...");
                        widened.set(i * 3 + 1, widened.get(i * 3 + 1) + "...");
                        widened.set(i * 3 + 2, widened.get(i * 3 + 2) + "...");
                    }
                    case '-' -> {
                        widened.set(i * 3, widened.get(i * 3) + "...");
                        widened.set(i * 3 + 1, widened.get(i * 3 + 1) + "---");
                        widened.set(i * 3 + 2, widened.get(i * 3 + 2) + "...");
                    }
                    case '|' -> {
                        widened.set(i * 3, widened.get(i * 3) + ".|.");
                        widened.set(i * 3 + 1, widened.get(i * 3 + 1) + ".|.");
                        widened.set(i * 3 + 2, widened.get(i * 3 + 2) + ".|.");
                    }
                    case 'L' -> {
                        widened.set(i * 3, widened.get(i * 3) + ".|.");
                        widened.set(i * 3 + 1, widened.get(i * 3 + 1) + ".--");
                        widened.set(i * 3 + 2, widened.get(i * 3 + 2) + "...");
                    }
                    case 'J' -> {
                        widened.set(i * 3, widened.get(i * 3) + ".|.");
                        widened.set(i * 3 + 1, widened.get(i * 3 + 1) + "--.");
                        widened.set(i * 3 + 2, widened.get(i * 3 + 2) + "...");
                    }
                    case '7' -> {
                        widened.set(i * 3, widened.get(i * 3) + "...");
                        widened.set(i * 3 + 1, widened.get(i * 3 + 1) + "--.");
                        widened.set(i * 3 + 2, widened.get(i * 3 + 2) + ".|.");
                    }
                    case 'F' -> {
                        widened.set(i * 3, widened.get(i * 3) + "...");
                        widened.set(i * 3 + 1, widened.get(i * 3 + 1) + ".--");
                        widened.set(i * 3 + 2, widened.get(i * 3 + 2) + ".|.");
                    }
                    case 'S' -> {
                        widened.set(i * 3, widened.get(i * 3) + "---");
                        widened.set(i * 3 + 1, widened.get(i * 3 + 1) + "|X|");
                        widened.set(i * 3 + 2, widened.get(i * 3 + 2) + "---");
                    }
                }
            }
        }
        return widened;
    }

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input10.txt");

        int[] indices = new int[2];
        for (int i = 0; i < stringList.size(); i++) {
            for (int j = 0; j < stringList.get(i).length(); j++) {
                if (stringList.get(i).charAt(j) == 'S') {
                    indices[0] = j;
                    indices[1] = i;
                    break;
                }
            }
        }

        List<int[]> coords = parseCoordinates(stringList, indices);
        coords.remove(coords.size() - 1);
        int distance = (coords.size() + 1) >> 1;

        System.out.println("Part 1: " + distance);
        List<Coordinate> coordinateList = coords.stream().map(co -> new Coordinate(co[0], co[1])).toList();
        Set<Coordinate> dotSet = new HashSet<>();

        for (int i = 0; i < stringList.size(); i++) {
            for (int j = 0; j < stringList.get(i).length(); j++) {
                if (stringList.get(i).charAt(j) == '.' || !coordinateList.contains(new Coordinate(j, i))) {
                    dotSet.add(new Coordinate(j, i));
                }
            }
        }

        int coordinateCount = dotSet.size();

        Set<Coordinate> outsideSet = new HashSet<>();
        Set<Coordinate> coordsSet = new HashSet<>();
        List<String> widened = widen(stringList);
        int maxX = widened.get(0).length() - 1;
        int maxY = widened.size() - 1;
        for (int i = 0; i < widened.size(); i++) {
            for (int j = 0; j < widened.get(i).length(); j++) {
                if (widened.get(i).charAt(j) != '.' && coordinateList.contains(new Coordinate(j / 3, i / 3))) {
                    coordsSet.add(new Coordinate(j, i));
                }
            }
        }

        // This takes a few minutes!
        for (Coordinate coordinate : dotSet) {
            Coordinate outOrIn = new Coordinate(coordinate.x * 3, coordinate.y * 3);
            if (tryToEscape(outOrIn, coordsSet, maxX, maxY)) {
                outsideSet.add(outOrIn);
            }
        }

        int result = coordinateCount - outsideSet.size();
        System.out.println("Part 2: " + result);
    }
}
