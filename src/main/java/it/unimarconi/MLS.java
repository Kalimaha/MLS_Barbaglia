package it.unimarconi;

import it.unimarconi.beans.StatisticheSimulazione;
import it.unimarconi.commons.Generatore;
import it.unimarconi.generatori.Generatore3Erlang;
import it.unimarconi.generatori.GeneratoreEsponenziale;
import it.unimarconi.system.DoubleCPU;
import it.unimarconi.system.SingleCPU;
import it.unimarconi.utils.Stats;

import java.util.ArrayList;

public class MLS {

    static int p = 2;

    static int jobTotali = 5;

    static int passo = 1;

    static ArrayList<Double> avgs = new ArrayList<Double>();

    static ArrayList<Double> sds = new ArrayList<Double>();

    static long x0_arrivi = 3;

    static long x0_cpu = 5;

    static long x0_io = 7;

    static long x0_routing = 11;

    public static void testExp() {
        Generatore3Erlang e = new Generatore3Erlang(3, 1, 12, Math.pow(30, 1));
        for (int i = 0 ; i < 10 ; i ++)
            System.out.print(e.getNext3Erlang() + ", ");
        System.out.println();
//        e = new GeneratoreEsponenziale(1220703125, 3, 214748648, Math.pow(30, -1));
//        for (int i = 0 ; i < 10 ; i ++)
//            System.out.print(e.getNextExp() + ", ");
//        System.out.println();
    }

    public static void main(String[] args) {

        ArrayList<Double> tempiUscitaMedi = new ArrayList<Double>();

        for (int i = passo ; i <= jobTotali ; i += passo) {

            for (int j = 1 ; j <= p ; j++) {
                int a = generaA(j);
                SingleCPU singleCPU = new SingleCPU(x0_arrivi, x0_cpu, x0_io, x0_routing, i);

                ArrayList<Double> tempiUscitaSingoloRun = singleCPU.simula();
                double tmp = 0;
                for (Double d : tempiUscitaSingoloRun) {
//                    System.out.println("\t" + d);
                    tmp += d;
                }
                tmp /= tempiUscitaSingoloRun.size();
//                System.out.println("Run " + j + ", jobs: " + tempiUscitaSingoloRun.size() + ", media: " + tmp);
//                System.out.println("\tRun: " + j + " + Media per " + tempiUscitaSingoloRun.size() + " jobs: " + tmp);
                tempiUscitaMedi.add(tmp);

//                System.out.println("Jobs: " + i + "\t\tRun " + j + ": " + mediaTempiUscita);
                x0_arrivi = singleCPU.getGeneratoreArrivi().getX0();
                x0_cpu = singleCPU.getGeneratoreCPU().getX0();
                x0_io = singleCPU.getGeneratoreIO().getX0();
                x0_routing = singleCPU.getGeneratoreRouting().getX0();
//                System.out.println(x0_arrivi + " | " + x0_cpu + " | " + x0_io + " | " + x0_routing);
            }

            double somma = 0.0;
            for (Double d : tempiUscitaMedi) {
//                System.out.println("\ttempiUscitaMedi: " + d);
                somma += d;
            }
//            System.out.println("\tSOMMA: " + somma);
//            System.out.println("\tP: " + p);
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
            tempiUscitaMedi = new ArrayList<Double>();

        }

//        testExp();

//        System.out.println("\n\n\nFINE SIMULAZIONE");

    }

    private static void print(int jobs) {
        System.out.println("Jobs: " + jobs);
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