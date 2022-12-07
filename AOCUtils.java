package aocjava;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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

            List<String> empty = Collections.emptyList();
            return empty;
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

    public static List<String> divideString(String str, String del) {
        List<String> l = new ArrayList<>();
        String sub = str;
        int p = str.indexOf(del);

        while (p != -1) {
            l.add(sub.substring(0, p));
            sub = sub.substring(p + del.length());
            p = sub.indexOf(del);
        }

        if (sub.length() > 0) {
            l.add(sub);
        }

        return l;
    }
}
