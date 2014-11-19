package it.unimarconi.commons;

public class Calendar {

    private int t_a;

    private int t_cpu;

    private int t_io;

    private int t_end_sim;

    public Calendar() {
        this.setT_cpu(Integer.MAX_VALUE);
        this.setT_io(Integer.MAX_VALUE);
        this.setT_end_sim(5000);
    }

    public int getT_a() {
        return t_a;
    }

    public void setT_a(int t_a) {
        this.t_a = t_a;
    }

    public int getT_cpu() {
        return t_cpu;
    }

    public void setT_cpu(int t_cpu) {
        this.t_cpu = t_cpu;
    }

    public int getT_io() {
        return t_io;
    }

    public void setT_io(int t_io) {
        this.t_io = t_io;
    }

    public int getT_end_sim() {
        return t_end_sim;
    }

    public void setT_end_sim(int t_end_sim) {
        this.t_end_sim = t_end_sim;
    }

}
