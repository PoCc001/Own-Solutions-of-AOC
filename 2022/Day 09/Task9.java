package aocjava;

import java.util.ArrayList;
import java.util.List;

public class Task9 {
    static class Square {
        private boolean visitedByTail;

        public Square() {
            visitedByTail = false;
        }

        public void visit() {
            visitedByTail = true;
        }

        public boolean isVisited() {
            return visitedByTail;
        }
    }

    static class DynamicMatrix {
        private final List<List<Square>> matrix;
        private final int[] head;
        private final int[] tail;

        public DynamicMatrix() {
            matrix = new ArrayList<>();
            matrix.add(new ArrayList<>());
            Square start = new Square();
            start.visit();
            matrix.get(0).add(start);
            head = new int[2];
            tail = new int[2];
        }

        public void appendRow(boolean top) {
            if (top) {
                matrix.add(new ArrayList<>());

                for (int i = 0; i < matrix.get(0).size(); i++) {
                    matrix.get(matrix.size() - 1).add(new Square());
                }
            } else {
                matrix.add(0, new ArrayList<>());

                for (int i = 0; i < matrix.get(1).size(); i++) {
                    matrix.get(0).add(new Square());
                }

                head[1]++;
                tail[1]++;
            }
        }

        public void appendColumn(boolean left) {
            if (left) {
                for (int i = 0; i < matrix.size(); i++) {
                    matrix.get(i).add(0, new Square());
                }

                head[0]++;
                tail[0]++;
            } else {
                for (int i = 0; i < matrix.size(); i++) {
                    matrix.get(i).add(new Square());
                }
            }
        }

        public void visitSquare(int x, int y) {
            matrix.get(y).get(x).visit();
        }

        public boolean tailIsLeft() {
            return tail[0] + 1 == head[0] && tail[1] == head[1];
        }

        public boolean tailIsRight() {
            return tail[0] - 1 == head[0] && tail[1] == head[1];
        }

        public boolean tailIsAbove() {
            return tail[0] == head[0] && tail[1] - 1 == head[1];
        }

        public boolean tailIsBelow() {
            return tail[0] == head[0] && tail[1] + 1 == head[1];
        }

        public boolean tailIsDLA() {
            return tail[0] + 1 == head[0] && tail[1] - 1 == head[1];
        }

        public boolean tailIsDRA() {
            return tail[0] - 1 == head[0] && tail[1] - 1 == head[1];
        }

        public boolean tailIsDRB() {
            return tail[0] - 1 == head[0] && tail[1] + 1 == head[1];
        }

        public boolean tailIsDLB() {
            return tail[0] + 1 == head[0] && tail[1] + 1 == head[1];
        }

        public int determinePos() {
            if (tailIsLeft()) {
                return 6;
            } else if (tailIsAbove()) {
                return 0;
            } else if (tailIsRight()) {
                return 2;
            } else if (tailIsBelow()) {
                return 4;
            } else if (tailIsDLA()) {
                return 7;
            } else if (tailIsDRA()) {
                return 1;
            } else if (tailIsDRB()) {
                return 3;
            } else if (tailIsDLB()) {
                return 5;
            } else {
                return -1;
            }
        }

        public void step(int d) {
            int p = determinePos();

            if (d == 0) {
                if (p >= 3 && p <= 5) {
                    tail[1]++;
                    tail[0] = head[0];
                }

                head[1]++;

                if (head[1] == matrix.size()) {
                    appendRow(true);
                }
            } else if (d == 1) {
                if (p >= 5 && p <= 7) {
                    tail[0]++;
                    tail[1] = head[1];
                }

                head[0]++;

                if (head[0] == matrix.get(head[1]).size()) {
                    appendColumn(false);
                }
            } else if (d == 2) {
                if (p == 7 || p == 0 || p == 1) {
                    tail[1]--;
                    tail[0] = head[0];
                }

                head[1]--;

                if (head[1] == -1) {
                    appendRow(false);
                }
            } else {
                if (p >= 1 && p <= 3) {
                    tail[0]--;
                    tail[1] = head[1];
                }

                head[0]--;

                if (head[0] == -1) {
                    appendColumn(true);
                }
            }

            visitSquare(tail[0], tail[1]);
        }

        public void repeatedSteps(int n, int d) {
            for (int i = 0; i < n; i++) {
                step(d);
            }
        }

        public int countVisitedSquares() {
            int count = 0;
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < matrix.get(i).size(); j++) {
                    if (matrix.get(i).get(j).isVisited()) {
                        count++;
                    }
                }
            }

            return count;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();

            for (int i = matrix.size() - 1; i >= 0; i--) {
                s.append('{');
                for (int j = 0; j < matrix.get(i).size(); j++) {
                    if (matrix.get(i).get(j).isVisited()) {
                        s.append('1');
                    } else {
                        s.append('0');
                    }

                    if (i == head[1] && j == head[0]) {
                        s.append('#');
                    } else {
                        s.append(' ');
                    }

                    if (i == tail[1] && j == tail[0]) {
                        s.append('*');
                    } else {
                        s.append(' ');
                    }

                    if (j != matrix.get(i).size() - 1) {
                        s.append(", ");
                    }
                }
                s.append('}');
                s.append('\n');
            }

            return s.toString();
        }
    }

    static class DynamicMatrix2 {
        private final List<List<Square>> matrix;
        private final int[][] knots;


        public DynamicMatrix2() {
            matrix = new ArrayList<>();
            matrix.add(new ArrayList<>());
            matrix.get(0).add(new Square());
            knots = new int[10][2];
        }

        public void appendRow(boolean top) {
            if (top) {
                matrix.add(new ArrayList<>());

                for (int i = 0; i < matrix.get(0).size(); i++) {
                    matrix.get(matrix.size() - 1).add(new Square());
                }
            } else {
                matrix.add(0, new ArrayList<>());

                for (int i = 0; i < matrix.get(1).size(); i++) {
                    matrix.get(0).add(new Square());
                }

                for (int i = 0; i < knots.length; i++) {
                    knots[i][1]++;
                }
            }
        }

        public void appendColumn(boolean left) {
            if (left) {
                for (int i = 0; i < matrix.size(); i++) {
                    matrix.get(i).add(0, new Square());
                }

                for (int i = 0; i < knots.length; i++) {
                    knots[i][0]++;
                }
            } else {
                for (int i = 0; i < matrix.size(); i++) {
                    matrix.get(i).add(new Square());
                }
            }
        }

        public void visitSquare(int x, int y) {
            matrix.get(y).get(x).visit();
        }

        private void stepOneKnot(int n) {
            if (Math.abs(knots[n][0] - knots[n - 1][0]) <= 1 && Math.abs(knots[n][1] - knots[n - 1][1]) <= 1) {
                return;
            } else if (Math.abs(knots[n][0] - knots[n - 1][0]) > 1 && Math.abs(knots[n][1] - knots[n - 1][1]) > 1) {
                if (knots[n][0] < knots[n - 1][0]) {
                    if (knots[n][1] < knots[n - 1][1]) {
                        knots[n][1] = knots[n - 1][1] - 1;
                    } else {
                        knots[n][1] = knots[n - 1][1] + 1;
                    }

                    knots[n][0] = knots[n - 1][0] - 1;
                } else {
                    if (knots[n][1] < knots[n - 1][1]) {
                        knots[n][1] = knots[n - 1][1] - 1;
                    } else {
                        knots[n][1] = knots[n - 1][1] + 1;
                    }

                    knots[n][0] = knots[n - 1][0] + 1;
                }
            } else if (Math.abs(knots[n][0] - knots[n - 1][0]) > 1) {
                if (knots[n][0] < knots[n - 1][0]) {
                    knots[n][0] = knots[n - 1][0] - 1;
                } else {
                    knots[n][0] = knots[n - 1][0] + 1;
                }
                knots[n][1] = knots[n - 1][1];
            } else {
                if (knots[n][1] < knots[n - 1][1]) {
                    knots[n][1] = knots[n - 1][1] - 1;
                } else {
                    knots[n][1] = knots[n - 1][1] + 1;
                }
                knots[n][0] = knots[n - 1][0];
            }
        }

        public void step(int d) {
            if (d == 0) {
                knots[0][1]++;

                if (knots[0][1] == matrix.size()) {
                    appendRow(true);
                }
            } else if (d == 1) {
                knots[0][0]++;

                if (knots[0][0] == matrix.get(0).size()) {
                    appendColumn(false);
                }
            } else if (d == 2) {
                knots[0][1]--;
                if (knots[0][1] == 0) {
                    appendRow(false);
                }
            } else if (d == 3) {
                knots[0][0]--;

                if (knots[0][0] == 0) {
                    appendColumn(true);
                }
            }

            for (int i = 1; i < knots.length; i++) {
                stepOneKnot(i);
            }

            visitSquare(knots[9][0], knots[9][1]);
        }

        public void repeatedSteps(int n, int d) {
            for (int i = 0; i < n; i++) {
                step(d);
            }
        }

        public int countVisitedSquares() {
            int count = 0;
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < matrix.get(i).size(); j++) {
                    if (matrix.get(i).get(j).isVisited()) {
                        count++;
                    }
                }
            }

            return count;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();

            for (int i = matrix.size() - 1; i >= 0; i--) {
                s.append('{');

                for (int j = 0; j < matrix.get(i).size(); j++) {
                    boolean isInRope = false;

                    for (int k = 0; k < knots.length; k++) {
                        if (knots[k][0] == j && knots[k][1] == i) {
                            isInRope = true;
                            s.append(k);
                            break;
                        }
                    }

                    if (!isInRope) {
                        if (matrix.get(i).get(j).isVisited()) {
                            s.append('.');
                        } else {
                            s.append(' ');
                        }
                    }

                    if (j != matrix.get(i).size() - 1) {
                        s.append(" ");
                    }
                }

                s.append('}');
                s.append('\n');
            }

            return s.toString();
        }
    }

    public static int getDirection(char c) {
        if (c == 'U') {
            return 0;
        } else if (c == 'R') {
            return 1;
        } else if (c == 'D') {
            return 2;
        } else {
            return 3;
        }
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input9.txt");
        DynamicMatrix matrix = new DynamicMatrix();

        for (int i = 0; i < arr.length; i++) {
            char c = arr[i].charAt(0);
            int n = Integer.parseInt(arr[i].substring(2));
            matrix.repeatedSteps(n, getDirection(c));
        }

        int count = matrix.countVisitedSquares();

        System.out.println("Part 1: " + count);

        DynamicMatrix2 matrix2 = new DynamicMatrix2();

        for (int i = 0; i < arr.length; i++) {
            char c = arr[i].charAt(0);
            int n = Integer.parseInt(arr[i].substring(2));
            matrix2.repeatedSteps(n, getDirection(c));
        }

        count = matrix2.countVisitedSquares();

        System.out.println("Part 2: " + count);
    }
}
