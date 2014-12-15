package it.unimarconi.generatori;

public class Generatore {

    private final int a = 1220703125;

    private final long m = 214748648;

    private long x0;

    private double ri;

    public Generatore(long x0) {
        this.setX0(x0);
    }

    public long getNext() {
        long out = this.getX0();
        this.setX0((this.getA() * this.getX0()) % this.getM());
        return out;
    }

    public double getNextRi() {
        long seed = this.getNext();
        this.setRi(1.0 * seed / this.getM());
        return this.getRi();
    }

    public int getA() {
        return a;
    }

    public long getM() {
        return m;
    }

    public long getX0() {
        return x0;
    }

    public void setX0(long x0) {
        this.x0 = x0;
    }

    public double getRi() {
        return ri;
    }

    public void setRi(double ri) {
        this.ri = ri;
    }

}