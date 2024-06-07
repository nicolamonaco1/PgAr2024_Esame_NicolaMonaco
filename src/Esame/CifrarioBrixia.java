package Esame;

import java.util.Scanner;
public class CifrarioBrixia {
    public void cifrario() {
        Scanner scanner = new Scanner(System.in);

        int numero = scanner.nextInt();
        String provocazione = scanner.nextLine();

        String[] paroleCriptate = new String[numero];
        for (int i = 0; i < numero; i++) {
            paroleCriptate[i] = scanner.next();
        }

        String[] chiavi = new String[numero];
        for (int i = 0; i < numero; i++) {
            chiavi[i] = scanner.next();
        }

        for (int i = 0; i < numero; i++) {
            String decriptate = decriptaParole(paroleCriptate[i], chiavi[i]);
            System.out.print(decriptate + " ");
        }
        System.out.println("\n");
        System.out.flush();
    }

    private static String decriptaParole(String parolaCriptata, String chiave) {
        StringBuilder paroleDecriptate = new StringBuilder();

        for (int i = 0; i < parolaCriptata.length(); i++) {
            char carattereCriptato = parolaCriptata.charAt(i);
            char chiaveCarattere = chiave.charAt(i % chiave.length());

            int divario = chiaveCarattere - 'a';

            char carattereDecriptato;
            if (Character.isUpperCase(carattereCriptato)) {
                carattereDecriptato = (char) ('A' + (carattereCriptato - 'A' - divario + 26) % 26);
            } else if (Character.isLowerCase(carattereCriptato)) {
                carattereDecriptato = (char) ('a' + (carattereCriptato - 'a' - divario + 26) % 26);
            } else {
                carattereDecriptato = carattereCriptato;
            }

            paroleDecriptate.append(carattereDecriptato);
        }

        return paroleDecriptate.toString();
    }

}