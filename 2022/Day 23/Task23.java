package aocjava;

import java.util.*;

public class Task23 {
    enum Direction {
        N, NE, E, SE, S, SW, W, NW
    }

    static class Elf {
        private final int[] pos = new int[2];
        private final int[] oldPos = new int[2];
        private boolean usePos = true;

        public Elf(int x, int y) {
            pos[0] = x;
            pos[1] = y;
            oldPos[0] = x;
            oldPos[1] = y;
        }

        public void goNorth() {
            pos[1]--;
        }

        public void goEast() {
            pos[0]++;
        }

        public void goSouth() {
            pos[1]++;
        }

        public void goWest() {
            pos[0]--;
        }

        public void finalizePos() {
            if (usePos) {
                oldPos[0] = pos[0];
                oldPos[1] = pos[1];
            } else {
                pos[0] = oldPos[0];
                pos[1] = oldPos[1];
            }
            usePos = true;
        }

        public void revertPos() {
            usePos = false;
        }

        public int[] getPos() {
            return new int[]{oldPos[0], oldPos[1]};
        }

        public boolean conflict(Elf other) {
            return pos[0] == other.pos[0] && pos[1] == other.pos[1];
        }

        public boolean hasMoved() {
            return pos[0] != oldPos[0] || pos[1] != oldPos[1];
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Elf elf = (Elf) o;
            return Arrays.equals(pos, elf.pos) && Arrays.equals(oldPos, elf.oldPos);
        }

        @Override
        public int hashCode() {
            int result = Arrays.hashCode(pos);
            result = 31 * result + Arrays.hashCode(oldPos);
            return result;
        }
    }

    public static List<Direction> getDirectionsOfAdjacentElves(List<Elf> elves, final Elf elf) {
        List<Elf> adjacentElves = elves.stream().filter(e -> Math.abs(e.getPos()[0] - elf.getPos()[0]) <= 1 && Math.abs(e.getPos()[1] - elf.getPos()[1]) <= 1
                                    && !elf.equals(e)).toList();

        List<Direction> directions = new ArrayList<>();

        Optional<Elf> northElf = adjacentElves.stream().filter(e -> e.getPos()[0] == elf.getPos()[0] && e.getPos()[1] == elf.getPos()[1] - 1).findAny();
        Optional<Elf> northEastElf = adjacentElves.stream().filter(e -> e.getPos()[0] == elf.getPos()[0] + 1 && e.getPos()[1] == elf.getPos()[1] - 1).findAny();
        Optional<Elf> eastElf = adjacentElves.stream().filter(e -> e.getPos()[0] == elf.getPos()[0] + 1 && e.getPos()[1] == elf.getPos()[1]).findAny();
        Optional<Elf> southEastElf = adjacentElves.stream().filter(e -> e.getPos()[0] == elf.getPos()[0] + 1 && e.getPos()[1] == elf.getPos()[1] + 1).findAny();
        Optional<Elf> southElf = adjacentElves.stream().filter(e -> e.getPos()[0] == elf.getPos()[0] && e.getPos()[1] == elf.getPos()[1] + 1).findAny();
        Optional<Elf> southWestElf = adjacentElves.stream().filter(e -> e.getPos()[0] == elf.getPos()[0] - 1 && e.getPos()[1] == elf.getPos()[1] + 1).findAny();
        Optional<Elf> westElf = adjacentElves.stream().filter(e -> e.getPos()[0] == elf.getPos()[0] - 1 && e.getPos()[1] == elf.getPos()[1]).findAny();
        Optional<Elf> northWestElf = adjacentElves.stream().filter(e -> e.getPos()[0] == elf.getPos()[0] - 1 && e.getPos()[1] == elf.getPos()[1] - 1).findAny();

        if (northElf.isPresent()) {
            directions.add(Direction.N);
        }
        if (northEastElf.isPresent()) {
            directions.add(Direction.NE);
        }
        if (eastElf.isPresent()) {
            directions.add(Direction.E);
        }
        if (southEastElf.isPresent()) {
            directions.add(Direction.SE);
        }
        if (southElf.isPresent()) {
            directions.add(Direction.S);
        }
        if (southWestElf.isPresent()) {
            directions.add(Direction.SW);
        }
        if (westElf.isPresent()) {
            directions.add(Direction.W);
        }
        if (northWestElf.isPresent()) {
            directions.add(Direction.NW);
        }

        return directions;
    }

    public static String mapToString(List<Elf> elves) {
        int minX = elves.stream().mapToInt(e -> e.getPos()[0]).min().getAsInt();
        int maxX = elves.stream().mapToInt(e -> e.getPos()[0]).max().getAsInt();
        int minY = elves.stream().mapToInt(e -> e.getPos()[1]).min().getAsInt();
        int maxY = elves.stream().mapToInt(e -> e.getPos()[1]).max().getAsInt();

        StringBuilder sb = new StringBuilder();

        for (int i = minY; i <= maxY; i++) {
            final int y = i;
            for (int j = minX; j <= maxX; j++) {
                final int x = j;
                Optional<Elf> elf = elves.stream().filter(e -> e.getPos()[0] == x && e.getPos()[1] == y).findAny();
                if (elf.isPresent()) {
                    sb.append('#');
                } else {
                    sb.append('.');
                }
            }

            if (i != maxY) {
                sb.append('\n');
            }
        }

        return sb.toString();
    }

    public static boolean conditionalMove(Elf elf, int round, int move, List<Direction> directions) {
        int d = (round + move) % 4;

        if (d == 0) {
            if (!directions.contains(Direction.N) && !directions.contains(Direction.NW) && !directions.contains(Direction.NE)) {
                elf.goNorth();
                return true;
            }
        } else if (d == 1) {
            if (!directions.contains(Direction.S) && !directions.contains(Direction.SW) && !directions.contains(Direction.SE)) {
                elf.goSouth();
                return true;
            }
        } else if (d == 2) {
            if (!directions.contains(Direction.W) && !directions.contains(Direction.NW) && !directions.contains(Direction.SW)) {
                elf.goWest();
                return true;
            }
        } else if (d == 3) {
            if (!directions.contains(Direction.E) && !directions.contains(Direction.NE) && !directions.contains(Direction.SE)) {
                elf.goEast();
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input23.txt");
        List<Elf> elves1 = new ArrayList<>();
        List<Elf> elves2 = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length(); j++) {
                if (arr[i].charAt(j) == '#') {
                    elves1.add(new Elf(j, i));
                    elves2.add(new Elf(j, i));
                }
            }
        }

        for (int i = 0; i < 10; i++) {
            for (Elf e : elves1) {
                List<Direction> directions = getDirectionsOfAdjacentElves(elves1, e);
                if (!directions.isEmpty()) {
                    if (!conditionalMove(e, i, 0, directions)) {
                        if (!conditionalMove(e, i, 1, directions)) {
                            if (!conditionalMove(e, i, 2, directions)) {
                                conditionalMove(e, i, 3, directions);
                            }
                        }
                    }
                }
            }

            for (Elf e1 : elves1) {
                for (Elf e2 : elves1) {
                    if (!e1.equals(e2) && e1.conflict(e2)) {
                        e1.revertPos();
                        e2.revertPos();
                    }
                }
            }

            for (Elf e : elves1) {
                e.finalizePos();
            }
        }

        int minX = elves1.stream().mapToInt(e -> e.getPos()[0]).min().getAsInt();
        int maxX = elves1.stream().mapToInt(e -> e.getPos()[0]).max().getAsInt();
        int minY = elves1.stream().mapToInt(e -> e.getPos()[1]).min().getAsInt();
        int maxY = elves1.stream().mapToInt(e -> e.getPos()[1]).max().getAsInt();

        long area = (long)(maxX - minX + 1) * (long)(maxY - minY + 1);
        long result = area - elves1.size();

        System.out.println("Part 1: " + result);

        int i = 0;
        boolean someHaveMoved = false;
        do {
            for (Elf e : elves2) {
                List<Direction> directions = getDirectionsOfAdjacentElves(elves2, e);
                if (!directions.isEmpty()) {
                    if (!conditionalMove(e, i, 0, directions)) {
                        if (!conditionalMove(e, i, 1, directions)) {
                            if (!conditionalMove(e, i, 2, directions)) {
                                conditionalMove(e, i, 3, directions);
                            }
                        }
                    }
                }
            }

            for (Elf e1 : elves2) {
                for (Elf e2 : elves2) {
                    if (!e1.equals(e2) && e1.conflict(e2)) {
                        e1.revertPos();
                        e2.revertPos();
                    }
                }
            }

            someHaveMoved = false;

            for (Elf e : elves2) {
                someHaveMoved = someHaveMoved || e.hasMoved();
                e.finalizePos();
            }

            i++;
        } while (someHaveMoved);

        System.out.println("Part 2: " + i);
    }
}