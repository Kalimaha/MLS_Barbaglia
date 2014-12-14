package it.unimarconi;

import it.unimarconi.system.SingleCPU;

import java.util.ArrayList;

public class MLS {

    static int p = 500;

    static int jobTotali = 500;

    static int passo = 10;

    static ArrayList<Double> avgs = new ArrayList<Double>();

    static ArrayList<Double> sds = new ArrayList<Double>();

    static long x0_arrivi = 229;

    static long x0_cpu_1 = 227;
    static long x0_cpu_2 = 233;
    static long x0_cpu_3 = 135;

    static long x0_io_1 = 255;
    static long x0_io_2 = 135;
    static long x0_io_3 = 273;

    static long x0_routing = 227;

    public static void main(String[] args) {

        ArrayList<Double> tempiUscitaMedi = new ArrayList<Double>();

        for (int i = passo ; i <= jobTotali ; i += passo) {

            for (int j = 1 ; j <= p ; j++) {
                int a = generaA(j);
                SingleCPU singleCPU = new SingleCPU(x0_arrivi, x0_cpu_1, x0_cpu_2, x0_cpu_3, x0_io_1, x0_io_2, x0_io_3, x0_routing, i);

                double tmp = singleCPU.simula();
//                System.out.println("Run " + j + ", jobs: " + tempiUscitaSingoloRun.size() + ", media: " + tmp);
//                System.out.println("\tRun: " + j + " + Media per " + tempiUscitaSingoloRun.size() + " jobs: " + tmp);
                tempiUscitaMedi.add(tmp);
//                System.out.println(tmp + "," + (tmp / i) + "," + i + "," + j);
//                System.out.println(tmp);

//                System.out.println("Jobs: " + i + "\t\tRun " + j + ": " + mediaTempiUscita);
                x0_arrivi = singleCPU.getGeneratoreArrivi().getX0();
                x0_cpu_1 = singleCPU.getCpu().getGeneratore().getG1().getX0();
                x0_cpu_2 = singleCPU.getCpu().getGeneratore().getG2().getX0();
                x0_cpu_3 = singleCPU.getCpu().getGeneratore().getG3().getX0();
//                System.out.println("\t\t" + cpu_seeds[0] + " | " + cpu_seeds[1] + " | " + cpu_seeds[2]);
                x0_io_1 = singleCPU.getIo().getGeneratore().getG1().getX0();
                x0_io_2 = singleCPU.getIo().getGeneratore().getG2().getX0();
                x0_io_3 = singleCPU.getIo().getGeneratore().getG3().getX0();
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
            somma = somma / p;
//            System.out.println("\tSOMMA/p: " + somma);
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
            System.out.print("[" + i + "," + (int)avgs.get(i / passo - 1).doubleValue() + "],");
        }
        System.out.println();
        for (int i = passo ; i <= jobs ; i += passo) {
            System.out.print("[" + i + "," + (int)sds.get(i / passo - 1).doubleValue() + "],");
        }
        System.out.println();
        System.out.println();
    }

    private static int generaA(int t) {
        return 8 * t + 5;
    }

}