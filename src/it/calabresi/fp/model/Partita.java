package it.calabresi.fp.model;

import it.calabresi.fp.ui.Visualizza;
import it.calabresi.fp.utils.Costanti;
import it.calabresi.fp.utils.LeggiFile;

import java.util.*;

public class Partita {
    private int nPlayer;
    private ArrayList<Player> listaPlayer = new ArrayList<>();
    private int turno = 0;

    private Stack<Carte> mazzo = new Stack<>();
    private ArrayList<Carte> carteScartate = new ArrayList<>();

    private ArrayList<Carte> tipologiaCarte = new ArrayList<>();
    private ArrayList<String> ruoli = new ArrayList<>();

    public Partita(int nPlayer) {
        this.nPlayer = nPlayer;
    }

    public void inizializzazione() {
        LeggiFile.getInformazioni(this);

        //Settaggio Ruoli
        int[] nRuoli = new int[ruoli.size()];
        for (int i = 0; i < nRuoli.length; i++) {
            nRuoli[i] = 0;
        }

        nRuoli[0] = 1;  //Sceriffo
        nRuoli[1] = 2;  //FuoriLegge
        nRuoli[2] = 0;  //Vice
        nRuoli[3] = 1;  //Rinnegato

        if (nPlayer >= 5) {
            nRuoli[2] += 1;
        }
        if (nPlayer >= 6) {
            nRuoli[1] += 1;
        }
        if (nPlayer >= 7) {
            nRuoli[2] += 1;
        }

        for (Player tmp: listaPlayer) {
            for (int i = 0; i < nRuoli.length; i++) {
                if (nRuoli[i] > 0) {
                    nRuoli[i] -= 1;
                    tmp.setRuolo(ruoli.get(i));
                    break;
                }
            }
        }

        for (int i = listaPlayer.size() - 1; i >= 0; i--) {
            if (Objects.equals(listaPlayer.get(i).getRuolo(), "")) {
                listaPlayer.remove(i);
            }
        }

        Player sceriffo = listaPlayer.getFirst();
        Collections.shuffle(listaPlayer);

        for (int i = 0; i < listaPlayer.size(); i++) {
            if (listaPlayer.get(i) == sceriffo) {
                listaPlayer.remove(listaPlayer.get(i));
            }
        }
        listaPlayer.addFirst(sceriffo);

        //Imposta punti vita dei vari giocatori
        for (Player tmp: listaPlayer) {
            if (tmp == sceriffo) {
                tmp.setPf(5);
            } else {
                tmp.setPf(4);
            }
        }

        //Pescaggio iniziale
        for (Player tmp: listaPlayer) {
            this.pescaCarte(tmp, tmp.getPf());
        }
    }

    public void turno() {
        Player player = listaPlayer.get(turno);

        Visualizza.statusPlayer(player);
        this.pescaCarte(player, 2);
        player.giocaCarte(this);

        turno += 1;
        if (turno >= listaPlayer.size()) {
            turno = 0;
        }
    }

    public void pescaCarte(Player player, int n) {
        if (mazzo.size() < n) {
            for (Carte tmp: mazzo) {
                addCartaScarto(mazzo.pop());
            }

            Collections.shuffle(carteScartate);
            for (Carte tmp : carteScartate) {
                this.mazzo.push(tmp);
            }

            carteScartate.clear();
        }

        for (int i = 1; i <= n; i++) {
            Carte cartaPescata = mazzo.pop();
            player.addCarta(cartaPescata);
        }
    }

    public void setMazzo(ArrayList<Carte> mazzo,    ArrayList<Carte> armi) {
        for (int i = mazzo.size() - 1; i >= 0; i--) {
            if (mazzo.get(i).getTipo() == TipoCarte.ALTRO) {
                mazzo.remove(mazzo.get(i));
            }
        }

        do {
            Collections.shuffle(mazzo);

            for (Carte tmp : mazzo) {
                if (this.mazzo.size() < Costanti.NUMERO_CARTE_NO_EQUIP) {
                    this.mazzo.push(tmp);
                } else {
                    break;
                }
            }
        } while (this.mazzo.size() < Costanti.NUMERO_CARTE_NO_EQUIP);

        for (Carte tmp : armi) {
            this.mazzo.push(tmp);
        }
        Collections.shuffle(mazzo, new Random());
    }

    public void addRuolo(String ruolo) {
        ruoli.add(ruolo);
    }

    public void setListaPlayer(ArrayList<Player> listaPlayer) {
        this.listaPlayer = listaPlayer;
    }

    public void addCartaScarto(Carte carta) {
        carteScartate.add(carta);
    }

    public ArrayList<Player> getListaPlayer() {
        return listaPlayer;
    }

    public int getTurno() {
        return turno;
    }

    public void removePlayer(Player player) {
        System.out.printf(Costanti.MESSAGGIO_MORTE_PLAYER, player.getNome(), player.getRuolo());

        for (Carte tmp: player.getCarteMano()) {
            addCartaScarto(tmp);
        }

        Player giocatoreTurno = listaPlayer.get(turno);
        if (player.getRuolo() == "Fuorilegge") {
            this.pescaCarte(giocatoreTurno, 3);
        }

        if (player.getRuolo() == "Vice" && giocatoreTurno.getRuolo() == "Sceriffo") {
            for (Carte tmp: giocatoreTurno.getCarteMano()) {
                addCartaScarto(tmp);
                giocatoreTurno.removeCarta(tmp);
            }
        }

        listaPlayer.remove(player);
    }

    public boolean verificaCondizioniFine() {
        int n_sceriffi = 0;
        int n_rinnegati = 0;
        int n_fuorilegge = 0;

        for (Player tmp: listaPlayer) {
            switch(tmp.getRuolo()) {
                case "Sceriffo":
                    n_sceriffi += 1;
                    break;

                case "Rinnegato":
                    n_rinnegati += 1;
                    break;

                case "Fuorilegge":
                    n_fuorilegge += 1;
                    break;
            }
        }

        if (n_sceriffi == 0) {
            if (n_fuorilegge == 0) {
                System.out.println("Vince il rinnegato!");
            } else {
                System.out.println("Vincono i fuorilegge!");
            }

            return true;    //Partita finisce
        }

        if (n_fuorilegge == 0 && n_rinnegati == 0) {
            System.out.println("Vincono lo sceriffo e il vice-sceriffo!");
            return true;    //Partita finisce
        }

        return false;   //Partita continua
    }

    public int getnPlayer() {
        return nPlayer;
    }
}
