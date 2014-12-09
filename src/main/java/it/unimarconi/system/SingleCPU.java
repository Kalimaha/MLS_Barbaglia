package it.unimarconi.system;

import it.unimarconi.beans.CPU;
import it.unimarconi.beans.IO;
import it.unimarconi.beans.Job;
import it.unimarconi.beans.StatisticheSimulazione;
import it.unimarconi.commons.Calendar;
import it.unimarconi.commons.Event;
import it.unimarconi.commons.Generatore;
import it.unimarconi.utils.Stats;

import java.util.ArrayList;
import java.util.List;

/** @author Guido Barbaglia */
public class SingleCPU {

    private CPU cpu;

    private IO io;

    private Calendar calendar;

    private double clock;

    private Generatore generatoreArrivi;

    private Generatore generatoreCPU;

    private Generatore generatoreIO;

    private Generatore generatoreRouting;

    private ArrayList<Double> tempiUscita;

    private StatisticheSimulazione statisticheSimulazione;

    private int a;

    private int jobTotali;

    public SingleCPU(int a, int jobTotali) {
        this.setA(a);
        this.setJobTotali(jobTotali);
        this.setIo(new IO());
        this.setCpu(new CPU());
        this.setClock(0);
        this.setGeneratoreArrivi(new Generatore(this.getA(), 1, 32, 0.033));
        this.setGeneratoreCPU(new Generatore(this.getA(), 1, 32, 0.5));
        this.setGeneratoreIO(new Generatore(this.getA(), 1, 32, 0.5));

        /* TODO: chabge a for the routing? */
        this.setGeneratoreRouting(new Generatore(61, 7, 15, 16, 1, 0.9));

        this.setCalendar(new Calendar());
        this.getCalendar().setTempoArrivo(this.getClock() + this.getGeneratoreArrivi().getNextExp());
        this.setTempiUscita(new ArrayList<Double>());
        this.setStatisticheSimulazione(new StatisticheSimulazione());
    }

    public StatisticheSimulazione simula() {
        return scheduler();
    }

    private StatisticheSimulazione scheduler() {
        while (this.getTempiUscita().size() < this.getJobTotali()) {
            Event next = this.getCalendar().get_next();
            this.setClock(this.getCalendar().get_next_time(next));
            switch (next) {
                case ARRIVAL: arrival(); break;
                case CPU: cpu(); break;
                case IO: io();break;
            }
        }
        return end_sim();
    }

    private void arrival() {

        /* Prevedi prossimo tempo di arrivo. */
        this.getCalendar().setTempoArrivo(this.getClock() + this.getGeneratoreArrivi().getNextExp());

        /* Crea nuovo job. */
        Job j = new Job();
        j.setTempoProcessamento(this.getGeneratoreCPU().getNextErlang3());
        j.setTempoArrivo(clock);

        /* Entra nella CPU se libera... */
        if (this.getCpu().isFree()) {
            this.getCpu().setFree(false);
            this.getCpu().setJob(j);
            this.getCalendar().setTempoCPU(this.getClock() + j.getTempoProcessamento());
        }

        /* ...altrimenti in coda. */
        else {
            this.getCpu().getQ().add(j);
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
                    this.getCalendar().setTempoIO(this.getClock() + this.getGeneratoreIO().getNextErlang3());
                } else {
                    this.getIo().getQ().add(this.getCpu().getJob());
                    this.getCpu().setJob(null);
                    this.getCpu().setFree(true);
                }
                break;
            case OUT:
                this.getCpu().setFree(true);
                this.getCpu().getJob().setTempoUscita(this.getClock());
                this.getTempiUscita().add(this.getCpu().getJob().getTempoJob());
                break;
        }
        if (this.getCpu().getQ().size() > 0) {
            this.getCpu().setJob(this.getCpu().getJobFromQ());
            this.getCpu().setFree(false);
            this.getCalendar().setTempoCPU(this.getClock() + this.getGeneratoreCPU().getNextErlang3());
        } else {
            this.getCalendar().setTempoCPU(Double.MAX_VALUE);
            this.getCpu().setFree(true);
        }
    }

    private void io() {
        if (this.getCpu().isFree()) {
            this.getCpu().setFree(false);
            this.getCpu().setJob(this.getIo().getJob());
            this.getIo().setJob(null);
            this.getCalendar().setTempoCPU(this.getClock() + this.getGeneratoreCPU().getNextErlang3());
        } else {
            this.getCpu().getQ().add(this.getIo().getJob());
            this.getIo().setJob(null);
            this.getIo().setFree(true);
        }
        if (this.getIo().getQ().size() > 0) {
            this.getIo().setJob(this.getIo().getJobFromQ());
            this.getIo().setFree(false);
            this.getCalendar().setTempoIO(this.getClock() + this.getGeneratoreIO().getNextErlang3());
        } else {
            this.getCalendar().setTempoIO(Double.MAX_VALUE);
            this.getIo().setFree(true);
        }
    }

    private StatisticheSimulazione end_sim() {
        this.getStatisticheSimulazione().setTempoMedio(Stats.media(this.getTempiUscita()));
        this.getStatisticheSimulazione().setVarianza(Stats.sd(this.getTempiUscita()));
        this.getStatisticheSimulazione().setJobs(this.getTempiUscita().size());
        return this.getStatisticheSimulazione();
    }

    public CPU getCpu() {
        return cpu;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public IO getIo() {
        return io;
    }

    public void setIo(IO io) {
        this.io = io;
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

    public Generatore getGeneratoreArrivi() {
        return generatoreArrivi;
    }

    public void setGeneratoreArrivi(Generatore generatoreArrivi) {
        this.generatoreArrivi = generatoreArrivi;
    }

    public Generatore getGeneratoreCPU() {
        return generatoreCPU;
    }

    public void setGeneratoreCPU(Generatore generatoreCPU) {
        this.generatoreCPU = generatoreCPU;
    }

    public Generatore getGeneratoreIO() {
        return generatoreIO;
    }

    public void setGeneratoreIO(Generatore generatoreIO) {
        this.generatoreIO = generatoreIO;
    }

    public Generatore getGeneratoreRouting() {
        return generatoreRouting;
    }

    public void setGeneratoreRouting(Generatore generatoreRouting) {
        this.generatoreRouting = generatoreRouting;
    }

    public StatisticheSimulazione getStatisticheSimulazione() {
        return statisticheSimulazione;
    }

    public void setStatisticheSimulazione(StatisticheSimulazione statisticheSimulazione) {
        this.statisticheSimulazione = statisticheSimulazione;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getJobTotali() {
        return jobTotali;
    }

    public void setJobTotali(int jobTotali) {
        this.jobTotali = jobTotali;
    }

}