package aocjava;

import java.util.*;
import java.util.stream.Collectors;

public class Task03 {
    private static boolean checkLine(String l) {
        return l.chars().allMatch(c -> c == '.');
    }
    private static boolean isAdjacent(String rowAbove, String thisRow, String rowBelow) {
        boolean aboveGood = rowAbove == null;
        boolean thisGood = (thisRow.charAt(0) == '.' || Character.isDigit(thisRow.charAt(0))) &&
                (thisRow.charAt(thisRow.length() - 1) == '.' || Character.isDigit(thisRow.charAt(thisRow.length() - 1)));
        boolean belowGood = rowBelow == null;

        return !((aboveGood || checkLine(rowAbove)) && thisGood && (belowGood || checkLine(rowBelow)));
    }

    private record Range(int start, int exclEnd) {
        boolean isInRange(int n) {
            return n >= start && n < exclEnd;
        }

        boolean atLeastOneInRange(int... ns) {
            for (int n : ns) {
                if (isInRange(n)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input03.txt");
        Map<Range, Long> partNumbers = new HashMap<>();
        for (int i = 0; i < stringList.size(); i++) {
            int index = 0;
            while (index < stringList.get(i).length()) {
                while (index < stringList.get(i).length() && !Character.isDigit(stringList.get(i).charAt(index))) {
                    index++;
                }
                int digitsStartIndex = index;
                while (index < stringList.get(i).length() && Character.isDigit(stringList.get(i).charAt(index))) {
                    index++;
                }
                int digitsEndIndex = index;
                String rowAbove = i == 0 ? null : stringList.get(i - 1).
                        substring(digitsStartIndex == 0 ? 0 : digitsStartIndex - 1, Math.min(digitsEndIndex + 1, stringList.get(i).length()));
                String thisRow = stringList.get(i).substring(digitsStartIndex == 0 ? 0 : digitsStartIndex - 1, Math.min(digitsEndIndex + 1, stringList.get(i).length()));
                String rowBelow = i == stringList.size() - 1 ? null : stringList.get(i + 1).
                        substring(digitsStartIndex == 0 ? 0 : digitsStartIndex - 1, Math.min(digitsEndIndex + 1, stringList.get(i).length()));
                if (isAdjacent(rowAbove, thisRow, rowBelow) && thisRow.chars().anyMatch(Character::isDigit)) {
                    partNumbers.put(new Range(digitsStartIndex + i * stringList.get(i).length(), digitsEndIndex + i * stringList.get(i).length()),
                            Long.parseLong(stringList.get(i).substring(digitsStartIndex, digitsEndIndex)));
                }
            }
        }
        System.out.println("Part 1: " + partNumbers.values().stream().mapToLong(n -> n).sum());

        long sumOfGearRatios = 0;

        for (int i = 0; i < stringList.size(); i++) {
            for (int j = 0; j < stringList.get(i).length(); j++) {
                if (stringList.get(i).charAt(j) == '*') {
                    final int fi = i;
                    final int fj = j;
                    Set<Range> rangeSet = partNumbers.keySet().stream().filter(r -> r.atLeastOneInRange(
                            fj == 0 ? -1 : fj - 1 + (fi - 1) * stringList.get(fi).length(), fj + (fi - 1) * stringList.get(fi).length(),
                            fj == stringList.get(fi).length() - 1 ? -1 : fj + 1 + (fi - 1) * stringList.get(fi).length(),
                            fj == 0 ? -1 : fj - 1 + fi * stringList.get(fi).length(), fj == stringList.get(fi).length() - 1 ? -1 : fj + 1 + fi * stringList.get(fi).length(),
                            fj == 0 ? -1 : fj - 1 + (fi + 1) * stringList.get(fi).length(), fj + (fi + 1) * stringList.get(fi).length(),
                            fj == stringList.get(fi).length() - 1 ? -1 : fj + 1 + (fi + 1) * stringList.get(fi).length()
                    )).collect(Collectors.toSet());

                    List<Long> numList = new ArrayList<>();

                    for (Range r : rangeSet) {
                        numList.add(partNumbers.get(r));
                    }

                    if (numList.size() == 2) {
                        sumOfGearRatios += numList.get(0) * numList.get(1);
                    }
                }
            }
        }

        System.out.println("Part 2: " + sumOfGearRatios);
    }
}
