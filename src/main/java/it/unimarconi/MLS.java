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

    private static ArrayList<Job> q_mcc;

    private static CPU cpu;

    private static IO io;

    private static int free_mcc;

    private static int counter_in;

    private static int counter_out;

    private static Calendar calendar;

    private static double clock;

    private static Generatore g_arrival;

    private static Generatore g_cpu;

    private static Generatore g_io;

    private static Generatore g_routing;

    private static Generatore g_mcc_size;

    private static double sum;

    private static ArrayList<Double> means = new ArrayList<Double>();

    public static void main(String[] args) {
        for (int i = 1 ; i <= 100 ; i++) {
            q_mcc = new ArrayList<Job>();
            io = new IO();
            cpu = new CPU();
            counter_in = 0;
            free_mcc = 1024;
            clock = 0;
            counter_out = 0;
            sum = 0;
            g_mcc_size = new Generatore(5, 1, 5, 0, free_mcc);
            g_routing = new Generatore(61, 7, 15, 12, 1, 0.9);
            g_cpu = new Generatore(5, 3, 5, 0.5);
            g_io = new Generatore(5, 5, 5, 0.5);
            g_arrival = new Generatore(5, 1, 5, 0.033);
            calendar = new Calendar(i * 10);
            calendar.setT_a(clock + g_arrival.getNextExp());
            scheduler();
        }
        for (int i = 0; i < means.size(); i++) {
            System.out.println(means.get(i));

        }
    }

    private static void scheduler() {
        while (clock < calendar.getT_end_sim()) {
            Event next = calendar.get_next();
//            System.out.println("Next Event: " + next + "\t\t\t\tCLOCK: " + clock);
            clock = calendar.get_next_time(next);
//            System.out.println(next + ": " + clock);
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
        j.setJob_size( g_mcc_size.getNextRange());
        if (j.getJob_size() <= free_mcc) {
            j.setProcessing_time(g_cpu.getNextErlang3());
            free_mcc -= j.getJob_size();
            j.setT_in(clock);
            counter_in++;

            if (cpu.isIs_free()) {
                cpu.setIs_free(false);
                cpu.setJob(j);
                calendar.setT_cpu(clock + j.getProcessing_time());
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
                    cpu.setIs_free(true);
                    calendar.setT_io(clock + g_io.getNextErlang3());
                } else {
                    io.getQ().add(cpu.getJob());
                    cpu.setJob(null);
                    cpu.setIs_free(true);
                }
                break;
            case OUT:
                cpu.setIs_free(true);
                cpu.getJob().setT_out(clock);
                free_mcc += cpu.getJob().getJob_size();
                if (q_mcc.size() > 0) {
                    for(int i=0; i < q_mcc.size(); i++) {
                        Job j = q_mcc.get(i);
                        if ( free_mcc >= j.getJob_size()) {
                            q_mcc.remove(i);
                            j.setT_in(clock);
                            free_mcc -= j.getJob_size();
                            j.setProcessing_time(g_cpu.getNextErlang3());
                            cpu.getQ().add(j);
                            counter_in++;
                        }
                    }
                }
                counter_out++;

                sum += cpu.getJob().getT_out() - cpu.getJob().getT_in();


//                System.out.println("Tr: " + (cpu.getJob().getT_out() - cpu.getJob().getT_in()));
//                System.out.println("\tIN: " + cpu.getJob().getT_in() + ", OUT: " + cpu.getJob().getT_out());
//                System.out.println();
                break;
        }
        if (cpu.getQ().size() > 0) {
            cpu.setJob(cpu.getJobFromQ());
            cpu.setIs_free(false);
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
            io.setIs_free(true);
        }
        if (io.getQ().size() > 0) {
            io.setJob(io.getJobFromQ());
            io.setIs_free(false);
            calendar.setT_io(clock + g_io.getNextErlang3());
        } else {
            calendar.setT_io(Double.MAX_VALUE);
            io.setIs_free(true);
        }
    }

    private static void end_sim() {
//        System.out.println();
//        System.out.println("IN : " + counter_in);
//        System.out.println("OUT: " + counter_out);
        System.out.println("mean: " + sum / counter_out);
        System.out.println("");
        means.add(sum/counter_out);
    }

}