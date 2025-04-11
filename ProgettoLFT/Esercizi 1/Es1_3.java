public class Es1_3{
    public static boolean isPari(char c){//metodo per controllare se un num è pari
        return (Character.getNumericValue(c) % 2 == 0);
    }

    public static boolean isDispari(char c){//metodo per controllare se un num è dispari
        return (Character.getNumericValue(c) % 2 != 0);
    }

    public static boolean isAtoK(char c){//metodo per controllare se un char è compreso tra A-K
        return (c >= 'A' && c <= 'K') || (c >= 'a' && c <= 'k');
    }

    public static boolean isLtoZ(char c){//metodo per controllare se un char è compreso tra L-Z
        return (c >= 'L' && c <= 'Z') || (c >= 'l' && c <= 'z');
    }

    public static boolean scan(String s)
    {
	int state = 0;
	int i = 0;

	while (state >= 0 && i < s.length() && state != 3 && state != 4) {//state = 3 stato finale, stato accettato, state = 4 stato non accettato, se siamo in uno di questi stati il ciclo finisce
            final char ch = s.charAt(i++);

            switch (state) {
                case 0://primo carattere --> no lettere, vedere se pari o dispari
                if (Character.isLetter(ch))
                    state = -1;
                else if (isPari(ch))
                    state = 1;
                else if (isDispari(ch))
                    state = 2;
                else 
                    state = -1;
                break;
                case 1://stato pari
                if(Character.isLetter(ch)){//se è lettera allora dobbiamo capire in che stato mandarlo,
                    if (isAtoK(ch))
                        state = 3;
                    else if (isLtoZ(ch))
                        state = 4;
                    else 
                        state = -1; 
                }else{//altrimenti continua a cambiare stato a seconda del numero pari o dispari
                    if (isPari(ch))
                        state = 1;
                    else if (isDispari(ch))
                        state = 2;
                    else 
                        state = -1;
                }
                break;
                case 2://stato dispari
                if(Character.isLetter(ch)){//se è lettera allora dobbiamo capire in che stato mandarlo,
                    if (isLtoZ(ch))
                        state = 3;
                    else if (isAtoK(ch))
                        state = 4;
                    else
                        state = -1;
                }else{//altrimenti continua a cambiare stato a seconda del numero pari o dispari
                    if (isDispari(ch))
                        state = 2;
                    else if (isPari(ch))
                        state = 1;
                    else
                        state = -1;
                }
                break;
            }
    }
	return state == 3;
    }

    public static void main(String[] args)
    {
	String vetOk[] = {"123456Bianchi", "654321Rossi", "2Bianchi", "122B"};
        String vetNope[] = {"654321Bianchi", "123456Rossi", "654322", "Rossi"};

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