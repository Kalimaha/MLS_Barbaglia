package it.unimarconi.beans;

import it.unimarconi.generatori.GeneratoreEsponenziale;

import java.util.ArrayList;

public class IOFIFO extends Unit {

    private GeneratoreEsponenziale generatore;

    private ArrayList<Job> ioQ;

    public IOFIFO(long x0, double media) {
        super();
        this.setFree(true);
        this.setIoQ(new ArrayList<Job>());
        this.setGeneratore(new GeneratoreEsponenziale(x0, media));
    }

    @Override
    public Job getJobFromQ() {
        Job j = this.getIoQ().get(0);
        this.getIoQ().remove(0);
        return j;
    }

    public GeneratoreEsponenziale getGeneratore() {
        return generatore;
    }

    public void setGeneratore(GeneratoreEsponenziale generatore) {
        this.generatore = generatore;
    }

    public ArrayList<Job> getIoQ() {
        return ioQ;
    }

    public void setIoQ(ArrayList<Job> ioQ) {
        this.ioQ = ioQ;
    }

}