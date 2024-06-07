package Esame;

import java.util.ArrayList;
import java.util.List;

public class Giocatore {
    private String nome;
    private Ruolo ruolo;
    private int puntiFerita;
    private List<Carta> mano;
    private List<Carta> equipaggiamento;
    private Carta cartaEquipaggiata;

    public void setEquipaggiamento(String equipaggiamento) {
        for(Carta mano : this.mano) {
            if(equipaggiamento.equalsIgnoreCase(mano.getNome())) {
                this.cartaEquipaggiata = cartaEquipaggiata;

            }
        }
    }

    public Giocatore(String nome, Ruolo ruolo, int puntiFerita) {
        this.nome = nome;
        this.ruolo = ruolo;
        this.puntiFerita = puntiFerita;
        this.mano = new ArrayList<>();
        this.equipaggiamento = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public int getPuntiFerita() {
        return puntiFerita;
    }

    public void aggiungiCartaMano(Carta carta) {
        mano.add(carta);
    }

    public void rimuoviCartaMano(Carta carta) {
        mano.remove(carta);
    }

    public List<Carta> getMano() {
        return mano;
    }

    public List<Carta> getEquipaggiamento() {
        return equipaggiamento;
    }

    public void aggiungiEquipaggiamento(Carta carta) {
        equipaggiamento.add(carta);
    }

    public boolean usaMancato() {
        for (Carta carta : mano) {
            if (carta.getNome().equals("Mancato!")) {
                mano.remove(carta);
                return true;
            }
        }
        return false;
    }

    public void subisciDanno(int danno) {
        this.puntiFerita -= danno;
        System.out.println(this.nome + " subisce " + danno + " danno(i). Punti ferita rimanenti: " + this.puntiFerita);
        if (this.puntiFerita <= 0) {
            this.puntiFerita = 0;
            System.out.println(this.nome + " è stato eliminato!");
        }
    }

    @Override
    public String toString() {
        return nome + " (" + ruolo + ")";
    }
}
