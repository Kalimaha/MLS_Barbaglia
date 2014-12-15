package it.unimarconi.generatori;

public class Generatore3Erlang extends Generatore {

    private double media;

    private Generatore g1;

    private Generatore g2;

    private Generatore g3;

    public Generatore3Erlang(long x0_1, long x0_2, long x0_3, double media) {
        super(-1);
        this.setMedia(media);
        this.setG1(new Generatore(x0_1));
        this.setG2(new Generatore(x0_2));
        this.setG3(new Generatore(x0_3));
    }

    public double getNext3Erlang () {
        double avgk = (-1 * this.getMedia()) / 3;
        double sumlog = 0.0;
        sumlog += Math.log(g1.getNextRi());
        sumlog += Math.log(g2.getNextRi());
        sumlog += Math.log(g3.getNextRi());
        return avgk * sumlog;

    }

    public Generatore getG1() {
        return g1;
    }

    public Generatore getG2() {
        return g2;
    }

    public Generatore getG3() {
        return g3;
    }

    public void setG1(Generatore g1) {
        this.g1 = g1;
    }

    public void setG2(Generatore g2) {
        this.g2 = g2;
    }

    public void setG3(Generatore g3) {
        this.g3 = g3;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

}