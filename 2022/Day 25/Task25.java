package aocjava;

import java.math.BigInteger;

public class Task25 {
    static class SNAFU {
        private final String number;

        public SNAFU(String snafu) {
            number = snafu;
        }

        public static SNAFU fromBigInteger(BigInteger bi) {
            StringBuilder sb = new StringBuilder();
            String str = bi.toString(5);
            boolean carry = false;
            for (int i = str.length() - 1; i >= 0; i--) {
                char digit = str.charAt(i);
                if (carry) {
                    digit += (char)(1);
                }

                if (digit == '5') {
                    sb.append('0');
                    if (i == 0) {
                        sb.append('1');
                    }
                    continue;
                }

                if (digit <= '2') {
                    carry = false;
                    sb.append(digit);
                } else {
                    sb.append(digit == '3' ? '=' : '-');
                    carry = true;
                }
            }

            return new SNAFU(sb.reverse().toString());
        }

        public BigInteger evaluate() {
            BigInteger value = BigInteger.ZERO;
            BigInteger five = BigInteger.valueOf(5);
            BigInteger power = BigInteger.ONE;
            for (int i = number.length() - 1; i >= 0; i--) {
                char digit = number.charAt(i);
                switch (digit) {
                    case '2' -> value = value.add(power.shiftLeft(1));
                    case '1' -> value = value.add(power);
                    case '-' -> value = value.subtract(power);
                    case '=' -> value = value.subtract(power.shiftLeft(1));
                }

                power = power.multiply(five);
            }

            return value;
        }

        @Override
        public String toString() {
            return number;
        }
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input25.txt");

        SNAFU[] snafus = new SNAFU[arr.length];

        BigInteger bigInteger = BigInteger.ZERO;
        for (int i = 0; i < arr.length; i++) {
            snafus[i] = new SNAFU(arr[i]);
            bigInteger = bigInteger.add(snafus[i].evaluate());
        }

        System.out.println("Part 1: " + SNAFU.fromBigInteger(bigInteger));
    }
}
