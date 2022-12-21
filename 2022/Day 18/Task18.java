package aocjava;

import java.util.*;
import java.util.stream.Collectors;

public class Task18 {
    static class Cube {
        public final int x;
        public final int y;
        public int z;

        public Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean isAdjacentTo(Cube c) {
            if (x == c.x && y == c.y) {
                return Math.abs(z - c.z) == 1;
            } else if (x == c.x && z == c.z) {
                return Math.abs(y - c.y) == 1;
            } else if (y == c.y && z == c.z) {
                return Math.abs(x - c.x) == 1;
            }

            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cube cube = (Cube) o;
            return x == cube.x && y == cube.y && z == cube.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public String toString() {
            return String.format("Cube: {x = %d, y = %d, z = %d}", x, y, z);
        }
    }

    static class LavaCube extends Cube {
        public LavaCube(int x, int y, int z) {
            super(x, y, z);
        }
    }

    static class AirCube extends Cube {
        private int numOfAdjacentCubes;

        public AirCube(int x, int y, int z) {
            super(x, y, z);
        }

        @Override
        public boolean isAdjacentTo(Cube c) {
            if (super.isAdjacentTo(c)) {
                numOfAdjacentCubes++;
                return true;
            } else {
                return false;
            }
        }

        public void resetAdjacentCubeCount() {
            numOfAdjacentCubes = 0;
        }

        public int getNumOfAdjacentCubes() {
            return numOfAdjacentCubes;
        }
    }

    public static int calcSides(List<Cube> cubes) {
        int sides = cubes.size() * 6;
        for (Cube c1 : cubes) {
            for (Cube c2 : cubes) {
                if (c1.isAdjacentTo(c2)) {
                    sides--;
                }
            }
        }

        return sides;
    }

    public static List<Cube> getCubeList(int x, int y, int z, List<Cube> cubes) {
        List<List<List<Cube>>> tensor = new ArrayList<>();
        for (int i = 0; i <= z; i++) {
            tensor.add(new ArrayList<>());
            for (int j = 0; j <= y; j++) {
                tensor.get(i).add(new ArrayList<>());
                for (int k = 0; k <= x; k++) {
                    Cube cube = new LavaCube(k, j, i);
                    if (cubes.contains(cube)) {
                        tensor.get(i).get(j).add(cube);
                    } else {
                        tensor.get(i).get(j).add(new AirCube(k, j, i));
                    }
                }
            }
        }

        return tensor.stream().flatMap(Collection::stream).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input18.txt");

        List<Cube> cubes = new ArrayList<>();
        for (String s : arr) {
            int x = Integer.parseInt(s.substring(0, s.indexOf(',')));
            s = s.substring(s.indexOf(',') + 1);
            int y = Integer.parseInt(s.substring(0, s.indexOf(',')));
            s = s.substring(s.indexOf(',') + 1);
            int z = Integer.parseInt(s);
            cubes.add(new LavaCube(x, y, z));
        }

        int sides = calcSides(cubes);

        System.out.println("Part 1: " + sides);

        int maxX = cubes.stream().mapToInt(c -> c.x).max().getAsInt();
        int maxY = cubes.stream().mapToInt(c -> c.y).max().getAsInt();
        int maxZ = cubes.stream().mapToInt(c -> c.z).max().getAsInt();

        List<Cube> cubeList = getCubeList(maxX, maxY, maxZ, cubes);
        List<Cube> airCubeList = cubeList.stream().filter(c -> c.getClass() == AirCube.class).collect(Collectors.toList());
        int size = 0;
        while (size != cubeList.size()) {
            size = cubeList.size();
            for (int i = 0; i < airCubeList.size();) {
                AirCube ac = (AirCube)(airCubeList.get(i));
                for (Cube cube : cubeList) {
                    ac.isAdjacentTo(cube);
                }

                if (ac.getNumOfAdjacentCubes() < 6) {
                    cubeList.remove(ac);
                    airCubeList.remove(i);
                } else {
                    i++;
                }
                ac.resetAdjacentCubeCount();
            }
        }

        cubes.addAll(airCubeList);

        sides = calcSides(cubes);
        System.out.println("Part 2: " + sides);
    }
}
