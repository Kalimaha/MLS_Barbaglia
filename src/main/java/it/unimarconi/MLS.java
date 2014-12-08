package it.unimarconi;

import it.unimarconi.system.SingleCPU;

public class MLS {

    public static void main(String[] args) {
        int time_step = 1000;
        for (int i = time_step; i <= 10000; i += time_step) {
            SingleCPU cpu = new SingleCPU(i);
            System.out.println(cpu.simula());
        }
    }

}