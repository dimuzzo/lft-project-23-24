public class Es1_6{
    public static boolean scan(String s)
    {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0://stato iniziale/finale
                    if (ch == '/')
                        state = 1;
                    else if (ch == 'a' || ch == '*')
                        state = 0;
                    else
                        state = -1;
                    break;

                case 1://stato finale
                    if (ch == '/')
                        state = 1;
                    else if (ch == 'a')
                        state = 0;
                    else if (ch == '*')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 2:
                    if (ch == '*')
                        state = 3;
                    else if (ch == 'a' || ch == '/')
                        state = 2;
                    else
                        state = -1;
                    break;

                case 3:
                    if (ch == 'a')
                        state = 2;
                    else if(ch == '*')
                        state = 3;
                    else if(ch == '/')
                        state = 0;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 0 || state == 1;
    }


    public static void main(String[] args){
        // Stringhe Ok:
        String s1 = "aaa/****/aa";
        String s2 = "aa/*a*a*/";
        String s3 = "aaaa";
        String s4 = "/****/";
        String s5 = "/*aa*/";
        String s6 = "*/a";
        String s7 = "a/**/***a";
        String s8 = "a/**/***/a";
        String s9 = "a/**/aa/***/a";

        // Stringhe NOPE:
        String s10 = "aaa/*/aa";
        String s11 = "a/**//***a";
        String s12 = "aa/*aa";
      
        System.out.println("\nStringhe OK: ");
	    System.out.println(scan(s1) ? "OK" : "NOPE");
        System.out.println(scan(s2) ? "OK" : "NOPE");
	    System.out.println(scan(s3) ? "OK" : "NOPE");
	    System.out.println(scan(s4) ? "OK" : "NOPE");
        System.out.println(scan(s5) ? "OK" : "NOPE");
	    System.out.println(scan(s6) ? "OK" : "NOPE");
        System.out.println(scan(s7) ? "OK" : "NOPE");
        System.out.println(scan(s8) ? "OK" : "NOPE");
        System.out.println(scan(s9) ? "OK" : "NOPE");
        System.out.println("\nStringhe Nope:");
	    System.out.println(scan(s10) ? "OK" : "NOPE");
	    System.out.println(scan(s11) ? "OK" : "NOPE");  
        System.out.println(scan(s12) ? "OK" : "NOPE");
    }
}