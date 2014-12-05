package it.unimarconi.beans;

public class CPU extends Unit {

    @Override
    public Job getJobFromQ() {
        int index = 0;
        double min = Double.MAX_VALUE;
        for(int i=0; i < this.getQ().size(); i++) {
            if (this.getQ().get(i).getTempoProcessamento() < min) {
                index = i;
                min = this.getQ().get(i).getTempoProcessamento();
            }
        }
        Job j = this.getQ().get(index);
        this.getQ().remove(index);
        return j;
    }

}