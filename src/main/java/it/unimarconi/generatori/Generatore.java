package it.unimarconi.generatori;

public class Generatore {

    private int a;

    private long m = 214748648;

    private int b;

    private long x0;

    private double ri;

    public Generatore(int a, long x0, int b) {
        this.setA(a);
        this.setB(b);
        this.setX0(x0);
//        this.setM((long)Math.pow(2.0, this.getB()));
    }

    public long getNext() {
        this.setX0((this.getA() * this.getX0()) % this.getM());
        return this.getX0();
    }

    public double getNextRi() {
        long seed = this.getNext();
        this.setRi(1.0 * seed / this.getM());
        return this.getRi();
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public long getM() {
        return m;
    }

    public void setM(long m) {
        this.m = m;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
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