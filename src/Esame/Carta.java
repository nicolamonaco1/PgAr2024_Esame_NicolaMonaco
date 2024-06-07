package Esame;
public class Carta {
    private String nome;
    private boolean equipaggiabile;
    private int gittata;

    public Carta(String nome, boolean equipaggiabile, int gittata) {
        this.nome = nome;
        this.equipaggiabile = equipaggiabile;
        this.gittata = gittata;
    }

    public String getNome() {
        return nome;
    }

    public boolean isEquipaggiabile() {
        return equipaggiabile;
    }
    public int getGittata() {
        return gittata;
    }

    @Override
    public String toString() {
        return nome;
    }
}
