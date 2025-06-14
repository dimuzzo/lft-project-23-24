public class Es1_7 {

    public static boolean isPari(char c) {
        return (Character.getNumericValue(c) % 2 == 0);
    }

    public static boolean isDispari(char c) {
        return (Character.getNumericValue(c) % 2 != 0);
    }

    public static boolean isAtoK(char c) {
        return (c >= 'A' && c <= 'K') || (c >= 'a' && c <= 'k');
    }

    public static boolean isLtoZ(char c) {
        return (c >= 'L' && c <= 'Z') || (c >= 'l' && c <= 'z');
    }

    public static boolean scan(String s) {
        int state = 0;
        int i = 0;
        boolean flagNum = false;
        boolean flagLet = false;
        boolean pari=false;
        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i);

            switch (state) {
                case 0://primo carattere --> no lettere,possibili spazi, altrimenti vedere se pari o dispari
                    if(ch == ' '){
                        state = 0;
                    }else{
                        if (Character.isLetter(ch)) {
                        state = -1;
                        } else if (isPari(ch)) {
                            state = 1;
                        } else if (isDispari(ch)) {
                            state = 2;
                        } else {
                            state = -1;
                        }
                    }
                    break;

                case 1://stato pari
                    if(ch == ' '){
                        flagNum = true;
                        state = 5;
                        pari = true;
                    } else{
                        if (Character.isLetter(ch)) {
                            if (isAtoK(ch)) {
                                state = 3;
                            } else if (isLtoZ(ch)) {
                                state = 4;
                            } else {
                                state = -1;
                            }

                        } else {
                            if (isPari(ch) && !flagNum) {
                                state = 1;
                            } else if (isDispari(ch) && !flagNum) {
                                state = 2;
                            } else {
                                state = -1;
                            }
                        }
                    }
                    break;
                case 2://stato dispari
                    if (ch == ' ') {
                        flagNum = true;
                        state = 5;
                        pari = false;
                    } else {
                        if (Character.isLetter(ch)) {
                            if (isLtoZ(ch)) {
                                state = 3;
                            } else if (isAtoK(ch)) {
                                state = 4;
                            } else {
                                state = -1;
                            }
                        } else {
                            if (isDispari(ch) && !flagNum) {
                                state = 2;
                            } else if (isPari(ch) && !flagNum) {
                                state = 1;
                            } else {
                                state = -1;
                            }

                        }
                        break;
                    }
                    
                case 3://stato finale, controlliamo non ci siano altri spazi e in caso ci siano vi sia una lettera maiuscola
                    if(ch == ' '){
                        flagLet = true;
                    } else {
                        if(flagLet){
                            if(Character.isUpperCase(ch)){
                                state = 3;
                                flagLet = false;
                            }
                            else{
                                //System.out.println("caso : is!UpperCase con char :"+ch);
                                state = -1;
                            }
                        }
                    }
                    break;
                case 5://stato temporaneo, dopo aver analizzato eventuali spazi intermediari passiamo o al 3 o al -1
                    if(ch == ' '){
                        state = 5;
                    }
                    else{
                        if(Character.isUpperCase(ch)){
                            flagLet = false;//flaglet = false perchè abbiamo una maiuscola
                            if((pari && isAtoK(ch)) || (!pari && isLtoZ(ch))){
                                state = 3;
                            }
                            else{
                                state = -1;
                            }
                        }
                    }
                    break;
                    
            }
            i++;
        }
        return state == 3;
    }

    public static void main(String[] args) {

        String vetOk[] = {"654321 Rossi", " 123456 Bianchi ", "123456De Gasperi"};
        String vetNope[] = {"123456Bia nchi"};

        System.out.println("\nStringhe OK: ");
        for (int i = 0; i < vetOk.length; i++) {
            System.out.println(scan(vetOk[i]) ? "OK" : "NOPE");
        }
//        System.out.println(scan(vetOk[1]) ? "OK" : "NOPE");
        System.out.println("\nStringhe Nope:");
        for (int i = 0; i < vetNope.length; i++) {
            System.out.println(scan(vetNope[i]) ? "OK" : "NOPE");
        }
               
    }
}
