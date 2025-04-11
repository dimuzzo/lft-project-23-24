public class Es1_8 {
public static boolean scan(String s)
    {
        int state = 0;
        int i = 0;

        while (state >= 0 && i < s.length()) {
            final char ch = s.charAt(i++);

            switch (state) {
                case 0:
                    if ((ch <= 'K' && ch >= 'A'))
                        state = 1;
                    else if((ch <= 'Z' && ch >= 'L'))
                        state = 2;
                    else if((Character.isLowerCase(ch)) || (Character.isDigit(ch)))
                        state = -1;
                    else
                        state = -1;
                    break;

                case 1:
                    if (Character.isLowerCase(ch))//controlliamo per primo se è una lettera, altrimenti negli altri if il funzionamento è imprevedibile
                        state = 1;
                    else if(Character.getNumericValue(ch)%2!=0)
                        state = 5;
                    else if (Character.getNumericValue(ch)%2==0)
                        state = 3;
                    else
                        state = -1;
                    break;

                case 2:
                    if (Character.isLowerCase(ch))//controlliamo per primo se è una lettera, altrimenti negli altri if il funzionamento è imprevedibile
                        state = 2;
                    else if(Character.getNumericValue(ch)%2==0)
                        state = 6;
                    else if (Character.getNumericValue(ch)%2!=0)
                        state = 4;
                    else
                        state = -1;
                    break;

                case 3:
                    if (Character.isLetter(ch))//controlliamo per primo se è una lettera, altrimenti negli altri if il funzionamento è imprevedibile
                        state = -1;
                    else if(Character.getNumericValue(ch)%2==0)
                        state = 3;
                    else if (Character.getNumericValue(ch)%2!=0)
                        state = 5;
                    else
                        state = -1;
                    break;

                case 4:
                    if (Character.isLetter(ch))//controlliamo per primo se è una lettera, altrimenti negli altri if il funzionamento è imprevedibile
                        state = -1;
                    else if(Character.getNumericValue(ch)%2==0)
                        state = 6;
                    else if (Character.getNumericValue(ch)%2!=0)
                        state = 4;
                    else
                        state = -1;
                    break;

                case 5:
                    if (Character.isLetter(ch))//controlliamo per primo se è una lettera, altrimenti negli altri if il funzionamento è imprevedibile
                        state = -1;
                    else if(Character.getNumericValue(ch)%2==0)
                        state = 3;
                    else if (Character.getNumericValue(ch)%2!=0)
                        state = 5;
                    else
                        state = -1;
                    break;

                case 6:
                    if (Character.isLetter(ch))//controlliamo per primo se è una lettera, altrimenti negli altri if il funzionamento è imprevedibile
                        state = -1;
                    else if(Character.getNumericValue(ch)%2==0)
                        state = 6;
                    else if (Character.getNumericValue(ch)%2!=0)
                        state = 4;
                    else
                        state = -1;
                    break;
            }
        }
        return state == 3 || state == 4;
    }

    public static void main(String[] args)
    {
        String vetOk[] = {"Bianchi123456", "Rossi654321", "Bianchi2", "B122"};
        String vetNope[] = {"Bianchi654321", "Rossi123456", "654322", "Rossi"};

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
