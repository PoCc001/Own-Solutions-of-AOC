package aocjava;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Task14 {
    static class Point {
        public final int x;
        public final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    enum Material {
        Stone, Air, Sand, Source
    }

    static class Matrix {
        private final List<List<Material>> matrix;
        private final int[] sourceCoords = new int[]{-1, -1};

        public Matrix(int x, int y) {
            matrix = new ArrayList<>();
            for (int i = 0; i < y; i++) {
                matrix.add(new ArrayList<>());
                for (int j = 0; j < x; j++) {
                    matrix.get(i).add(Material.Air);
                }
            }
        }

        public void setSourceCoords(int x, int y) {
            if (sourceCoords[0] != -1 && sourceCoords[1] != -1) {
                matrix.get(sourceCoords[1]).set(sourceCoords[0], Material.Air);
            }

            sourceCoords[0] = x;
            sourceCoords[1] = y;

            matrix.get(y).set(x, Material.Source);
        }

        public int[] getSourceCoords() {
            return sourceCoords;
        }

        public void addStones(Point from, Point to) {
            if (from.x > to.x) {
                for (int i = from.x; i >= to.x; i--) {
                    matrix.get(from.y).set(i, Material.Stone);
                }
            } else if (from.x < to.x) {
                for (int i = from.x; i <= to.x; i++) {
                    matrix.get(from.y).set(i, Material.Stone);
                }
            } else if (from.y > to.y) {
                for (int i = from.y; i >= to.y; i--) {
                    matrix.get(i).set(from.x, Material.Stone);
                }
            } else {
                for (int i = from.y; i <= to.y; i++) {
                    matrix.get(i).set(from.x, Material.Stone);
                }
            }
        }

        public void addCol(boolean left) {
            if (left) {
                for (List<Material> l : matrix) {
                    l.add(0, Material.Air);
                }

                sourceCoords[0]++;
            } else {
                for (List<Material> l : matrix) {
                    l.add(Material.Air);
                }
            }
        }

        public Point createSand() {
            Point sand = new Point(sourceCoords[0], sourceCoords[1] + 1);
            matrix.get(sand.y).set(sand.x, Material.Sand);
            return sand;
        }

        public int countSand() {
            int count = 0;
            for (List<Material> ml : matrix) {
                for (Material m : ml) {
                    if (m == Material.Sand) {
                        count++;
                    }
                }
            }

            return count;
        }

        public Material getMaterial(Point p) {
            if (p.y < 0 || p.y >= matrix.size() || p.x < 0 || p.x >= matrix.get(p.y).size()) {
                return Material.Air;
            } else {
                return matrix.get(p.y).get(p.x);
            }
        }

        public Material getMaterial(int x, int y) {
            return getMaterial(new Point(x, y));
        }

        private void removeSand(Point p) {
            if (getMaterial(p) == Material.Sand) {
                matrix.get(p.y).set(p.x, Material.Air);
            }
        }

        private void moveSand(Point from, Point to) {
            matrix.get(from.y).set(from.x, Material.Air);
            matrix.get(to.y).set(to.x, Material.Sand);
        }

        private int sandCanFallStraight(Point sand) {
            if (sand.y == matrix.size() - 1) {
                return -1;
            } else if (getMaterial(sand.x, sand.y + 1) != Material.Air) {
                return 0;
            } else {
                return 1;
            }
        }

        private int sandCanFallLeft(Point sand) {
            if (sand.y == matrix.size() - 1) {
                return -1;
            } else if (getMaterial(sand.x - 1, sand.y + 1) != Material.Air) {
                return 0;
            } else {
                return 1;
            }
        }

        private int sandCanFallRight(Point sand) {
            if (sand.y == matrix.size() - 1) {
                return -1;
            } else if (getMaterial(sand.x + 1, sand.y + 1) != Material.Air) {
                return 0;
            } else {
                return 1;
            }
        }

        private boolean simulateOneSandParticle() {
            Point sand = createSand();
            int decision1 = 0;
            int decision2 = 0;
            int decision3 = 0;

            while (true) {
                decision1 = sandCanFallStraight(sand);
                decision2 = sandCanFallLeft(sand);
                decision3 = sandCanFallRight(sand);

                if (decision1 == -1) {
                    return false;
                }

                if (decision1 == 1) {
                    Point newSand = new Point(sand.x, sand.y + 1);
                    moveSand(sand, newSand);
                    sand = newSand;
                } else if (decision2 == 1) {
                    Point newSand;
                    if (sand.x == 0) {
                        addCol(true);
                        newSand = new Point(sand.x, sand.y + 1);
                        moveSand(new Point(sand.x + 1, sand.y), newSand);
                    } else {
                        newSand = new Point(sand.x - 1, sand.y + 1);
                        moveSand(sand, newSand);
                    }
                    sand = newSand;
                } else if (decision3 == 1) {
                    Point newSand = new Point(sand.x + 1, sand.y + 1);
                    if (newSand.x == matrix.get(newSand.y).size()) {
                        addCol(false);
                    }
                    moveSand(sand, newSand);
                    sand = newSand;
                } else {
                    return true;
                }
            }
        }

        public int simulate() {
            while (simulateOneSandParticle()) {

            }


            return countSand() - 1;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < matrix.get(i).size(); j++) {
                    char c = '.';
                    if (matrix.get(i).get(j) == Material.Source) {
                        c = '+';
                    } else if (matrix.get(i).get(j) == Material.Sand) {
                        c = 'o';
                    } else if (matrix.get(i).get(j) == Material.Stone) {
                        c = '#';
                    }

                    sb.append(c);
                }

                if (i != matrix.size() - 1) {
                    sb.append('\n');
                }
            }

            return sb.toString();
        }
    }

    static class Matrix2 {
        private final List<List<Material>> matrix;
        private final int[] sourceCoords = new int[]{-1, -1};

        public Matrix2(int x, int y) {
            matrix = new ArrayList<>();
            for (int i = 0; i < y + 1; i++) {
                matrix.add(new ArrayList<>());
                for (int j = 0; j < x; j++) {
                    matrix.get(i).add(Material.Air);
                }
            }

            matrix.add(new ArrayList<>());
            for (int i = 0; i < x; i++) {
                matrix.get(matrix.size() - 1).add(Material.Stone);
            }
        }

        public void setSourceCoords(int x, int y) {
            if (sourceCoords[0] != -1 && sourceCoords[1] != -1) {
                matrix.get(sourceCoords[1]).set(sourceCoords[0], Material.Air);
            }

            sourceCoords[0] = x;
            sourceCoords[1] = y;

            matrix.get(y).set(x, Material.Source);
        }

        public int[] getSourceCoords() {
            return sourceCoords;
        }

        public void addStones(Point from, Point to) {
            if (from.x > to.x) {
                for (int i = from.x; i >= to.x; i--) {
                    matrix.get(from.y).set(i, Material.Stone);
                }
            } else if (from.x < to.x) {
                for (int i = from.x; i <= to.x; i++) {
                    matrix.get(from.y).set(i, Material.Stone);
                }
            } else if (from.y > to.y) {
                for (int i = from.y; i >= to.y; i--) {
                    matrix.get(i).set(from.x, Material.Stone);
                }
            } else {
                for (int i = from.y; i <= to.y; i++) {
                    matrix.get(i).set(from.x, Material.Stone);
                }
            }
        }

        public void addCol(boolean left) {
            if (left) {
                for (int i = 0; i < matrix.size(); i++) {
                    if (i != matrix.size() - 1) {
                        matrix.get(i).add(0, Material.Air);
                    } else {
                        matrix.get(i).add(0, Material.Stone);
                    }
                }

                sourceCoords[0]++;
            } else {
                for (int i = 0; i < matrix.size(); i++) {
                    if (i != matrix.size() - 1) {
                        matrix.get(i).add(Material.Air);
                    } else {
                        matrix.get(i).add(Material.Stone);
                    }
                }
            }
        }

        public Point createSand() {
            if (getMaterial(sourceCoords[0], sourceCoords[1]) != Material.Source) {
                return null;
            }
            Point sand = new Point(sourceCoords[0], sourceCoords[1]);
            matrix.get(sand.y).set(sand.x, Material.Sand);
            return sand;
        }

        public int countSand() {
            int count = 0;
            for (List<Material> ml : matrix) {
                for (Material m : ml) {
                    if (m == Material.Sand) {
                        count++;
                    }
                }
            }

            return count;
        }

        public Material getMaterial(Point p) {
            if (p.y < 0 || p.y >= matrix.size() || p.x < 0 || p.x >= matrix.get(p.y).size()) {
                return p.y == matrix.size() - 1 ? Material.Stone : Material.Air;
            } else {
                return matrix.get(p.y).get(p.x);
            }
        }

        public Material getMaterial(int x, int y) {
            return getMaterial(new Point(x, y));
        }

        private void removeSand(Point p) {
            if (getMaterial(p) == Material.Sand) {
                matrix.get(p.y).set(p.x, Material.Air);
            }
        }

        public void removeSand() {
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < matrix.get(i).size(); j++) {
                    removeSand(new Point(j, i));
                }
            }
        }

        private void moveSand(Point from, Point to) {
            if (from.x == sourceCoords[0] && from.y == sourceCoords[1]) {
                matrix.get(from.y).set(from.x, Material.Source);
            } else {
                matrix.get(from.y).set(from.x, Material.Air);
            }
            matrix.get(to.y).set(to.x, Material.Sand);
        }

        private int sandCanFallStraight(Point sand) {
            if (sand.y == matrix.size() - 1) {
                return -1;
            } else if (getMaterial(sand.x, sand.y + 1) != Material.Air) {
                return 0;
            } else {
                return 1;
            }
        }

        private int sandCanFallLeft(Point sand) {
            if (sand.y == matrix.size() - 1) {
                return -1;
            } else if (getMaterial(sand.x - 1, sand.y + 1) != Material.Air) {
                return 0;
            } else {
                return 1;
            }
        }

        private int sandCanFallRight(Point sand) {
            if (sand.y == matrix.size() - 1) {
                return -1;
            } else if (getMaterial(sand.x + 1, sand.y + 1) != Material.Air) {
                return 0;
            } else {
                return 1;
            }
        }

        private boolean simulateOneSandParticle() {
            Point sand = createSand();
            if (sand == null) {
                return false;
            }
            int decision1 = 0;
            int decision2 = 0;
            int decision3 = 0;

            while (true) {
                decision1 = sandCanFallStraight(sand);
                decision2 = sandCanFallLeft(sand);
                decision3 = sandCanFallRight(sand);

                if (decision1 == 1) {
                    Point newSand = new Point(sand.x, sand.y + 1);
                    moveSand(sand, newSand);
                    sand = newSand;
                } else if (decision2 == 1) {
                    Point newSand;
                    if (sand.x == 0) {
                        addCol(true);
                        newSand = new Point(sand.x, sand.y + 1);
                        moveSand(new Point(sand.x + 1, sand.y), newSand);
                    } else {
                        newSand = new Point(sand.x - 1, sand.y + 1);
                        moveSand(sand, newSand);
                    }
                    sand = newSand;
                } else if (decision3 == 1) {
                    Point newSand = new Point(sand.x + 1, sand.y + 1);
                    if (newSand.x == matrix.get(newSand.y).size()) {
                        addCol(false);
                    }
                    moveSand(sand, newSand);
                    sand = newSand;
                } else {
                    return true;
                }
            }
        }

        public int simulate() {
            while (simulateOneSandParticle()) {

            }


            return countSand();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < matrix.get(i).size(); j++) {
                    char c = '.';
                    if (matrix.get(i).get(j) == Material.Source) {
                        c = '+';
                    } else if (matrix.get(i).get(j) == Material.Sand) {
                        c = 'o';
                    } else if (matrix.get(i).get(j) == Material.Stone) {
                        c = '#';
                    }

                    sb.append(c);
                }

                if (i != matrix.size() - 1) {
                    sb.append('\n');
                }
            }

            return sb.toString();
        }
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input14.txt");
        List<List<Point>> points = new ArrayList<>();

        for (String s : arr) {
            points.add(new ArrayList<>());
            List<String> pointsInOneLine = AOCUtils.divideString(s, " -> ");

            for (String str : pointsInOneLine) {
                int i = str.indexOf(',');
                points.get(points.size() - 1).add(new Point(Integer.parseInt(str.substring(0, i)), Integer.parseInt(str.substring(i + 1))));
            }
        }

        int minX = points.stream().flatMap(Collection::stream).min(Comparator.comparingInt(p -> p.x)).get().x;
        int maxX = points.stream().flatMap(Collection::stream).max(Comparator.comparingInt(p -> p.x)).get().x;
        int maxY = points.stream().flatMap(Collection::stream).max(Comparator.comparingInt(p -> p.y)).get().y;

        Matrix matrix = new Matrix(maxX - minX + 1, maxY + 1);

        for (List<Point> point : points) {
            for (int j = 1; j < point.size(); j++) {
                matrix.addStones(new Point(point.get(j - 1).x - minX, point.get(j - 1).y), new Point(point.get(j).x - minX, point.get(j).y));
            }
        }

        matrix.setSourceCoords(500 - minX, 0);

        int count = matrix.simulate();

        System.out.println("Part 1: " + count);

        Matrix2 matrix2 = new Matrix2(maxX - minX + 1, maxY + 1);

        for (List<Point> point : points) {
            for (int j = 1; j < point.size(); j++) {
                matrix2.addStones(new Point(point.get(j - 1).x - minX, point.get(j - 1).y), new Point(point.get(j).x - minX, point.get(j).y));
            }
        }

        matrix2.setSourceCoords(500 - minX, 0);

        for (int i = 0; i < 10; i++) {
            matrix2.addCol(true);
        }

        for (int i = 0; i < 10; i++) {
            matrix2.addCol(false);
        }

        count = matrix2.simulate();

        System.out.println("Part 2: " + count);
    }
}
