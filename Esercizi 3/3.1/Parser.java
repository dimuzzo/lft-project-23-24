import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
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

    void match(int t) {//per controllare i simboli terminali, come da direttive
	if (look.tag == t) {
	    if (look.tag != Tag.EOF) move();
	} else error("syntax error");
    }

    public void start() {//metodo iniziale
	if(look.tag=='('||look.tag==256){//controllo se il token che stiamo analizzando è nell'insieme guida
            expr();//richiamo expr
            match(Tag.EOF);//controllo simbolo terminale
    }else{
            error("errore in start: mi aspettavo ( oppure un NUM");
        }
	// ... completare ...
    }

    private void expr() {
    if(look.tag=='('||look.tag==256){//controllo se il token che stiamo analizzando è nell'insieme guida
        term();//richiamo term
        exprp();//richiamo exprp
    }
    else{
        error("errore in expr: mi aspettavo (  oppure un NUM");
    }
    }

    private void exprp() {
	switch (look.tag) {
	    case '+':
            match('+');//controllo simbolo terminale
            term();//richiamo term
            exprp();//richiamo exprp
            break;
        case '-':
            match('-');//controllo simbolo terminale
            term();//richiamo term
            exprp();//richiamo exprp
            break;
        default:
            break;
	}
    }

    private void term() {
        if(look.tag=='('||look.tag==256){//controllo se il token che stiamo analizzando è nell'insieme guida
            fact();//richiamo fact
            termp();//richiamo termp
        }
        else{
            error("errore in term: mi aspettavo ( o un NUM");
        }
        
    }

    private void termp() {
        if(look.tag == '+' || look.tag=='-' || look.tag=='*' || look.tag=='/' || look.tag == Tag.EOF || look.tag == ')'){//controllo se il token che stiamo analizzando è nell'insieme guida
        switch (look.tag) {
            case '*':
                match('*');//controllo simbolo terminale
                fact();//richiamo fact
                termp();//richiamo termp
                break;
            case '/':
                match('/');//controllo simbolo terminale
                fact();//richiamo fact
                termp();//richiamo termp
                break;
            default:
                break;
        }
        }
        else{
            error("errore in termp: mi aspettavo +,-,*,/,) oppure EOF");
        }
    }

    private void fact() {
        if(look.tag == '('){//controllo se il token che stiamo analizzando è nell'insieme guida
            match('(');//controllo simbolo terminale
            expr();//richiamo expr
            match(')');//controllo simbolo terminale
        }
        else if(look.tag == 256){
            match(Tag.NUM);//controllo simbolo terminale
        }
        else{
            error("errore in fact: mi aspettavo '(' o NUM");
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\andre\\Desktop\\prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
