package aocjava;

import java.util.*;

public class Task14 {
    private record RoundStone(int x, int y) {
        RoundStone up() {
            return new RoundStone(x, y - 1);
        }

        RoundStone down() {
            return new RoundStone(x, y + 1);
        }

        RoundStone right() {
            return new RoundStone(x + 1, y);
        }

        RoundStone left() {
            return new RoundStone(x - 1, y);
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + "]";
        }
    }

    static List<String> rowsToCols(List<String> l) {
        List<String> cols = new ArrayList<>();
        for (int i = 0; i < l.get(0).length(); i++) {
            cols.add("");
            for (int j = 0; j < l.size(); j++) {
                cols.set(i, cols.get(i) + l.get(j).charAt(i));
            }
        }
        return cols;
    }

    static String boardToString(List<String> l, List<RoundStone> roundStones) {
        StringBuilder sb = new StringBuilder(l.get(0).replaceAll("O", "."));
        for (int i = 1; i < l.size(); i++) {
            sb.append('\n');
            sb.append(l.get(i).replaceAll("O", "."));
        }
        for (RoundStone s : roundStones) {
            sb.setCharAt(s.x + s.y * (l.get(0).length() + 1), 'O');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input14.txt");

        List<String> cols = rowsToCols(stringList);
        List<RoundStone> roundStones = new ArrayList<>();
        for (int x = 0; x < cols.size(); x++) {
            String c = cols.get(x);
            for (int i = 0; i < c.length(); i++) {
                if (c.charAt(i) == 'O') {
                    roundStones.add(new RoundStone(x, i));
                }
            }
        }

        for (int i = 0; i < roundStones.size(); i++) {
            RoundStone s = roundStones.get(i);
            while (roundStones.get(i).y != 0 && stringList.get(roundStones.get(i).y).charAt(s.x) != '#' && !roundStones.contains(new RoundStone(s.x, roundStones.get(i).y - 1))) {
                roundStones.set(i, roundStones.get(i).up());
            }
            if (stringList.get(roundStones.get(i).y).charAt(s.x) == '#') {
                roundStones.set(i, roundStones.get(i).down());
            }
        }
        long result = roundStones.stream().mapToLong(s -> stringList.size() - s.y).sum();

        System.out.println("Part 1: " + result);

        List<List<RoundStone>> states = new ArrayList<>();

        // wait a bit...
        while (!states.contains(roundStones) && states.indexOf(roundStones) % 4 != 0) {
            states.add(new ArrayList<>(roundStones));
            roundStones.sort(Comparator.comparingInt(s -> s.y));
            for (int i = 0; i < roundStones.size(); i++) {
                RoundStone s = roundStones.get(i);
                while (roundStones.get(i).y != 0 && stringList.get(roundStones.get(i).y - 1).charAt(s.x) != '#' && !roundStones.contains(new RoundStone(s.x, roundStones.get(i).y - 1))) {
                    roundStones.set(i, roundStones.get(i).up());
                }
            }
            if (states.contains(roundStones) && states.indexOf(roundStones) % 4 == 1) {
                break;
            }
            states.add(new ArrayList<>(roundStones));
            roundStones.sort(Comparator.comparingInt(s -> s.x));
            for (int i = 0; i < roundStones.size(); i++) {
                RoundStone s = roundStones.get(i);
                while (roundStones.get(i).x != 0 && stringList.get(s.y).charAt(roundStones.get(i).x - 1) != '#' && !roundStones.contains(new RoundStone(roundStones.get(i).x - 1, s.y))) {
                    roundStones.set(i, roundStones.get(i).left());
                }
            }
            if (states.contains(roundStones) && states.indexOf(roundStones) % 4 == 2) {
                 break;
            }
            states.add(new ArrayList<>(roundStones));
            roundStones.sort(Comparator.comparingInt((RoundStone s) -> s.y).reversed());
            for (int i = 0; i < roundStones.size(); i++) {
                RoundStone s = roundStones.get(i);
                while (roundStones.get(i).y != stringList.size() - 1 && stringList.get(roundStones.get(i).y + 1).charAt(s.x) != '#' && !roundStones.contains(new RoundStone(s.x, roundStones.get(i).y + 1))) {
                    roundStones.set(i, roundStones.get(i).down());
                }
            }
            if (states.contains(roundStones) && states.indexOf(roundStones) % 4 == 3) {
                break;
            }
            states.add(new ArrayList<>(roundStones));
            roundStones.sort(Comparator.comparingInt((RoundStone s) -> s.x).reversed());
            for (int i = 0; i < roundStones.size(); i++) {
                RoundStone s = roundStones.get(i);
                while (roundStones.get(i).x != stringList.get(0).length() - 1 && stringList.get(s.y).charAt(roundStones.get(i).x + 1) != '#' && !roundStones.contains(new RoundStone(roundStones.get(i).x + 1, s.y))) {
                    roundStones.set(i, roundStones.get(i).right());
                }
            }
        }
        int index = states.indexOf(roundStones);

        final long big = 1000000000;
        int rem = (int)((big * 4 - index) % (states.size() - index));

        result = states.get(rem + index).stream().mapToLong(s -> stringList.size() - s.y).sum();

        System.out.println("Part 2: " + result);
    }
}
