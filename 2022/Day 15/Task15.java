package aocjava;

import java.util.*;
import java.util.stream.Stream;

public class Task15 {
    static class Point {
        public final int x;
        public final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (getClass() != o.getClass()) {
                return false;
            } else {
                Point op = (Point)(o);
                return x == op.x && y == op.y;
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static int getManhattanDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    public static boolean isPointInDistanceOfOther(Point p1, Point p2, int manhattanDistance) {
        return manhattanDistance - Math.abs(p1.x - p2.x) - Math.abs(p1.y - p2.y) >= 0;
    }

    public static Set<Integer> addSensorAndNearestBeacon(int sx, int sy, int bx, int by, int rowOfInterest) {
        int manhattanDistance = getManhattanDistance(sx, sy, bx, by);

        Set<Integer> coverageInRow = new TreeSet<>();

        if (manhattanDistance >= Math.abs(sy - rowOfInterest)) {
            int dx = Math.abs(Math.abs(sy - rowOfInterest) - manhattanDistance);
            for (int i = sx - dx; i <= sx + dx; i++) {
                coverageInRow.add(i);
            }
        }
        return coverageInRow;
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input15.txt");
        final int rowOfInterest = 2000000;

        List<int[]> sensorCoords = new ArrayList<>();
        List<int[]> beaconCoords = new ArrayList<>();

        for (String row : arr) {
            int x1 = row.indexOf('x') + 2;
            int y1 = row.indexOf('y') + 2;
            int[] sCoords = new int[2];
            sCoords[0] = Integer.parseInt(row.substring(x1, row.indexOf(',')));
            sCoords[1] = Integer.parseInt(row.substring(y1, row.indexOf(':')));
            sensorCoords.add(sCoords);

            int x2 = row.indexOf('x', x1) + 2;
            int y2 = row.indexOf('y', y1) + 2;
            int[] bCoords = new int[2];
            bCoords[0] = Integer.parseInt(row.substring(x2, row.indexOf(',', x2)));
            bCoords[1] = Integer.parseInt(row.substring(y2));
            beaconCoords.add(bCoords);
        }

        Set<Integer> checkedCols = new TreeSet<>();
        for (int i = 0; i < sensorCoords.size(); i++) {
            int[] s = sensorCoords.get(i);
            int[] b = beaconCoords.get(i);
            Set<Integer> subset = addSensorAndNearestBeacon(s[0], s[1], b[0], b[1], rowOfInterest);
            checkedCols.addAll(subset);
        }

        List<Integer> beaconsOnRowOfInterest = beaconCoords.stream().filter(b -> b[1] == rowOfInterest).map(b -> b[0]).toList();

        for (Integer i : beaconsOnRowOfInterest) {
            checkedCols.remove(i);
        }

        System.out.println("Part 1: " + checkedCols.size());

        final int max = 4000000;

        Point beaconPoint = new Point(-1, -1);

        for (int i = 0; i < sensorCoords.size(); i++) {
            int manhattanDistance = getManhattanDistance(sensorCoords.get(i)[0], sensorCoords.get(i)[1], beaconCoords.get(i)[0], beaconCoords.get(i)[1]);
            Point sensor = new Point(sensorCoords.get(i)[0], sensorCoords.get(i)[1]);

            for (int j = 0; j <= manhattanDistance + 1; j++) {
                if (sensor.x + j >= 0 && sensor.x + j <= max && sensor.y + manhattanDistance + 1 - j >= 0 && sensor.y + manhattanDistance + 1 - j <= max) {
                    Point p = new Point(sensor.x + j, sensor.y + manhattanDistance + 1 - j);
                    boolean isInDistance = false;

                    for (int k = 0; k < sensorCoords.size(); k++) {
                        if (i != k) {
                            int d = getManhattanDistance(sensorCoords.get(k)[0], sensorCoords.get(k)[1], beaconCoords.get(k)[0], beaconCoords.get(k)[1]);
                            isInDistance = isPointInDistanceOfOther(p, new Point(sensorCoords.get(k)[0], sensorCoords.get(k)[1]), d);
                        }

                        if (isInDistance) {
                            break;
                        }
                    }

                    if (!isInDistance) {
                        beaconPoint = p;
                        break;
                    }
                }

                if (sensor.x + j >= 0 && sensor.x + j <= max && sensor.y - manhattanDistance - 1 + j >= 0 && sensor.y - manhattanDistance - 1 + j <= max) {
                    Point p = new Point(sensor.x + j, sensor.y - manhattanDistance - 1 + j);
                    boolean isInDistance = false;

                    for (int k = 0; k < sensorCoords.size(); k++) {
                        if (i != k) {
                            int d = getManhattanDistance(sensorCoords.get(k)[0], sensorCoords.get(k)[1], beaconCoords.get(k)[0], beaconCoords.get(k)[1]);
                            isInDistance = isPointInDistanceOfOther(p, new Point(sensorCoords.get(k)[0], sensorCoords.get(k)[1]), d);
                        }

                        if (isInDistance) {
                            break;
                        }
                    }

                    if (!isInDistance) {
                        beaconPoint = p;
                        break;
                    }
                }

                if (sensor.x - j >= 0 && sensor.x - j <= max && sensor.y + manhattanDistance + 1 - j >= 0 && sensor.y + manhattanDistance + 1 - j <= max) {
                    Point p = new Point(sensor.x - j, sensor.y + manhattanDistance + 1 - j);
                    boolean isInDistance = false;

                    for (int k = 0; k < sensorCoords.size(); k++) {
                        if (i != k) {
                            int d = getManhattanDistance(sensorCoords.get(k)[0], sensorCoords.get(k)[1], beaconCoords.get(k)[0], beaconCoords.get(k)[1]);
                            isInDistance = isPointInDistanceOfOther(p, new Point(sensorCoords.get(k)[0], sensorCoords.get(k)[1]), d);
                        }

                        if (isInDistance) {
                            break;
                        }
                    }

                    if (!isInDistance) {
                        beaconPoint = p;
                        break;
                    }
                }

                if (sensor.x - j >= 0 && sensor.x - j <= max && sensor.y - manhattanDistance - 1 + j >= 0 && sensor.y - manhattanDistance - 1 + j <= max) {
                    Point p = new Point(sensor.x - j, sensor.y - manhattanDistance - 1 + j);
                    boolean isInDistance = false;

                    for (int k = 0; k < sensorCoords.size(); k++) {
                        if (i != k) {
                            int d = getManhattanDistance(sensorCoords.get(k)[0], sensorCoords.get(k)[1], beaconCoords.get(k)[0], beaconCoords.get(k)[1]);
                            isInDistance = isPointInDistanceOfOther(p, new Point(sensorCoords.get(k)[0], sensorCoords.get(k)[1]), d);
                        }

                        if (isInDistance) {
                            break;
                        }
                    }

                    if (!isInDistance) {
                        beaconPoint = p;
                        break;
                    }
                }
            }

            if (beaconPoint.x != -1) {
                break;
            }
        }

        long frequency = (long)(beaconPoint.x) * 4000000L + beaconPoint.y;

        System.out.println("Part 2: " + frequency);
    }
}
