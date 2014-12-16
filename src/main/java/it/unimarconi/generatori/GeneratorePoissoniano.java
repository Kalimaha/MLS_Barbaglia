package it.unimarconi.generatori;

public class GeneratorePoissoniano extends Generatore {

    private double media;

    private double max;

    private GeneratoreEsponenziale generatoreEsponenziale;

    public GeneratorePoissoniano(long x0, double media) {
        super(x0);
        this.setMedia(media);
        this.setMax(Math.exp(-1 * media));
        this.setGeneratoreEsponenziale(new GeneratoreEsponenziale(135, 1));
    }

    public int getNextPoisson() {
        int n = 0;
        double p = this.getGeneratoreEsponenziale().getNextRi();
        while (p >= this.getMax()) {
            n++;
            p = p * this.getGeneratoreEsponenziale().getNextRi();
        }
        this.setGeneratoreEsponenziale(new GeneratoreEsponenziale(this.getGeneratoreEsponenziale().getX0(), 1));
        return n;
    }

    public double getMedia() {
        return media;
    }

    public void setMedia(double media) {
        this.media = media;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public GeneratoreEsponenziale getGeneratoreEsponenziale() {
        return generatoreEsponenziale;
    }

    public void setGeneratoreEsponenziale(GeneratoreEsponenziale generatoreEsponenziale) {
        this.generatoreEsponenziale = generatoreEsponenziale;
    }

}