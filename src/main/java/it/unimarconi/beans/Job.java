package it.unimarconi.beans;

public class Job {

    private double t_in;

    private double t_out;

    private int priority;

    private int job_size;

    public Job() {

    }

    public int getJob_size() {
        return job_size;
    }

    public void setJob_size(int job_size) {
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

}