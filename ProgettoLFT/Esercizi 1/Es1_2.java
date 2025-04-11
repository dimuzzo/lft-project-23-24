public class Es1_2{
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()){
            final char ch = s.charAt(i++); // salva in ch il prossimo carattere
            switch (state) {
                case 0:
                if (Character.isLetter(ch)) // ch è una lettera dell'alfabeto
                    state = 1;
                else if (ch == '_') // ch = _
                    state = 2;
                else if (Character.isDigit(ch)) // ch è un numero 0-9
                    state = -1;
                else
                    state = -1; // ch è un carattere non accettato
                break;

                case 1:
                if (Character.isLetter(ch)) // ch è una lettera dell'alfabeto
                    state = 1;
                else if (ch == '_') // ch = _
                    state = 1;
                else if (Character.isDigit(ch)) // ch è un numero 0-9
                    state = 1;
                else
                    state = -1; // ch è un carattere non accettato
                break;

                case 2:
                if (Character.isLetter(ch)) // ch è una lettera dell'alfabeto
                    state = 1;
                else if (ch == '_') // ch = _
                    state = 2;
                else if (Character.isDigit(ch)) // ch è un numero 0-9
                    state = 1;
                else
                    state = -1; // ch è un carattere non accettato
                break;
            }
        }
        return state==1 && state!=-1 && state!=2;
    }
    
    
    public static void main(String[] args)
    {
       // Stringhe ok:
        String s1 = "x";
        String s2 = "flag1";
        String s3 = "x2y2";
        String s4 = "x_1";
        String s5 = "lft_lab";
        String s6 = "_temp";
        String s7 = "x_1_y2";
        String s8 = "x___";
        String s9 = "_5";

        // Stringhe NOPE:
        String s10 = "5";
        String s11 = "221B";
        String s12 = "123";
        String s13 = "9_to5";
        String s14 = "__";

        System.out.println("Stringhe OK: ");
        System.out.println(scan(s1) ? "OK" : "NOPE");
        System.out.println(scan(s2) ? "OK" : "NOPE");
        System.out.println(scan(s3) ? "OK" : "NOPE");
        System.out.println(scan(s4) ? "OK" : "NOPE");
        System.out.println(scan(s5) ? "OK" : "NOPE");
        System.out.println(scan(s6) ? "OK" : "NOPE");
        System.out.println(scan(s7) ? "OK" : "NOPE");
        System.out.println(scan(s8) ? "OK" : "NOPE");
        System.out.println(scan(s9) ? "OK" : "NOPE");
        System.out.println("Stringhe Nope:");
        System.out.println(scan(s10) ? "OK" : "NOPE");
        System.out.println(scan(s11) ? "OK" : "NOPE");
        System.out.println(scan(s12) ? "OK" : "NOPE");
        System.out.println(scan(s13) ? "OK" : "NOPE");
        System.out.println(scan(s14) ? "OK" : "NOPE");
    }
}