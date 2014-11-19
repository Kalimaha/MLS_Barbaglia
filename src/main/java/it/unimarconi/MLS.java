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

    private static int free_mcc = 4096;

    private static Calendar calendar;

    private static double clock = 0;

    private static Generatore g_arrival = new Generatore(5, 1, 5, 0.033);

    private static Generatore g_cpu = new Generatore(5, 1, 5, 0.5);

    public static void main(String[] args) {
        calendar = new Calendar();
        calendar.setT_a(clock + g_arrival.getNextExp());
        scheduler();
    }

    private static void scheduler() {
        while (clock < calendar.getT_end_sim()) {
            Event next = calendar.get_next();
            System.out.println("Next Event: " + next);
            clock = calendar.get_next_time(next);
            System.out.println("CLOCK: " + clock);
            switch (next) {
                case ARRIVAL: arrival(); break;
                case CPU: cpu(); break;
                case IO: io();break;
            }
        }
        end_sim();
    }

    private static void arrival() {
        System.out.println("ARRIVAL: START");
        int job_size = 100;
        int priority = 1;
        Job j = new Job();
        if (job_size <= free_mcc) {
            free_mcc -= job_size;
            j.setT_in(clock);
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
        System.out.println("ARRIVAL: END");
    }

    private static void cpu() {
        System.out.println("CPU: START");
        System.out.println("CPU: END");
    }

    private static void io() {
        System.out.println("IO: START");
        System.out.println("IO: END");
    }

    private static void end_sim() {
        System.out.println("END SIMULATION: START");
        System.out.println("END SIMULATION: END");
    }

}