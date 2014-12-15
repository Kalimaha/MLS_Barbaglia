package it.unimarconi.beans;

import it.unimarconi.generatori.Generatore3Erlang;

import java.util.ArrayList;

public class IO extends Unit {

    private ArrayList<Job> ioQ;

    private Generatore3Erlang generatore;

    public IO(long x0_1, long x0_2, long x0_3, double media) {
        super();
        this.setIoQ(new ArrayList<Job>());
        this.setQ(null);
        this.setGeneratore(new Generatore3Erlang(x0_1, x0_2, x0_3, media));
    }

    @Override
    public Job getJobFromQ() {
        Job j = this.getIoQ().get(this.getIoQ().size() - 1);
        this.getIoQ().remove(this.getIoQ().size() - 1);
        return j;
    }

    public ArrayList<Job> getIoQ() {
        return ioQ;
    }

    public void setIoQ(ArrayList<Job> ioQ) {
        this.ioQ = ioQ;
    }

    public Generatore3Erlang getGeneratore() {
        return generatore;
    }

    public void setGeneratore(Generatore3Erlang generatore) {
        this.generatore = generatore;
    }

}