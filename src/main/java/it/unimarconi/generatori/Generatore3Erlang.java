package it.unimarconi.generatori;

public class Generatore3Erlang extends Generatore {

    private double media;

    public Generatore3Erlang(int a, long x0, int b, double media) {
        super(a, x0, b);
        this.setMedia(media);
    }

//    public double getNext3Erlang() {
//        double ri = this.getNextRi();
//        double avgk = -1 * this.getMedia() / 3;
//        double sumlog = 0.0;
//        for (int j = 0; j < 3; j++)
//            sumlog += Math.log(ri);
//        return avgk * sumlog;
//    }

    public double getNext3Erlang () {
        return ( (-1.0 /( 3 * this.getMedia() )) * Math.log(this.getNextRi()*this.getNextRi()* this.getNextRi()));
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

}