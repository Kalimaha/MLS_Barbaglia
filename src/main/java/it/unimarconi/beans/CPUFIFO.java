package it.unimarconi.beans;

import it.unimarconi.generatori.Generatore3Erlang;
import it.unimarconi.generatori.GeneratoreEsponenziale;
import it.unimarconi.utils.JobComparator;

import java.util.ArrayList;
import java.util.TreeSet;

public class CPUFIFO extends Unit {

    private GeneratoreEsponenziale generatore;

    private ArrayList<Job> cpuQ;

    public CPUFIFO(long x0, double media) {
        super();
        this.setFree(true);
        this.setCpuQ(new ArrayList<Job>());
        this.setGeneratore(new GeneratoreEsponenziale(x0, media));
    }

    @Override
    public Job getJobFromQ() {
        Job j = this.getCpuQ().get(0);
        this.getCpuQ().remove(0);
        return j;
    }

    public GeneratoreEsponenziale getGeneratore() {
        return generatore;
    }

    public void setGeneratore(GeneratoreEsponenziale generatore) {
        this.generatore = generatore;
    }

    public ArrayList<Job> getCpuQ() {
        return cpuQ;
    }

    public void setCpuQ(ArrayList<Job> cpuQ) {
        this.cpuQ = cpuQ;
    }

}