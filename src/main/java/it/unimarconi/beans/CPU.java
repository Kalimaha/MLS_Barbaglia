package it.unimarconi.beans;

import it.unimarconi.generatori.Generatore3Erlang;
import it.unimarconi.utils.JobComparator;

import java.util.TreeSet;

public class CPU extends Unit {

    private Generatore3Erlang generatore;

    public CPU(long x0_1, long x0_2, long x0_3, double media) {
        super();
        this.setFree(true);
        this.setQ(new TreeSet<Job>(new JobComparator()));
        generatore = new Generatore3Erlang(x0_1, x0_2, x0_3, media);
    }

    @Override
    public Job getJobFromQ() {
        Job j = this.getQ().iterator().next();
        this.getQ().remove(j);
        return j;
    }

    public void addJobToTheQueue(Job j) {
        this.getQ().add(j);
    }

    public Generatore3Erlang getGeneratore() {
        return generatore;
    }

}