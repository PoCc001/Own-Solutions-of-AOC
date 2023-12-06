package aocjava;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task06 {
    private record Calc(int maxTime, int recordDistance) {
        int getDistance(int timePressed) {
            return (maxTime - timePressed) * timePressed;
        }

        long getNumberOfGoodTimes() {
            long count = 0;
            for (int i = 1; i < maxTime; i++) {
                int d = getDistance(i);
                if (d > recordDistance) {
                    count++;
                }
            }
            return count;
        }
    }

    // Instead of the quadratic formula I've used a binary search algorithm LOL:
    private static BigInteger[] search(BigInteger distance, BigInteger maxTime) {
        BigInteger[] r = new BigInteger[2];
        r[0] = r[1] = maxTime.shiftRight(1);
        BigInteger higherr0 = r[0];
        BigInteger lowerr0 = BigInteger.ZERO;
        r[0] = r[0].shiftRight(1);
        while (maxTime.subtract(r[0]).multiply(r[0]).compareTo(distance) != 0) {
            if (lowerr0.add(BigInteger.ONE).compareTo(higherr0) == 0) {
                if (r[0].compareTo(lowerr0) == 0) {
                    r[0] = higherr0;
                } else {
                    r[0] = lowerr0;
                }
                break;
            }
            if (maxTime.subtract(r[0]).multiply(r[0]).compareTo(distance) < 0) {
                lowerr0 = r[0];
            } else {
                higherr0 = r[0];
            }
            r[0] = lowerr0.add(higherr0).shiftRight(1);
        }

        BigInteger higherr1 = maxTime;
        BigInteger lowerr1 = r[1];
        r[1] = higherr1.add(lowerr1).shiftRight(1);

        while (maxTime.subtract(r[1]).multiply(r[1]).compareTo(distance) != 0) {
            if (lowerr1.add(BigInteger.ONE).compareTo(higherr1) == 0) {
                if (r[1].compareTo(lowerr1) == 0) {
                    r[1] = higherr1;
                } else {
                    r[1] = lowerr1;
                }
                break;
            }
            if (maxTime.subtract(r[1]).multiply(r[1]).compareTo(distance) > 0) {
                lowerr1 = r[1];
            } else {
                higherr1 = r[1];
            }
            r[1] = lowerr1.add(higherr1).shiftRight(1);
        }

        // There might be 1-off errors! (In my case they weren't a problem)
        return r;
    }

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input06.txt");
        String times = stringList.get(0).substring(stringList.get(0).indexOf(':') + 1).trim();
        String distances = stringList.get(1).substring(stringList.get(1).indexOf(':') + 1).trim();
        List<Integer> timesList = Arrays.stream(times.split(" ")).filter(s -> s.length() != 0).map(Integer::parseInt).toList();
        List<Integer> distancesList = Arrays.stream(distances.split(" ")).filter(s -> s.length() != 0).map(Integer::parseInt).toList();
        List<Calc> calcs = new ArrayList<>();
        for (int i = 0; i < timesList.size(); i++) {
            calcs.add(new Calc(timesList.get(i), distancesList.get(i)));
        }

        BigInteger multCount = BigInteger.ONE;
        for (Calc c : calcs) {
            multCount = multCount.multiply(BigInteger.valueOf(c.getNumberOfGoodTimes()));
        }

        System.out.println("Part 1: " + multCount);

        BigInteger time = BigInteger.ZERO;
        for (Integer i : timesList) {
            time = time.multiply(BigInteger.TEN.pow(String.valueOf(i).length())).add(BigInteger.valueOf(i));
        }

        BigInteger distance = BigInteger.ZERO;
        for (Integer i : distancesList) {
            distance = distance.multiply(BigInteger.TEN.pow(String.valueOf(i).length())).add(BigInteger.valueOf(i));
        }

        BigInteger[] results = search(distance, time);
        BigInteger result = results[1].subtract(results[0]);

        System.out.println("Part 2: " + result);
    }
}
