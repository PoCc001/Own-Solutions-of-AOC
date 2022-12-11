package aocjava;

import java.util.*;

public class Task11 {
    static class Item {
        private long worryLevel = 0;

        public Item(long worryLevel) {
            this.worryLevel = worryLevel;
        }

        public void setWorryLevel(long l) {
            worryLevel = l;
        }

        public long getWorryLevel() {
            return worryLevel;
        }
    }

    static class Monkey {
        private final List<Item> items;
        private long overallItemCount = 0;
        private final int ifTrue;
        private final int ifFalse;

        public Monkey(int ifTrue, int ifFalse, int... worryLevels) {
            items = new LinkedList<>();

            for (int i = 0; i < worryLevels.length; i++) {
                items.add(new Item(worryLevels[i]));
            }

            this.ifTrue = ifTrue;
            this.ifFalse = ifFalse;
        }

        public void throwItem(Monkey other) {
            if (items.size() > 0) {
                other.receive(items.get(0));
                items.remove(0);
                overallItemCount++;
            }
        }

        public void receive(Item item) {
            items.add(item);
        }

        public long getOverallItemCount() {
            return overallItemCount;
        }

        public Item getItem() {
            return items.size() > 0 ? items.get(0) : null;
        }

        public int getIfTrue() {
            return ifTrue;
        }

        public int getIfFalse() {
            return ifFalse;
        }
    }

    public static long newWorryLevel(String[] arr, int monkeyIndex, long oldWorryLevel) {
        String s = arr[7 * monkeyIndex + 2].substring(23);
        long old = s.substring(2).equals("old") ? oldWorryLevel : Long.parseLong(s.substring(2));
        if (s.charAt(0) == '+') {
            return (oldWorryLevel + old) / 3;
        } else {
            return (oldWorryLevel * old) / 3;
        }
    }

    public static long newWorryLevel2(String[] arr, int monkeyIndex, long oldWorryLevel) {
        String s = arr[7 * monkeyIndex + 2].substring(23);
        long old = s.substring(2).equals("old") ? oldWorryLevel : Long.parseLong(s.substring(2));
        if (s.charAt(0) == '+') {
            return oldWorryLevel + old;
        } else {
            return oldWorryLevel * old;
        }
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input11.txt");

        int monkeyLength = arr.length / 7 + (arr.length % 7 == 0 ? 0 : 1);
        Monkey[] monkeys = new Monkey[monkeyLength];

        for (int i = 0; i < monkeys.length; i++) {
            List<String> items = AOCUtils.divideString(arr[7 * i + 1].substring(18), ", ");
            int[] worryLevels = new int[items.size()];
            for (int j = 0; j < worryLevels.length; j++) {
                worryLevels[j] = Integer.parseInt(items.get(j));
            }

            int ifTrue = Integer.parseInt(arr[7 * i + 4].substring(29));
            int ifFalse = Integer.parseInt(arr[7 * i + 5].substring(30));

            monkeys[i] = new Monkey(ifTrue, ifFalse, worryLevels);
        }

        for (int i = 0; i < 20 * monkeys.length; i++) {
            Item item = monkeys[i % monkeys.length].getItem();

            while (item != null) {
                item.setWorryLevel(newWorryLevel(arr, i % monkeys.length, item.getWorryLevel()));
                int test = Integer.parseInt(arr[(i % monkeys.length) * 7 + 3].substring(21));
                boolean b = item.getWorryLevel() % test == 0;

                Monkey m;
                if (b) {
                    m = monkeys[monkeys[i % monkeys.length].getIfTrue()];
                } else {
                    m = monkeys[monkeys[i % monkeys.length].getIfFalse()];
                }
                monkeys[i % monkeys.length].throwItem(m);
                item = monkeys[i % monkeys.length].getItem();
            }
        }

        SortedSet<Monkey> sortedMonkeys = new TreeSet<>(Comparator.comparingLong(Monkey::getOverallItemCount));
        sortedMonkeys.addAll(List.of(monkeys));

        long max = sortedMonkeys.last().getOverallItemCount();
        long secondMax = sortedMonkeys.headSet(sortedMonkeys.last()).last().getOverallItemCount();

        System.out.println("Part 1: " + (max * secondMax));


        monkeys = new Monkey[monkeyLength];
        long mod = 1L;

        for (int i = 0; i < monkeys.length; i++) {
            List<String> items = AOCUtils.divideString(arr[7 * i + 1].substring(18), ", ");
            int[] worryLevels = new int[items.size()];
            for (int j = 0; j < worryLevels.length; j++) {
                worryLevels[j] = Integer.parseInt(items.get(j));
            }

            int ifTrue = Integer.parseInt(arr[7 * i + 4].substring(29));
            int ifFalse = Integer.parseInt(arr[7 * i + 5].substring(30));

            monkeys[i] = new Monkey(ifTrue, ifFalse, worryLevels);
            long test = Long.parseLong(arr[(i % monkeys.length) * 7 + 3].substring(21));
            mod *= test;
        }

        for (int i = 0; i < 10000 * monkeys.length; i++) {
            Item item = monkeys[i % monkeys.length].getItem();

            while (item != null) {
                item.setWorryLevel(newWorryLevel2(arr, i % monkeys.length, item.getWorryLevel()) % mod);
                long test = Long.parseLong(arr[(i % monkeys.length) * 7 + 3].substring(21));
                boolean b = item.getWorryLevel() % test == 0;

                Monkey m;
                if (b) {
                    m = monkeys[monkeys[i % monkeys.length].getIfTrue()];
                } else {
                    m = monkeys[monkeys[i % monkeys.length].getIfFalse()];
                }
                monkeys[i % monkeys.length].throwItem(m);
                item = monkeys[i % monkeys.length].getItem();
            }
        }

        sortedMonkeys = new TreeSet<>(Comparator.comparingLong(Monkey::getOverallItemCount));
        sortedMonkeys.addAll(List.of(monkeys));

        max = sortedMonkeys.last().getOverallItemCount();
        secondMax = sortedMonkeys.headSet(sortedMonkeys.last()).last().getOverallItemCount();

        System.out.println("Part 1: " + (max * secondMax));
    }
}
