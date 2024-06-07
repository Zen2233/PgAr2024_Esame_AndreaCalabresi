package it.calabresi.fp.utils;

import it.calabresi.fp.model.Carte;
import it.calabresi.fp.model.Partita;
import it.calabresi.fp.model.Player;
import it.calabresi.fp.model.TipoCarte;
import it.calabresi.fp.ui.Visualizza;

import java.util.ArrayList;
import java.util.Scanner;

public class FunzioniCarte {
    public static void usaCarta(Carte carta, Player player, Partita partita) {
        Scanner scanner = new Scanner(System.in);
        int quantiPlayer = partita.getListaPlayer().size();
        int turno = partita.getTurno();
        int distanza = 0;
        int nPlayer = 0;
        Player target = null;
        ArrayList<Carte> carteManoTarget = null;
        Carte cartaTarget = null;
        int nCarta = 0;

        int carteMirino = 0;
        int carteMustang = 0;
        for (Carte tmp: player.getCarteMano()) {
            switch (tmp.getTipo()) {
                case TipoCarte.MIRINO:
                    carteMirino += 1;
                case TipoCarte.MUSTANG:
                    carteMustang += 1;
            }
        }

        switch (carta.getTipo()) {
            case TipoCarte.BANG:
                distanza = player.getArmaEquipaggiata().getDistanza();
                distanza = distanza - carteMirino + carteMustang;
                if (distanza < 1) {
                    distanza = 1;
                }

                nPlayer = verificaPlayerColpibile(turno, quantiPlayer, distanza);

                partita.getListaPlayer().get(nPlayer).riceviDanno(1, partita);
                break;

            case TipoCarte.PANICO:
                distanza = 1;
                nPlayer = verificaPlayerColpibile(turno, quantiPlayer, distanza);

                target = partita.getListaPlayer().get(nPlayer);
                carteManoTarget = target.getCarteMano();

                Visualizza.carte(carteManoTarget);
                do {
                    System.out.println(Costanti.TITOLO_INSERIREN_CARTA);
                    nCarta = scanner.nextInt();
                } while (nCarta < 1 && nCarta > carteManoTarget.size());
                nCarta -= 1;

                cartaTarget = target.getCarteMano().get(nCarta);
                target.removeCarta(cartaTarget);
                player.addCarta(cartaTarget);

                break;

            case TipoCarte.CAT_BALOU:
                distanza = quantiPlayer;
                nPlayer = verificaPlayerColpibile(turno, quantiPlayer, distanza);

                target = partita.getListaPlayer().get(nPlayer);
                carteManoTarget = target.getCarteMano();

                Visualizza.carte(carteManoTarget);
                do {
                    System.out.println(Costanti.TITOLO_CARTA_SCARTARE);
                    nCarta = scanner.nextInt();
                } while (nCarta < 1 && nCarta > carteManoTarget.size());
                nCarta -= 1;

                cartaTarget = target.getCarteMano().get(nCarta);
                target.removeCarta(cartaTarget);

                break;
            case TipoCarte.GATLING:
                ArrayList<Player> listaPlayer = partita.getListaPlayer();
                for (Player tmp: listaPlayer) {
                    tmp.riceviDanno(1, partita);
                }

                break;
        }
    }

    public static int verificaPlayerColpibile(int turno, int quantiPlayer, int distanza) {
        Scanner scanner = new Scanner(System.in);
        int nPlayer = 0;
        int distanzaDestra = 0;
        int distanzaSinistra = 0;

        do {
            do {
                System.out.println(Costanti.TITOLO_PLAYER_COLPIBILE);
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

        return nPlayer;
    }
}
