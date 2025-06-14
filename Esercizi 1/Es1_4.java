public class Es1_4{
    public static boolean scan(String s)
    {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if (ch == '+' || ch == '-')
                        state = 1;
                    else if(ch >= '0' && ch <= '9')
                        state = 2;
                    else if(ch == '.')
                        state = 3;
                    else if(ch == 'e')
                        state = -1;
                    else
                        state = -1;
                    break;

                case 1:
                    if (ch >= '0' && ch <= '9')
                        state = 2;
                    else if(ch == '.')
                        state = 3;
                    else if(ch == '+' || ch == '-' || ch == 'e')
                        state = -1;
                    else
                        state = -1;
                    break;

                case 2:
                    if (ch >= '0' && ch <= '9')
                        state = 2;
                    else if(ch == '.')
                        state = 3;
                    else if(ch == 'e')
                        state = 4;
                    else if (ch == '+' || ch == '-')
                        state = -1;
                    else
                        state = -1;
                    break;

                case 3:
                    if (ch >= '0' && ch <= '9')
                        state = 6;
                    else if (ch == '+' || ch == '-' || ch == 'e' || ch == '.')
                        state = -1;
                    else
                        state = -1;
                    break;

                case 4:
                    if ((ch >= '1' && ch <= '9') || ch == '-')
                        state = 5;
                    else if (ch == '+' || ch == '0' || ch == 'e' || ch == '.')
                        state = -1;
                    else
                        state = -1;
                    break;

                case 5:
                    if (ch >= '0' && ch <= '9')
                        state = 5;
                    else if(ch == '.')
                        state = 4;
                    else if (ch == '+' || ch == 'e' || ch == '-')
                        state = -1;
                    else
                        state = -1;
                    break;

                case 6:
                    if (ch >= '0' && ch <= '9')
                        state = 6;
                    else if (ch == 'e')
                        state = 4;
                    else if (ch == '+' || ch == '.' || ch == '-')
                        state = -1;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 2 || state == 5 || state == 6;
    }

    public static void main(String[] args)
    {
        // Stringhe Ok:
        String s1 = "123";
        String s2 = "123.5";
        String s3 = ".567";
        String s4 = "+7.5";
        String s5 = "-.7";
        String s6 = "67e10";
        String s7 = "1e-2";
        String s8 = "-.7e2";
        String s9 = "1e2.3";

        // Stringhe NOPE:
        String s10 = ".";
        String s11 = "e3";
        String s12 = "123.";
        String s13 = "+e6";
        String s14 = "1.2.3";
        String s15 = "4e5e6";
        String s16 = "++3";

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
        System.out.println(scan(s13) ? "OK" : "NOPE");
        System.out.println(scan(s14) ? "OK" : "NOPE");
        System.out.println(scan(s15) ? "OK" : "NOPE");
        System.out.println(scan(s16) ? "OK" : "NOPE");
    }
}