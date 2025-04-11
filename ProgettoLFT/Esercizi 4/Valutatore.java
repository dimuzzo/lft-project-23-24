import java.io.*; 
public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) { 
	lex = l; 
	pbr = br;
	move(); 
    }
   
    void move() { 
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) { 
		throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error");
    }

    public void start() { 
        if(look.tag=='('||look.tag==256){
            int expr_val;
            expr_val = expr();
            match(Tag.EOF);
            System.out.println(expr_val);// risultato della valutazione
        }
        else{
            error("errore in start: mi aspettavo ( oppure un NUM");
        }
    }

    private int expr() { 
    if(look.tag=='('||look.tag==256){
        int term_val, exprp_val;
        term_val = term();//come da sdt
        exprp_val = exprp(term_val);//come da sdt
        return exprp_val;
    }
    else{
        error("errore in expr: mi aspettavo (  oppure un NUM");
        return -1;
    }

    }

    private int exprp(int exprp_i) {
	int term_val, exprp_val;
	switch (look.tag) {
	case '+':
            match('+');
            term_val = term();//come da sdt
            exprp_val = exprp(exprp_i + term_val);//come da sdt
            break;
        case '-':
            match('-');
            term_val = term();//come da sdt
            exprp_val = exprp(exprp_i - term_val);//come da sdt
            break;
        default: 
            exprp_val = exprp_i;
            break;
	}
    return exprp_val;
    }

    private int term() { 
        if(look.tag=='('||look.tag==256){
            int term_val,termp_val,fact_val;
            fact_val = fact();
            termp_val = termp(fact_val);//come da sdt
            term_val = termp_val;//come da sdt
            return term_val;
        }
        else{
            error("errore in term: mi aspettavo ( o un NUM");
            return -1;
        }
    }
    
    private int termp(int termp_i) { 
    if(look.tag == '+' || look.tag=='-' || look.tag=='*' || look.tag=='/' || look.tag == Tag.EOF || look.tag == ')'){
	int termp_val,fact_val;
        switch (look.tag) {
        case '*':
                match('*');
                fact_val = fact();
                termp_i = termp_i * fact_val;//come da sdt
                termp_val = termp(termp_i);//come da sdt
                break;
            case '/':
                match('/');
                fact_val = fact();
                termp_i = termp_i / fact_val;//come da sdt
                termp_val = termp(termp_i);//come da sdt
                break;
            default: 
                termp_val = termp_i;//come da sdt
                break;
	    }   
        return termp_val;
        }
        else{
            error("errore in termp: mi aspettavo +,-,*,/,) oppure EOF");
            return -1;
        }
    }
    
    private int fact() { 
        int fact_val,expr_val;
        switch (look.tag) {
            case '(':
                match('(');
                expr_val = expr();
                match(')');
                fact_val = expr_val;//per il return
                break;
            case Tag.NUM:
                fact_val = ((NumberTok)look).digit;//casting per accedere a NumberTok.digit
                match(Tag.NUM);
                break;
            default:
                error("syntax error");
                fact_val = 0;//return fittizio 
                break;
        }
        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\andre\\Desktop\\prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
