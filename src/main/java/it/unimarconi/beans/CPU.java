package it.unimarconi.beans;

import java.util.ArrayList;

public class CPU {

    private Job job;

    private boolean is_free;

    private ArrayList<Job> q;

    public CPU() {
        this.setIs_free(true);
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

    public boolean isIs_free() {
        return is_free;
    }

    public void setIs_free(boolean is_free) {
        this.is_free = is_free;
    }

    public ArrayList<Job> getQ() {
        return q;
    }

    public void setQ(ArrayList<Job> q) {
        this.q = q;
    }

}