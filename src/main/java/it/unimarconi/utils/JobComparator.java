package it.unimarconi.utils;

import it.unimarconi.beans.Job;

import java.util.Comparator;

public class JobComparator implements Comparator<Job> {

    public int compare(Job a, Job b) {
        return a.compareTo(b);
    }

}