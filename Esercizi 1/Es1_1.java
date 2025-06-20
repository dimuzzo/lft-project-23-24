public class Es1_1 {

    public static boolean scan(String s)
    {
		int state = 0;
		int i = 0;

		while (state >= 0 && i < s.length()) {
			final char ch = s.charAt(i++);//ch = char che stiamo guardando attualmente
			switch (state) {
			case 0://stato iniziale
			if (ch == '0')
				state = 1;
			else if (ch == '1')
				state = 0;
			else
				state = -1;
			break;

			case 1:
			if (ch == '0')
				state = 2;
			else if (ch == '1')
				state = 0;
			else
				state = -1;
			break;

			case 2:
			if (ch == '0')
				state = 3;
			else if (ch == '1')
				state = 0;
			else
				state = -1;
			break;

			case 3:
			if (ch == '0' || ch == '1')
				state = 3;
			else
				state = -1;
			break;
				case -1:
					break;
			}
		}
	return state != 3 && state != -1;//se è == 3 allora abbiamo una stringa che contiene 3 zeri consecutivi quindi return false, analogo con -1
    }
    
    public static void main(String[] args) {
        System.out.println(scan("010101") ? "OK" : "NOPE"); //test con stringa "010101"
    }
    
}
