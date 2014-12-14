package it.unimarconi.generatori;

public class GeneratoreIntervallo extends Generatore {

    private double min;

    private double max;

    public GeneratoreIntervallo(long x0, double min, double max) {
        super(x0);
        this.setMin(min);
        this.setMax(max);
    }

    public double getNextRange() {
        return this.getMin() + this.getNextRi() * (this.getMax() - this.getMin());
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

}