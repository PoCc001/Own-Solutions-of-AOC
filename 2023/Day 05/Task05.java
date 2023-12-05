package aocjava;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Task05 {
    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input05.txt");
        List<Long> seeds = Arrays.stream(stringList.get(0).substring(stringList.get(0).indexOf(':') + 2).split(" ")).map(Long::parseLong).toList();
        List<Function<Long, Long>> maps = new ArrayList<>();
        for (int i = 2; i < stringList.size(); i++) {
            i++;
            String line = stringList.get(i);
            List<Function<Long, Long>> partFns = new ArrayList<>();
            while (line.length() != 0) {
                List<Long> range = Arrays.stream(line.split(" ")).map(Long::parseLong).toList();
                partFns.add(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long l) {
                        if (l >= range.get(1) && l < range.get(1) + range.get(2)) {
                            return range.get(0) + l - range.get(1);
                        } else {
                            return null;
                        }
                    }
                });
                i++;
                if (i == stringList.size()) {
                    break;
                }
                line = stringList.get(i);
            }
            maps.add(new Function<Long, Long>() {
                @Override
                public Long apply(Long l) {
                    for (Function<Long, Long> f : partFns) {
                        if (f.apply(l) != null) {
                            return f.apply(l);
                        }
                    }
                    return l;
                }
            });
        }

        long minLocation = Long.MAX_VALUE;
        for (Long seed : seeds) {
            long value = seed;
            for (Function<Long, Long> map : maps) {
                value = map.apply(value);
            }
            minLocation = Math.min(minLocation, value);
        }
        System.out.println("Part 1: " + minLocation);

        // This code solves Part 2 quite fast (below it is another piece of code showing how I originally did it to get the star)
        // I created it while the other code for Part 2 was running, because I was dissatisfied with the poor performance (I can do better than this! - And I did)
        List<Function<Long, Long>> reverseMaps = new ArrayList<>();
        for (int i = 2; i < stringList.size(); i++) {
            i++;
            String line = stringList.get(i);
            List<Function<Long, Long>> partFns = new ArrayList<>();
            while (line.length() != 0) {
                List<Long> range = Arrays.stream(line.split(" ")).map(Long::parseLong).toList();
                partFns.add(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long l) {
                        if (l >= range.get(0) && l < range.get(0) + range.get(2)) {
                            return range.get(1) + l - range.get(0);
                        } else {
                            return null;
                        }
                    }
                });
                i++;
                if (i == stringList.size()) {
                    break;
                }
                line = stringList.get(i);
            }
            reverseMaps.add(new Function<Long, Long>() {
                @Override
                public Long apply(Long l) {
                    for (Function<Long, Long> f : partFns) {
                        if (f.apply(l) != null) {
                            return f.apply(l);
                        }
                    }
                    return l;
                }
            });
        }

        long val = Stream.iterate(0L, l -> l + 1L).map(l -> {
            long value = l;
            for (int i = reverseMaps.size() - 1; i >= 0; i--) {
                value = reverseMaps.get(i).apply(value);
            }
            return value;
        }).filter(l -> {
            for (int i = 0; i < seeds.size() / 2; i++) {
                if (l >= seeds.get(i * 2) && l < seeds.get(i * 2) + seeds.get(i * 2 + 1)) {
                    return true;
                }
            }
            return false;
        }).findFirst().get();
        for (Function<Long, Long> map : maps) {
            val = map.apply(val);
        }

        System.out.println("Part 2 (fast code): " + val);

        // I originally solved Part 2 with the following code:
        // This part takes some time (several minutes - quite a lot of them actually)!
        minLocation = Long.MAX_VALUE;
        for (int i = 0; i < seeds.size() / 2; i++) {
            for (long j = seeds.get(i * 2); j < seeds.get(i * 2) + seeds.get(i * 2 + 1); j++) {
                long value = j;
                for (Function<Long, Long> map : maps) {
                    value = map.apply(value);
                }
                minLocation = Math.min(minLocation, value);
            }
        }
        System.out.println("Part 2 (original slow code): " + minLocation);
    }
}
