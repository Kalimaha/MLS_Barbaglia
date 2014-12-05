package it.unimarconi;

import it.unimarconi.beans.CPU;
import it.unimarconi.beans.IO;
import it.unimarconi.beans.Job;
import it.unimarconi.commons.Calendar;
import it.unimarconi.commons.Event;
import it.unimarconi.commons.Generatore;

import java.util.HashMap;

public class MLS {

    private static CPU cpu;

    private static IO io;

    private static Calendar calendar;

    private static double clock;

    private static Generatore g_arrival;

    private static Generatore g_cpu;

    private static Generatore g_io;

    private static Generatore g_routing;

    private static double sum;

    private static double avg;

    private static double jobs;

    private static HashMap<String, Integer> events_map;

    public static void main(String[] args) {
        int time_step = 1000;
        for (int i = time_step ; i <= 1000 ; i += time_step) {
            io = new IO();
            cpu = new CPU();
            clock = 0;
            g_routing = new Generatore(61, 7, 15, 12, 1, 0.9);
            g_cpu = new Generatore(5, 3, 5, 0.5);
            g_io = new Generatore(5, 5, 5, 0.5);
            g_arrival = new Generatore(5, 1, 5, 0.033);
            calendar = new Calendar(i);
            calendar.setTempoArrivo(clock + g_arrival.getNextExp());
            sum = 0;
            avg = 0;
            jobs = 0;
            events_map = new HashMap<String, Integer>();
            events_map.put(Event.ARRIVAL.toString(), 0);
            events_map.put(Event.CPU.toString(), 0);
            events_map.put(Event.IO.toString(), 0);
            events_map.put(Event.END_SIM.toString(), 0);
            events_map.put(Event.OUT.toString(), 0);
            scheduler();
        }
    }

    private static void scheduler() {
        while (clock < calendar.getTempoFineSimulazione()) {
            Event next = calendar.get_next();
            clock = calendar.get_next_time(next);
            events_map.put(next.toString(), 1 + events_map.get(next.toString()));
            switch (next) {
                case ARRIVAL: arrival(); break;
                case CPU: cpu(); break;
                case IO: io();break;
            }
        }
        end_sim();
    }

    private static void arrival() {

        /* Prevedi prossimo tempo di arrivo. */
        calendar.setTempoArrivo(clock + g_arrival.getNextExp());

        /* Crea nuovo job. */
        Job j = new Job();
        j.setTempoProcessamento(g_cpu.getNextErlang3());
        j.setTempoArrivo(clock);

        /* Entra nella CPU se libera... */
        if (cpu.isFree()) {
            cpu.setFree(false);
            cpu.setJob(j);
            calendar.setTempoCPU(clock + j.getTempoProcessamento());
        }

        /* ...altrimenti in coda. */
        else {
            cpu.getQ().add(j);
        }

    }

    private static void cpu() {
        switch (g_routing.getNextRoute()) {
            case IO:
                if (io.isFree()) {
                    io.setFree(false);
                    io.setJob(cpu.getJob());
                    cpu.setJob(null);
                    cpu.setFree(true);
                    calendar.setTempoIO(clock + g_io.getNextErlang3());
                } else {
                    io.getQ().add(cpu.getJob());
                    cpu.setJob(null);
                    cpu.setFree(true);
                }
                break;
            case OUT:
                events_map.put(Event.OUT.toString(), 1 + events_map.get(Event.OUT.toString()));
                cpu.setFree(true);
                cpu.getJob().setTempoUscita(clock);
                sum += cpu.getJob().getTempoJob();
                jobs++;
                break;
        }
        if (cpu.getQ().size() > 0) {
            cpu.setJob(cpu.getJobFromQ());
            cpu.setFree(false);
            calendar.setTempoCPU(clock + g_cpu.getNextErlang3());
        } else {
            calendar.setTempoCPU(Double.MAX_VALUE);
            cpu.setFree(true);
        }
    }

    private static void io() {
        if (cpu.isFree()) {
            cpu.setFree(false);
            cpu.setJob(io.getJob());
            io.setJob(null);
            calendar.setTempoCPU(clock + g_cpu.getNextErlang3());
        } else {
            cpu.getQ().add(io.getJob());
            io.setJob(null);
            io.setFree(true);
        }
        if (io.getQ().size() > 0) {
            io.setJob(io.getJobFromQ());
            io.setFree(false);
            calendar.setTempoIO(clock + g_io.getNextErlang3());
        } else {
            calendar.setTempoIO(Double.MAX_VALUE);
            io.setFree(true);
        }
    }

    private static void end_sim() {
        for (String key : events_map.keySet())
            System.out.println(key + ": " + events_map.get(key));
        System.out.println("Tempo di uscita medio: " + sum / jobs);
        System.out.println("Coda CPU: " + cpu.getQ().size());
        System.out.println("Coda I/O: " + io.getQ().size());
        System.out.println();
    }

}