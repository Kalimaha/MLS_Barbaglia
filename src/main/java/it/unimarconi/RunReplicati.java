package it.unimarconi;

import it.unimarconi.generatori.GeneratoreIntervallo;
import it.unimarconi.system.SingleCPU;
import it.unimarconi.system.SingleCPUConvalida;

import java.util.ArrayList;
import java.util.List;

public class RunReplicati {

    /* Variabili per la simulazione. */
    private int run = 500;

    /* Numero di job da cui considero il sistema stabile. */
    private int n0 = 500;
    private int delta = 3000;

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

        ArrayList<Double> yj = new ArrayList<Double>();
        ArrayList<Double> zj = new ArrayList<Double>();
        ArrayList<Integer> nj = new ArrayList<Integer>();

        for (int i = 1 ; i <= run ; i++) {
            int jobsTotali = (int)g_intervallo.getNextRange();
//            SingleCPU singleCPU = new SingleCPU(seed_arrivi,
//                                                seed_cpu_1, seed_cpu_2, seed_cpu_3,
//                                                seed_io_1,  seed_io_2,  seed_io_3,
//                                                seed_routing,
//                                                jobsTotali);
            SingleCPUConvalida singleCPU = new SingleCPUConvalida(seed_arrivi,
                    seed_cpu_1,
                    seed_io_1,
                    seed_routing,
                    jobsTotali);
            ArrayList<Double> tmp = singleCPU.simulaPerRunReplicati();
            List<Double> l = tmp.subList(n0 - 1, tmp.size() - 1);
            System.out.println("original size: " + tmp.size() + "\t\t\tsize: " + l.size());
            double sum = 0.0;
            double sumsq = 0.0;
            for (Double d : l) {
                sum += d;
                sumsq += Math.pow(d, 2);
            }
            yj.add(sum);
            zj.add(sumsq);
            nj.add(l.size());
        }

        double y_avg = 0.0;
        for (Double d : yj)
            y_avg += d;

        double z_avg = 0.0;
        for (Double d : zj)
            z_avg += d;

        double n_avg = 0.0;
        for (Integer d : nj)
            n_avg += d;

        System.out.println("Run? " + run);
        System.out.println("E(x): " + y_avg / n_avg);
        System.out.println();

        double s211 = 0.0;
        for (Double y : yj)
            s211 += Math.pow(y - y_avg, 2);
        s211 /= (yj.size() - 1);
        System.out.println("s211: " + s211);

        double s222 = 0.0;
        for (Integer n : nj)
            s222 += Math.pow(n - n_avg, 2);
        s222 /= (nj.size() - 1);
        System.out.println("s222: " + s222);

        double s212 = 0.0;
        for (int i = 0 ; i < yj.size() ; i++)
            s212 += (yj.get(i) - y_avg) * (nj.get(i) - n_avg);
        s212 /= (yj.size() - 1);
        System.out.println("s12: " + s212);
        System.out.println();

        double f = y_avg / n_avg;
        System.out.println("f: " + f);
        System.out.println();

        double s2 = s211 - (2 * f * s212) + (f * f *s222);
        System.out.println("s2: " + s2);

        double s = Math.sqrt(s2);
        System.out.println("s: " + s);
        System.out.println();

        double d = s / (n_avg * Math.sqrt(nj.size()));
        System.out.println("d: " + d);
        System.out.println();

        System.out.println("[" + (f - d * 1.645) + ", " + (f + d * 1.645) + "]");
        System.out.println();

    }

}