package aocjava;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

public class Task12 {
    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("/home/johannes/Schreibtisch/AdventOfCode/Inputs/Input12.txt");

        long result = 0;

        for (String s : stringList) {
            String[] split = s.split(" ");
            int unknownCount = (int)split[0].chars().filter(c -> c == '?').count();
            BigInteger bigInteger = BigInteger.ONE.shiftLeft(unknownCount).subtract(BigInteger.ONE);
            int count = 0;
            List<Integer> parity = new LinkedList<>(Stream.of(split[1].split(",")).map(Integer::parseInt).toList());
            while (bigInteger.signum() >= 0) {
                StringBuilder matchedBuilder = new StringBuilder();
                BigInteger bits = bigInteger;
                for (int i = 0; i < split[0].length(); i++) {
                    if (s.charAt(i) == '?') {
                        matchedBuilder.append(bits.testBit(0) ? '#' : '.');
                        bits = bits.shiftRight(1);
                    } else {
                        matchedBuilder.append(split[0].charAt(i));
                    }
                }
                String matched = matchedBuilder.toString();
                parity = new LinkedList<>(Stream.of(split[1].split(",")).map(Integer::parseInt).toList());
                boolean matches = true;
                for (int i = 0; i < matched.length(); i++) {
                    if (matched.charAt(i) != '.') {
                        if (parity.size() == 0) {
                            matches = false;
                            break;
                        }
                        int l = 0;
                        while (i < matched.length() && matched.charAt(i) == '#') {
                            l++;
                            i++;
                        }
                        if (l != parity.remove(0)) {
                            matches = false;
                            break;
                        }
                    }
                }
                matches = matches && parity.isEmpty();
                count += matches ? 1 : 0;
                bigInteger = bigInteger.subtract(BigInteger.ONE);
            }
            result += count;
        }

        System.out.println("Part 1: " + result);
    }
}
