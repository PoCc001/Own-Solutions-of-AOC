package aocjava;

import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AOCUtils {
    public static List<String> fileToStringList(String fileName) {
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            List<String> strList = new ArrayList<>();
            br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                strList.add(line);
            }

            br.close();
            
            return strList;
        } catch (Exception e) {
            e.printStackTrace();
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("Couldn't close BufferedReader!");
                    ioe.printStackTrace();
                    System.exit(1);
                }
            }

            return Collections.emptyList();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("Couldn't close BufferedReader!");
                    ioe.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }

    public static Stream<String> fileToStringStream(String fileName) {
        File file = new File(fileName);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            return br.lines();
        } catch (Exception e) {
            e.printStackTrace();
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("Couldn't close BufferedReader!");
                    ioe.printStackTrace();
                    System.exit(1);
                }
            }

            return Stream.of();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    System.out.println("Couldn't close BufferedReader!");
                    ioe.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }
    
    public static String[] fileToStringArray(String fileName) {
        List<String> strList = fileToStringList(fileName);
        
        if (strList.isEmpty()) {
            return new String[0];
        }
        
        String[] arr = new String[strList.size()];
        
        for (int i = 0; i < arr.length; i++) {
            arr[i] = strList.get(i);
        }
        
        return arr;
    }

    public static List<String> divideString(final String str, String... del) {
        List<String> l = new ArrayList<>();
        String sub = str;
        String currDel = Arrays.stream(del).min(Comparator.comparingInt(str::indexOf)).orElse(null);
        int p = currDel != null ? str.indexOf(currDel) : -1;

        while (p != -1) {
            l.add(sub.substring(0, p));
            sub = sub.substring(p + currDel.length());
            currDel = Arrays.stream(del).min(Comparator.comparingInt(sub::indexOf)).get();
            p = str.indexOf(currDel);
        }

        if (sub.length() > 0) {
            l.add(sub);
        }

        return l;
    }

    public static IntStream stringsToInts(Stream<String> ss, String... dividers) {
        if (dividers.length == 0) {
            return ss.mapToInt(Integer::parseInt);
        } else {
            return ss.flatMapToInt(s -> divideString(s, dividers).stream().mapToInt(Integer::parseInt));
        }
    }

    public static class DynamicPriorityQueue<T> extends PriorityQueue<T> {
        private final List<T> uncompList;
        private final List<Comparable<T>> compList;
        private final Comparator<T> comparator;

        public DynamicPriorityQueue(int initialCapacity, Comparator<T> c) {
            if (c == null) {
                throw new NullPointerException("Comparator must be non-null!");
            }
            compList = null;
            uncompList = new ArrayList<>(initialCapacity);
            comparator = c;
        }

        public DynamicPriorityQueue(int initialCapacity) {
            compList = new ArrayList<>(initialCapacity);
            uncompList = null;
            comparator = null;
        }

        public DynamicPriorityQueue() {
            this(0);
        }

        public DynamicPriorityQueue(Comparator<T> c) {
            this(0, c);
        }

        @Override
        public boolean add(T t) {
            if (t == null) {
                throw new NullPointerException("Cannot add null values!");
            } else if (!(t instanceof Comparable) && comparator == null) {
                throw new ClassCastException("comparator is null, but the type has no natural order!");
            } else if (compList != null) {
                compList.add((Comparable<T>)t);
            } else {
                uncompList.add(t);
            }
            return true;
        }

        @Override
        public boolean offer(T t) {
            return add(t);
        }

        @Override
        public boolean remove(Object o) {
            return compList == null ? uncompList.remove(o) : compList.remove(o);
        }

        @Override
        public boolean contains(Object o) {
            return compList == null ? uncompList.contains(o) : compList.contains(o);
        }

        @Override
        public T peek() {
            return compList == null ? uncompList.stream().min(comparator).orElse(null) : (T)compList.stream().min(Comparator.<Comparable>naturalOrder()).orElse(null);
        }

        @Override
        public T poll() {
            T t = peek();
            remove(t);
            return t;
        }

        @Override
        public int size() {
            return compList == null ? uncompList.size() : compList.size();
        }

        @Override
        public Object[] toArray() {
            return compList == null ? uncompList.toArray() : compList.toArray();
        }

        @Override
        public <E> E[] toArray(E[] es) {
            return compList == null ? uncompList.toArray(es) : compList.toArray(es);
        }

        @Override
        public void clear() {
            if (compList == null) {
                uncompList.clear();
            } else {
                compList.clear();
            }
        }

        @Override
        public Iterator<T> iterator() {
            return compList == null ? uncompList.iterator() : compList.stream().map(c -> (T)c).toList().iterator();
        }

        @Override
        public Comparator<T> comparator() {
            return comparator;
        }

        @Override
        public boolean removeIf(Predicate<? super T> filter) {
            if (compList == null) {
                return uncompList.removeIf(filter);
            } else {
                boolean removedAtLeastOne = false;
                for (Comparable<T> c : compList) {
                    T t = (T)c;
                    if (!filter.test(t)) {
                        compList.remove(t);
                        removedAtLeastOne = true;
                    }
                }
                return true;
            }
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c.contains(null)) {
                throw new NullPointerException("One element in the collection is null!");
            }
            return compList == null ? uncompList.removeAll(c) : compList.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            if (c.contains(null)) {
                throw new NullPointerException("One element in the collection is null!");
            }
            return compList == null ? uncompList.retainAll(c) : compList.retainAll(c);
        }

        @Override
        public void forEach(Consumer<? super T> action) {
            if (action == null) {
                throw new NullPointerException("Action is null!");
            }
            if (compList == null) {
                uncompList.forEach(action);
            } else {
                for (Comparable<T> c : compList) {
                    action.accept((T)c);
                }
            }
        }
    }
}
