package it.unimarconi;

import it.unimarconi.system.DoubleCPU;
import it.unimarconi.system.SingleCPU;
import it.unimarconi.system.SingleCPUConvalida;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RimozionePolarizzazioneIniziale {

    /* Variabili per la simulazione. */
    private int run = 1;
    private int jobTotali = 1;
    private int passo = 1;

    /* Vettori per le statistiche. */
    private ArrayList<Double> avgs = new ArrayList<Double>();
    private ArrayList<Double> sds = new ArrayList<Double>();

    /* Seed arrivi e routing. */
    private long seed_arrivi = 229;
    private long seed_routing = 227;
    private long seed_routing_cpu = 135;

    /* Seed per la CPU. */
    private long seed_cpu_1 = 233;
    private long seed_cpu_2 = 135;
    private long seed_cpu_3 = 179;

    /* Seed per I/O. */
    private long seed_io_1 = 255;
    private long seed_io_2 = 135;
    private long seed_io_3 = 273;

    public void eseguiSimulazione() {

        /* Inizializzazione vettore. */
        ArrayList<Double> tempiUscitaMedi = new ArrayList<Double>();

        /* Cicla sui job. */
        for (int i = passo ; i <= jobTotali ; i += passo) {

//            seed_arrivi = 229;
//            seed_routing = 227;
//            seed_routing_cpu = 135;
//            seed_cpu_1 = 233;
//            seed_cpu_2 = 135;
//            seed_cpu_3 = 179;
//            seed_io_1 = 255;
//            seed_io_2 = 135;
//            seed_io_3 = 273;

            /* Cicla sui run. */
            for (int j = 1 ; j <= run ; j++) {

                /* Genera un sistema a singola CPU e registra la media dei tempi dei job. */
                SingleCPU singleCPU = new SingleCPU(seed_arrivi,
                                                    seed_cpu_1, seed_cpu_2, seed_cpu_3,
                                                    seed_io_1,  seed_io_2,  seed_io_3,
                                                    seed_routing,
                                                    i);
                tempiUscitaMedi.add(singleCPU.simula());

                /* Aggiornamento dei seeds. */
                seed_arrivi = singleCPU.getGeneratoreArrivi().getX0();
                seed_cpu_1 = singleCPU.getCpu().getGeneratore().getG1().getX0();
                seed_cpu_2 = singleCPU.getCpu().getGeneratore().getG2().getX0();
                seed_cpu_3 = singleCPU.getCpu().getGeneratore().getG3().getX0();
                seed_io_1 = singleCPU.getIo().getGeneratore().getG1().getX0();
                seed_io_2 = singleCPU.getIo().getGeneratore().getG2().getX0();
                seed_io_3 = singleCPU.getIo().getGeneratore().getG3().getX0();
                seed_routing = singleCPU.getGeneratoreRouting().getX0();

            }

            /* Calcolo della media sperimentale. */
            double somma = 0.0;
            for (Double d : tempiUscitaMedi)
                somma += d;
            somma = somma / run;

            /* Calcolo della deviazione standard sperimentale. */
            double sd = 0.0;
            for (Double d : tempiUscitaMedi)
                sd += Math.pow(d - somma, 2);
            sd /= (run - 1);

            /* Registraione dei valori. */
            avgs.add(somma);
            sds.add(sd);

            /* Reset del vettore. */
            tempiUscitaMedi = new ArrayList<Double>();

        }

        /* Stampa i vettori per i grafici. */
        print(jobTotali);

    }

    public void eseguiSimulazioneDoppiaCPU() {

        /* Inizializzazione vettore. */
        ArrayList<Double> tempiUscitaMedi = new ArrayList<Double>();

        /* Cicla sui job. */
        for (int i = passo ; i <= jobTotali ; i += passo) {

            System.out.println(i + " job totali...");

            /* Cicla sui run. */
            for (int j = 1 ; j <= run ; j++) {

                /* Genera un sistema a singola CPU e registra la media dei tempi dei job. */
                DoubleCPU doubleCPU = new DoubleCPU(seed_arrivi,
                                                    seed_cpu_1, seed_cpu_2, seed_cpu_3,
                                                    seed_io_1,  seed_io_2,  seed_io_3,
                                                    seed_routing,
                                                    seed_routing_cpu,
                                                    i);
                tempiUscitaMedi.add(doubleCPU.simula());

                /* Aggiornamento dei seeds. */
                seed_arrivi = doubleCPU.getGeneratoreArrivi().getX0();
                seed_cpu_1 = doubleCPU.getGeneratoreCPU().getG1().getX0();
                seed_cpu_2 = doubleCPU.getGeneratoreCPU().getG2().getX0();
                seed_cpu_3 = doubleCPU.getGeneratoreCPU().getG3().getX0();
                seed_io_1 = doubleCPU.getIo().getGeneratore().getG1().getX0();
                seed_io_2 = doubleCPU.getIo().getGeneratore().getG2().getX0();
                seed_io_3 = doubleCPU.getIo().getGeneratore().getG3().getX0();
                seed_routing = doubleCPU.getGeneratoreRouting().getX0();

            }

            /* Calcolo della media sperimentale. */
            double somma = 0.0;
            for (Double d : tempiUscitaMedi)
                somma += d;
            somma = somma / run;

            /* Calcolo della deviazione standard sperimentale. */
            double sd = 0.0;
            for (Double d : tempiUscitaMedi)
                sd += Math.pow(d - somma, 2);
            sd /= (run - 1);

            /* Registraione dei valori. */
            avgs.add(somma);
            sds.add(sd);

            /* Reset del vettore. */
            tempiUscitaMedi = new ArrayList<Double>();

        }

        /* Stampa i vettori per i grafici. */
        print(jobTotali);

    }

    public void eseguiSimulazioneConvalida() {

        /* Inizializzazione vettore. */
        ArrayList<Double> tempiUscitaMedi = new ArrayList<Double>();

        /* Cicla sui job. */
        for (int i = passo ; i <= jobTotali ; i += passo) {

            seed_arrivi = 229;
            seed_routing = 227;
            seed_routing_cpu = 135;
            seed_cpu_1 = 233;
            seed_cpu_2 = 135;
            seed_io_1 = 255;
            seed_io_2 = 135;
            seed_io_3 = 273;

            /* Cicla sui run. */
            for (int j = 1 ; j <= run ; j++) {

                /* Genera un sistema a singola CPU e registra la media dei tempi dei job. */
                SingleCPUConvalida singleCPU = new SingleCPUConvalida(seed_arrivi,
                                                                      seed_cpu_1,
                                                                      seed_io_1,
                                                                      seed_routing,
                                                                      i);
                tempiUscitaMedi.add(singleCPU.simula());

                /* Aggiornamento dei seeds. */
                seed_arrivi = singleCPU.getGeneratoreArrivi().getX0();
                seed_cpu_1 = singleCPU.getCpu().getGeneratore().getX0();
                seed_io_1 = singleCPU.getIo().getGeneratore().getX0();
                seed_routing = singleCPU.getGeneratoreRouting().getX0();

            }

            /* Calcolo della media sperimentale. */
            double somma = 0.0;
            for (Double d : tempiUscitaMedi)
                somma += d;
            somma = somma / run;

            /* Calcolo della deviazione standard sperimentale. */
            double sd = 0.0;
            for (Double d : tempiUscitaMedi)
                sd += Math.pow(d - somma, 2);
            sd /= (run - 1);

            /* Registraione dei valori. */
            avgs.add(somma);
            sds.add(sd);

            /* Reset del vettore. */
            tempiUscitaMedi = new ArrayList<Double>();

        }

        /* Stampa i vettori per i grafici. */
        print(jobTotali);

    }

    private void print(int jobs) {
        System.out.println("Jobs: " + jobs);
        DecimalFormat df = new DecimalFormat("####0.00");
        for (int i = passo ; i <= jobs ; i += passo) {
            System.out.print("[" + i + "," + df.format(avgs.get(i / passo - 1)) + "],");
        }
        System.out.println();
        for (int i = passo ; i <= jobs ; i += passo) {
            System.out.print("[" + i + "," + df.format(sds.get(i / passo - 1)) + "],");
        }
        System.out.println();
        System.out.println();
    }

}