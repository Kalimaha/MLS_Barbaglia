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

/** @author Guido Barbaglia */
public class DoubleCPU {

    private CPU cpu1;

    private CPU cpu2;

    private IO io;

    private Calendar calendar;

    private double clock;

    private Generatore generatoreArrivi;

    private Generatore generatoreRoutingCPU;

    private Generatore generatoreCPU1;

    private Generatore generatoreCPU2;

    private Generatore generatoreIO;

    private Generatore generatoreRouting;

    private ArrayList<Double> tempiUscita;

    private StatisticheSimulazione statisticheSimulazione;

    private int a;

    private int jobTotali;

    public DoubleCPU(int a, int jobTotali) {
        this.setA(a);
        this.setJobTotali(jobTotali);
        this.setIo(new IO());
        this.setCpu1(new CPU());
        this.setCpu2(new CPU());
        this.setClock(0);
        this.setGeneratoreArrivi(new Generatore(this.getA(), 1, 64, 0.033));

        this.setGeneratoreRoutingCPU(new Generatore(this.getA(), 1, 64, 0.5));
        this.setGeneratoreRouting(new Generatore(61, 7, 15, 64, 1, 0.9));

        this.setGeneratoreCPU1(new Generatore(this.getA(), 1, 64, 0.5));
        this.setGeneratoreCPU2(new Generatore(this.getA(), 1, 64, 0.5));
        this.setGeneratoreIO(new Generatore(this.getA(), 1, 64, 0.5));

        /* TODO: change a for the routing? */
        this.setGeneratoreRouting(new Generatore(61, 7, 15, 64, 1, 0.9));

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
            Event next = this.getCalendar().get_next_double_cpu();
            this.setClock(this.getCalendar().get_next_time(next));
            switch (next) {
                case ARRIVAL: arrival(); break;
                case CPU1: cpu1(); break;
                case CPU2: cpu2(); break;
                case IO: io();break;
            }
        }
        return end_sim();
    }

    private void arrival() {

        /* Prevedi prossimo tempo di arrivo. */
        this.getCalendar().setTempoArrivo(this.getClock() + this.getGeneratoreArrivi().getNextExp());

        Event nextCPU = this.getGeneratoreRoutingCPU().getNextRouteCPU();

        /* Crea nuovo job. */
        Job j = new Job();
        switch (nextCPU) {
            case CPU1: j.setTempoProcessamento(this.getGeneratoreCPU1().getNextErlang3()); break;
            case CPU2: j.setTempoProcessamento(this.getGeneratoreCPU2().getNextErlang3()); break;
        }
        j.setTempoArrivo(clock);

        /* Entra nella CPU se libera... */
        switch (nextCPU) {
            case CPU1:
                if (this.getCpu1().isFree()) {
                    this.getCpu1().setFree(false);
                    this.getCpu1().setJob(j);
                    this.getCalendar().setTempoCPU(this.getClock() + j.getTempoProcessamento());
                } else {
                    this.getCpu1().addJobToTheQueue(j);
                }
                break;
            case CPU2:
                if (this.getCpu2().isFree()) {
                    this.getCpu2().setFree(false);
                    this.getCpu2().setJob(j);
                    this.getCalendar().setTempoCPU(this.getClock() + j.getTempoProcessamento());
                } else {
                    this.getCpu2().addJobToTheQueue(j);
                }
                break;
        }


    }

    private void cpu1() {
        switch (this.getGeneratoreRouting().getNextRoute()) {
            case IO:
                if (this.getIo().isFree()) {
                    this.getIo().setFree(false);
                    this.getIo().setJob(this.getCpu1().getJob());
                    this.getCpu1().setJob(null);
                    this.getCpu1().setFree(true);
                    this.getCalendar().setTempoIO(this.getClock() + this.getGeneratoreIO().getNextErlang3());
                } else {
                    this.getIo().getIoQ().add(this.getCpu1().getJob());
                    this.getCpu1().setJob(null);
                    this.getCpu1().setFree(true);
                }
                break;
            case OUT:
                this.getCpu1().setFree(true);
                this.getCpu1().getJob().setTempoUscita(this.getClock());
                this.getTempiUscita().add(this.getCpu1().getJob().getTempoJob());
                if (this.getTempiUscita().size() % 100 == 0) {
                    ArrayList<Double> copia = (ArrayList<Double>)this.getTempiUscita().clone();
                    System.out.println(this.getTempiUscita().size() + ", " + Stats.media(copia));
                }
                break;
        }
        if (this.getCpu1().getQ().size() > 0) {
            this.getCpu1().setJob(this.getCpu1().getJobFromQ());
            this.getCpu1().setFree(false);
            this.getCalendar().setTempoCPU(this.getClock() + this.getGeneratoreCPU1().getNextErlang3());
        } else {
            this.getCalendar().setTempoCPU1(Double.MAX_VALUE);
            this.getCpu1().setFree(true);
        }
    }

    private void cpu2() {
        Event nextCPU = this.getGeneratoreRoutingCPU().getNextRouteCPU();
        switch (this.getGeneratoreRouting().getNextRoute()) {
            case IO:
                if (this.getIo().isFree()) {
                    this.getIo().setFree(false);
                    switch (nextCPU) {
                        case CPU1:
                            this.getIo().setJob(this.getCpu1().getJob());
                            this.getCpu1().setJob(null);
                            this.getCpu1().setFree(true);
                            break;
                        case CPU2:
                            this.getIo().setJob(this.getCpu2().getJob());
                            this.getCpu2().setJob(null);
                            this.getCpu2().setFree(true);
                            break;
                    }
                    this.getCalendar().setTempoIO(this.getClock() + this.getGeneratoreIO().getNextErlang3());
                } else {
                    this.getIo().getIoQ().add(this.getCpu2().getJob());
                    this.getCpu2().setJob(null);
                    this.getCpu2().setFree(true);
                }
                break;
            case OUT:
                switch (nextCPU) {
                    case CPU1:
                        if (this.getCpu1().getJob() != null) {
                            this.getCpu1().setFree(true);
                            this.getCpu1().getJob().setTempoUscita(this.getClock());
                            this.getTempiUscita().add(this.getCpu1().getJob().getTempoJob());
                        }
                        break;
                    case CPU2:
                        if (this.getCpu2().getJob() != null) {
                            this.getCpu2().setFree(true);
                            this.getCpu2().getJob().setTempoUscita(this.getClock());
                            this.getTempiUscita().add(this.getCpu2().getJob().getTempoJob());
                        }
                        break;
                }
                if (this.getTempiUscita().size() % 100 == 0) {
                    ArrayList<Double> copia = (ArrayList<Double>)this.getTempiUscita().clone();
                    System.out.println(this.getTempiUscita().size() + ", " + Stats.media(copia));
                }
                break;
        }
        if (this.getCpu2().getQ().size() > 0) {
            this.getCpu2().setJob(this.getCpu2().getJobFromQ());
            this.getCpu2().setFree(false);
            this.getCalendar().setTempoCPU(this.getClock() + this.getGeneratoreCPU1().getNextErlang3());
        } else {
            this.getCalendar().setTempoCPU1(Double.MAX_VALUE);
            this.getCpu2().setFree(true);
        }
    }

    private void io() {
        Event nextCPU = this.getGeneratoreRoutingCPU().getNextRouteCPU();
        switch (nextCPU) {
            case CPU1:
                if (this.getCpu1().isFree()) {
                    this.getCpu1().setFree(false);
                    this.getCpu1().setJob(this.getIo().getJob());
                    this.getIo().setJob(null);
                    this.getCalendar().setTempoCPU(this.getClock() + this.getGeneratoreCPU1().getNextErlang3());
                } else {
                    this.getCpu1().addJobToTheQueue(this.getIo().getJob());
                    this.getIo().setJob(null);
                    this.getIo().setFree(true);
                }
                break;
            case CPU2:
                if (this.getCpu2().isFree()) {
                    this.getCpu2().setFree(false);
                    this.getCpu2().setJob(this.getIo().getJob());
                    this.getIo().setJob(null);
                    this.getCalendar().setTempoCPU(this.getClock() + this.getGeneratoreCPU2().getNextErlang3());
                } else {
                    this.getCpu2().addJobToTheQueue(this.getIo().getJob());
                    this.getIo().setJob(null);
                    this.getIo().setFree(true);
                }
                break;
        }
        if (this.getIo().getIoQ().size() > 0) {
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

    public Generatore getGeneratoreRoutingCPU() {
        return generatoreRoutingCPU;
    }

    public void setGeneratoreRoutingCPU(Generatore generatoreRoutingCPU) {
        this.generatoreRoutingCPU = generatoreRoutingCPU;
    }

    public Generatore getGeneratoreCPU1() {
        return generatoreCPU1;
    }

    public void setGeneratoreCPU1(Generatore generatoreCPU1) {
        this.generatoreCPU1 = generatoreCPU1;
    }

    public Generatore getGeneratoreCPU2() {
        return generatoreCPU2;
    }

    public void setGeneratoreCPU2(Generatore generatoreCPU2) {
        this.generatoreCPU2 = generatoreCPU2;
    }

    public CPU getCpu1() {
        return cpu1;
    }

    public void setCpu1(CPU cpu1) {
        this.cpu1 = cpu1;
    }

    public CPU getCpu2() {
        return cpu2;
    }

    public void setCpu2(CPU cpu2) {
        this.cpu2 = cpu2;
    }

}