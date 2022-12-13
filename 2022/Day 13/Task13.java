package aocjava;

import java.util.*;

public class Task13 {
    interface Node extends Comparable<Node> {
        @Override
        int compareTo(Node other);
    }

    static class IntNode implements Node {
        private final int number;

        public IntNode(int n) {
            number = n;
        }

        @Override
        public int compareTo(Node other) {
            if (other.getClass() == IntNode.class) {
                IntNode oIntNode = (IntNode)(other);
                return Integer.compare(number, oIntNode.number);
            } else {
                ListNode oListNode = (ListNode)(other);
                ListNode thisListNode = new ListNode(this);
                return thisListNode.compareTo(oListNode);
            }
        }

        @Override
        public String toString() {
            return String.valueOf(number);
        }
    }

    static class ListNode implements Node {
        private final List<Node> nodeList;

        public ListNode(Node... nodes) {
            nodeList = List.of(nodes);
        }

        public ListNode(Collection<Node> nodes) {
            nodeList = new ArrayList<>();
            nodeList.addAll(nodes);
        }

        public static ListNode createNode(String s) {
            s = s.substring(1, s.length() - 1);
            List<Node> subNodes = new ArrayList<>();

            int index = 0;
            while (s.length() != 0) {
                if (s.charAt(0) != '[') {
                    int indexOfComma = s.indexOf(',');
                    if (indexOfComma < 0) {
                        subNodes.add(new IntNode(Integer.parseInt(s)));
                    } else {
                        subNodes.add(new IntNode(Integer.parseInt(s.substring(0, indexOfComma))));
                    }

                    if (s.indexOf(',') < 0) {
                        break;
                    }

                    s = s.substring(s.indexOf(',') + 1);
                } else {
                    index++;
                    int i = 1;
                    while (index != 0) {
                        if (s.charAt(i) == '[') {
                            index++;
                        } else if (s.charAt(i) == ']') {
                            index--;
                        }

                        i++;
                    }

                    subNodes.add(createNode(s.substring(0, i)));
                    s = s.substring(Math.min(i + 1, s.length()));
                }
            }

            return new ListNode(subNodes);
        }
        @Override
        public int compareTo(Node other) {
            if (other.getClass() == IntNode.class) {
                if (nodeList.size() == 0) {
                    return -1;
                } else if (nodeList.size() > 1) {
                    return nodeList.get(0).compareTo(other) < 0 ? -1 : 1;
                } else {
                    ListNode oListNode = new ListNode(other);
                    return compareTo(oListNode);
                }
            } else {
                ListNode oListNode = (ListNode)(other);
                int min = Math.min(nodeList.size(), oListNode.nodeList.size());
                for (int i = 0; i < min; i++) {
                    int r = nodeList.get(i).compareTo(oListNode.nodeList.get(i));
                    if (r != 0) {
                        return r;
                    }
                }

                return Integer.compare(nodeList.size(), oListNode.nodeList.size());
            }
        }

        @Override
        public String toString() {
            return nodeList.toString();
        }
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input13.txt");

        List<Node[]> pairs = new ArrayList<>();
        for (int i = 0; i < arr.length; i += 3) {
            Node[] pair = new Node[2];
            pair[0] = ListNode.createNode(arr[i]);
            pair[1] = ListNode.createNode(arr[i + 1]);
            pairs.add(pair);
        }

        int correctPairIndexSum = 0;

        for (int i = 0; i < pairs.size(); i++) {
            if (pairs.get(i)[0].compareTo(pairs.get(i)[1]) <= 0) {
                correctPairIndexSum += (i + 1);
            }
        }

        System.out.println("Part 1: " + correctPairIndexSum);

        List<Node> sortedList = new ArrayList<>();
        Node divider1 = new ListNode(new ListNode(new IntNode(2)));
        Node divider2 = new ListNode(new ListNode(new IntNode(6)));
        sortedList.add(divider1);
        sortedList.add(divider2);

        for (Node[] pair : pairs) {
            sortedList.add(pair[0]);
            sortedList.add(pair[1]);
        }

        sortedList.sort(Comparator.naturalOrder());
        int div1Index = sortedList.indexOf(divider1) + 1;
        int div2Index = sortedList.indexOf(divider2) + 1;

        System.out.println("Part 2: " + (div1Index * div2Index));
    }
}
