package it.unimarconi.generatori;

public class Generatore3Erlang extends Generatore {

    private double media;

    private Generatore g1;

    private Generatore g2;

    private Generatore g3;

    public Generatore3Erlang(int a, long x0, int b, double media) {
        super(a, x0, b);
        this.setMedia(media);
        g1 = new Generatore(a, 5, b);
        g2 = new Generatore(a, 9, b);
        g3 = new Generatore(a, 67, b);
    }

    public double getNext3Erlang () {
        double avgk = -1 * this.getMedia() / 3;
        double sumlog = 0.0;
        sumlog += Math.log(g1.getNextRi());
        sumlog += Math.log(g2.getNextRi());
        sumlog += Math.log(g3.getNextRi());
        return avgk * sumlog;

    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

}