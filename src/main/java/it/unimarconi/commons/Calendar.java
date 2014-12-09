package it.unimarconi.commons;

public class Calendar {

    private double tempoArrivo;

    private double tempoCPU;

    private double tempoIO;

    public Calendar() {
        this.setTempoCPU(Integer.MAX_VALUE);
        this.setTempoIO(Integer.MAX_VALUE);
    }

    public Event get_next() {
        Event out = Event.ARRIVAL;
        double min = tempoArrivo;
        if (tempoCPU < min) {
            min = tempoCPU;
            out = Event.CPU;
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

}