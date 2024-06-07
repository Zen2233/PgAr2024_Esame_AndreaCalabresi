package it.calabresi.fp.utils;

import it.calabresi.fp.model.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.*;
import java.io.File;
import java.util.ArrayList;

public class LeggiFile {
    private static XMLInputFactory xmlif = XMLInputFactory.newInstance();
    private static XMLStreamReader xmlr;

    public static void getInformazioni(Partita partita) {
        try {
            File inputFile = new File(Costanti.DIR_INPUT_FILE);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList ruoliList = doc.getElementsByTagName("ruolo");
            for (int i = 0; i < ruoliList.getLength(); i++) {
                Node ruoloNode = ruoliList.item(i);
                partita.addRuolo(ruoloNode.getTextContent());
            }

            NodeList personaggiList = doc.getElementsByTagName("personaggio");
            ArrayList<Player> listaPlayer = new ArrayList<>();
            Player player = null;
            for (int i = 0; i < personaggiList.getLength(); i++) {
                Node personaggioNode = personaggiList.item(i);
                if (personaggioNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element personaggioElement = (Element) personaggioNode;
                    String nome = personaggioElement.getElementsByTagName("nome").item(0).getTextContent();
                    String descrizione = personaggioElement.getElementsByTagName("descrizione").item(0).getTextContent();
                    String pf = personaggioElement.getAttribute("pf");

                    player = new Player(nome, descrizione, Integer.parseInt(pf));
                    listaPlayer.add(player);
                }
            }
            partita.setListaPlayer(listaPlayer);

            ArrayList<Carte> armi = new ArrayList<>();

            // Leggi e stampa le armi
            Carte carta = null;
            NodeList armiList = doc.getElementsByTagName("arma");
            for (int i = 0; i < armiList.getLength(); i++) {
                Node armaNode = armiList.item(i);
                if (armaNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element armaElement = (Element) armaNode;
                    String nome = armaElement.getElementsByTagName("nome").item(0).getTextContent();
                    String distanza = armaElement.getElementsByTagName("distanza").item(0).getTextContent();
                    // Leggi e stampa le copie delle armi
                    NodeList copieList = armaElement.getElementsByTagName("copia");
                    for (int j = 0; j < copieList.getLength(); j++) {
                        Node copiaNode = copieList.item(j);
                        if (copiaNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element copiaElement = (Element) copiaNode;
                            String valore = copiaElement.getElementsByTagName("valore").item(0).getTextContent();
                            String seme = copiaElement.getElementsByTagName("seme").item(0).getTextContent();
                            carta = new Carte(nome, "", true, valore, seme, TipoCarte.ARMA, Integer.parseInt(distanza));
                            armi.add(carta);
                        }
                    }
                }
            }

            ArrayList<Carte> mazzo = new ArrayList<>();

            // Leggi e stampa le carte
            NodeList carteList = doc.getElementsByTagName("carta");
            for (int i = 0; i < carteList.getLength(); i++) {
                Node cartaNode = carteList.item(i);
                if (cartaNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element cartaElement = (Element) cartaNode;
                    String nome = cartaElement.getElementsByTagName("nome").item(0).getTextContent();
                    TipoCarte tipo;
                    switch (nome) {
                        case "BANG!":
                            tipo = TipoCarte.BANG;
                            break;
                        case "Mancato!":
                            tipo = TipoCarte.MANCATO;
                            break;
                        default:
                            tipo = TipoCarte.ALTRO;
                            break;
                    }

                    String descrizione = cartaElement.getElementsByTagName("descrizione").item(0).getTextContent();
                    String equipaggiabileT = cartaElement.getAttribute("equipaggiabile");
                    boolean equipaggiabile = false;
                    if (equipaggiabileT.equals("true")) {
                        equipaggiabile = true;
                    }

                    // Leggi e stampa le copie delle carte
                    NodeList copieList = cartaElement.getElementsByTagName("copia");
                    for (int j = 0; j < copieList.getLength(); j++) {
                        Node copiaNode = copieList.item(j);
                        if (copiaNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element copiaElement = (Element) copiaNode;
                            String valore = copiaElement.getElementsByTagName("valore").item(0).getTextContent();
                            String seme = copiaElement.getElementsByTagName("seme").item(0).getTextContent();
                            carta = new Carte(nome, descrizione, equipaggiabile, valore, seme, tipo, 0);
                            mazzo.add(carta);
                        }
                    }
                }
            }

            partita.setMazzo(mazzo, armi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}