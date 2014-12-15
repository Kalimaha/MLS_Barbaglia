package it.unimarconi;

import it.unimarconi.generatori.GeneratoreIntervallo;
import it.unimarconi.system.SingleCPU;

import java.util.ArrayList;

public class RunReplicati {

    /* Variabili per la simulazione. */
    private int run = 500;

    /* Numero di job da cui considero il sistema stabile. */
    private int n0 = 320;
    private int delta = 100;

    /* Vettori per le statistiche. */
    private ArrayList<Double> avgs = new ArrayList<Double>();
    private ArrayList<Double> sds = new ArrayList<Double>();

    /* Seed arrivi e routing. */
    private long seed_arrivi = 229;
    private long seed_routing = 227;

    /* Seed per il generatore di intervallo. */
    private long seed_range = 255;

    /* Seed per la CPU. */
    private long seed_cpu_1 = 227;
    private long seed_cpu_2 = 233;
    private long seed_cpu_3 = 135;

    /* Seed per I/O. */
    private long seed_io_1 = 255;
    private long seed_io_2 = 135;
    private long seed_io_3 = 273;

    public void eseguiSimulazione() {

        GeneratoreIntervallo g_intervallo = new GeneratoreIntervallo(seed_range, n0, n0 + delta);

        ArrayList<Double> sums = new ArrayList<Double>();
        ArrayList<Integer> ns = new ArrayList<Integer>();

        for (int i = 1 ; i <= run ; i++) {
            int jobsTotali = (int)g_intervallo.getNextRange();
            SingleCPU singleCPU = new SingleCPU(seed_arrivi,
                                                seed_cpu_1, seed_cpu_2, seed_cpu_3,
                                                seed_io_1,  seed_io_2,  seed_io_3,
                                                seed_routing,
                                                jobsTotali);
            ArrayList<Double> l = singleCPU.simulaPerRunReplicati();
            System.out.print(jobsTotali + ", ");
//            System.out.println(l.size() + " tempi [" + jobsTotali + "]");
            double sum = 0.0;
            double sumsq = 0.0;
            for (Double d : l) {
                sum += d;
                sumsq += Math.pow(d, 2);
            }
            sums.add(sum);
            ns.add(l.size());
        }
        System.out.println();

        double sum1 = 0.0;
        for (Double d : sums)
            sum1 += d;
        System.out.println(sum1 + " for " + sums.size());

        double sum2 = 0.0;
        for (Integer d : ns)
            sum2 += d;
        System.out.println(sum2 + " for " + ns.size());

        System.out.println("Run? " + run);
        System.out.println(sum1 / sum2);

    }

}