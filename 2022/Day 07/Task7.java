package aocjava;

import java.util.*;

public class Task7 {
    static interface Node {
        String getName();
        int getSize();
    }

    static class FileNode implements Node {
        private int size;
        private String name;

        public FileNode(String n, int s) {
            size = s;
            name = n;
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            return size;
        }

        @Override
        public String toString() {
            return getSize() + " " + getName();
        }
    }

    static class DirNode implements Node {
        private List<Node> nodes;
        private String name;
        private DirNode parent;

        public DirNode(String n, DirNode p) {
            nodes = new ArrayList<>();
            name = n;
            parent = p;
        }

        public void addNodes(Node... ns) {
            for (int i = 0; i < ns.length; i++) {
                if (ns[i].getClass() == DirNode.class) {
                    DirNode d = (DirNode)(ns[i]);
                    d.setParent(this);
                }

                nodes.add(ns[i]);
            }
        }

        private void setParent(DirNode p) {
            parent = p;
        }

        public void addNodes(List<Node> ns) {
            addNodes(ns.toArray(new Node[0]));
        }

        public String getName() {
            return name;
        }

        public int getSize() {
            int size = 0;

            for (Node n : nodes) {
                size += n.getSize();
            }

            return size;
        }

        public Node[] getNodes() {
            return nodes.toArray(new Node[0]);
        }

        public DirNode getParent() {
            return parent;
        }

        @Override
        public String toString() {
            return "dir " + getName();
        }

        // only gets DirNodes
        public List<Node> traverse() {
            List<Node> list = new ArrayList<>();

            list.add(this);

            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i).getClass() == DirNode.class) {
                    DirNode d = (DirNode)(nodes.get(i));
                    list.addAll(d.traverse());
                }
            }

            return list;
        }
    }

    static class FileTree {
        private DirNode rootNode;
        private DirNode currNode;

        public FileTree() {
            rootNode = new DirNode("/", null);
        }

        public int getSize() {
            return currNode.getSize();
        }

        public int getOverallSize() {
            return rootNode.getSize();
        }

        public void navigate(String cmd) {
            if (cmd.equals("/")) {
                currNode = rootNode;
            } else if (cmd.equals("..")) {
                currNode = currNode.getParent();
            } else {
                for (int i = 0; i < currNode.getNodes().length; i++) {
                    if (currNode.getNodes()[i].getName().equals(cmd)) {
                        if (currNode.getNodes()[i].getClass() == DirNode.class) {
                            currNode = (DirNode)currNode.getNodes()[i];
                        } else {
                            System.out.println("Is a file!");
                        }
                        return;
                    }
                }
            }
        }

        public Node[] getNodes() {
            return currNode.getNodes();
        }

        public void addNodes(Node... ns) {
            for (int i = 0; i < ns.length; i++) {
                boolean isAlreadyThere = false;
                for (int j = 0; j < getNodes().length; j++) {
                    if (ns[i].getName().equals(getNodes()[j].getName())) {
                        isAlreadyThere = true;
                        break;
                    }
                }

                if (!isAlreadyThere) {
                    currNode.addNodes(ns[i]);
                }
            }
        }

        public List<Node> traverseAll() {
            return rootNode.traverse();
        }
    }

    public static int doLine(String[] arr, FileTree t, int l) {
        String line = arr[l];

        if (line.charAt(2) == 'c' && line.charAt(3) == 'd') {
            t.navigate(line.substring(5));
            l++;
        } else {
            List<String> names = new ArrayList<>();
            l++;
            while (l < arr.length && arr[l].charAt(0) != '$') {
                names.add(arr[l]);
                if (arr[l].charAt(0) == 'd') {
                    t.addNodes(new DirNode(arr[l].substring(4), null));
                } else {
                    int size = Integer.parseInt(arr[l].substring(0, arr[l].indexOf(' ')));
                    String name = arr[l].substring(arr[l].indexOf(' ') + 1);
                    t.addNodes(new FileNode(name, size));
                }
                l++;
            }

        }
        return l;
    }

    public static void main(String[] args) {
        String[] arr = AOCUtils.fileToStringArray("/home/johannes/Schreibtisch/AdventOfCode/Inputs/Input7.txt");
        FileTree tree = new FileTree();

        int line = 0;
        while (line < arr.length) {
            line = doLine(arr, tree, line);
        }

        long sumSizes = 0L;
        List<Node> list = tree.traverseAll();

        for (Node n : list) {
            int size = n.getSize();
            if (size <= 100000) {
                sumSizes += (long)(size);
            }
        }

        System.out.println("Part 1: " + sumSizes);

        long usedMem = tree.getOverallSize();
        long freeMem = 70000000L - usedMem;
        long memToFree = 30000000L - freeMem;

        SortedSet<Node> ordered = new TreeSet<>(Comparator.comparingInt(Node::getSize));
        for (Node n : list) {
            if (n.getSize() >= memToFree) {
                ordered.add(n);
            }
        }

        System.out.println("Part 2: " + ordered.first().getSize());
    }
}
