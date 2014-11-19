package it.unimarconi;

import it.unimarconi.beans.CPU;
import it.unimarconi.beans.IO;
import it.unimarconi.beans.Job;
import it.unimarconi.commons.Calendar;
import it.unimarconi.commons.Event;
import it.unimarconi.commons.GCM;
import it.unimarconi.commons.Generatore;

import java.util.ArrayList;

public class MLS {

    private static ArrayList<Job> q_mcc = new ArrayList<Job>();

    private static CPU cpu = new CPU();

    private static IO io = new IO();

    private static int free_mcc = 1000000000;

    private static int counter_in = 0;

    private static int counter_out = 0;

    private static Calendar calendar;

    private static double clock = 0;

    private static Generatore g_arrival = new Generatore(5, 1, 5, 0.033);

    private static Generatore g_cpu = new Generatore(5, 3, 5, 0.5);

    private static Generatore g_io = new Generatore(5, 5, 5, 0.5);

    private static Generatore g_routing = new Generatore(61, 7, 15, 12, 1, 0.9);

    public static void main(String[] args) {
        calendar = new Calendar();
        calendar.setT_a(clock + g_arrival.getNextExp());
        scheduler();
    }

    private static void scheduler() {
        while (clock < calendar.getT_end_sim()) {
            Event next = calendar.get_next();
//            System.out.println("Next Event: " + next + "\t\t\t\tCLOCK: " + clock);
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
        int job_size = 100;
        int priority = 1;
        Job j = new Job();
        if (job_size <= free_mcc) {
            free_mcc -= job_size;
            j.setT_in(clock);
            counter_in++;
            j.setPriority(priority);
            j.setJob_size(job_size);
            if (cpu.isIs_free()) {
                cpu.setIs_free(false);
                cpu.setJob(j);
                calendar.setT_cpu(clock + g_cpu.getNextErlang3());
            } else {
                cpu.getQ().add(j);
            }
        } else {
            q_mcc.add(j);
        }
        calendar.setT_a(clock + g_arrival.getNextExp());
    }

    private static void cpu() {
        switch (g_routing.getNextRoute()) {
            case IO:
                if (io.isIs_free()) {
                    io.setIs_free(false);
                    io.setJob(cpu.getJob());
                    cpu.setJob(null);
                    calendar.setT_io(clock + g_io.getNextErlang3());
                } else {
                    io.getQ().add(cpu.getJob());
                    cpu.setJob(null);
                }
                break;
            case OUT:
                cpu.getJob().setT_out(clock);
                free_mcc += cpu.getJob().getJob_size();
                if (q_mcc.size() > 0) {
                    int job_size = 100;
                    int priority = 1;
                    Job j = new Job();
                    free_mcc -= job_size;
                    cpu.getQ().add(j);
                }
                counter_out++;
                System.out.println("Tr: " + (cpu.getJob().getT_out() - cpu.getJob().getT_in()));
                break;
        }
        if (cpu.getQ().size() > 0) {
            cpu.setJob(cpu.getJobFromQ());
            calendar.setT_cpu(clock + g_cpu.getNextErlang3());
        } else {
            calendar.setT_cpu(Double.MAX_VALUE);
            cpu.setIs_free(true);
        }
    }

    private static void io() {
        if (cpu.isIs_free()) {
            cpu.setIs_free(false);
            cpu.setJob(io.getJob());
            io.setJob(null);
            calendar.setT_cpu(clock + g_cpu.getNextErlang3());
        } else {
            cpu.getQ().add(io.getJob());
            io.setJob(null);
        }
        if (io.getQ().size() > 0) {
            io.setJob(io.getJobFromQ());
            calendar.setT_io(clock + g_io.getNextErlang3());
        } else {
            calendar.setT_io(Double.MAX_VALUE);
            io.setIs_free(true);
        }
    }

    private static void end_sim() {
        System.out.println();
        System.out.println("IN : " + counter_in);
        System.out.println("OUT: " + counter_out);
    }

}