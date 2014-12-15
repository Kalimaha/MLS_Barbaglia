package it.unimarconi.beans;

import java.util.UUID;

public class Job implements Comparable<Job> {

    private double tempoArrivo;

    private double tempoUscita;

    private double tempoJob;

    private double tempoProcessamento;

    public String name = UUID.randomUUID().toString().substring(0, 4);

    public Job() {

    }

    public Job(double tempoProcessamento) {
        this.setTempoProcessamento(tempoProcessamento);
    }

    public double getTempoProcessamento() {
        return tempoProcessamento;
    }

    public void setTempoProcessamento(double tempoProcessamento) {
        this.tempoProcessamento = tempoProcessamento;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTempoArrivo() {
        return tempoArrivo;
    }

    public void setTempoArrivo(double tempoArrivo) {
        this.tempoArrivo = tempoArrivo;
    }

    public double getTempoUscita() {
        return tempoUscita;
    }

    public void setTempoUscita(double tempoUscita) {
        this.tempoUscita = tempoUscita;
        this.setTempoJob(this.getTempoUscita() - this.getTempoArrivo());
    }

    public double getTempoJob() {
        return tempoJob;
    }

    public void setTempoJob(double tempoJob) {
        this.tempoJob = tempoJob;
    }


    @Override
    public int compareTo(Job b) {
        if (this.getTempoProcessamento() > b.getTempoProcessamento())
            return 1;
        else if (this.getTempoProcessamento() < b.getTempoProcessamento())
            return -1;
        else
            return 0;
    }

}