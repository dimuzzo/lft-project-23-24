public class Es1_10 {

    public static boolean scan(String s) {
        String nome = "Paolo";
        if (s.length() == nome.length()) {//per controllare le lunghezze delle 2 stringhe
            int i = 0;
            boolean flagDirty = false;//flag 1 singola modifica
            boolean flagError = false;//si attiva se presenti più di 1 errore

            for (i = 0; i < nome.length(); i++) {
                if (s.charAt(i) == nome.charAt(i)) {
                } else {//caso in cui non siano uguali i caratteri
                    if (flagDirty) {
                        flagError = true;
                    } else {
                        flagDirty = true;
                    }
                }
            }
            return !flagError;//se false iniziale, quindi niente errori return true, altrimenti false
        } else {
            return false;
        }

    }

    public static void main(String[] args){
        
        String vetOk[] = {"Pjolo", "caolo", "Pa%lo", "Paola", "Parlo"};
        String vetNope[] = {"Eva", "Perro", "Pietro", "P*o*o"};

        System.out.println("\nStringhe OK: ");
        for (int i = 0; i < vetOk.length; i++) {
            System.out.println(scan(vetOk[i]) ? "OK" : "NOPE");
        }

        System.out.println("\nStringhe Nope:");
        for (int i = 0; i < vetNope.length; i++) {
            System.out.println(scan(vetNope[i]) ? "OK" : "NOPE");
        }
        
    }
}
