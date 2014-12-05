package it.unimarconi;

import it.unimarconi.beans.CPU;
import it.unimarconi.beans.IO;
import it.unimarconi.beans.Job;
import it.unimarconi.commons.Calendar;
import it.unimarconi.commons.Event;
import it.unimarconi.commons.Generatore;

public class MLS {

    private static CPU cpu;

    private static IO io;

    private static Calendar calendar;

    private static double clock;

    private static Generatore g_arrival;

    private static Generatore g_cpu;

    private static Generatore g_io;

    private static Generatore g_routing;

    public static void main(String[] args) {
        io = new IO();
        cpu = new CPU();
        clock = 0;
        g_routing = new Generatore(61, 7, 15, 12, 1, 0.9);
        g_cpu = new Generatore(5, 3, 5, 0.5);
        g_io = new Generatore(5, 5, 5, 0.5);
        g_arrival = new Generatore(5, 1, 5, 0.033);
        calendar = new Calendar(5000);
        calendar.setT_a(clock + g_arrival.getNextExp());
        scheduler();
    }

    private static void scheduler() {
        while (clock < calendar.getT_end_sim()) {
            Event next = calendar.get_next();
            clock = calendar.get_next_time(next);
            switch (next) {
                case ARRIVAL: arrival(); break;
                case CPU: cpu(); break;
                case IO: io();break;
            }
        }
        end_sim();
    }

    private static void arrival() {
        Job j = new Job();
        j.setProcessing_time(g_cpu.getNextErlang3());
        j.setTempoArrivo(clock);
        if (cpu.isFree()) {
            cpu.setFree(false);
            cpu.setJob(j);
            calendar.setT_cpu(clock + j.getProcessing_time());
        } else {
            cpu.getQ().add(j);
        }
        calendar.setT_a(clock + g_arrival.getNextExp());
    }

    private static void cpu() {
        switch (g_routing.getNextRoute()) {
            case IO:
                if (io.isFree()) {
                    io.setFree(false);
                    io.setJob(cpu.getJob());
                    cpu.setJob(null);
                    cpu.setFree(true);
                    calendar.setT_io(clock + g_io.getNextErlang3());
                } else {
                    io.getQ().add(cpu.getJob());
                    cpu.setJob(null);
                    cpu.setFree(true);
                }
                break;
            case OUT:
                cpu.setFree(true);
                cpu.getJob().setTempoUscita(clock);
                break;
        }
        if (cpu.getQ().size() > 0) {
            cpu.setJob(cpu.getJobFromQ());
            cpu.setFree(false);
            calendar.setT_cpu(clock + g_cpu.getNextErlang3());
        } else {
            calendar.setT_cpu(Double.MAX_VALUE);
            cpu.setFree(true);
        }
    }

    private static void io() {
        if (cpu.isFree()) {
            cpu.setFree(false);
            cpu.setJob(io.getJob());
            io.setJob(null);
            calendar.setT_cpu(clock + g_cpu.getNextErlang3());
        } else {
            cpu.getQ().add(io.getJob());
            io.setJob(null);
            io.setFree(true);
        }
        if (io.getQ().size() > 0) {
            io.setJob(io.getJobFromQ());
            io.setFree(false);
            calendar.setT_io(clock + g_io.getNextErlang3());
        } else {
            calendar.setT_io(Double.MAX_VALUE);
            io.setFree(true);
        }
    }

    private static void end_sim() {

    }

}