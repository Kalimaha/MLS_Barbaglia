package it.unimarconi;

import it.unimarconi.beans.StatisticheSimulazione;
import it.unimarconi.commons.Generatore;
import it.unimarconi.system.DoubleCPU;
import it.unimarconi.system.SingleCPU;
import it.unimarconi.utils.Stats;

import java.util.ArrayList;

public class MLS {

    static int p = 1;
    static int jobTotali = 100;
    static int passo = 100;
    static ArrayList<Double> avgs = new ArrayList<Double>();
    static ArrayList<Double> sds = new ArrayList<Double>();

    public static void main(String[] args) {

        for (int i = passo ; i <= jobTotali ; i += passo) {

            ArrayList<Double> tempiUscitaMedi = new ArrayList<Double>();

            for (int j = 1 ; j <= p ; j++) {
                int a = generaA(j);
                SingleCPU cpu = new SingleCPU(a, i);
                double mediaTempiUscita = cpu.simula().getTempoMedio();
                tempiUscitaMedi.add(mediaTempiUscita);
//                System.out.println("a: " + a + "\t\tJobs: " + i + "\t\tRun " + j + ": " + mediaTempiUscita);
            }

            double somma = 0.0;
            for (Double d : tempiUscitaMedi)
                somma += d;
            somma /= p;
//            System.out.println("Media delle Medie per " + i + " jobs: " + somma);

            double sd = 0.0;
            for (Double d : tempiUscitaMedi)
                sd += Math.pow(d - somma, 2);
            sd /= (p - 1);
//            System.out.println("Deviazione Standard per " + i + " jobs: " + sd);

//            System.out.println(i + ", " + somma + ", " + sd);
            avgs.add(somma);
            sds.add(sd);
//            System.out.print("[" + i + "," + sd + "],");
            print(i);

        }

        System.out.println("\n\n\nFINE SIMULAZIONE");

    }

    private static void print(int jobs) {
        for (int i = passo ; i <= jobs ; i += passo) {
            System.out.print("[" + i + "," + avgs.get(i / passo - 1) + "],");
        }
        System.out.println();
        for (int i = passo ; i <= jobs ; i += passo) {
            System.out.print("[" + i + "," + sds.get(i / passo - 1) + "],");
        }
        System.out.println();
        System.out.println();
    }

    private static int generaA(int t) {
        return 8 * t + 5;
    }

}