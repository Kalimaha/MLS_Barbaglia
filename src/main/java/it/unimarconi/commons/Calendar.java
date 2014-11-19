package it.unimarconi.commons;

public class Calendar {

    private double t_a;

    private double t_cpu;

    private double t_io;

    private double t_end_sim;

    public Calendar() {
        this.setT_cpu(Integer.MAX_VALUE);
        this.setT_io(Integer.MAX_VALUE);
        this.setT_end_sim(500);
    }

    public Event get_next() {
        Event out = Event.ARRIVAL;
        double min = t_a;
        if (t_cpu < min) {
            min = t_cpu;
            out = Event.CPU;
        }
        if (t_io < min) {
            min = t_io;
            out = Event.IO;
        }

        if (t_end_sim < min) {
            min = t_end_sim;
            out = Event.END_SIM;
        }
        return out;
    }

    public double get_next_time(Event e) {
        switch (e) {
            case ARRIVAL: return this.getT_a();
            case CPU: return this.getT_cpu();
            case IO: return this.getT_io();
            case END_SIM: return this.getT_end_sim();
        }
        return 0;
    }

    public double getT_a() {
        return t_a;
    }

    public void setT_a(double t_a) {
        this.t_a = t_a;
    }

    public double getT_cpu() {
        return t_cpu;
    }

    public void setT_cpu(double t_cpu) {
        this.t_cpu = t_cpu;
    }

    public double getT_io() {
        return t_io;
    }

    public void setT_io(double t_io) {
        this.t_io = t_io;
    }

    public double getT_end_sim() {
        return t_end_sim;
    }

    public void setT_end_sim(double t_end_sim) {
        this.t_end_sim = t_end_sim;
    }

}
