package aocjava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task01Part2 {
    private static final Map<String, Integer> stringIntMap1 = Map.of(
            "0", 0,
            "1", 1,
            "2", 2,
            "3", 3,
            "4", 4,
            "5", 5,
            "6", 6,
            "7", 7,
            "8", 8,
            "9", 9
    );
    private static final Map<String, Integer> stringIntMap2 = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input01.txt");
        List<Integer> numbers = new ArrayList<>();
        Map<String, Integer> stringIntMap = new HashMap<>(stringIntMap1);
        stringIntMap.putAll(stringIntMap2);
        for (String s : stringList) {
            int index = Integer.MAX_VALUE;
            int digit = 0;
            for (Map.Entry<String, Integer> entry : stringIntMap.entrySet()) {
                int i = s.indexOf(entry.getKey());
                if (i != -1 && index > i) {
                    index = i;
                    digit = entry.getValue() * 10;
                }
            }
            index = -1;
            int d = 0;
            for (Map.Entry<String, Integer> entry : stringIntMap.entrySet()) {
                int i = s.lastIndexOf(entry.getKey());
                if (index < i) {
                    index = i;
                    d = entry.getValue();
                }
            }
            numbers.add(digit + d);
        }

        System.out.println("Part 2: " + numbers.stream().mapToLong(i -> (long)i).sum());
    }
}
