package it.unimarconi.beans;

import java.util.ArrayList;

public class IO {

    private Job job;

    private boolean is_free;

    private ArrayList<Job> q;

    public IO() {
        this.setIs_free(true);
        this.setQ(new ArrayList<Job>());
    }

    public Job getJobFromQ() {
        Job j = this.getQ().get(this.getQ().size()-1);
        this.getQ().remove(this.getQ().size()-1);
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