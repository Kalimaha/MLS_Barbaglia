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
        int index = 0;
        double min = Double.MAX_VALUE;
        for(int i=0; i < this.getQ().size(); i++) {
            if (this.getQ().get(i).getProcessing_time() < min) {
                index = i;
                min = this.getQ().get(i).getProcessing_time();
            }
        }
        Job j = this.getQ().get(index);
        this.getQ().remove(index);
        return j;
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