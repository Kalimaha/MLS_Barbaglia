package it.unimarconi;

public class MLS {

    public static void main(String[] args) {

        System.out.println("Sistema");
        new RimozionePolarizzazioneIniziale().eseguiSimulazione();

//        System.out.println("Convalida");
//        new RimozionePolarizzazioneIniziale().eseguiSimulazioneConvalida();

        System.out.println("Run Replicati");
        new RunReplicati().eseguiSimulazione();

//        System.out.println("Doppia CPU");
//        new RimozionePolarizzazioneIniziale().eseguiSimulazioneDoppiaCPU();

    }

}

