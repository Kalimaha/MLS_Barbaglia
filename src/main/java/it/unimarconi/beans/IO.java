package it.unimarconi.beans;

import java.util.ArrayList;

public class IO extends Unit {

    private ArrayList<Job> ioQ;

    public IO() {
        super();
        this.setIoQ(new ArrayList<Job>());
        this.setQ(null);
    }

    @Override
    public Job getJobFromQ() {
        Job j = this.getIoQ().get(this.getIoQ().size() - 1);
        this.getIoQ().remove(this.getIoQ().size() - 1);
        return j;
    }

    public ArrayList<Job> getIoQ() {
        return ioQ;
    }

    public void setIoQ(ArrayList<Job> ioQ) {
        this.ioQ = ioQ;
    }

}