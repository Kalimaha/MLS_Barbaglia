package it.unimarconi.beans;

import java.util.UUID;

public class Job {

    private double tempoArrivo;

    private double tempoUscita;

    private double processing_time;

    private double job_size;

    public String name = UUID.randomUUID().toString().substring(0, 4);

    public Job() {

    }

    public double getJob_size() {
        return job_size;
    }

    public void setJob_size(double job_size) {
        this.job_size = job_size;
    }

    public double getProcessing_time() {
        return processing_time;
    }

    public void setProcessing_time(double processing_time) {
        this.processing_time = processing_time;
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
    }

}