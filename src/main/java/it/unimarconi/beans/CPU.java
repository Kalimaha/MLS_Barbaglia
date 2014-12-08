package it.unimarconi.beans;

import it.unimarconi.utils.JobComparator;

import java.util.Collections;

public class CPU extends Unit {

    @Override
    public Job getJobFromQ() {
        Collections.sort(this.getQ(), new JobComparator());
        Job j = this.getQ().get(0);
        this.getQ().remove(0);
        return j;
    }

}