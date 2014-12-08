package it.unimarconi;

import it.unimarconi.beans.StatisticheSimulazione;
import it.unimarconi.system.SingleCPU;

public class MLS {

    public static void main(String[] args) {
        int time_step = 500;
        double previous_sd = Double.MIN_VALUE;
        double previous_avg = Double.MIN_VALUE;
        for (int i = time_step; i <= 6000; i += time_step) {
            StatisticheSimulazione stats = new SingleCPU(i).simula();
            System.out.println("Tempo di fine simulazione: " + i);
            System.out.println(stats);
            if (stats.getVarianza() < previous_sd)
                System.out.println("************* LA VARIANZA DIMINUISCE *************");
            if (stats.getTempoMedio() > 0.9 * previous_avg && stats.getTempoMedio() < 1.1 * previous_avg)
                System.out.println("********** IL TEMPO MEDIO SI STABILIZZA **********");
            previous_sd = stats.getVarianza();
            previous_avg = stats.getTempoMedio();
            System.out.println();
        }
    }

}