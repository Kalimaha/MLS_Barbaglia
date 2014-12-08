package it.unimarconi;

import it.unimarconi.beans.StatisticheSimulazione;
import it.unimarconi.system.SingleCPU;

public class MLS {

    public static void main(String[] args) {
        int time_step = 5;
        double previous_sd = Double.MIN_VALUE;
        double previous_avg = Double.MIN_VALUE;
        int avg_count = 0;
        int sd_count = 0;
        int stop_condition = 10;
        for (int i = time_step * time_step; i <= 1000; i += time_step) {
            StatisticheSimulazione stats = new SingleCPU(i).simula();
            System.out.println("Tempo di fine simulazione: " + i);
            System.out.println(stats);
            System.out.println("Condizione sul tempo medio: " + avg_count);
            System.out.println("Condizione sulla varianza : " + sd_count);
            if (stats.getVarianza() < previous_sd) {
                System.out.println("************* LA VARIANZA DIMINUISCE *************");
                sd_count++;
            } else {
                sd_count--;
                if (sd_count < 0)
                    sd_count = 0;
            }
            if (stats.getTempoMedio() > 0.9 * previous_avg && stats.getTempoMedio() < 1.1 * previous_avg) {
                System.out.println("********** IL TEMPO MEDIO SI STABILIZZA **********");
                avg_count++;
            } else {
                avg_count--;
                if (avg_count < 0)
                    avg_count = 0;
            }
            if (avg_count >= stop_condition && sd_count >= stop_condition) {
                System.out.println();
                System.out.println("****************************************************************");
                System.out.println("*                                                              *");
                System.out.println("*          Sistema polarizzato dopo " + i + " jobs.                  *");
                System.out.println("*                                                              *");
                System.out.println("****************************************************************");
                break;
            } else {
                previous_sd = stats.getVarianza();
                previous_avg = stats.getTempoMedio();
                System.out.println();
            }
        }
    }

}