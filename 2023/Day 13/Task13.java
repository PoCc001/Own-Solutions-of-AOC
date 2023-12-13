package aocjava;

import java.util.*;

public class Task13 {
    static int searchHorizontalReflection(List<String> lines, int... doNotSearchLines) {
        Set<Integer> noSearchSet = new HashSet<>(Arrays.stream(doNotSearchLines).boxed().toList());
        for (int i = 0; i < lines.size() - 1; i++) {
            if (noSearchSet.contains(i)) {
                continue;
            }
            boolean equal = true;
            for (int j = 0; j < lines.size(); j++) {
                int min = i - j;
                int max = i + j + 1;
                if (min < 0 || max >= lines.size()) {
                    break;
                } else if (!lines.get(min).equals(lines.get(max))) {
                    equal = false;
                    break;
                }
            }

            if (equal) {
                return i + 1;
            }
        }

        return 0;
    }

    static boolean colsEqual(List<String> lines, int col1, int col2) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).charAt(col1) != lines.get(i).charAt(col2)) {
                return false;
            }
        }
        return true;
    }

    static int searchVerticalReflection(List<String> lines, int... doNotSearchCols) {
        Set<Integer> noSearchSet = new HashSet<>(Arrays.stream(doNotSearchCols).boxed().toList());
        int size = lines.get(0).length();
        for (int i = 0; i < size - 1; i++) {
            if (noSearchSet.contains(i)) {
                continue;
            }
            boolean equal = true;
            for (int j = 0; j < size; j++) {
                int min = i - j;
                int max = i + j + 1;
                if (min < 0 || max >= size) {
                    break;
                } else if (!colsEqual(lines, min, max)) {
                    equal = false;
                    break;
                }
            }
            if (equal) {
                return i + 1;
            }
        }

        return 0;
    }

    static int linesOneOff(String l1, String l2) {
        int offCount = 0;
        int offCol = 0;
        for (int i = 0; i < l1.length(); i++) {
            if (l1.charAt(i) != l2.charAt(i)) {
                offCol = i;
                offCount++;
            }
            if (offCount > 1) {
                return -1;
            }
        }
        return offCount == 1 ? offCol : -1;
    }

    static int colsOneOff(List<String> lines, int col1, int col2) {
        int offCount = 0;
        int offLine = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).charAt(col1) != lines.get(i).charAt(col2)) {
                offLine = i;
                offCount++;
            }
            if (offCount > 1) {
                return -1;
            }
        }
        return offCount == 1 ? offLine : -1;
    }

    static char other(char c) {
        return c == '.' ? '#' : '.';
    }

    // very long method...
    static void correctSmudge(List<String> lines) {
        int reflectionRow = searchHorizontalReflection(lines);
        int reflectionColumn = searchVerticalReflection(lines);

        for (int i = 0; i < lines.size() - 1; i++) {
            int line1 = 0;
            int line2 = 0;
            int col = 0;
            boolean oneOff = false;
            for (int j = 0; j < lines.size(); j++) {
                int min = i - j;
                int max = i + j + 1;
                if (min < 0 || max >= lines.size()) {
                    break;
                }
                if (linesOneOff(lines.get(min), lines.get(max)) != -1) {
                    col = linesOneOff(lines.get(min), lines.get(max));
                    line1 = min;
                    line2 = max;
                    oneOff = true;
                    break;
                }
            }

            if (oneOff) {
                lines.set(line1, lines.get(line1).
                        substring(0, col) +
                        other(lines.get(line1).charAt(col)) +
                        lines.get(line1).substring(Math.min(col + 1, lines.get(line1).length())));
                int h = searchHorizontalReflection(lines);
                int v = searchVerticalReflection(lines);
                if (h != 0) {
                    if (h != reflectionRow) {
                        return;
                    }
                } else {
                    h = searchHorizontalReflection(lines, reflectionRow - 1);
                    v = searchVerticalReflection(lines, reflectionColumn - 1);
                    if (h != 0) {
                        return;
                    }
                    if (v != 0) {
                        return;
                    }
                }
                if (v != 0) {
                    if (v != reflectionColumn) {
                        return;
                    }
                } else {
                    h = searchHorizontalReflection(lines, reflectionRow - 1);
                    v = searchVerticalReflection(lines, reflectionColumn - 1);
                    if (h != 0) {
                        return;
                    }
                    if (v != 0) {
                        return;
                    }
                }
                lines.set(line1, lines.get(line1).
                        substring(0, col) +
                        other(lines.get(line1).charAt(col)) +
                        lines.get(line1).substring(Math.min(col + 1, lines.get(line1).length())));
                lines.set(line2, lines.get(line2).
                        substring(0, col) +
                        other(lines.get(line2).charAt(col)) +
                        lines.get(line2).substring(Math.min(col + 1, lines.get(line2).length())));
                h = searchHorizontalReflection(lines);
                v = searchVerticalReflection(lines);
                if (h != 0) {
                    if (h != reflectionRow) {
                        return;
                    }
                } else {
                    h = searchHorizontalReflection(lines, reflectionRow - 1);
                    v = searchVerticalReflection(lines, reflectionColumn - 1);
                    if (h != 0) {
                        return;
                    }
                    if (v != 0) {
                        return;
                    }
                }
                if (v != 0) {
                    if (v != reflectionColumn) {
                        return;
                    }
                } else {
                    h = searchHorizontalReflection(lines, reflectionRow - 1);
                    v = searchVerticalReflection(lines, reflectionColumn - 1);
                    if (h != 0) {
                        return;
                    }
                    if (v != 0) {
                        return;
                    }
                }
                lines.set(line2, lines.get(line2).
                        substring(0, col) +
                        other(lines.get(line2).charAt(col)) +
                        lines.get(line2).substring(Math.min(col + 1, lines.get(line2).length())));
            }
        }

        int size = lines.get(0).length();
        for (int i = 0; i < size - 1; i++) {
            int line = 0;
            int col1 = 0;
            int col2 = 0;
            boolean oneOff = false;
            for (int j = 0; j < size; j++) {
                int min = i - j;
                int max = i + j + 1;
                if (min < 0 || max >= size) {
                    break;
                }
                if (colsOneOff(lines, min, max) != -1) {
                    line = colsOneOff(lines, min, max);
                    col1 = min;
                    col2 = max;
                    oneOff = true;
                    break;
                }
            }

            if (oneOff) {
                lines.set(line, lines.get(line).
                        substring(0, col1) +
                        other(lines.get(line).charAt(col1)) +
                        lines.get(line).substring(Math.min(col1 + 1, lines.get(line).length())));
                int h = searchHorizontalReflection(lines);
                int v = searchVerticalReflection(lines);
                if (h != 0) {
                    if (h != reflectionRow) {
                        return;
                    }
                } else {
                    h = searchHorizontalReflection(lines, reflectionRow - 1);
                    v = searchVerticalReflection(lines, reflectionColumn - 1);
                    if (h != 0) {
                        return;
                    }
                    if (v != 0) {
                        return;
                    }
                }
                if (v != 0) {
                    if (v != reflectionColumn) {
                        return;
                    }
                } else {
                    h = searchHorizontalReflection(lines, reflectionRow - 1);
                    v = searchVerticalReflection(lines, reflectionColumn - 1);
                    if (h != 0) {
                        return;
                    }
                    if (v != 0) {
                        return;
                    }
                }
                lines.set(line, lines.get(line).
                        substring(0, col1) +
                        other(lines.get(line).charAt(col1)) +
                        lines.get(line).substring(Math.min(col1 + 1, lines.get(line).length())));
                lines.set(line, lines.get(line).
                        substring(0, col2) +
                        other(lines.get(line).charAt(col2)) +
                        lines.get(line).substring(Math.min(col2 + 1, lines.get(line).length())));
                h = searchHorizontalReflection(lines);
                v = searchVerticalReflection(lines);
                if (h != 0) {
                    if (h != reflectionRow) {
                        return;
                    }
                } else {
                    h = searchHorizontalReflection(lines, reflectionRow - 1);
                    v = searchVerticalReflection(lines, reflectionColumn - 1);
                    if (h != 0) {
                        return;
                    }
                    if (v != 0) {
                        return;
                    }
                }
                if (v != 0) {
                    if (v != reflectionColumn) {
                        return;
                    }
                } else {
                    h = searchHorizontalReflection(lines, reflectionRow - 1);
                    v = searchVerticalReflection(lines, reflectionColumn - 1);
                    if (h != 0) {
                        return;
                    }
                    if (v != 0) {
                        return;
                    }
                }
                lines.set(line, lines.get(line).
                        substring(0, col2) +
                        other(lines.get(line).charAt(col2)) +
                        lines.get(line).substring(Math.min(col2 + 1, lines.get(line).length())));
            }
        }
    }

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input13.txt");

        List<List<String>> blocks = new ArrayList<>();

        blocks.add(new ArrayList<>());

        for (int i = 0; i < stringList.size(); i++) {
            if (stringList.get(i).length() != 0) {
                blocks.get(blocks.size() - 1).add(stringList.get(i));
            } else {
                blocks.add(new ArrayList<>());
            }
        }

        List<Long> horizontals = blocks.stream().map(Task13::searchHorizontalReflection).map(i -> (long)i).toList();
        List<Long> verticals = blocks.stream().map(Task13::searchVerticalReflection).map(i -> (long)i).toList();

        long result = 100L * horizontals.stream().mapToLong(l -> l).sum() + verticals.stream().mapToLong(l -> l).sum();

        System.out.println("Part 1: " + result);

        List<? extends List<String>> blocks2 = new ArrayList<>(blocks).stream().map(ArrayList::new).toList();

        blocks2.forEach(Task13::correctSmudge);

        horizontals = blocks2.stream().map(b -> searchHorizontalReflection(b, searchHorizontalReflection(blocks.get(blocks2.indexOf(b))) - 1)).map(i -> (long)i).toList();
        verticals = blocks2.stream().map(b -> searchVerticalReflection(b, searchVerticalReflection(blocks.get(blocks2.indexOf(b))) - 1)).map(i -> (long)i).toList();

        result = 100L * horizontals.stream().mapToLong(l -> l).sum() + verticals.stream().mapToLong(l -> l).sum();

        System.out.println("Part 2: " + result);
    }
}
