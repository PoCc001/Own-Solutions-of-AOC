package aocjava;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Task07 {
    private record CamelCard(String hand, Long bid) {}

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input07.txt");
        List<CamelCard> camelCards = stringList.stream().
                map(s -> new CamelCard(s.substring(0, s.indexOf(' ')), Long.parseLong(s.substring(s.indexOf(' ') + 1)))).toList();

        List<CamelCard> sortableCamelCards = new ArrayList<>(camelCards);

        // maybe I didn't consider some cases in this mess of comparators, but it worked for my input
        sortableCamelCards.sort(Comparator.comparingLong((CamelCard cc) -> cc.hand().chars().distinct().count()).reversed().thenComparing((cc1, cc2) -> {
            if (cc1.hand().chars().distinct().count() == 3 || cc1.hand().chars().distinct().count() == 2) {
                long maxInCommon1 = 0;
                for (int i = 0; i < 5; i++) {
                    final int fi = i;
                    maxInCommon1 = Math.max(cc1.hand().chars().filter(c -> c == cc1.hand().charAt(fi)).count(), maxInCommon1);
                }
                long maxInCommon2 = 0;
                for (int i = 0; i < 5; i++) {
                    final int fi = i;
                    maxInCommon2 = Math.max(cc2.hand().chars().filter(c -> c == cc2.hand().charAt(fi)).count(), maxInCommon2);
                }
                return Long.compare(maxInCommon1, maxInCommon2);
            } else {
                return 0;
            }
        }).thenComparing((cc1, cc2) -> {
            for (int i = 0; i < 5; i++) {
                char c1 = cc1.hand().charAt(i);
                char c2 = cc2.hand().charAt(i);
                if (c1 != c2) {
                    if (Character.isDigit(c1) && Character.isDigit(c2)) {
                        return Character.compare(c1, c2);
                    } else if (Character.isDigit(c1) && !Character.isDigit(c2)) {
                        return -1;
                    } else if (!Character.isDigit(c1) && Character.isDigit(c2)) {
                        return 1;
                    } else {
                        if (c1 == 'A') {
                            return 1;
                        } else if (c2 == 'A') {
                            return -1;
                        } else if (c1 == 'K') {
                            return 1;
                        } else if (c2 == 'K') {
                            return -1;
                        } else if (c1 == 'Q') {
                            return 1;
                        } else if (c2 == 'Q') {
                            return -1;
                        } else if (c1 == 'J') {
                            return 1;
                        } else if (c2 == 'J') {
                            return -1;
                        }
                    }
                }
            }
            return 0;
        }));

        long result1 = 0;

        for (int i = 0; i < sortableCamelCards.size(); i++) {
            result1 += sortableCamelCards.get(i).bid() * (i + 1L);
        }

        System.out.println("Part 1: " + result1);

        // maybe I didn't consider some cases in this mess of comparators, but it worked for my input
        sortableCamelCards.sort(
                (CamelCard cc1, CamelCard cc2) -> {
                    long maxInCommon1 = 0;
                    long jokerCount1 = 0;
                    char maxInCommonChar1 = 'J';
                    for (int i = 0; i < 5; i++) {
                        final int fi = i;
                        if (cc1.hand().charAt(i) == 'J') {
                            jokerCount1 = cc1.hand().chars().filter(c -> c == cc1.hand().charAt(fi)).count();
                        } else if (cc1.hand().chars().filter(c -> c == cc1.hand().charAt(fi)).count() > maxInCommon1) {
                            maxInCommon1 = cc1.hand().chars().filter(c -> c == cc1.hand().charAt(fi)).count();
                            maxInCommonChar1 = cc1.hand().charAt(i);
                        }
                    }

                    final char fMaxInCommonChar1 = maxInCommonChar1;

                    maxInCommon1 += jokerCount1;

                    long maxInCommon2 = 0;
                    long jokerCount2 = 0;
                    char maxInCommonChar2 = 'J';
                    for (int i = 0; i < 5; i++) {
                        final int fi = i;
                        if (cc2.hand().charAt(i) == 'J') {
                            jokerCount2 = cc2.hand().chars().filter(c -> c == cc2.hand().charAt(fi)).count();
                        } else if (cc2.hand().chars().filter(c -> c == cc2.hand().charAt(fi)).count() > maxInCommon2) {
                            maxInCommon2 = cc2.hand().chars().filter(c -> c == cc2.hand().charAt(fi)).count();
                            maxInCommonChar2 = cc2.hand().charAt(i);
                        }
                    }
                    maxInCommon2 += jokerCount2;

                    final char fMaxInCommonChar2 = maxInCommonChar2;

                    int comp = Long.compare(cc2.hand().chars().map(c -> c == 'J' ? fMaxInCommonChar2 : c).distinct().count(), cc1.hand().chars().map(c -> c == 'J' ? fMaxInCommonChar1 : c).distinct().count());

                    if (maxInCommon1 != maxInCommon2) {
                        return Long.compare(maxInCommon1, maxInCommon2);
                    } else if (comp != 0 && (maxInCommon1 == 2 || maxInCommon1 == 3)) {
                        return comp;
                    } else {
                        for (int i = 0; i < 5; i++) {
                            char c1 = cc1.hand().charAt(i);
                            char c2 = cc2.hand().charAt(i);
                            if (c1 != c2) {
                                if (c1 == 'J') {
                                    return -1;
                                } else if (c2 == 'J') {
                                    return 1;
                                } else if (Character.isDigit(c1) && Character.isDigit(c2)) {
                                    return Character.compare(c1, c2);
                                } else if (Character.isDigit(c1) && !Character.isDigit(c2)) {
                                    return -1;
                                } else if (!Character.isDigit(c1) && Character.isDigit(c2)) {
                                    return 1;
                                } else {
                                    if (c1 == 'A') {
                                        return 1;
                                    } else if (c2 == 'A') {
                                        return -1;
                                    } else if (c1 == 'K') {
                                        return 1;
                                    } else if (c2 == 'K') {
                                        return -1;
                                    } else if (c1 == 'Q') {
                                        return 1;
                                    } else if (c2 == 'Q') {
                                        return -1;
                                    }
                                }
                            }
                        }
                        return 0;
                    }
                }
        );

        long result2 = 0;

        for (int i = 0; i < sortableCamelCards.size(); i++) {
            result2 += sortableCamelCards.get(i).bid() * (i + 1L);
        }

        System.out.println("Part 2: " + result2);
    }
}
