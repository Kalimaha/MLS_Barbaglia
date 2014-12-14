package it.unimarconi;

import it.unimarconi.beans.Job;
import it.unimarconi.utils.JobComparator;
import junit.framework.TestCase;

import java.util.TreeSet;

public class QTest extends TestCase {

    public void testLIFO() {
        TreeSet<Job> q = new TreeSet<Job>();
        q.add(new Job(4.2));
        q.add(new Job(3.2));
        q.add(new Job(5.2));
        q.add(new Job(2.2));
        q.add(new Job(9.2));
        Job j = q.iterator().next();
        System.out.println(j.getTempoProcessamento());
    }

    public void testSPTF() {
        TreeSet<Job> q = new TreeSet<Job>(new JobComparator());
        q.add(new Job(4.2));
        q.add(new Job(3.2));
        q.add(new Job(5.2));
        q.add(new Job(2.2));
        q.add(new Job(9.2));
        q.add(new Job(1.2));
        q.add(new Job(0.3));
        q.add(new Job(19));
        Job j = q.iterator().next();
        System.out.println(j.getTempoProcessamento());
    }

}