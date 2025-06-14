public class Es1_5{
    public static boolean scan(String s){
        int state = 0;
        int i = 0;
        while (state >= 0 && i < s.length()){
            int intch = s.charAt(i++); // salvo il codice ASCII del carattere
            switch (state) {
                case 0:
                if (intch==47) // ch==/
                    state = 1;
                else
                    state = -1; // ch è un carattere non accettato
                break;

                case 1:
                if (intch==42) // ch==*
                    state = 2;
                else
                    state = -1;
                break;

                case 2:
                if (intch==97 || intch==47) // ch==a || ch==/
                    state = 2;
                else if (intch==42) // ch==*
                    state = 3;
                else
                    state = -1; // ch è un carattere non accettato
                break;

                case 3:
                if (intch==97) // ch==a
                    state = 2;
                else if (intch==42) // ch==*
                    state = 3;
                else if (intch==47) // ch==/
                    state = 4;
                else
                    state = -1; // ch è un carattere non accettato
                break;

                case 4:
                if (i!=s.length())state = -1; // ci sono altri caratteri dopo la chiusura del commento
                break;
            }
        }
        return state==4;
    }

    public static void main(String[] args){
        // Stringhe Ok:
        String s1 = "/****/";
        String s2 = "/*a*a*/";
        String s3 = "/*a/**/";
        String s4 = "/**a///a/a**/";
        String s5 = "/**/";
        String s6 = "/*/*/";

        // Stringhe NOPE:
        String s7 = "/*/";
        String s8 = "/**/***/";
      
        System.out.println("\nStringhe OK: ");
	    System.out.println(scan(s1) ? "OK" : "NOPE");
        System.out.println(scan(s2) ? "OK" : "NOPE");
	    System.out.println(scan(s3) ? "OK" : "NOPE");
	    System.out.println(scan(s4) ? "OK" : "NOPE");
        System.out.println(scan(s5) ? "OK" : "NOPE");
	    System.out.println(scan(s6) ? "OK" : "NOPE");
        System.out.println("\nStringhe Nope:");
	    System.out.println(scan(s7) ? "OK" : "NOPE");
	    System.out.println(scan(s8) ? "OK" : "NOPE");  
    }
}