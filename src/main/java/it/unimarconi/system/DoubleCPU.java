package it.unimarconi.system;

import it.unimarconi.beans.CPU;
import it.unimarconi.beans.IO;
import it.unimarconi.beans.Job;
import it.unimarconi.commons.Calendar;
import it.unimarconi.commons.Event;
import it.unimarconi.generatori.Generatore3Erlang;
import it.unimarconi.generatori.GeneratoreEsponenziale;
import it.unimarconi.generatori.GeneratoreIperesponenziale;
import it.unimarconi.utils.JobComparator;

import java.util.ArrayList;
import java.util.TreeSet;

/** @author Guido Barbaglia */
public class DoubleCPU {

    private CPU cpu1;

    private CPU cpu2;

    private IO io;

    private Calendar calendar;

    private double clock;

    private GeneratoreEsponenziale generatoreArrivi;

    private Generatore3Erlang generatoreCPU;

    private GeneratoreIperesponenziale generatoreRouting;

    private GeneratoreIperesponenziale generatoreRoutingCPU;

    private ArrayList<Double> tempiUscita;

    private int jobTotali;

    private TreeSet<Job> doubleCPUQ;

    public DoubleCPU(long x0_arrivi, long x0_cpu_1, long x0_cpu_2, long x0_cpu_3, long x0_io_1, long x0_io_2, long x0_io_3, long x0_routing, long x0_routing_cpu, int jobTotali) {
        this.setJobTotali(jobTotali);
        this.setIo(new IO(x0_io_1, x0_io_2, x0_io_3, 2));
        this.setCpu1(new CPU());
        this.setCpu2(new CPU());
        this.setClock(0);
        this.setGeneratoreArrivi(new GeneratoreEsponenziale(x0_arrivi, 30));
        this.setGeneratoreRouting(new GeneratoreIperesponenziale(x0_routing, 0.9));
        this.setCalendar(new Calendar());
        this.getCalendar().setTempoArrivo(this.getClock() + this.getGeneratoreArrivi().getNextExp());
        this.setTempiUscita(new ArrayList<Double>());
        this.setDoubleCPUQ(new TreeSet<Job>(new JobComparator()));
        this.setGeneratoreCPU(new Generatore3Erlang(x0_cpu_1, x0_cpu_2, x0_cpu_3, 4));
    }

    public Double simula() {

        /* Simula fino al numero di job richiesti. */
        while (this.getTempiUscita().size() < this.getJobTotali()) {
            Event next = this.getCalendar().get_next_double_cpu();
            this.setClock(this.getCalendar().get_next_time_double_cpu(next));
            System.out.println(clock + " " + next);
            switch (next) {
                case ARRIVAL: arrival(); break;
                case CPU1: cpu1(); break;
                case CPU2: cpu2(); break;
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

    private void arrival() {

        /* Prevedi prossimo tempo di arrivo. */
        this.getCalendar().setTempoArrivo(this.getClock() + this.getGeneratoreArrivi().getNextExp());

        /* Crea nuovo job. */
        Job j = new Job();
        double tmp = this.getGeneratoreCPU().getNext3Erlang();
        j.setTempoProcessamento(tmp);
        j.setTempoArrivo(this.getClock());

        /* Seleziona prossima unita' CPU. */
//        System.out.println(this.getClock() + " " + this.getCpu1().isFree() + "_" + this.getCpu2().isFree());
        if (this.getCpu1().isFree()) {
            this.getCpu1().setFree(false);
            this.getCpu1().setJob(j);
            this.getCalendar().setTempoCPU1(this.getClock() + j.getTempoProcessamento());
        } else if (this.getCpu2().isFree()) {
            this.getCpu2().setFree(false);
            this.getCpu2().setJob(j);
            this.getCalendar().setTempoCPU2(this.getClock() + j.getTempoProcessamento());
//            System.out.println(this.getCalendar().getTempoCPU2() + " ARRIVAL > CPU2");
        } else {
            this.getDoubleCPUQ().add(j);
        }

    }

    public Job getJobFromQ() {
        Job j = this.getDoubleCPUQ().iterator().next();
        this.getDoubleCPUQ().remove(j);
        return j;
    }

    private void cpu1() {
        switch (this.getGeneratoreRouting().getNextRoute()) {
            case IO:
                if (this.getIo().isFree()) {
                    this.getIo().setFree(false);
                    this.getIo().setJob(this.getCpu1().getJob());
                    this.getCpu1().setJob(null);
                    this.getCpu1().setFree(true);
                    double tmp = this.getIo().getGeneratore().getNext3Erlang();
//                    System.out.println(tmp);
                    this.getIo().getJob().setTempoProcessamento(tmp);
                    this.getCalendar().setTempoIO(this.getClock() + this.getIo().getJob().getTempoProcessamento());
//                    System.out.println("\tCPU1 " + this.getCalendar().getTempoIO());
                } else {
                    Job j = this.getCpu1().getJob();
                    double tmp = this.getIo().getGeneratore().getNext3Erlang();
//                    System.out.println(tmp);
                    j.setTempoProcessamento(tmp);
                    this.getCpu1().setJob(null);
                    this.getCpu1().setFree(true);
                }
                break;
            case OUT:
                this.getCpu1().setFree(true);
                this.getCpu1().getJob().setTempoUscita(this.getClock());
                this.getTempiUscita().add(this.getCpu1().getJob().getTempoJob());
                this.getCpu1().setJob(null);
                break;
        }
        if (this.getCpu1().getQ().size() > 0) {
            this.getCpu1().setJob(this.getJobFromQ());
            this.getCpu1().setFree(false);
            this.getCalendar().setTempoCPU1(this.getClock() + this.getCpu1().getJob().getTempoProcessamento());
        } else {
            this.getCalendar().setTempoCPU1(Double.MAX_VALUE);
            this.getCpu1().setFree(true);
            this.getCpu1().setJob(null);
        }
    }

    private void cpu2() {
        switch (this.getGeneratoreRouting().getNextRoute()) {
            case IO:
                if (this.getIo().isFree()) {
                    this.getIo().setFree(false);
                    this.getIo().setJob(this.getCpu2().getJob());
                    this.getCpu2().setJob(null);
                    this.getCpu2().setFree(true);
                    double tmp = this.getIo().getGeneratore().getNext3Erlang();
                    this.getIo().getJob().setTempoProcessamento(tmp);
                    this.getCalendar().setTempoIO(this.getClock() + this.getIo().getJob().getTempoProcessamento());
//                    System.out.println("\tCPU2 " + this.getCalendar().getTempoIO());
                } else {
                    Job j = this.getCpu2().getJob();
                    double tmp = this.getIo().getGeneratore().getNext3Erlang();
//                    System.out.println(tmp);
                    j.setTempoProcessamento(tmp);
                    this.getCpu2().setJob(null);
                    this.getCpu2().setFree(true);
                }
                break;
            case OUT:
                this.getCpu2().setFree(true);
                this.getCpu2().getJob().setTempoUscita(this.getClock());
                this.getTempiUscita().add(this.getCpu2().getJob().getTempoJob());
                this.getCpu2().setJob(null);
                break;
        }
        if (this.getCpu2().getQ().size() > 0) {
            this.getCpu2().setJob(this.getJobFromQ());
            this.getCpu2().setFree(false);
            this.getCalendar().setTempoCPU2(this.getClock() + this.getCpu2().getJob().getTempoProcessamento());
        } else {
            this.getCalendar().setTempoCPU2(Double.MAX_VALUE);
            this.getCpu2().setFree(true);
            this.getCpu2().setJob(null);
        }
    }

    private void io() {

        /* Seleziona prossima unita' CPU. */
        if (this.getCpu1().isFree()) {
            this.getCpu1().setFree(false);
            this.getCpu1().setJob(this.getIo().getJob());
            this.getIo().setJob(null);
            double tmp = this.getGeneratoreCPU().getNext3Erlang();
            this.getCpu1().getJob().setTempoProcessamento(tmp);
            this.getCalendar().setTempoCPU1(this.getClock() + this.getCpu1().getJob().getTempoProcessamento());
        } else if (this.getCpu2().isFree()) {
            double tmp = this.getGeneratoreCPU().getNext3Erlang();
            this.getIo().getJob().setTempoProcessamento(this.getGeneratoreCPU().getNext3Erlang());
            this.getCpu2().setFree(false);
            this.getCpu2().setJob(this.getIo().getJob());
            this.getIo().setJob(null);
            this.getCpu2().getJob().setTempoProcessamento(tmp);
            this.getCalendar().setTempoCPU2(this.getClock() + this.getCpu2().getJob().getTempoProcessamento());
        } else {
            double tmp = this.getGeneratoreCPU().getNext3Erlang();
            this.getIo().getJob().setTempoProcessamento(tmp);
            this.getDoubleCPUQ().add(this.getIo().getJob());
            this.getIo().setJob(null);
            this.getIo().setFree(true);
        }

        /* Controlla la coda di I/O. */
        if (this.getIo().getIoQ().size() > 0) {
            this.getIo().setJob(this.getIo().getJobFromQ());
            this.getIo().setFree(false);
            double tmp = this.getIo().getGeneratore().getNext3Erlang();
//            System.out.println(tmp);
            this.getIo().getJob().setTempoProcessamento(tmp);
            this.getCalendar().setTempoIO(this.getClock() + this.getIo().getJob().getTempoProcessamento());
//            System.out.println("\tIO " + this.getCalendar().getTempoIO());
        } else {
            this.getCalendar().setTempoIO(Double.MAX_VALUE);
            this.getIo().setFree(true);
            this.getIo().setJob(null);
        }

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

    public TreeSet<Job> getDoubleCPUQ() {
        return doubleCPUQ;
    }

    public void setDoubleCPUQ(TreeSet<Job> doubleCPUQ) {
        this.doubleCPUQ = doubleCPUQ;
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

    public GeneratoreIperesponenziale getGeneratoreRoutingCPU() {
        return generatoreRoutingCPU;
    }

    public void setGeneratoreRoutingCPU(GeneratoreIperesponenziale generatoreRoutingCPU) {
        this.generatoreRoutingCPU = generatoreRoutingCPU;
    }

    public Generatore3Erlang getGeneratoreCPU() {
        return generatoreCPU;
    }

    public void setGeneratoreCPU(Generatore3Erlang generatoreCPU) {
        this.generatoreCPU = generatoreCPU;
    }

}