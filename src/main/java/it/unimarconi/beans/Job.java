package it.unimarconi.beans;

import java.util.UUID;

public class Job {

    private double t_in;

    private double t_out;

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

    public double getT_in() {
        return t_in;
    }

    public void setT_in(double t_in) {
        this.t_in = t_in;
    }

    public double getT_out() {
        return t_out;
    }

    public void setT_out(double t_out) {
        this.t_out = t_out;
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
}