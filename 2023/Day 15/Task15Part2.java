package aocjava;

import java.util.ArrayList;
import java.util.List;

public class Task15Part2 {
    private record Lens(String id, int focalLength) {}
    private record Box(List<Lens> lenses) {}

    private static int hash(String c) {
        int hash = 0;
        for (int i = 0; i < Math.max(c.indexOf('-'), c.indexOf('=')); i++) {
            hash += c.charAt(i);
            hash *= 17;
            hash &= 255;
        }
        return hash;
    }

    public static void main(String[] args) {
        List<String> stringList = AOCUtils.fileToStringList("Input15.txt");
        Box[] boxes = new Box[256];
        for (int i = 0; i < 256; i++) {
            boxes[i] = new Box(new ArrayList<>());
        }
        String[] commands = stringList.get(0).split(",");
        for (String c : commands) {
            Box b = boxes[hash(c)];
            String id = c.substring(0, Math.max(c.indexOf('-'), c.indexOf('=')));
            Lens lens = null;
            for (Lens l : b.lenses()) {
                if (l.id().equals(id)) {
                    lens = l;
                }
            }
            if (lens != null) {
                if (c.indexOf('-') != -1) {
                    b.lenses().remove(lens);
                } else {
                    int index = b.lenses().indexOf(lens);
                    b.lenses().set(index, new Lens(id, Integer.parseInt(c.substring(c.indexOf('=') + 1))));
                }
            } else {
                if (c.indexOf('=') != -1) {
                    b.lenses().add(new Lens(id, Integer.parseInt(c.substring(c.indexOf('=') + 1))));
                }
            }
        }
        long result = 0;
        for (int i = 0; i < boxes.length; i++) {
            long intermediate = 0;
            for (int j = 0; j < boxes[i].lenses().size(); j++) {
                intermediate += (i + 1L) * (j + 1L) * boxes[i].lenses().get(j).focalLength;
            }
            result += intermediate;
        }
        System.out.println("Part 2: " + result);
    }
}
