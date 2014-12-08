package it.unimarconi.beans;

/** @author Guido Barbaglia */
public class StatisticheSimulazione {

    private double tempoMedio;

    private double varianza;

    private int jobs;

    public double getTempoMedio() {
        return tempoMedio;
    }

    public void setTempoMedio(double tempoMedio) {
        this.tempoMedio = tempoMedio;
    }

    public double getVarianza() {
        return varianza;
    }

    public int getJobs() {
        return jobs;
    }

    public void setJobs(int jobs) {
        this.jobs = jobs;
    }

    public void setVarianza(double varianza) {
        this.varianza = varianza;
    }

    @Override
    public String toString() {
        return "Tempo Medio: " + this.getTempoMedio() +
                "\t\t\tJobs: " + this.getJobs() +
                "\t\t\tVarianza: " + this.getVarianza();
    }
}