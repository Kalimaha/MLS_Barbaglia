package it.unimarconi.system;

import it.unimarconi.beans.CPU;
import it.unimarconi.beans.IO;
import it.unimarconi.beans.Job;
import it.unimarconi.commons.Calendar;
import it.unimarconi.commons.Event;
import it.unimarconi.generatori.GeneratoreEsponenziale;
import it.unimarconi.generatori.GeneratoreIperesponenziale;

import javax.swing.tree.ExpandVetoException;
import java.util.ArrayList;

/** @author Guido Barbaglia */
public class DoubleCPU {

    private CPU cpu1;

    private CPU cpu2;

    private IO io;

    private Calendar calendar;

    private double clock;

    private GeneratoreEsponenziale generatoreArrivi;

    private GeneratoreIperesponenziale generatoreRouting;

    private GeneratoreIperesponenziale generatoreRoutingCPU;

    private ArrayList<Double> tempiUscita;

    private int jobTotali;

    public DoubleCPU(long x0_arrivi, long x0_cpu_a_1, long x0_cpu_a_2, long x0_cpu_a_3, long x0_cpu_b_1, long x0_cpu_b_2, long x0_cpu_b_3, long x0_io_1, long x0_io_2, long x0_io_3, long x0_routing, long x0_routing_cpu, int jobTotali) {
        this.setJobTotali(jobTotali);
        this.setIo(new IO(x0_io_1, x0_io_2, x0_io_3, 2));
        this.setCpu1(new CPU(x0_cpu_a_1, x0_cpu_a_2, x0_cpu_a_3, 4));
        this.setCpu2(new CPU(x0_cpu_b_1, x0_cpu_b_2, x0_cpu_b_3, 4));
        this.setClock(0);
        this.setGeneratoreArrivi(new GeneratoreEsponenziale(x0_arrivi, 30));
        this.setGeneratoreRouting(new GeneratoreIperesponenziale(x0_routing, 0.9));
        this.setGeneratoreRoutingCPU(new GeneratoreIperesponenziale(x0_routing, 0.5));
        this.setCalendar(new Calendar());
        this.getCalendar().setTempoArrivo(this.getClock() + this.getGeneratoreArrivi().getNextExp());
        this.setTempiUscita(new ArrayList<Double>());
    }

    public Double simula() {

        /* Simula fino al numero di job richiesti. */
        while (this.getTempiUscita().size() < this.getJobTotali()) {
            Event next = this.getCalendar().get_next_double_cpu();
            this.setClock(this.getCalendar().get_next_time_double_cpu(next));
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

        /* Routing COU. */
        Event nextCPU = this.getGeneratoreRoutingCPU().getNextCPU();
        Job j;

        switch (nextCPU) {

            case CPU1:

                /* Crea nuovo job. */
                j = new Job();
                j.setTempoProcessamento(this.getCpu1().getGeneratore().getNext3Erlang());
                j.setTempoArrivo(this.getClock());

                /* Entra nella CPU se libera... */
                if (this.getCpu1().isFree()) {
                    this.getCpu1().setFree(false);
                    this.getCpu1().setJob(j);
                    this.getCalendar().setTempoCPU1(this.getClock() + j.getTempoProcessamento());
                }

                /* ...altrimenti in coda. */
                else {
                    this.getCpu1().addJobToTheQueue(j);
                }

                break;

            case CPU2:

                /* Crea nuovo job. */
                j = new Job();
                j.setTempoProcessamento(this.getCpu2().getGeneratore().getNext3Erlang());
                j.setTempoArrivo(this.getClock());

                /* Entra nella CPU se libera... */
                if (this.getCpu2().isFree()) {
                    this.getCpu2().setFree(false);
                    this.getCpu2().setJob(j);
                    this.getCalendar().setTempoCPU2(this.getClock() + j.getTempoProcessamento());
                }

                /* ...altrimenti in coda. */
                else {
                    this.getCpu2().addJobToTheQueue(j);
                }

                break;

        }

    }

    private void cpu1() {
        switch (this.getGeneratoreRouting().getNextRoute()) {
            case IO:
                if (this.getCpu1().getJob() != null) {
                    this.getIo().setFree(false);
                    this.getIo().setJob(this.getCpu1().getJob());
                    this.getCpu1().setJob(null);
                    this.getCpu1().setFree(true);
                    this.getIo().getJob().setTempoProcessamento(this.getIo().getGeneratore().getNext3Erlang());
                    this.getCalendar().setTempoIO(this.getClock() + this.getIo().getJob().getTempoProcessamento());
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
                this.getCpu1().setJob(null);
                break;
        }
        if (this.getCpu1().getQ().size() > 0) {
            this.getCpu1().setJob(this.getCpu1().getJobFromQ());
            this.getCpu1().setFree(false);
            this.getCpu1().getJob().setTempoProcessamento(this.getCpu1().getGeneratore().getNext3Erlang());
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
                    this.getIo().getJob().setTempoProcessamento(this.getIo().getGeneratore().getNext3Erlang());
                    this.getCalendar().setTempoIO(this.getClock() + this.getIo().getJob().getTempoProcessamento());
                } else {
                    this.getIo().getIoQ().add(this.getCpu2().getJob());
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
            this.getCpu2().setJob(this.getCpu2().getJobFromQ());
            this.getCpu2().setFree(false);
            this.getCpu2().getJob().setTempoProcessamento(this.getCpu2().getGeneratore().getNext3Erlang());
            this.getCalendar().setTempoCPU2(this.getClock() + this.getCpu2().getJob().getTempoProcessamento());
        } else {
            this.getCalendar().setTempoCPU2(Double.MAX_VALUE);
            this.getCpu2().setFree(true);
            this.getCpu2().setJob(null);
        }
    }

    private void io() {
        Event nextCPU = this.getGeneratoreRoutingCPU().getNextCPU();
        switch (nextCPU) {
            case CPU1:
                if (this.getCpu1().isFree()) {
                    this.getCpu1().setFree(false);
                    this.getCpu1().setJob(this.getIo().getJob());
                    this.getIo().setJob(null);
                    this.getCpu1().getJob().setTempoProcessamento(this.getCpu1().getGeneratore().getNext3Erlang());
                    this.getCalendar().setTempoCPU1(this.getClock() + this.getCpu1().getJob().getTempoProcessamento());
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
                    this.getCpu2().getJob().setTempoProcessamento(this.getCpu2().getGeneratore().getNext3Erlang());
                    this.getCalendar().setTempoCPU2(this.getClock() + this.getCpu2().getJob().getTempoProcessamento());
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
            this.getIo().getJob().setTempoProcessamento(this.getIo().getGeneratore().getNext3Erlang());
            this.getCalendar().setTempoIO(this.getClock() + this.getIo().getJob().getTempoProcessamento());
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

    public CPU getCpu1() {
        return cpu1;
    }

    public CPU getCpu2() {
        return cpu2;
    }

    public void setCpu1(CPU cpu1) {
        this.cpu1 = cpu1;
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

}