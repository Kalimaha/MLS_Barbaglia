package it.unimarconi.beans;

import it.unimarconi.utils.JobComparator;

import java.util.Collections;

public class CPU extends Unit {

    @Override
    public Job getJobFromQ() {
        Collections.sort(this.getQ(), new JobComparator());
        Job j = this.getQ().get(this.getQ().size() - 1);
        this.getQ().remove(this.getQ().size()-1);
        return j;
    }

}