package it.unimarconi;

import it.unimarconi.beans.StatisticheSimulazione;
import it.unimarconi.commons.Generatore;
import it.unimarconi.system.SingleCPU;
import it.unimarconi.utils.Stats;

import java.util.ArrayList;

public class MLS {

    public static void main(String[] args) {
        Generatore generatoreIncrementoJobTotali = new Generatore(5, 1, 5, 0.0, 100.0);
        ArrayList<Double> medie = new ArrayList<Double>();
        int jobTotali = 100;
        for (int i = 0 ; i < 25 ; i++) {
            double incremento = (double)generatoreIncrementoJobTotali.getNextRange();
            jobTotali += incremento;
            double mediaDelleMedie = eseguiRun(jobTotali);
            medie.add(mediaDelleMedie);
        }
//        double media = 0;
//        for (Double d : medie)
//            media += d;
//        media /= 10;
//        System.out.println("MEDIA: " + media);
//        double sd = 0;
//        for (Double d : medie)
//            sd += Math.pow(d - media, 2);
//        sd /= 10 - 1;
//        System.out.printf("SD: " + sd);
    }

    public static double eseguiRun(int jobTotali) {
        int p = 30;
        ArrayList<Double> medie = new ArrayList<Double>();
        for (int i = 0 ; i < p ; i++) {
            int a = generaA(i);
//            System.out.print("Job Totali: " + jobTotali + "\t\t\tParametro a: " + a + "\t\t\t\t");
            SingleCPU cpu = new SingleCPU(a, jobTotali);
            StatisticheSimulazione stats = cpu.simula();
            double tempoMedio = stats.getTempoMedio();
            medie.add(tempoMedio);
//            System.out.println("Tempo Medio: " + tempoMedio);
        }
        double mediaDelleMedie = 0;
        for (Double d : medie)
            mediaDelleMedie += d;
        mediaDelleMedie /= p;
        double sd = 0;
        for (Double d : medie)
            sd += Math.pow(d - mediaDelleMedie, 2);
        sd /= p - 1;
        System.out.println("Job Totali: " + jobTotali + "\t\tMedia delle medie: " + (mediaDelleMedie / 30) + "\t\t\tSD: " + sd);
        return mediaDelleMedie;
    }

    private static int generaA(int t) {
        return 8 * t + 5;
    }

}