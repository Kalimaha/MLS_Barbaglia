package it.unimarconi.commons;

public class GCM {

    private int a;

    private long m;

    private int b;

    private long seed;

    private double ri;

    /**
     * @param a    Parametro a del generatore congruente moltiplicativo
     * @param seed Seme iniziale X0 per il generatore congruente moltiplicativo
     * @param b    Potenza di 2 per generare il modulo m = 2 ^b
     */
    public GCM(int a, long seed, int b) {
        this.setA(a);
        this.setB(b);
        this.setSeed(seed);
        this.setM((long)Math.pow(2.0, this.getB()));
    }

    /**
     * Restituisce un valore intero generato tramite il generatore congruente moltiplicativo
     * <p/>
     * @return Valore generato tramite il generatore congruente moltiplicativo
     */
    public long getNext() {
        this.setSeed((this.getA() * this.getSeed()) % this.getM());
        return this.getSeed();
    }

    /**
     * Restituisce un valore compreso tra 0 ed 1 generato tramite il generatore congruente moltiplicativo
     * <p/>
     * @return Valore compreso tra 0 ed 1 generato tramite il generatore congruente moltiplicativo
     */
    public double getNextRi() {
        long seed = this.getNext();
        this.setRi(1.0 * seed / this.getM());
        return this.getRi();
    }

    /**
     * Genera una sequenza contenente i numeri 4v + 1 con
     * v = 0, 1, …, 2^(b-1)-1
     *
     * @return  Sequenza predetta dal Corollario Q
     */
    public double[] generate4v1() {
        int items = (int)Math.pow(2.0, this.getB() - 2) - 1;
        double[] out = new double[1 + items];
        for (int i = 0 ; i <= items ; i++)
            out[i] = 4 * i + 1;
        return out;
    }

    /**
     * Genera una sequenza contenente i numeri 4v + 3 con
     * v = 0, 1, …, 2^(b-1)-1
     *
     * @return  Sequenza predetta dal Corollario Q
     */
    public double[] generate4v3() {
        int items = (int)Math.pow(2.0, this.getB() - 2) - 1;
        double[] out = new double[1 + items];
        for (int i = 0 ; i <= items ; i++)
            out[i] = 4 * i + 3;
        return out;
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

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public double getRi() {
        return ri;
    }

    public void setRi(double ri) {
        this.ri = ri;
    }

    @Override
    public String toString() {
        return "GCM{" +
                "a=" + a +
                ", m=" + m +
                ", b=" + b +
                ", seed=" + seed +
                '}';
    }

}