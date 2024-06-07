package it.calabresi.fp.main;

import it.calabresi.fp.model.Partita;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int nPlayer;

        do {
            System.out.println("Inserire numero giocatori (tra 4 e 7):");
            nPlayer = scanner.nextInt();
        } while (nPlayer < 4 && nPlayer > 7);

        Partita partita = new Partita(nPlayer);
        partita.inizializzazione();

        boolean condizioniPartita = false;
        do {
            partita.turno();
            condizioniPartita = partita.verificaCondizioniFine();
        } while(!condizioniPartita);
    }
}
