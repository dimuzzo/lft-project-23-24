public class Es1_9 {

    public static boolean isStringInAlphabet(String s) {
        boolean flag = true;
        for (int i = 0; i < s.length(); i++) {
            if (flag) {//se flag diventa false, non si può più modificare flag
                flag = (s.charAt(i) == 'a') || (s.charAt(i) == 'b');//false se char != a o != b
            }
        }
        return flag;//return flag,true se ogni char ==a o ==b
    }

    public static boolean scan(String s) {
        if (isStringInAlphabet(s)) {//eseguiamo il codice solo se la stringa contiene char dell'alfabeto
            boolean flag = false;
            int contatore = 1;//conto le iterazione del ciclo al contrario
            for (int i = s.length() - 1; i >= 0 && contatore <= 3; i--) {//faccio partire l'analisi della stringa dalla fine
                if (flag) {
                } else {
                    flag = s.charAt(i) == 'a';//true se un char è ==a, quindi a occorre almeno una volta nelle ultime 3 posizioni
                }
                contatore++;//incremento contatore, se arriviamo a 3 allora terminiamo il ciclo
            }
            return flag;
        } else {
            System.out.println("Errore: stringa contiene elementi al di fuori dell'alfabeto {a,b}");
            return false;
        }
    }

    public static void main(String[] args) {

        String vetOk[] = {"bbaba", "baaaaaaa", "aaaaaaa", "a", "ba", "bba", "aa", "bbbababab"};
        String vetNope[] = {"abbbbbb", "bbabbbbbbbb", "b", "acaab"};

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
