package it.unimarconi.beans;

import java.util.ArrayList;

public class Unit {

    private Job job;

    private boolean free;

    private ArrayList<Job> q;

    public Unit() {
        this.setFree(true);
        this.setQ(new ArrayList<Job>());
    }

    public Job getJobFromQ() {
        return this.getJob();
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public ArrayList<Job> getQ() {
        return q;
    }

    public void setQ(ArrayList<Job> q) {
        this.q = q;
    }

}