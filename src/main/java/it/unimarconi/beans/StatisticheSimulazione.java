package it.unimarconi.beans;

/** @author Guido Barbaglia */
public class StatisticheSimulazione {

    private double tempoMedio;

    private double varianza;

    public StatisticheSimulazione() {

    }

    public StatisticheSimulazione(double tempoMedio, double varianza) {
        this.setTempoMedio(tempoMedio);
        this.setVarianza(varianza);
    }

    public double getTempoMedio() {
        return tempoMedio;
    }

    public void setTempoMedio(double tempoMedio) {
        this.tempoMedio = tempoMedio;
    }

    public double getVarianza() {
        return varianza;
    }

    public void setVarianza(double varianza) {
        this.varianza = varianza;
    }

    @Override
    public String toString() {
        return "Tempo Medio: " + this.getTempoMedio() +
                "\t\t\tVarianza: " + this.getVarianza();
    }
}