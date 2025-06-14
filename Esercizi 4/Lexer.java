import java.io.*; 
import java.util.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';//carattere attuale

    private void readch(BufferedReader br) {//legge carattere successivo
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {//metodo principale
        while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }
        
        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;
                
	// ... gestire i casi di ( ) [ ] { } + - * / ; , ... //
            
            case '(':
                peek = ' ';//per far andare avanti la lettura
                return Token.lpt;
                
            case ')':
                peek = ' ';//per far andare avanti la lettura
                return Token.rpt;
                
            case '[':
                peek = ' ';//per far andare avanti la lettura
                return Token.lpq;
                
            case ']':
                peek = ' ';//per far andare avanti la lettura
                return Token.rpq;
                
            case '{':
                peek = ' ';//per far andare avanti la lettura
                return Token.lpg;
                
            case '}':
                peek = ' ';//per far andare avanti la lettura
                return Token.rpg;
                
            case '+':
                peek = ' ';//per far andare avanti la lettura
                return Token.plus;
                
            case '-':
                peek = ' ';//per far andare avanti la lettura
                return Token.minus;
                
            case '*':
                peek = ' ';//per far andare avanti la lettura
                return Token.mult;
                
            case '/':
                readch(br);
                //
                if (Character.isDigit(peek) || Character.isLetter(peek) || peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {//operando divisione, quindi return div
                    return Token.div;
                } else if(peek== '/'){//qui c'è un commento, leggiamo carattere fino a EOF o newLine
                    while(peek != '\n' && peek != (char)-1){
                        readch(br);
                    }
                    peek = ' ';//per far andare avanti la lettura
                    return this.lexical_scan(br);//per returnare gli altri elementi, return prossimo elemento
                } else if(peek== '*'){ //commento più righe
                    boolean flagStar = false;//flag di * 
                    boolean flagW = false;//flag completo che interrompe il ciclo della lettura del commento
                    while(peek != (char)-1 && !flagW){
                        readch(br);
                        switch(peek){
                                case '*':flagStar = true;break;
                                case '/':if(flagStar==true)//case dove controlliamo se prima dello / c'è *, in tal caso siamo arrivati a fine commento
                                        flagW = true; break;
                                default: 
                                    flagStar = false;//perchè altrimenti con *z/ il Lexer non avrebbe dato errori, non è stato chiuso il commento
                                    break;
                        }
                    }
                    if(flagW){//commento finito, ripristiniamo il peek e facciamo andare avanti la lettura
                        peek = ' ';//per far andare avanti la lettura
                        return this.lexical_scan(br);
                    }
                    else{//errore per mancata chiusura commento
                        System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                    }
                }else{//errore per carattere dopo /, diverso da /,*, o carattere che returna Token.div
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
                
            case ';':
                peek = ' ';//per far andare avanti la lettura
                return Token.semicolon;
                
            case ',':
                peek = ' ';//per far andare avanti la lettura
                return Token.comma;
                
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';//per far andare avanti la lettura
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            // ... gestire i casi di || < > <= >= == <> ... //
                
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';//per far andare avanti la lettura
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }    
                
            case (char)-1:
                return new Token(Tag.EOF);
            case '<':
                readch(br);
                if (peek == '>') {
                    peek = ' ';//per far andare avanti la lettura
                    return Word.ne;
                } else if(peek == '='){
                    peek = ' ';//per far andare avanti la lettura
                    return Word.le;
                }
                else if(Character.isDigit(peek) || Character.isLetter(peek) || peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r'){
                    peek = ' ';//per far andare avanti la lettura
                    return Word.lt;
                }
                else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case '>':
                readch(br);
                if(peek == '='){
                    peek = ' ';//per far andare avanti la lettura
                    return Word.ge;
                }
                else if(Character.isDigit(peek) || Character.isLetter(peek) || peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r'){
                    peek = ' ';//per far andare avanti la lettura
                    return Word.gt;
                }
                else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case '=':
                readch(br);
                if(peek == '='){
                    peek = ' ';//per far andare avanti la lettura
                    return Word.eq;
                }else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            case ':':
                readch(br);
                if(peek == '='){
                    peek = ' ';//per far andare avanti la lettura
                    return Word.init;
                }else {
                    System.err.println("Erroneous character"
                            + " after & : "  + peek );
                    return null;
                }
            default:
                boolean flagCheckLetNum = false;//flag per controllare se c'è almeno una lettere o un numero
                if (Character.isLetter(peek) || peek == '_') {//analisi word,controlliamo che la prima lettera non sia un numero
	// ... gestire il caso degli identificatori e delle parole chiave //
                    String string="";
                    while (Character.isLetter(peek) || Character.isDigit(peek) || peek == '_') {
                        if(Character.isLetter(peek) || Character.isDigit(peek))
                            flagCheckLetNum = true;
                        string += peek;
                        readch(br);
                    }
                    switch(string){//controlliamo se la stringa ottenuta non sia una parola chiave
                        case "assign":
                            return Word.assign;
                        case "to":
                            return Word.to;
                        case "if":
                            return Word.iftok;
                        case "else":
                            return Word.elsetok;
                        case "do":
                            return Word.dotok;
                        case "for":
                            return Word.fortok;
                        case "begin":
                            return Word.begin;
                        case "end":
                            return Word.end;
                        case "print":
                            return Word.print;
                        case "read":
                            return Word.read;
                        default://caso identificatore
                            if(flagCheckLetNum)
                            return new Word(Tag.ID,string);
                            else{
                                System.err.println("Erroneous character"
                            + " after & : "  + peek );
                            return null;
                            }

                    }
                } else if (Character.isDigit(peek)) {//per numeri, controllo ci sia un numero

	// ... gestire il caso dei numeri ... //
                    String num="";
                    while (Character.isDigit(peek)) {
                        num += peek;
                        readch(br);
                    }
                    if(peek == '_' || Character.isLetter(peek)){
                        System.err.println("Erroneous character"
                            + " after & : "  + peek );
                            return null;
                    }
                    else{
                        
                        if(num.charAt(0)== '0'){//per controllare che non inizi con 0 un numero
                            for(int i=0;i<num.length();i++){//controlliamo che ci siano solo 0
                                if(num.charAt(i)!= '0'){//altrimenti errore
                                    System.err.println("Erroneous character"
                                        + " after & : "  + peek );
                                    return null;
                                }
                            }
                        }
                        return new NumberTok(Integer.valueOf(num));
                        
                        
                    }

                } else {//errore in default no lettera o numero o _
                        System.err.println("Erroneous character: " 
                                + peek );
                        return null;
                }
         }        
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {e.printStackTrace();}    
    }

}