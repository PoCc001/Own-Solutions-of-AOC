package aocjava;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Task11 {
    private record Coordinate(int x, int y) {
        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input11.txt");

        List<String> expanded = new ArrayList<>();

        for (String s : stringList) {
            if (s.indexOf('#') == -1) {
                expanded.add(s);
            }
            expanded.add(s);
        }

        for (int i = 0; i < expanded.get(0).length(); i++) {
            final int fi = i;
            String s = expanded.stream().map(str -> String.valueOf(str.charAt(fi))).reduce("", (str, c) -> str + c);
            if (s.indexOf('#') == -1) {
                for (int j = 0; j < expanded.size(); j++) {
                    String str = expanded.get(j);
                    String str1 = str.substring(0, i);
                    String str2 = str.substring(i);
                    expanded.set(j, str1 + '.' + str2);
                }
                i++;
            }
        }

        Set<Coordinate> galaxyCoordinates = new HashSet<>();

        for (int i = 0; i < expanded.size(); i++) {
            String s = expanded.get(i);
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '#') {
                    galaxyCoordinates.add(new Coordinate(j, i));
                }
            }
        }

        Set<Set<Coordinate>> pairs = new HashSet<>();

        for (Coordinate c : galaxyCoordinates) {
            for (Coordinate other : galaxyCoordinates.stream().filter(co -> !co.equals(c)).collect(Collectors.toSet())) {
                Set<Coordinate> pair = new HashSet<>();
                pair.add(c);
                pair.add(other);
                pairs.add(pair);
            }
        }

        long result = 0;

        for (List<Coordinate> pair : pairs.stream().map(s -> s.stream().toList()).collect(Collectors.toSet())) {
            Coordinate c1 = pair.get(0);
            Coordinate c2 = pair.get(1);
            result += Math.abs(c1.x - c2.x) + Math.abs(c1.y - c2.y);
        }

        System.out.println("Part 1: " + result);

        final int factor = 1000000;

        Set<Integer> expandedRows = new HashSet<>();
        for (int i = 0; i < stringList.size(); i++) {
            if (stringList.get(i).indexOf('#') == -1) {
                expandedRows.add(i);
            }
        }
        Set<Integer> expandedColumns = new HashSet<>();
        for (int i = 0; i < stringList.get(0).length(); i++) {
            final int fi = i;
            String s = stringList.stream().map(str -> String.valueOf(str.charAt(fi))).reduce("", (str, c) -> str + c);
            if (s.indexOf('#') == -1) {
                expandedColumns.add(i);
            }
        }

        galaxyCoordinates = new HashSet<>();

        for (int i = 0; i < stringList.size(); i++) {
            String s = stringList.get(i);
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '#') {
                    galaxyCoordinates.add(new Coordinate(j, i));
                }
            }
        }

        pairs = new HashSet<>();

        for (Coordinate c : galaxyCoordinates) {
            for (Coordinate other : galaxyCoordinates.stream().filter(co -> !co.equals(c)).collect(Collectors.toSet())) {
                Set<Coordinate> pair = new HashSet<>();
                pair.add(c);
                pair.add(other);
                pairs.add(pair);
            }
        }

        result = 0;

        for (List<Coordinate> pair : pairs.stream().map(s -> s.stream().toList()).collect(Collectors.toSet())) {
            Coordinate c1 = pair.get(0);
            Coordinate c2 = pair.get(1);
            long countRows = expandedRows.stream().filter(y -> (c1.y > y && c2.y < y) || (c2.y > y && c1.y < y)).count();
            long countCols = expandedColumns.stream().filter(x -> (c1.x > x && c2.x < x) || (c2.x > x && c1.x < x)).count();
            result += Math.abs(c1.x - c2.x) + countRows * (factor - 1) + Math.abs(c1.y - c2.y) + countCols * (factor - 1);
        }

        System.out.println("Part 2: " + result);
    }
}
