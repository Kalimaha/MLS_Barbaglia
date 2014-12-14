package it.unimarconi.system;

import it.unimarconi.beans.CPU;
import it.unimarconi.beans.IO;
import it.unimarconi.beans.Job;
import it.unimarconi.beans.StatisticheSimulazione;
import it.unimarconi.commons.Calendar;
import it.unimarconi.commons.Event;
import it.unimarconi.commons.Generatore;
import it.unimarconi.generatori.Generatore3Erlang;
import it.unimarconi.generatori.GeneratoreEsponenziale;
import it.unimarconi.generatori.GeneratoreIperesponenziale;
import it.unimarconi.utils.Stats;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/** @author Guido Barbaglia */
public class SingleCPU {

    private CPU cpu;

    private IO io;

    private Calendar calendar;

    private double clock;

    private GeneratoreEsponenziale generatoreArrivi;

    private Generatore3Erlang generatoreCPU;

    private Generatore3Erlang generatoreIO;

    private GeneratoreIperesponenziale generatoreRouting;

    private ArrayList<Double> tempiUscita;

    private StatisticheSimulazione statisticheSimulazione;

    private int jobTotali;

    private final int a = 1220703125;

    private final int b = 28;

    public SingleCPU(long x0_arrivi, long x0_cpu, long x0_io, long x0_routing, int jobTotali) {

        this.setJobTotali(jobTotali);
        this.setIo(new IO());
        this.setCpu(new CPU());
        this.setClock(0);

        this.setGeneratoreArrivi(new GeneratoreEsponenziale(a, x0_arrivi, b, 0.033));

        this.setGeneratoreCPU(new Generatore3Erlang(a, x0_cpu, b, 0.5));
        this.setGeneratoreIO(new Generatore3Erlang(a, x0_io, b, 0.5));
        this.setGeneratoreRouting(new GeneratoreIperesponenziale(a, x0_routing, b, 0.9));

        this.setCalendar(new Calendar());
        this.getCalendar().setTempoArrivo(this.getClock() + this.getGeneratoreArrivi().getNextExp());
        this.setTempiUscita(new ArrayList<Double>());
        this.setStatisticheSimulazione(new StatisticheSimulazione());
    }

    public ArrayList<Double> simula() {
        return scheduler();
    }

    private ArrayList<Double> scheduler() {
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
        j.setTempoProcessamento(this.getGeneratoreCPU().getNext3Erlang());
        j.setTempoArrivo(this.getClock());

        /* Entra nella CPU se libera... */
        if (this.getCpu().isFree()) {
            this.getCpu().setFree(false);
            this.getCpu().setJob(j);
            this.getCalendar().setTempoCPU(this.getClock() + j.getTempoProcessamento());
        }

        /* ...altrimenti in coda. */
        else {
            this.getCpu().addJobToTheQueue(j);
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
                    this.getIo().getJob().setTempoProcessamento(this.getGeneratoreIO().getNext3Erlang());
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
//                if (this.getTempiUscita().size() % 100 == 0) {
//                    ArrayList<Double> copia = (ArrayList<Double>)this.getTempiUscita().clone();
//                    System.out.println(this.getTempiUscita().size() + ", " + Stats.media(copia));
//                }
                this.getCpu().setJob(null);
                break;
        }
        if (this.getCpu().getQ().size() > 0) {
            this.getCpu().setJob(this.getCpu().getJobFromQ());
            this.getCpu().setFree(false);
            this.getCpu().getJob().setTempoProcessamento(this.getGeneratoreCPU().getNext3Erlang());
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
            this.getCpu().getJob().setTempoProcessamento(this.getGeneratoreCPU().getNext3Erlang());
            this.getCalendar().setTempoCPU(this.getClock() + this.getCpu().getJob().getTempoProcessamento());
        } else {
            this.getCpu().addJobToTheQueue(this.getIo().getJob());
            this.getIo().setJob(null);
            this.getIo().setFree(true);
        }
        if (this.getIo().getIoQ().size() > 0) {
            this.getIo().setJob(this.getIo().getJobFromQ());
            this.getIo().setFree(false);
            this.getIo().getJob().setTempoProcessamento(this.getGeneratoreIO().getNext3Erlang());
            this.getCalendar().setTempoIO(this.getClock() + this.getIo().getJob().getTempoProcessamento());
        } else {
            this.getCalendar().setTempoIO(Double.MAX_VALUE);
            this.getIo().setFree(true);
            this.getIo().setJob(null);
        }
    }

    private ArrayList<Double> end_sim() {
//        System.out.println(this.getTempiUscita().size() + " tempi uscita");
//        this.getStatisticheSimulazione().setTempoMedio(Stats.media(this.getTempiUscita()));
//        this.getStatisticheSimulazione().setVarianza(Stats.sd(this.getTempiUscita()));
//        this.getStatisticheSimulazione().setJobs(this.getTempiUscita().size());
//        return this.getStatisticheSimulazione();
        return this.getTempiUscita();
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

    public GeneratoreEsponenziale getGeneratoreArrivi() {
        return generatoreArrivi;
    }

    public void setGeneratoreArrivi(GeneratoreEsponenziale generatoreArrivi) {
        this.generatoreArrivi = generatoreArrivi;
    }

    public Generatore3Erlang getGeneratoreCPU() {
        return generatoreCPU;
    }

    public void setGeneratoreCPU(Generatore3Erlang generatoreCPU) {
        this.generatoreCPU = generatoreCPU;
    }

    public Generatore3Erlang getGeneratoreIO() {
        return generatoreIO;
    }

    public void setGeneratoreIO(Generatore3Erlang generatoreIO) {
        this.generatoreIO = generatoreIO;
    }

    public GeneratoreIperesponenziale getGeneratoreRouting() {
        return generatoreRouting;
    }

    public void setGeneratoreRouting(GeneratoreIperesponenziale generatoreRouting) {
        this.generatoreRouting = generatoreRouting;
    }

    public StatisticheSimulazione getStatisticheSimulazione() {
        return statisticheSimulazione;
    }

    public void setStatisticheSimulazione(StatisticheSimulazione statisticheSimulazione) {
        this.statisticheSimulazione = statisticheSimulazione;
    }

    public int getJobTotali() {
        return jobTotali;
    }

    public void setJobTotali(int jobTotali) {
        this.jobTotali = jobTotali;
    }

}