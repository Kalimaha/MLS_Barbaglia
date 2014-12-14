package it.unimarconi.generatori;

public class GeneratoreEsponenziale extends Generatore {

    private double media;

    public GeneratoreEsponenziale(int a, long x0, int b, double media) {
        super(a, x0, b);
        this.setMedia(media);
    }

    public double getNextExp() {
        return -1.0 * this.getMedia() * Math.log(this.getNextRi());
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

}
