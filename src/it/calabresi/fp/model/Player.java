package it.calabresi.fp.model;

import it.calabresi.fp.ui.Visualizza;
import it.calabresi.fp.utils.Costanti;
import it.calabresi.fp.utils.FunzioniCarte;
import it.kibo.fp.lib.Menu;

import java.util.ArrayList;
import java.util.Scanner;

import static javax.swing.UIManager.get;

public class Player {
    private String nome;
    private String ruolo = "";
    private String descrizione;
    private int pf;
    private ArrayList<Carte> carteMano = new ArrayList<>();
    private Carte cartaEquipaggiata = new Carte("Colt-45", "", true, "", "", TipoCarte.ARMA, 1);

    public Player(String nome, String descrizione, int pf) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.pf = pf;
    }

    public void giocaCarte(Partita partita) {
        Scanner scanner = new Scanner(System.in);
        int nCarta = 0;
        int condizioneBang = 1;

        do {
            Visualizza.carte(carteMano);
            do {
                System.out.println("Inserire numero carta (0 per fine turno):");
                nCarta = scanner.nextInt();
            } while (nCarta < 0 && nCarta > carteMano.size());

            if (nCarta == 0) {
                break;
            }
            nCarta -= 1;

            Carte carta = carteMano.get(nCarta);

            switch(carta.getTipo()) {
                case TipoCarte.BANG:
                    if (condizioneBang > 0) {
                        if (cartaEquipaggiata.getDistanza() <= partita.getnPlayer()) {
                            condizioneBang -= 1;
                            partita.addCartaScarto(carta);
                            carteMano.remove(nCarta);

                            partita.addCartaScarto(cartaEquipaggiata);
                            carteMano.remove(cartaEquipaggiata);

                            Visualizza.tavolo(partita.getListaPlayer(), this);
                            FunzioniCarte.usaCarta(carta, this, partita);
                            cartaEquipaggiata = new Carte("Colt-45", "", true, "", "", TipoCarte.ARMA, 1);
                        }
                    }
                    break;
                case TipoCarte.ARMA:
                    if (carta.getTipo().equals("Colt-45")) {
                        partita.addCartaScarto(carta);
                    }
                    cartaEquipaggiata = carta;

                    carteMano.remove(nCarta);
                    Visualizza.statusPlayer(this);
                    break;
                case TipoCarte.BIRRA:
                    pf += 1;
                    partita.addCartaScarto(carta);
                    carteMano.remove(nCarta);
                    break;
                case TipoCarte.SALOON:
                    for (Player tmp: partita.getListaPlayer()) {
                        tmp.addPf(1);
                    }
                    partita.addCartaScarto(carta);
                    carteMano.remove(nCarta);
                    break;
                case TipoCarte.DILIGENZA:
                    partita.addCartaScarto(carta);
                    carteMano.remove(nCarta);

                    partita.pescaCarte(this, 2);
                    break;
                case TipoCarte.WELLS_FARGO:
                    partita.addCartaScarto(carta);
                    carteMano.remove(nCarta);

                    partita.pescaCarte(this, 3);
                    break;
                case TipoCarte.PANICO:
                    partita.addCartaScarto(carta);
                    carteMano.remove(nCarta);

                    FunzioniCarte.usaCarta(carta, this, partita);
                    break;
                case TipoCarte.CAT_BALOU:
                    partita.addCartaScarto(carta);
                    carteMano.remove(nCarta);

                    FunzioniCarte.usaCarta(carta, this, partita);
                    break;
                case TipoCarte.GATLING:
                    partita.addCartaScarto(carta);
                    carteMano.remove(nCarta);

                    FunzioniCarte.usaCarta(carta, this, partita);
                    break;
                case TipoCarte.MIRINO: case TipoCarte.MUSTANG:
                    carta.updateEquipaggiato();
                    break;
            }
        } while(nCarta >= 0);
    }

    public int getPf() {
        return pf;
    }

    public void addPf(int n) {
        this.pf += n;
    }

    public void setPf(int pf) {
        this.pf = pf;
    }

    public void riceviDanno(int danno, Partita partita) {

        Carte cartaMancato = null;
        Carte cartaBirra = null;
        Carte cartaBarile = null;
        for (Carte tmp: carteMano) {
            if (tmp.getTipo() == TipoCarte.MANCATO) {
                cartaMancato = tmp;
                break;
            }

            if (tmp.getTipo() == TipoCarte.BIRRA) {
                cartaBirra = tmp;
                break;
            }

            if (tmp.getTipo() == TipoCarte.BARILE) {
                cartaBarile = tmp;
                break;
            }
        }

        int scelta = 0;
        if (cartaMancato != null) {
            Visualizza.statusPlayer(this);
            Menu menu = new Menu (Costanti.TITOLO_USOMANCATO, Costanti.OPZIONI_USOMANCATO, false, true, true);

            scelta = menu.choose();
        }

        if (cartaBarile != null) {
            Visualizza.statusPlayer(this);
            Menu menu = new Menu ("Vuoi usare carta Barile?", Costanti.OPZIONI_USOMANCATO, false, true, true);

            scelta = menu.choose();

            partita.pescaCarte(this, 1);
            Carte cartaPescata = getCarteMano().getLast();

            if (cartaPescata.getSeme().equals("CUORI")) {
                removeCarta(cartaPescata);
                partita.addCartaScarto(cartaPescata);
            } else {
                scelta = 0;
            }
        }

        if (scelta == 2) {
            if (cartaMancato != null) {
                partita.addCartaScarto(cartaMancato);
                carteMano.remove(cartaMancato);
            } else {
                partita.addCartaScarto(cartaBarile);
                carteMano.remove(cartaBarile);
            }
        } else {
                pf -= danno;
                if (pf <= 0) {
                    if (cartaBirra == null) {
                        partita.removePlayer(this);
                    } else {
                        pf = 1;
                        partita.addCartaScarto(cartaBirra);
                        carteMano.remove(cartaBirra);
                    }
                }
        }
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public void addCarta(Carte carta) {
        carteMano.add(carta);
    }

    public ArrayList<Carte> getCarteMano() {
        return carteMano;
    }

    public void removeCarta(Carte carta) {
        carteMano.remove(carta);
    }

    public Carte getArmaEquipaggiata() {
        return cartaEquipaggiata;
    }
}
