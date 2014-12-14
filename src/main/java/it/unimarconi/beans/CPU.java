package it.unimarconi.beans;

import it.unimarconi.utils.JobComparator;

import java.util.Collections;
import java.util.TreeSet;

public class CPU extends Unit {

    public CPU() {
        super();
        this.setFree(true);
//        this.setQ(new ArrayList<Job>());
        this.setQ(new TreeSet<Job>(new JobComparator()));
    }

    @Override
    public Job getJobFromQ() {
//        Collections.sort(this.getQ(), new JobComparator());
        Job j = this.getQ().iterator().next();
        this.getQ().remove(j);
        return j;
    }

    public void addJobToTheQueue(Job j) {
        this.getQ().add(j);
//        Collections.sort(this.getQ(), new JobComparator());
    }

}