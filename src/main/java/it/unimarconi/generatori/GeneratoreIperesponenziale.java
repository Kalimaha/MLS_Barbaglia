package it.unimarconi.generatori;

import it.unimarconi.commons.Event;

public class GeneratoreIperesponenziale extends Generatore {

    private double p;

    public GeneratoreIperesponenziale(long x0, double p) {
        super(x0);
        this.setP(p);
    }

    public Event getNextRoute() {
        double ri = this.getNextRi();
        if (ri <= this.getP())
            return Event.IO;
        else
            return Event.OUT;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

}
