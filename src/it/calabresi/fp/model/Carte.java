package it.calabresi.fp.model;

public class Carte {
    private TipoCarte tipo;
    private String nome;
    private String descrizione;
    private boolean equipaggiabile;
    private String valore;
    private String seme;
    private int distanza = 0;

    public Carte(String nome, String descrizione, boolean equipaggiabile, String valore, String seme, TipoCarte tipo, int distanza) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.equipaggiabile = equipaggiabile;
        this.valore = valore;
        this.seme = seme;
        this.tipo = tipo;
        this.distanza = distanza;
    }

    public TipoCarte getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public boolean isEquipaggiabile() {
        return equipaggiabile;
    }

    public String getValore() {
        return valore;
    }

    public String getSeme() {
        return seme;
    }

    public int getDistanza() {
        return distanza;
    }
}
