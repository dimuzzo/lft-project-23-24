import java.io.*;

public class parser_linguaggio {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public parser_linguaggio(Lexer l, BufferedReader br) {
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

    void match(int t) {//controllo simboli terminali
	if (look.tag == t) {
	    if (look.tag != Tag.EOF) move();//se EOF allora fine file, non avanziamo più perchè è impossibile
	} else error("syntax error");
    }

    public void prog(){
        if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{')//controllo insiemi guida
        {
            statlist();
            match(Tag.EOF);//match simbolo terminale
        }
        else{
            error("errore in prog: mi aspettavo Assign, Print, Read, For, If o {");
        } 
    }

    public void statlist(){
        if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{')//controllo insiemi guida
        {
            stat();
            statlistp();
        }
        else{
            error("errore in statlist: mi aspettavo Assign, Print, Read, For, If o {");
        }
    }

    public void statlistp(){
        switch(look.tag){
            case ';':
                match(';');//controllo simbolo terminale
                stat();
                statlistp();
                break;
            default:
                break;
        }
    }

    public void stat(){
        switch(look.tag){
            case Tag.ASSIGN:
                match(Tag.ASSIGN);//controllo simbolo terminale
                assignlist();
                break;
            case Tag.PRINT:
                match(Tag.PRINT);//controllo simbolo terminale
                match('(');//controllo simbolo terminale
                exprlist();
                match(')');//controllo simbolo terminale
                break;
            case Tag.READ:
                match(Tag.READ);//controllo simbolo terminale
                match('(');//controllo simbolo terminale
                idlist();
                match(')');//controllo simbolo terminale
                break;
            case Tag.FOR:
                match(Tag.FOR);//controllo simbolo terminale
                match('(');//controllo simbolo terminale
                stat1();
                break;
            case Tag.IF:
                match(Tag.IF);//controllo simbolo terminale
                match('(');//controllo simbolo terminale
                bexpr();
                match(')');//controllo simbolo terminale
                stat();
                stat2();
                break;
            case '{':
                match(Token.lpg.tag);//controllo simbolo terminale
                statlist();
                match(Token.rpg.tag);//controllo simbolo terminale
            case -1:
                break;
            default:
                error("errore in stat: mi aspettavo un Assign, Print, Read, For, If o { invece ho avuto:"+look.tag);
                break;
        }
    }

    public void stat1(){
        switch(look.tag){
            case Tag.ID:
                match(Tag.ID);//controllo simbolo terminale
                match(Tag.INIT);//controllo simbolo terminale
                expr();
                match(Token.semicolon.tag);//controllo simbolo terminale
                bexpr();
                match(')');//controllo simbolo terminale
                match(Tag.DO);//controllo simbolo terminale
                stat();
                break;
            default:
                bexpr();
                match(')');//controllo simbolo terminale
                match(Tag.DO);//controllo simbolo terminale
                stat();
                break;
        }
    }

    public void stat2(){
        switch(look.tag){
            case Tag.ELSE:
                match(Tag.ELSE);//controllo simbolo terminale
                stat();
                match(Tag.END);//controllo simbolo terminale
                break;
            case Tag.END:
                match(Tag.END);//controllo simbolo terminale
                break;
        }
    }


    public void assignlist(){
        if(look.tag == '{' || look.tag == '['){//controllo insiemi guida
            match('[');//controllo simbolo terminale
            expr();
            match(Tag.TO);//controllo simbolo terminale
            idlist();
            match(']');//controllo simbolo terminale
            assignlistp();
        }
        else{
            error("errore in assignlist: mi aspettavo { invece ho :"+look.tag);
        }
    }
    
    public void assignlistp(){
        if(look.tag == '['){//controllo insiemi guida
            match('[');//controllo simbolo terminale
            expr();
            match(Tag.TO);//controllo simbolo terminale
            idlist();
            match(']');
            assignlistp();
        }
        else if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{' || look.tag == ';' || look.tag == Tag.ELSE || look.tag == Tag.END){//controllo insiemi guida
            
        }
        else{
            error("errore in assignlistp: mi aspettavo [ o assign o print o read o for o if o else o end o ; o {");
        }
            
                
    }

    public void idlist(){
        if(look.tag == Tag.ID){//controllo insiemi guida
           match(Tag.ID);//controllo simbolo terminale
           idlistp(); 
        }
        else{
            error("errore in idlist: mi aspettavo ID");
        }
    }

    public void idlistp(){
        if(look.tag == ','){//controllo insiemi guida
            match(',');//controllo simbolo terminale
            match(Tag.ID);//controllo simbolo terminale
            idlistp();
        }
        else if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{' || look.tag == ';' || look.tag == Tag.ELSE || look.tag == Tag.END || look.tag == ']' || look.tag == ')'){//controllo insiemi guida produzione --> epsilon
            
        }
        else{
            error("errore in idlistp : mi aspettavo , o assign o print o read o for o if o { o ; o else o end o ] e invece ho "+ look.tag);
        }
            
    }

    public void bexpr(){
        if(look.tag == Tag.RELOP){//controllo insiemi guida
            match(Tag.RELOP);//controllo simbolo terminale
            expr();
            expr();
        }
        else{
            error("errore in bexpr: mi aspettavo RELOP");
        }
        
    }

    public void expr(){
        switch(look.tag){
            case'+':
                match('+');//controllo simbolo terminale
                match('(');//controllo simbolo terminale
                exprlist();
                match(')');//controllo simbolo terminale
                break;
            case'-':
                match('-');//controllo simbolo terminale
                expr();
                expr();
                break;
            case'*':
                match('*');//controllo simbolo terminale
                match('(');//controllo simbolo terminale
                exprlist();
                match(')');//controllo simbolo terminale
                break;
            case'/':
                match('/');//controllo simbolo terminale
                expr();
                expr();
                break;
            case Tag.NUM:
                match(Tag.NUM);//controllo simbolo terminale
                break;
            case Tag.ID:
                match(Tag.ID);//controllo simbolo terminale
                break;
            case -1:
                break;
            default:
                error("errore in expr : mi aspettavo + - / * NUM ID O EOF");
                break;
        }
    }

    public void exprlist(){
        if(look.tag == Token.plus.tag || look.tag == Token.minus.tag || look.tag == Token.mult.tag || look.tag == Token.div.tag || look.tag == Tag.NUM || look.tag == Tag.ID){//controllo insiemi guida
            expr();
            exprlistp();
        }
        else{
            error("errore in exprlist : mi aspettavo + / - * o NUM o ID");
        }
        
    }

    public void exprlistp(){
        if(look.tag == ','){//controllo insiemi guida
            match(',');//controllo simbolo terminale
            expr();
            exprlistp();
        }
        else if(look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{' || look.tag == ';' || look.tag == Tag.ELSE || look.tag == Tag.END || look.tag == ')'){//controllo insiemi guida produzioni epsilon
            
            
        }
        
        else{
            error("errore in exprlistp : mi aspettavo , assign o print o read o for o if o { o ; o else o end o )");
        }
    }
		
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\andre\\Desktop\\prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            parser_linguaggio parser = new parser_linguaggio(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
