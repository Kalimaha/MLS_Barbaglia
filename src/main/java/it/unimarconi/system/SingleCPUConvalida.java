package it.unimarconi.system;

import it.unimarconi.beans.*;
import it.unimarconi.commons.Calendar;
import it.unimarconi.commons.Event;
import it.unimarconi.generatori.GeneratoreEsponenziale;
import it.unimarconi.generatori.GeneratoreIperesponenziale;
import it.unimarconi.generatori.GeneratorePoissoniano;

import java.util.ArrayList;

/** @author Guido Barbaglia */
public class SingleCPUConvalida {

    private CPUFIFO cpu;

    private IOFIFO io;

    private Calendar calendar;

    private double clock;

    private GeneratorePoissoniano generatoreArrivi;

    private GeneratoreIperesponenziale generatoreRouting;

    private ArrayList<Double> tempiUscita;

    private int jobTotali;

    public SingleCPUConvalida(long x0_arrivi, long x0_cpu, long x0_io, long x0_routing, int jobTotali) {
        this.setJobTotali(jobTotali);
        this.setIo(new IOFIFO(x0_io, 2));
        this.setCpu(new CPUFIFO(x0_cpu, 2));
        this.setClock(0);
        this.setGeneratoreArrivi(new GeneratorePoissoniano(x0_arrivi, 25));
        this.setGeneratoreRouting(new GeneratoreIperesponenziale(x0_routing, 0.9));
        this.setCalendar(new Calendar());
        this.getCalendar().setTempoArrivo(this.getClock() + this.getGeneratoreArrivi().getNextPoisson());
        this.setTempiUscita(new ArrayList<Double>());
    }

    public Double simula() {

        /* Simula fino al numero di job richiesti. */
        while (this.getTempiUscita().size() < this.getJobTotali()) {
            Event next = this.getCalendar().get_next();
            this.setClock(this.getCalendar().get_next_time(next));
            switch (next) {
                case ARRIVAL: arrival(); break;
                case CPU: cpu(); break;
                case IO: io();break;
            }
        }

        /* Calcola la media dei tempi di uscita dei jobs. */
        double avg = 0;
        for (Double d : this.getTempiUscita())
            avg += d;

        /* Restituisci la media. */
        return avg / this.getTempiUscita().size();

    }

    public ArrayList<Double> simulaPerRunReplicati() {

        /* Simula fino al numero di job richiesti. */
        while (this.getTempiUscita().size() < this.getJobTotali()) {
            Event next = this.getCalendar().get_next();
            this.setClock(this.getCalendar().get_next_time(next));
            switch (next) {
                case ARRIVAL: arrival(); break;
                case CPU: cpu(); break;
                case IO: io();break;
            }
        }

        /* Restituisci la media. */
        return this.getTempiUscita();

    }

    private void arrival() {

        /* Prevedi prossimo tempo di arrivo. */
        this.getCalendar().setTempoArrivo(this.getClock() + this.getGeneratoreArrivi().getNextPoisson());

        /* Crea nuovo job. */
        Job j = new Job();
        j.setTempoProcessamento(this.getCpu().getGeneratore().getNextExp());
        j.setTempoArrivo(this.getClock());

        /* Entra nella CPU se libera... */
        if (this.getCpu().isFree()) {
            this.getCpu().setFree(false);
            this.getCpu().setJob(j);
            this.getCalendar().setTempoCPU(this.getClock() + j.getTempoProcessamento());
        }

        /* ...altrimenti in coda. */
        else {
            this.getCpu().getCpuQ().add(j);
        }

    }

    private void cpu() {
        switch (this.getGeneratoreRouting().getNextRoute()) {
            case IO:
                if (this.getIo().isFree()) {
                    this.getIo().setFree(false);
                    this.getIo().setJob(this.getCpu().getJob());
                    this.getCpu().setJob(null);
                    this.getCpu().setFree(true);
                    this.getIo().getJob().setTempoProcessamento(this.getIo().getGeneratore().getNextExp());
                    this.getCalendar().setTempoIO(this.getClock() + this.getIo().getJob().getTempoProcessamento());
                } else {
                    this.getIo().getIoQ().add(this.getCpu().getJob());
                    this.getCpu().setJob(null);
                    this.getCpu().setFree(true);
                }
                break;
            case OUT:
                this.getCpu().setFree(true);
                this.getCpu().getJob().setTempoUscita(this.getClock());
                this.getTempiUscita().add(this.getCpu().getJob().getTempoJob());
                this.getCpu().setJob(null);
                break;
        }
        if (this.getCpu().getCpuQ().size() > 0) {
            this.getCpu().setJob(this.getCpu().getJobFromQ());
            this.getCpu().setFree(false);
            this.getCpu().getJob().setTempoProcessamento(this.getCpu().getGeneratore().getNextExp());
            this.getCalendar().setTempoCPU(this.getClock() + this.getCpu().getJob().getTempoProcessamento());
        } else {
            this.getCalendar().setTempoCPU(Double.MAX_VALUE);
            this.getCpu().setFree(true);
            this.getCpu().setJob(null);
        }
    }

    private void io() {
        if (this.getCpu().isFree()) {
            this.getCpu().setFree(false);
            this.getCpu().setJob(this.getIo().getJob());
            this.getIo().setJob(null);
            this.getCpu().getJob().setTempoProcessamento(this.getCpu().getGeneratore().getNextExp());
            this.getCalendar().setTempoCPU(this.getClock() + this.getCpu().getJob().getTempoProcessamento());
        } else {
            this.getCpu().getCpuQ().add(this.getIo().getJob());
            this.getIo().setJob(null);
            this.getIo().setFree(true);
        }
        if (this.getIo().getIoQ().size() > 0) {
            this.getIo().setJob(this.getIo().getJobFromQ());
            this.getIo().setFree(false);
            this.getIo().getJob().setTempoProcessamento(this.getIo().getGeneratore().getNextExp());
            this.getCalendar().setTempoIO(this.getClock() + this.getIo().getJob().getTempoProcessamento());
        } else {
            this.getCalendar().setTempoIO(Double.MAX_VALUE);
            this.getIo().setFree(true);
            this.getIo().setJob(null);
        }
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public double getClock() {
        return clock;
    }

    public void setClock(double clock) {
        this.clock = clock;
    }

    public ArrayList<Double> getTempiUscita() {
        return tempiUscita;
    }

    public void setTempiUscita(ArrayList<Double> tempiUscita) {
        this.tempiUscita = tempiUscita;
    }

    public GeneratorePoissoniano getGeneratoreArrivi() {
        return generatoreArrivi;
    }

    public void setGeneratoreArrivi(GeneratorePoissoniano generatoreArrivi) {
        this.generatoreArrivi = generatoreArrivi;
    }

    public GeneratoreIperesponenziale getGeneratoreRouting() {
        return generatoreRouting;
    }

    public void setGeneratoreRouting(GeneratoreIperesponenziale generatoreRouting) {
        this.generatoreRouting = generatoreRouting;
    }

    public int getJobTotali() {
        return jobTotali;
    }

    public void setJobTotali(int jobTotali) {
        this.jobTotali = jobTotali;
    }

    public CPUFIFO getCpu() {
        return cpu;
    }

    public void setCpu(CPUFIFO cpu) {
        this.cpu = cpu;
    }

    public IOFIFO getIo() {
        return io;
    }

    public void setIo(IOFIFO io) {
        this.io = io;
    }

}