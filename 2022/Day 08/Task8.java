package aocjava;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Task8 {
    static class Tree {
        private final int height;
        private final int x;
        private final int y;

        public Tree(int h, int x, int y) {
            height = h;
            this.x = x;
            this.y = y;
        }

        public int getHeight() {
            return height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(height) ^ Integer.hashCode(x) ^ Integer.hashCode(y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (getClass() != o.getClass()) {
                return false;
            } else {
                Tree ot = (Tree)(o);
                return height == ot.height && x == ot.x && y == ot.y;
            }
        }
    }

    public static void fillGrid(Tree[][] grid, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length(); j++) {
                grid[i][j] = new Tree(Integer.parseInt(arr[i].substring(j, j + 1)), j, i);
            }
        }
    }

    public static Tree isVisible(Tree t, Tree[][] grid, int x, int y) {
        int maxHeightSoFar = -1;
        if (x == -1) {
            for (int i = 0; i < grid[y].length; i++) {
                if (grid[y][i].equals(t)) {
                    return t.getHeight() > maxHeightSoFar ? t : null;
                }
                maxHeightSoFar = Math.max(maxHeightSoFar, grid[y][i].getHeight());
            }
        } else if (x == grid[0].length) {
            for (int i = grid[y].length - 1; i >= 0; i--) {
                if (grid[y][i].equals(t)) {
                    return t.getHeight() > maxHeightSoFar ? t : null;
                }
                maxHeightSoFar = Math.max(maxHeightSoFar, grid[y][i].getHeight());
            }
        } else if (y == -1) {
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][x].equals(t)) {
                    return t.getHeight() > maxHeightSoFar ? t : null;
                }
                maxHeightSoFar = Math.max(maxHeightSoFar, grid[i][x].getHeight());
            }
        } else {
            for (int i = grid.length - 1; i >= 0; i--) {
                if (grid[i][x].equals(t)) {
                    return t.getHeight() > maxHeightSoFar ? t : null;
                }
                maxHeightSoFar = Math.max(maxHeightSoFar, grid[i][x].getHeight());
            }
        }

        return null;
    }

    public static int getViewDistance(Tree t, Tree[][] grid, int d) {
        int distance = 0;
        if (d == 0) {
            for (int i = t.getY() - 1; i >= 0; i--) {
                distance++;
                if (grid[i][t.getX()].getHeight() >= t.getHeight()) {
                    break;
                }
            }

            return distance;
        } else if (d == 1) {
            for (int i = t.getX() + 1; i < grid[t.getY()].length; i++) {
                distance++;
                if (grid[t.getY()][i].getHeight() >= t.getHeight()) {
                    break;
                }
            }

            return distance;
        } else if (d == 2) {
            for (int i = t.getY() + 1; i < grid.length; i++) {
                distance++;
                if (grid[i][t.getX()].getHeight() >= t.getHeight()) {
                    break;
                }
            }

            return distance;
        } else {
            for (int i = t.getX() - 1; i >= 0; i--) {
                distance++;
                if (grid[t.getY()][i].getHeight() >= t.getHeight()) {
                    break;
                }
            }

            return distance;
        }
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input8.txt");
        Tree[][] grid = new Tree[arr.length][arr[0].length()];
        fillGrid(grid, arr);

        Set<Tree> visibleTrees = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Tree t = isVisible(grid[i][j], grid, -1, i);
                if (t != null) {
                    visibleTrees.add(t);
                }
            }

            for (int j = grid[i].length - 1; j >= 0; j--) {
                Tree t = isVisible(grid[i][j], grid, grid[i].length, i);
                if (t != null) {
                    visibleTrees.add(t);
                }
            }
        }

        for (int i = 0; i < grid[0].length; i++) {
            for (int j = 0; j < grid.length; j++) {
                Tree t = isVisible(grid[j][i], grid, i, -1);
                if (t != null) {
                    visibleTrees.add(t);
                }
            }

            for (int j = grid.length - 1; j >= 0; j--) {
                Tree t = isVisible(grid[j][i], grid, i, grid.length);
                if (t != null) {
                    visibleTrees.add(t);
                }
            }
        }

        System.out.println("Part 1: " + visibleTrees.size());

        SortedSet<Integer> scenicScores = new TreeSet<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                int scenicScore = 1;
                for (int k = 0; k < 4; k++) {
                    scenicScore *= getViewDistance(grid[j][i], grid, k);
                }
                scenicScores.add(scenicScore);
            }
        }

        System.out.println("Part 2: " + scenicScores.last());
    }
}
