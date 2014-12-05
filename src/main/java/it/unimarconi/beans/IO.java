package it.unimarconi.beans;

public class IO extends Unit {

    @Override
    public Job getJobFromQ() {
        Job j = this.getQ().get(this.getQ().size() - 1);
        this.getQ().remove(this.getQ().size()-1);
        return j;
    }

}