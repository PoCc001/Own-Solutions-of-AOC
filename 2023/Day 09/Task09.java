package aocjava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Task09 {
    private static class History {
        private List<List<Integer>> history;

        History(List<Integer> ints) {
            history = new ArrayList<>();
            history.add(new ArrayList<>(ints));
        }

        List<Integer> lastStep() {
            return history.get(history.size() - 1);
        }

        void calculateOneStep() {
            List<Integer> nextStep = new ArrayList<>();
            for (int i = 0; i < lastStep().size() - 1; i++) {
                nextStep.add(lastStep().get(i + 1) - lastStep().get(i));
            }
            history.add(nextStep);
        }

        void calculateAll() {
            while (!(lastStep().get(0) == 0 && lastStep().stream().distinct().toList().size() == 1)) {
                calculateOneStep();
            }
        }

        long predictNext() {
            int prediction = 0;
            lastStep().add(prediction);
            for (int i = history.size() - 2; i >= 0; i--) {
                prediction = history.get(i + 1).get(history.get(i + 1).size() - 1) + history.get(i).get(history.get(i).size() - 1);
                history.get(i).add(prediction);
            }
            return prediction;
        }

        long predictPrevious() {
            int prediction = 0;
            lastStep().add(0, prediction);
            for (int i = history.size() - 2; i >= 0; i--) {
                prediction = history.get(i).get(0) - history.get(i + 1).get(0);
                history.get(i).add(0, prediction);
            }
            return prediction;
        }

        @Override
        public String toString() {
            String str = "";
            for (int i = 0; i < history.size(); i++) {
                str += history.get(i).toString();
                if (i != history.size() - 1) {
                    str += "\n";
                }
            }
            return str;
        }
    }

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input09.txt");

        List<History> histories = stringList.stream().
                map(s -> new History(Arrays.stream(s.split(" ")).filter(st -> st.length() != 0).map(Integer::parseInt).toList())).toList();

        for (History history : histories) {
            history.calculateAll();
        }

        long result = histories.stream().mapToLong(History::predictNext).sum();

        System.out.println("Part 1: " + result);

        result = histories.stream().mapToLong(History::predictPrevious).sum();

        System.out.println("Part 2: " + result);
    }
}
