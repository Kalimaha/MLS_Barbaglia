package it.unimarconi;

import it.unimarconi.beans.Job;

import java.util.ArrayList;

public class MLS {

    private ArrayList<Job> q_mcc = new ArrayList<Job>();

    private ArrayList<Job> q_cpu = new ArrayList<Job>();

    private ArrayList<Job> q_io = new ArrayList<Job>();

    private boolean is_cpu_busy = false;

    private boolean is_io_busy = false;

    private int free_mcc = 4096;

    public static void main(String[] args) {

    }

}