package Esame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Gioco {
    private List<Giocatore> giocatori;
    private List<Carta> mazzo;
    private List<Carta> scarti;
    private Scanner scanner;
    private CifrarioBrixia provocazione;

    public Gioco() {
        this.giocatori = new ArrayList<>();
        this.mazzo = new ArrayList<>();
        this.scarti = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.provocazione = new CifrarioBrixia();
    }

    public void iniziaPartita() {
        int numGiocatori;
        do {
            System.out.println("Inserisci il numero di giocatori (minimo 4 massimo 7): ");
            numGiocatori = scanner.nextInt();
        } while (numGiocatori < 4 || numGiocatori > 7);
        assegnaRuoli(numGiocatori);
        mescolaMazzo();
        distribuisciCarteIniziali();
        gestisciTurni();
    }

    private void assegnaRuoli(int numGiocatori) {
        giocatori.add(new Giocatore("Sceriffo", Ruolo.SCERIFFO, 5));

        List<Ruolo> ruoli = new ArrayList<>();
        ruoli.add(Ruolo.RINNEGATO);

        for (int i = 0; i < numGiocatori - 2; i++) {
            if (i < 2) {
                ruoli.add(Ruolo.FUORILEGGE);
            } else if (i == 2 && numGiocatori > 4) {
                ruoli.add(Ruolo.VICE);
            } else if (i == 3 && numGiocatori > 5) {
                ruoli.add(Ruolo.FUORILEGGE);
            } else if (i == 4 && numGiocatori == 7) {
                ruoli.add(Ruolo.VICE);
            }
        }

        Collections.shuffle(ruoli);

        for (int i = 0; i < numGiocatori-1; i++) {
            giocatori.add(new Giocatore("Giocatore " + "" + i, ruoli.get(i), 4));
        }
    }

    private void mescolaMazzo() {
        // Aggiungi le carte al mazzo
        for (int i = 0; i < 50; i++) {
            mazzo.add(new Carta("BANG!", false, 1));
        }
        for (int i = 0; i < 24; i++) {
            mazzo.add(new Carta("Mancato!", false, 1));
        }
        mazzo.add(new Carta("Schofield", true, 2));
        mazzo.add(new Carta("Schofield", true, 2));
        mazzo.add(new Carta("Schofield", true, 2));
        mazzo.add(new Carta("Remington", true, 3));
        mazzo.add(new Carta("Rev. Carabine", true, 4));
        mazzo.add(new Carta("Winchester", true, 5));

        Collections.shuffle(mazzo);
    }

    private void distribuisciCarteIniziali() {
        for (Giocatore giocatore : giocatori) {
            for (int i = 0; i < giocatore.getPuntiFerita(); i++) {
                giocatore.aggiungiCartaMano(pescaCarta());
            }
        }
    }

    private Carta pescaCarta() {
        if (mazzo.isEmpty()) {
            rigeneraMazzo();
        }
        return mazzo.remove(0);
    }

    private void rigeneraMazzo() {
        mazzo.addAll(scarti);
        scarti.clear();
        Collections.shuffle(mazzo);
    }

    private void gestisciTurni() {
        int indiceGiocatoreCorrente = 0;

        while (!partitaConclusa()) {
            Giocatore giocatoreCorrente = giocatori.get(indiceGiocatoreCorrente);
            if (giocatoreCorrente.getPuntiFerita() > 0) {
                System.out.println("Turno di " + giocatoreCorrente.getNome());
                eseguiTurno(giocatoreCorrente);
            }
            indiceGiocatoreCorrente = (indiceGiocatoreCorrente + 1) % giocatori.size();
        }
        concludePartita();
    }

    private void eseguiTurno(Giocatore giocatore) {
        // Fase 1: Pescare due carte
        System.out.println(giocatore.getNome() + " pesca due carte.");
        giocatore.aggiungiCartaMano(pescaCarta());
        giocatore.aggiungiCartaMano(pescaCarta());

        // Fase 2: Giocare carte
        System.out.println("Carte in mano di " + giocatore.getNome() + ": " + giocatore.getMano());
        boolean continuaAGiocare = true;
        boolean giocatoBang = false;

        while (continuaAGiocare && giocatore.getMano().size() > 0) {
            System.out.println("Scegli una carta da giocare (digita il numero corrispondente, o -1 per terminare):");
            for (int i = 0; i < giocatore.getMano().size(); i++) {
                System.out.println(i + ": " + giocatore.getMano().get(i).getNome());
            }
            int scelta = scanner.nextInt();
            if (scelta == -1) {
                System.out.println("Vuoi mandare una provocazione ad un giocatore? (si/no)");
                scanner.nextLine();  // Consumare la newline
                String risposta;
                do {
                    risposta = scanner.nextLine();
                } while (!risposta.equals("si") && !risposta.equals("no"));
                if (risposta.equals("si")) {
                    System.out.println("Scegli il giocatore da provocare");
                    for(int i = 0; i < giocatori.size(); i++) {
                        if(giocatori.get(i) != giocatore){
                            System.out.println(i + ": " + giocatori.get(i).getNome());
                        }
                    }
                    int indice = scanner.nextInt();
                    scanner.nextLine();
                    Giocatore gDaDissare = null;
                    if(indice >= 0 && indice < giocatori.size() && giocatori.get(indice) != giocatore) {
                        gDaDissare = giocatori.get(indice);
                    }
                    System.out.println("Inserisci il messaggio, comprensivo di algoritmo di decodificazione, da inviare a : " + gDaDissare.getNome());
                    provocazione.cifrario();
                }
                continuaAGiocare = false;
            } else if (scelta >= 0 && scelta < giocatore.getMano().size()) {
                Carta carta = giocatore.getMano().get(scelta);
                if (carta.getNome().equals("BANG!") && giocatoBang) {
                    System.out.println("Hai già giocato una carta BANG! in questo turno.");
                } else {
                    boolean colpoAndatoASegno = giocaCarta(giocatore, carta);
                    if (carta.getNome().equals("BANG!") && colpoAndatoASegno) {
                        giocatoBang = true;
                    }
                }
            }
        }

        // Fase 3: Scartare le carte in eccesso
        while (giocatore.getMano().size() > giocatore.getPuntiFerita()) {
            System.out.println("Scegli una carta da scartare (digita il numero corrispondente):");
            for (int i = 0; i < giocatore.getMano().size(); i++) {
                System.out.println(i + ": " + giocatore.getMano().get(i).getNome());
            }
            System.out.flush();
            int scelta = scanner.nextInt();
            if (scelta >= 0 && scelta < giocatore.getMano().size()) {
                Carta carta = giocatore.getMano().remove(scelta);
                scarti.add(carta);
                System.out.println("Hai scartato: " + carta.getNome());
            }
        }
    }
    private boolean giocaCarta(Giocatore giocatore, Carta carta) {
        ArrayList<Integer> indici = new ArrayList<>();

        if (carta.getNome().equals("BANG!")) {
            System.out.println(giocatore.getNome() + " gioca BANG!");
            System.out.println("Scegli un bersaglio:");

            for (int i = 0; i < giocatori.size(); i++) {
                Giocatore bersaglio = giocatori.get(i);
                if (bersaglio != giocatore && bersaglio.getPuntiFerita() > 0) {
                    int distanza = calcolaDistanza(giocatore, bersaglio);
                    if (distanza <= 1) {
                        System.out.println(i + ": " + bersaglio.getNome());
                    } else {
                        for (Carta cartaArma : giocatore.getMano()) {
                            if ((cartaArma.getNome().equals("Schofield") && distanza <= 2) ||
                                    (cartaArma.getNome().equals("Remington") && distanza <= 3) ||
                                    (cartaArma.getNome().equals("Rev. Carabine") && distanza <= 4) ||
                                    (cartaArma.getNome().equals("Winchester") && distanza <= 5)) {
                                System.out.println(i + ": " + bersaglio.getNome());
                                indici.add(i);
                                break;
                            }
                        }
                    }
                }
            }
            int scelta;
            do {
                 scelta = scanner.nextInt();
            }while(!indici.contains(scelta));
            if (scelta >= 0 && scelta < giocatori.size()) {
                Giocatore bersaglio = giocatori.get(scelta);
                if (bersaglio != giocatore && !giocaMancato(bersaglio)) {
                    bersaglio.subisciDanno(1);
                    giocatore.rimuoviCartaMano(carta);
                    scarti.add(carta);
                    return true;
                } else {
                    System.out.println("Bersaglio non valido.");
                }
            }
        } else {
            System.out.println(giocatore.getNome() + " gioca " + carta.getNome());
            if(carta.getNome().equalsIgnoreCase("Schofield")) giocatore.setEquipaggiamento("Schofield");
            if(carta.getNome().equalsIgnoreCase("Remington")) giocatore.setEquipaggiamento("Remington");
            if(carta.getNome().equalsIgnoreCase("Rev. Carabine")) giocatore.setEquipaggiamento("Rev. Carabine");
            if(carta.getNome().equalsIgnoreCase("Winchester")) giocatore.setEquipaggiamento("Winchester");

            giocatore.rimuoviCartaMano(carta);
            scarti.add(carta);
        }
        return false;
    }


    private boolean partitaConclusa() {
        int numeroFuorileggeVivi = 0;
        int numeroViceVivi = 0;
        int numeroRinnegatoVivi = 0;
        int numeroSceriffiVivi = 0;

        for (Giocatore giocatore : giocatori) {
            if (giocatore.getPuntiFerita() > 0) {
                switch (giocatore.getRuolo()) {
                    case FUORILEGGE:
                        numeroFuorileggeVivi++;
                        break;
                    case VICE:
                        numeroViceVivi++;
                        break;
                    case RINNEGATO:
                        numeroRinnegatoVivi++;
                        break;
                    case SCERIFFO:
                        numeroSceriffiVivi++;
                        break;
                }
            }
        }

        // Condizioni di vittoria
        if (numeroSceriffiVivi == 0) {
            System.out.println("I fuorilegge e il rinnegato hanno vinto!");
            return true;
        }
        if (numeroFuorileggeVivi == 0 && numeroRinnegatoVivi == 0) {
            System.out.println("Gli sceriffi e i vice hanno vinto!");
            return true;
        }
        if (numeroSceriffiVivi == 1 && numeroFuorileggeVivi == 0 && numeroViceVivi == 0) {
            System.out.println("Il rinnegato ha vinto!");
            return true;
        }

        return false;
    }
    private boolean giocaMancato(Giocatore giocatore) {
        if (giocatore.getMano().stream().anyMatch(carta -> carta.getNome().equals("Mancato!"))) {
            String risposta;
            do {
                System.out.println(giocatore.getNome() + " hai la carta Mancato! Vuoi giocarla per annullare un BANG! diretto a te? (si/no)");
                risposta = scanner.nextLine();
            } while (!risposta.equalsIgnoreCase("si") && !risposta.equalsIgnoreCase("no"));
            if (risposta.equalsIgnoreCase("si")) {
                        System.out.println(giocatore.getNome() + " annulla il BANG!");
                        giocatore.rimuoviCartaMano(giocatore.getMano().stream().filter(carta -> carta.getNome().equals("Mancato!")).findFirst().orElse(null));
                        System.out.println("Il BANG! è annullato.");
                        return true;
            }

        }
        System.out.println(giocatore.getNome() + " non ha la carta Mancato! o non ha deciso di giocarla.");
        System.out.println("Il BANG! non è stato annullato.");
        return false;
    }
    private int calcolaDistanza(Giocatore giocatore1, Giocatore giocatore2) {
        int indiceGiocatore1 = giocatori.indexOf(giocatore1);
        int indiceGiocatore2 = giocatori.indexOf(giocatore2);
        int distanzaOraria = (indiceGiocatore2 - indiceGiocatore1 + giocatori.size()) % giocatori.size();
        int distanzaAntioraria = (indiceGiocatore1 - indiceGiocatore2 + giocatori.size()) % giocatori.size();
        return Math.min(distanzaOraria, distanzaAntioraria);
    }



    private void concludePartita() {
        System.out.println("La partita è conclusa.");
    }
}