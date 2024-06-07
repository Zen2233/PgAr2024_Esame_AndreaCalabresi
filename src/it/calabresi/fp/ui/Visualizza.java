package it.calabresi.fp.ui;

import it.calabresi.fp.model.Carte;
import it.calabresi.fp.model.Player;
import it.calabresi.fp.model.TipoCarte;
import it.kibo.fp.lib.PrettyStrings;

import java.util.ArrayList;

import static it.kibo.fp.lib.PrettyStrings.repeatChar;

public class Visualizza {

    public static void carte(ArrayList<Carte> carte) {
        ArrayList<String> elenco = new ArrayList<>();
        String title = "CARTE IN MANO";
        int lengthMax = title.length();

        StringBuilder list = new StringBuilder();
        for (int i = 0; i < carte.size(); i++) {
            Carte tmp = carte.get(i);
            String text = (i + 1) + ". " + tmp.getNome();

            if (tmp.getTipo().equals(TipoCarte.ARMA)) {
                text += " [Distanza: " + tmp.getDistanza() + "]";
            }

            if (tmp.isEquipaggiato()) {
                text += " [Equipaggiato]";
            }

            int len = text.length();
            if (lengthMax < len) {
                lengthMax = len;
            }

            elenco.add(text);
        }

        for (String tmp: elenco) {
            int len = tmp.length();
            if (lengthMax < len) {
                lengthMax = len;
            }
        }
        lengthMax += 4;

        title = PrettyStrings.frame(title, lengthMax, true, true);

        for (String tmp: elenco) {
            list.append("| " + PrettyStrings.column(tmp, lengthMax - 4) + " |\n");
        }
        list.append(repeatChar('-', lengthMax) + '\n');

        System.out.print(title);
        System.out.print(list);
    }

    //Visualizza tavoloe indica a quale posto è seduto il giocatore interessato
    public static void tavolo(ArrayList<Player> players, Player player) {
        ArrayList<String> elenco = new ArrayList<>();
        String title = "TAVOLO";
        int lengthMax = title.length();

        StringBuilder list = new StringBuilder();
        for (int i = 0; i < players.size(); i++) {
            Player tmp = players.get(i);
            String text = (i + 1) + ". " + tmp.getNome();

            if (tmp.getRuolo().equals("Sceriffo")) {
                text += " [Sceriffo]";
            }

            if (tmp == player) {
                text += " [IO]";
            }

            int len = text.length();
            if (lengthMax < len) {
                lengthMax = len;
            }

            elenco.add(text);
        }

        for (String tmp: elenco) {
            int len = tmp.length();
            if (lengthMax < len) {
                lengthMax = len;
            }
        }
        lengthMax += 4;

        title = PrettyStrings.frame(title, lengthMax, true, true);

        for (String tmp: elenco) {
            list.append("| " + PrettyStrings.column(tmp, lengthMax - 4) + " |\n");
        }
        list.append(repeatChar('-', lengthMax) + '\n');

        System.out.print(title);
        System.out.print(list);
    }

    public static void statusPlayer(Player player) {
        String text = "Nome: " + player.getNome() + " [" + player.getRuolo() + "] | Punti vita: " + player.getPf() + " | Arma equipaggiata: " + player.getArmaEquipaggiata().getNome() + "[" + player.getArmaEquipaggiata().getDistanza() + "]";
        int lengthMax = text.length() + 4;

        text = PrettyStrings.frame(text, lengthMax, true, true);
        System.out.print(text);
    }
}
