package it.unimarconi.beans;

import java.util.TreeSet;

public class Unit {

    private Job job;

    private boolean free;

    private TreeSet<Job> q;

    public Unit() {
        this.setFree(true);
        this.setQ(new TreeSet<Job>());
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

    public TreeSet<Job> getQ() {
        return q;
    }

    public void setQ(TreeSet<Job> q) {
        this.q = q;
    }

}