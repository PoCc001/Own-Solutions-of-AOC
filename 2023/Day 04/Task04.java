package aocjava;

import java.math.BigInteger;
import java.util.*;

public class Task04 {
    private record Card(int index, List<String> winningNums, List<String> nums, int matchCount) {}

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input04.txt");
        BigInteger totalWorth = BigInteger.ZERO;
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            String s = stringList.get(i);
            String s1 = s.substring(s.indexOf(':') + 2, s.indexOf('|') - 1);
            String s2 = s.substring(s.indexOf('|') + 2);
            List<String> winningNums = Arrays.stream(s1.split(" ")).filter(str -> str.length() != 0).toList();
            final List<String> nums = Arrays.stream(s2.split(" ")).filter(str -> str.length() != 0).toList();
            totalWorth = totalWorth.add(BigInteger.ONE.shiftLeft((int)winningNums.stream().filter(nums::contains).count() - 1));
            cards.add(new Card(i + 1, winningNums, nums, (int)winningNums.stream().filter(nums::contains).count()));
        }
        System.out.println("Part 1: " + totalWorth);

        Queue<Card> cardsToDealWith = new ArrayDeque<>(cards);
        int cardCount = cards.size();
        while (!cardsToDealWith.isEmpty()) {
            Card c = cardsToDealWith.poll();
            int count = 0;
            for (int i = c.index() + 1; i <= cards.size() && i <= c.index() + c.matchCount(); i++) {
                cardsToDealWith.add(cards.get(i - 1));
                count++;
            }
            cardCount += count;
        }
        System.out.println("Part 2: " + cardCount);
    }
}
