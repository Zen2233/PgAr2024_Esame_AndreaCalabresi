package it.calabresi.fp.utils;

import it.calabresi.fp.model.Carte;
import it.calabresi.fp.model.Partita;
import it.calabresi.fp.model.Player;
import it.calabresi.fp.model.TipoCarte;

import java.util.ArrayList;
import java.util.Scanner;

public class FunzioniCarte {
    public static void usaCarta(Carte carta, Carte arma, Partita partita) {

        Scanner scanner = new Scanner(System.in);
        if (carta.getTipo() == TipoCarte.BANG) {
            boolean colpibile = false;
            int distanzaDestra = 0;
            int distanzaSinistra = 0;
            int quantiPlayer = partita.getListaPlayer().size();
            int turno = partita.getTurno();
            int distanza = arma.getDistanza();
            int nPlayer = 0;

            do {
                do {
                    System.out.println("Inserire numero giocatore:");
                    nPlayer = scanner.nextInt();
                } while (nPlayer < 1 && nPlayer > quantiPlayer);
                nPlayer -= 1;

                distanzaDestra = (nPlayer - turno);
                distanzaSinistra = (turno - nPlayer);

                if (distanzaDestra < 0) {
                    distanzaDestra = (quantiPlayer - turno) + nPlayer;
                }

                if (distanzaSinistra < 0) {
                    distanzaSinistra = turno + (quantiPlayer - turno);
                }
            } while (distanzaDestra < distanza && distanzaSinistra < distanza);

            partita.getListaPlayer().get(nPlayer).riceviDanno(1, partita);
        }
    }

    public static void verificaPlayerColpibile() {

    }
}
