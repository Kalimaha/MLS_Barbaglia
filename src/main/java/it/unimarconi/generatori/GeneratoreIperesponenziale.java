package it.unimarconi.generatori;

import it.unimarconi.commons.Event;

public class GeneratoreIperesponenziale extends Generatore {

    private double p;

    public GeneratoreIperesponenziale(long x0, double p) {
        super(x0);
        this.setP(p);
    }

    public Event getNextRoute() {
        return this.getNextRi() <= this.getP() ? Event.IO : Event.OUT;
    }

    public Event getNextCPU() {
        return this.getNextRi() <= this.getP() ? Event.CPU2 : Event.CPU1;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

}
