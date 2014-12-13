package it.unimarconi;

import it.unimarconi.beans.StatisticheSimulazione;
import it.unimarconi.commons.Generatore;
import it.unimarconi.system.DoubleCPU;
import it.unimarconi.system.SingleCPU;
import it.unimarconi.utils.Stats;

import java.util.ArrayList;

public class MLS {

    public static void main(String[] args) {

        Generatore generatoreIncrementoJobTotali = new Generatore(5, 1, 5, 0.0, 100.0);
        int p = 200;
        int jobTotali = 1000;

        for (int i = 1 ; i <= p ; i++) {
            int a = generaA(i);
//            SingleCPU cpu = new SingleCPU(a, jobTotali);
            DoubleCPU cpu = new DoubleCPU(a, jobTotali);
//            System.out.println("Run " + i);
            cpu.simula();
        }

    }

    private static int generaA(int t) {
        return 8 * t + 5;
    }

}