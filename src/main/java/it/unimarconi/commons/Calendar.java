package it.unimarconi.commons;

public class Calendar {

    private double tempoArrivo;

    private double tempoCPU;

    private double tempoCPU1;

    private double tempoCPU2;

    private double tempoIO;

    public Calendar() {
        this.setTempoCPU(Double.MAX_VALUE);
        this.setTempoCPU1(Double.MAX_VALUE);
        this.setTempoCPU2(Double.MAX_VALUE);
        this.setTempoIO(Double.MAX_VALUE);
    }

    public Event get_next() {
        Event out = null;
        double min = Double.MAX_VALUE;
        if (tempoCPU < min) {
            min = tempoCPU;
            out = Event.CPU;
        }
        if (tempoArrivo < min) {
            min = tempoArrivo;
            out = Event.ARRIVAL;
        }
        if (tempoIO < min)
            out = Event.IO;
        return out;
    }

    public double get_next_time_double_cpu(Event e) {
        switch (e) {
            case ARRIVAL: return this.getTempoArrivo();
            case CPU1: return this.getTempoCPU1();
            case CPU2: return this.getTempoCPU2();
            case IO: return this.getTempoIO();
        }
        return 0;
    }

    public Event get_next_double_cpu() {
        Event out = null;
        double min = Double.MAX_VALUE;
        if (tempoCPU1 < min) {
            min = tempoCPU1;
            out = Event.CPU1;
        }
        if (tempoCPU2 < min) {
            min = tempoCPU2;
            out = Event.CPU2;
        }
        if (tempoArrivo < min) {
            min = tempoArrivo;
            out = Event.ARRIVAL;
        }
        if (tempoIO < min)
            out = Event.IO;
        return out;
    }

    public double get_next_time(Event e) {
        switch (e) {
            case ARRIVAL: return this.getTempoArrivo();
            case CPU: return this.getTempoCPU();
            case IO: return this.getTempoIO();
        }
        return 0;
    }

    public double getTempoArrivo() {
        return tempoArrivo;
    }

    public void setTempoArrivo(double tempoArrivo) {
        this.tempoArrivo = tempoArrivo;
    }

    public double getTempoCPU() {
        return tempoCPU;
    }

    public void setTempoCPU(double tempoCPU) {
        this.tempoCPU = tempoCPU;
    }

    public double getTempoIO() {
        return tempoIO;
    }

    public void setTempoIO(double tempoIO) {
        this.tempoIO = tempoIO;
    }

    public double getTempoCPU1() {
        return tempoCPU1;
    }

    public void setTempoCPU1(double tempoCPU1) {
        this.tempoCPU1 = tempoCPU1;
    }

    public double getTempoCPU2() {
        return tempoCPU2;
    }

    public void setTempoCPU2(double tempoCPU2) {
        this.tempoCPU2 = tempoCPU2;
    }

}