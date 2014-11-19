package it.unimarconi.commons;

public class Generatore {

    private GCM gcm1;

    private GCM gcm2;

    private int a;

    private int b;

    private long seed1;

    private long seed2;

    private double min;

    private double max;

    private int avg;

    private double p;

    public Generatore(int a, long seed, int b, double min, double max) {
        this.setA(a);
        this.setSeed1(seed);
        this.setB(b);
        this.setGcm1(new GCM(this.getA(), this.getSeed1(), this.getB()));
        this.setMin(min);
        this.setMax(max);
    }

    public Generatore(int a, long seed, int b, int avg) {
        this.setA(a);
        this.setSeed1(seed);
        this.setB(b);
        this.setGcm1(new GCM(this.getA(), this.getSeed1(), this.getB()));
        this.setAvg(avg);
    }

    public Generatore(int a, long seed1, long seed2, int b, int avg, double p) {
        this.setA(a);
        this.setSeed1(seed1);
        this.setSeed2(seed2);
        this.setB(b);
        this.setP(p);
        this.setGcm1(new GCM(this.getA(), this.getSeed1(), this.getB()));
        this.setGcm2(new GCM(this.getA(), this.getSeed2(), this.getB()));
        this.setAvg(avg);
    }

    public double getNextRange() {
        return this.getMin() + this.getGcm1().getNextRi() * (this.getMax() - this.getMin());
    }

    public double getNextExp() {
        return -1.0 * this.getAvg() * Math.log(this.getGcm1().getNextRi());
    }

    public double getNextHyperExp() {
        double ri = this.getGcm1().getNextRi();
        double xi = -1.0 * 1.0 * Math.log(this.getGcm2().getNextRi());
        if (ri <= this.getP())
            return xi * (this.getAvg() / (2.0 * this.getP()));
        else
            return xi * (this.getAvg() / (2.0 * (1.0 - this.getP())));
    }

    public GCM getGcm1() {
        return gcm1;
    }

    public void setGcm1(GCM gcm) {
        this.gcm1 = gcm;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public long getSeed1() {
        return seed1;
    }

    public void setSeed1(long seed1) {
        this.seed1 = seed1;
    }

    public long getSeed2() {
        return seed2;
    }

    public void setSeed2(long seed2) {
        this.seed2 = seed2;
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

    public int getAvg() {
        return avg;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public GCM getGcm2() {
        return gcm2;
    }

    public void setGcm2(GCM gcm2) {
        this.gcm2 = gcm2;
    }

}