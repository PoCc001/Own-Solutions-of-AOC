package aocjava;

import java.util.*;

public class Task12 {
    static class Node {
        private final int[] pos;
        private final char c;
        private int priority;

        public Node(int[] p, char c) {
            pos = p;
            this.c = c;
            priority = Integer.MAX_VALUE;
        }

        public int getX() {
            return pos[0];
        }

        public int getY() {
            return pos[1];
        }

        public char getChar() {
            return c;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int p) {
            priority = p;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o.getClass() != getClass()) {
                return false;
            } else {
                Node on = (Node)(o);
                return on.getX() == getX() && on.getY() == getY();
            }
        }

        @Override
        public String toString() {
            return String.valueOf(priority);
        }
    }

    static class AOCPriorityQueue {
        private static List<Node> nodes;

        public AOCPriorityQueue(Collection<Node> c) {
            nodes = new LinkedList<>();
            nodes.addAll(c);
        }

        public Node remove() {
            if (nodes.size() == 0) {
                return null;
            }

            Node smallest = nodes.get(0);

            for (Node n : nodes) {
                if (n.getPriority() < smallest.getPriority()) {
                    smallest = n;
                }
            }

            nodes.remove(smallest);

            return smallest;
        }

        public boolean updatePriority(Node n, int newPriority) {
            for (Node node : nodes) {
                if (n.equals(node)) {
                    node.setPriority(newPriority);
                    return true;
                }
            }

            return false;
        }

        public int size() {
            return nodes.size();
        }

        public boolean isEmpty() {
            return nodes.isEmpty();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('{');

            nodes.sort(Comparator.comparingInt(Node::getPriority));

            for (int i = 0; i < nodes.size(); i++) {
                sb.append(nodes.get(i).getPriority());

                if (i != nodes.size() - 1) {
                    sb.append(", ");
                }
            }

            sb.append('}');
            return sb.toString();
        }
    }

    static class Graph {
        private final List<Node> nodes;
        private final List<Node[]> edges;
        private final int[] posS;
        private final int[] posE;

        public Graph(String[] arr) {
            nodes = new ArrayList<>();
            edges = new ArrayList<>();
            posS = new int[2];
            posE = new int[2];
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length(); j++) {
                    if (arr[i].charAt(j) == 'S') {
                        posS[0] = j;
                        posS[1] = i;
                        nodes.add(new Node(new int[]{j, i}, 'a'));
                    } else if (arr[i].charAt(j) == 'E') {
                        posE[0] = j;
                        posE[1] = i;
                        nodes.add(new Node(new int[]{j, i}, 'z'));
                    } else {
                        nodes.add(new Node(new int[]{j, i}, arr[i].charAt(j)));
                    }
                }
            }

            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length(); j++) {
                    if (i > 0 && getHeight(arr[i - 1].charAt(j)) - 1 <= getHeight(arr[i].charAt(j))) {
                        edges.add(new Node[]{findNode(j, i), findNode(j, i - 1)});
                    }

                    if (j > 0 && getHeight(arr[i].charAt(j - 1)) - 1 <= getHeight(arr[i].charAt(j))) {
                        edges.add(new Node[]{findNode(j, i), findNode(j - 1, i)});
                    }

                    if (i < arr.length - 1 && getHeight(arr[i + 1].charAt(j)) - 1 <= getHeight(arr[i].charAt(j))) {
                        edges.add(new Node[]{findNode(j, i), findNode(j, i + 1)});
                    }

                    if (j < arr[i].length() - 1 && getHeight(arr[i].charAt(j + 1)) - 1 <= getHeight(arr[i].charAt(j))) {
                        edges.add(new Node[]{findNode(j, i), findNode(j + 1, i)});
                    }
                }
            }
        }

        private static int getHeight(char c) {
            if (c == 'S') {
                return 0;
            } else if (c == 'E') {
                return 'z' - 'a';
            } else {
                return c - 'a';
            }
        }

        public Node findNode(int x, int y) {
            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i).getX() == x && nodes.get(i).getY() == y) {
                    return nodes.get(i);
                }
            }

            return null;
        }

        public List<Node[]> getEdgesFromNode(Node n) {
            List<Node[]> edges = new ArrayList<>();
            for (int i = 0; i < this.edges.size(); i++) {
                if (this.edges.get(i)[0].equals(n)) {
                    edges.add(this.edges.get(i));
                }
            }

            return edges;
        }

        public List<Node> getNodes() {
            return nodes;
        }

        public boolean isS(Node n) {
            return n.getX() == posS[0] && n.getY() == posS[1];
        }

        public boolean isE(Node n) {
            return n.getX() == posE[0] && n.getY() == posE[1];
        }

        public Node getS() {
            return findNode(posS[0], posS[1]);
        }

        public Node getE() {
            return findNode(posE[0], posE[1]);
        }

        public void resetPriorities() {
            for (Node n : nodes) {
                n.setPriority(Integer.MAX_VALUE);
            }
        }

        public void invertEdges() {
            for (Node[] edge : edges) {
                Node tmp = edge[0];
                edge[0] = edge[1];
                edge[1] = tmp;
            }
        }
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("Input12.txt");
        Graph graph = new Graph(arr);
        graph.getS().setPriority(0);
        AOCPriorityQueue queue = new AOCPriorityQueue(graph.getNodes());

        while (!queue.isEmpty()) {
            Node smallestPriorityNode = queue.remove();
            int p = smallestPriorityNode.getPriority();
            p++;
            List<Node[]> edges = graph.getEdgesFromNode(smallestPriorityNode);
            for (Node[] edge : edges) {
                if (edge[1].getPriority() > p) {
                    queue.updatePriority(edge[1], p);
                }
            }
        }

        Node e = graph.getE();

        System.out.println("Part 1: " + e.getPriority());

        graph.resetPriorities();
        graph.invertEdges();
        int minDistance = 0;

        graph.getE().setPriority(0);
        queue = new AOCPriorityQueue(graph.getNodes());

        while (!queue.isEmpty()) {
            Node smallestPriorityNode = queue.remove();

            if (smallestPriorityNode.getChar() == 'a') {
                minDistance = smallestPriorityNode.getPriority();
                break;
            }

            int p = smallestPriorityNode.getPriority();
            p++;
            List<Node[]> edges = graph.getEdgesFromNode(smallestPriorityNode);
            for (Node[] edge : edges) {
                if (edge[1].getPriority() > p) {
                    queue.updatePriority(edge[1], p);
                }
            }
        }

        System.out.println("Part 2: " + minDistance);
    }
}
