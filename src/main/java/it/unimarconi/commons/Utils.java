package it.unimarconi.commons;

import java.util.ArrayList;

public class Utils {

    public static ArrayList<Double> toList(double[] a) {
        ArrayList<Double> l = new ArrayList<Double>();
        for (int i = 0 ; i < a.length ; i++)
            l.add(a[i]);
        return l;
    }

    public static void print(ArrayList<Double> l) {
        String s = "[";
        for (int i = 0 ; i < l.size() ; i++) {
            s += l.get(i);
            if (i < l.size() - 1)
                s += ", ";
        }
        s += "]";
        System.out.println(s);
    }

    public static void print(double[] l) {
        String s = "[";
        for (int i = 0 ; i < l.length ; i++) {
            s += l[i];
            if (i < l.length - 1)
                s += ", ";
        }
        s += ", ...]";
        System.out.println(s);
    }

    public static void print(double[] l, int limit) {
        String s = "[";
        for (int i = 0 ; i < limit ; i++) {
            s += l[i];
            if (i < limit - 1)
                s += ", ";
        }
        s += ", ...]";
        System.out.println(s);
    }

    public static void print(ArrayList<Double> l, int limit) {
        String s = "[";
        for (int i = 0 ; i < limit ; i++) {
            s += l.get(i);
            if (i < limit - 1)
                s += ", ";
        }
        s += "]";
        System.out.println(s);
    }

}