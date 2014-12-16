package it.unimarconi;

import it.unimarconi.beans.Job;
import it.unimarconi.generatori.Generatore3Erlang;
import it.unimarconi.generatori.GeneratorePoissoniano;
import it.unimarconi.utils.JobComparator;
import it.unimarconi.utils.Stats;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class QTest extends TestCase {

    private long seed_cpu_1 = 233;

    private long seed_cpu_2 = 135;

    private long seed_cpu_3 = 179;

    public void _testLIFO() {
        TreeSet<Job> q = new TreeSet<Job>();
        q.add(new Job(4.2));
        q.add(new Job(3.2));
        q.add(new Job(5.2));
        q.add(new Job(2.2));
        q.add(new Job(9.2));
        Job j = q.iterator().next();
        System.out.println(j.getTempoProcessamento());
    }

    public void _testSPTF() {

        Generatore3Erlang g = new Generatore3Erlang(seed_cpu_1, seed_cpu_2, seed_cpu_3, 2);
        TreeSet<Job> q = new TreeSet<Job>(new JobComparator());
        for (int i = 0 ; i < 10 ; i++) {
            double next = g.getNext3Erlang();
            Job j = new Job();
            j.setTempoProcessamento(next);
            q.add(j);
            System.out.println(next);
        }
        System.out.println();
        for (int i = 0 ; i < 10 ; i++) {
            Job j = q.iterator().next();
            System.out.println("Remove: " + j.getTempoProcessamento());
            q.remove(j);
        }

    }

    public void testPoisson() {
        GeneratorePoissoniano g = new GeneratorePoissoniano(135, 30);
        ArrayList<Double> ns = new ArrayList<Double>();
        for (int i = 0 ; i < 1000 ; i++) {
            double d = g.getNextPoisson();
            ns.add(d);
            System.out.print(d + ", ");
        }
        System.out.println();
        System.out.println(Stats.media(ns));
    }

}