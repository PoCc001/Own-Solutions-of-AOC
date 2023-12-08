package aocjava;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class Task08 {
    private static class Node {
        final String name;
        Node left;
        Node right;

        Node(String n) {
            name = n;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(name, node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

    static class OrderedHybridList {
        private final List<Long> greedyList = new ArrayList<>();
        private Function<Long, Long> lazyList;

        OrderedHybridList(List<Long> l, Function<Long, Long> f) {
            greedyList.addAll(l);
            lazyList = f;
        }

        OrderedHybridList(Function<Long, Long> f) {
            this(Collections.emptyList(), f);
        }

        boolean contains(Long l) {
            if (greedyList.contains(l)) {
                return true;
            } else {
                for (long i = 0; ; i++) {
                    Long value = lazyList.apply(i);
                    if (value > l) {
                        return false;
                    } else if (value.equals(l)) {
                        return true;
                    }
                }
            }
        }

        Long get(long i) {
            if (i < greedyList.size()) {
                return greedyList.get((int)i);
            } else {
                return lazyList.apply(i - greedyList.size());
            }
        }

        List<Long> take(int n) {
            if (n <= greedyList.size()) {
                return greedyList.subList(0, n);
            } else {
                List<Long> l = new ArrayList<>(greedyList);
                for (int i = 0; i < n - greedyList.size(); i++) {
                    l.add(lazyList.apply((long)i));
                }
                return l;
            }
        }
    }

    static Stream<Long> getIndicesOfEnds(Stream<Long> indices, int lengthOfLoop, int nrOfRecordedNodes) {
        int maxIndex = nrOfRecordedNodes - lengthOfLoop;
        List<Long> indicesList = indices.toList();
        Stream<Long> recordedIndices = indicesList.stream().filter(i -> i < maxIndex);
        if (indicesList.stream().noneMatch(i -> i >= maxIndex)) {
            return recordedIndices;
        } else {
            List<Long> otherIndices = indicesList.stream().filter(i -> i >= maxIndex).toList();
            return Stream.concat(recordedIndices, Stream.iterate(otherIndices, i -> otherIndices.stream().map(l -> l + lengthOfLoop).toList()).
                    flatMap(Collection::stream));
        }
    }

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input08.txt");

        List<Character> instructions = stringList.get(0).chars().mapToObj(c -> (Character)(char)c).toList();

        Map<String, List<String>> nodesMap = new HashMap<>();

        for (int i = 2; i < stringList.size(); i++) {
            String s = stringList.get(i);
            nodesMap.put(s.substring(0, s.indexOf(' ')), List.of(s.substring(s.indexOf('(') + 1, s.indexOf(',')), s.substring(s.indexOf(',') + 2, s.indexOf(')'))));
        }

       String start = "AAA";
        String end = "ZZZ";
        String current = start;

        int index = 0;
        long size = 0;
        while (!current.equals(end)) {
            if (instructions.get(index) == 'L') {
                current = nodesMap.get(current).get(0);
            } else {
                current = nodesMap.get(current).get(1);
            }
            index = (index + 1) % instructions.size();
            size++;
        }

        System.out.println("Part 1: " + size);

        // For Part 2 the solution is not automatically provided. Through manual investigation (also of the output) below I found out that each start node has
        // exactly one end node and that the time step at which this end node is reached repeats every nth step where n is the time step of reaching the end node the first time.
        // So you can calculate the lcm of all the time steps at which the end nodes are reached for the first time which gives you the result.

        List<String> currentNodes = new ArrayList<>(nodesMap.keySet().stream().filter(s -> s.charAt(2) == 'A').toList());       // mutable!
        Set<String> endNodes = new HashSet<>(nodesMap.keySet().stream().filter(s -> s.charAt(2) == 'Z').toList());

        List<Node> allNodes = nodesMap.keySet().stream().map(Node::new).toList();

        for (Node n : allNodes) {
            Node leftNode = new Node(nodesMap.get(n.name).get(0));
            Node rightNode = new Node(nodesMap.get(n.name).get(1));
            n.left = allNodes.get(allNodes.indexOf(leftNode));
            n.right = allNodes.get(allNodes.indexOf(rightNode));
        }

        List<List<Node>> nodeLoops = new ArrayList<>();
        Map<Node, Map<Node, List<Integer>>> endIndicesMap = new HashMap<>();
        List<Integer> loopingIndices = new ArrayList<>();
        List<Integer> lengths = new ArrayList<>();

        for (String curr : currentNodes) {
            List<Node> nodesList = new ArrayList<>();
            nodesList.add(allNodes.stream().filter(n -> n.name.equals(curr)).findAny().get());
            nodeLoops.add(nodesList);
        }

        for (List<Node> ln : nodeLoops) {
            index = 0;
            size = 0;
            Node curr = ln.get(0);
            ln.remove(0);
            Map<Node, List<Integer>> endIndices = new HashMap<>();
            while (true) {
                ln.add(curr);
                curr = instructions.get(index) == 'L' ? curr.left : curr.right;
                List<Integer> indices = AOCUtils.getAllIndices(ln, curr);
                if (curr.name.charAt(2) == 'Z') {
                    if (endIndices.containsKey(curr)) {
                        endIndices.get(curr).add((int)size + 1);
                    } else {
                        List<Integer> endIndicesList = new ArrayList<>();
                        endIndicesList.add((int)size + 1);
                        endIndices.put(curr, endIndicesList);
                    }
                }
                index = (index + 1) % instructions.size();
                size++;
                if (indices.stream().map(i -> i % instructions.size()).toList().contains(index)) {
                    size -= indices.get(0);
                    loopingIndices.add(indices.get(0));
                    break;
                }
            }
            endIndicesMap.put(ln.get(0), endIndices);
            lengths.add((int)size);
        }

        List<OrderedHybridList> ohlList = new ArrayList<>();

        for (int mapIndex = 0; mapIndex < currentNodes.size(); mapIndex++) {
            Map<Node, List<Integer>> m = endIndicesMap.get(new Node(currentNodes.get(mapIndex)));
            final long maxIndex = loopingIndices.get(mapIndex);
            final long loopLength = lengths.get(mapIndex);
            List<Long> distinctIndices = m.values().stream().flatMap(l -> l.stream().map(i -> (long)i)).filter(l -> l < maxIndex).toList();
            List<Long> otherIndices = m.values().stream().flatMap(l -> l.stream().map(i -> (long)i)).filter(l -> l >= maxIndex).toList();
            ohlList.add(new OrderedHybridList(distinctIndices, l -> {
                int listIndex = (int)(l % otherIndices.size());
                long factor = l / otherIndices.size();
                return otherIndices.get(listIndex) + loopLength * factor;
            }));
        }

        ohlList.forEach(l -> System.out.println(l.take(10)));

        current = currentNodes.get(0);

        index = 0;
        size = 0;
        for (int i = 0; i < 120830; i++) {
            if (instructions.get(index) == 'L') {
                current = nodesMap.get(current).get(0);
            } else {
                current = nodesMap.get(current).get(1);
            }
            index = (index + 1) % instructions.size();
            size++;
        }

        System.out.println(current);

        // You can do it the slow way - I have no idea, how long this takes (at least one hour, but I can't guarantee, it will finish within you lifetime)!

        index = 0;
        size = 0;
        while (!endNodes.containsAll(currentNodes)) {
            int instrIndex = instructions.get(index) == 'L' ? 0 : 1;
            currentNodes.replaceAll(key -> nodesMap.get(key).get(instrIndex));
            index = (index + 1) % instructions.size();
            size++;
        }

        System.out.println("Part 2: " + size);
    }
}
