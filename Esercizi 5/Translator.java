import java.io.*;

public class Translator {

    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;
    int count_expr = 0;//variabile globale che conta quanti code.emitOperation deve stampare
    boolean flag_for = false;//flag utilizzata per evitare di stampare ulteriori label mentre siamo nello stat del for

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    // continua l'analisi lessicale
    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    // confronta due token
    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) {
                move();
            }
        } else {
            error("syntax error");
        }
    }

    // genera un errore con il messagio della stringa s
    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s + look.tag);
    }

    public void prog() {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{') {
            int lnext_prog = code.newLabel();//label prossima istruzione
            statlist(lnext_prog);
            code.emit(OpCode.GOto, 0);//GoTo label finale
            code.emitLabel(lnext_prog);//label finale
            match(Tag.EOF);
            try {
                code.toJasmin();
            } catch (java.io.IOException e) {
                System.out.println("IO error\n");
            };
        } else {
            error("errore in prog: mi aspettavo Assign, Print, Read, For, If o {");
        }
    }

    public void statlist(int lprev_statlist) {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{') {
            stat(lprev_statlist);
            statlistp(lprev_statlist);
            if (!flag_for) {//se siamo dentro ad un ciclo for non aggiungiamo label
                lprev_statlist = code.newLabel();
                code.emit(OpCode.GOto, lprev_statlist);
                code.emitLabel(lprev_statlist);
            }

        } else {
            error("errore in statlist: mi aspettavo Assign, Print, Read, For, If o {");
        }
    }

    public void statlistp(int lprev_statlistp) {
        switch (look.tag) {
            case ';':
                match(';');
                if (!flag_for) {//se siamo dentro ad un ciclo for non aggiungiamo label
                    lprev_statlistp = code.newLabel();
                    code.emit(OpCode.GOto, lprev_statlistp);
                    code.emitLabel(lprev_statlistp);
                }
                stat(lprev_statlistp);
                statlistp(lprev_statlistp);
                break;
            default:
                break;
        }
    }

    public void stat(int lprev_stat) {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                assignlist(lprev_stat, lprev_stat, true);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist(lprev_stat, true);//true perchè deve stampare ad ogni exprlist
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                code.emit(OpCode.invokestatic, 0);
                match('(');
                idlist(lprev_stat, 0, false, true, false);//true perchè deve leggere il valore per ogni idlist
                match(')');
                break;
            case Tag.FOR://abbiamo fattorizzato questa produzione creando il metodo stat1();
                match(Tag.FOR);
                flag_for = true;//la inizializziamo a true
                match('(');
                stat1(lprev_stat);
                flag_for = false;//una volta terminato il for la possiamo resettare
                break;
            case Tag.IF://abbiamo fattorizzato questa produzione creando il metodo stat2();
                match(Tag.IF);
                match('(');
                int lprev_stat_next = code.newLabel();//label if
                int lprev_stat_next2 = code.newLabel();//label else
                int lprev_stat_next3 = code.newLabel();//label end
                bexpr(lprev_stat_next, lprev_stat_next2);
                match(')');
                code.emit(OpCode.label, lprev_stat_next);//stampa label if
                stat(lprev_stat);//statement dell'if
//                code.emit(OpCode.GOto, lprev_stat_next3);//goto end label, perchè abbiamo finito l'if
                stat2(lprev_stat_next2, lprev_stat_next3);
                break;
            case '{':
                match(Token.lpg.tag);
                statlist(lprev_stat);
                match(Token.rpg.tag);
            case -1:
                break;
            default:
                error("errore in stat: mi aspettavo un Assign, Print, Read, For, If o { invece ho avuto:" + look.tag);
                break;
        }
    }

    public void stat1(int lprev_stat1) {//fattorizzazione for
        int tempLab = lprev_stat1;
        lprev_stat1 = code.newLabel();
        int end_for_label = code.newLabel();
        switch (look.tag) {
            case Tag.ID://case per capire in che produzione siamo,
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                boolean nuovo = false;//indica se la variabile(identificato) è nuova
                if (id_addr == -1) {
                    nuovo = true;
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                match(Tag.INIT);
                expr(lprev_stat1, false);
                if (nuovo) {
                    code.emit(OpCode.istore, id_addr);
                } else {//attenzione all'istore o iload

                    code.emit(OpCode.iload, id_addr);//attenzione all'istore o iload
                }
                code.emitLabel(lprev_stat1);
                match(Token.semicolon.tag);
                bexpr(lprev_stat1, end_for_label);
                match(')');
                match(Tag.DO);
                stat(lprev_stat1);
                code.emit(OpCode.GOto, lprev_stat1);//goto di inizio ciclo
                code.emitLabel(end_for_label);//label di fine ciclo
                break;
            default://se non siamo nella prima produzione saremo sicuramente nell'altra
                int temp_else = code.newLabel();
                bexpr(tempLab, temp_else);
                match(')');
                match(Tag.DO);
                stat(lprev_stat1);
                code.emit(OpCode.GOto, tempLab);//GoTo inizio label ciclo for
                code.emitLabel(temp_else);//emittiamo codice di fine label
                break;
        }
    }

    public void stat2(int lprev_stat2, int end_label) {//fattorizzazione if -- gli passiamo else, end
        switch (look.tag) {//
            case Tag.ELSE://case per la prossima produzione
                match(Tag.ELSE);
                code.emit(OpCode.GOto, end_label);
                code.emitLabel(lprev_stat2);
                stat(lprev_stat2);
                code.emit(OpCode.GOto, end_label);
                code.emitLabel(end_label);
                match(Tag.END);
                break;
            case Tag.END://altra produzione
                match(Tag.END);
                code.emit(OpCode.GOto, lprev_stat2);
                code.emitLabel(lprev_stat2);
                break;
        }
    }

    public void assignlist(int lprev_assignlist, int ldc, boolean stored) {
        if (look.tag == '{' || look.tag == '[') {
            match('[');
            int ldc2 = expr(lprev_assignlist, false);
            match(Tag.TO);
            idlist(lprev_assignlist, ldc, true, false, stored);
            match(']');
            assignlistp(lprev_assignlist, ldc2, stored);
        } else {
            error("errore in assignlist: mi aspettavo { invece ho :" + look.tag);
        }
    }

    public void assignlistp(int lprev_assignlistp, int ldc, boolean stored) {
        if (look.tag == '[') {//per produzione di [ expr, controllo insieme guida
            match('[');
            int ldc2 = expr(lprev_assignlistp, false);
            match(Tag.TO);
            idlist(lprev_assignlistp, ldc, true, false, stored);
            match(']');
            assignlistp(lprev_assignlistp, ldc2, stored);
        } else if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{' || look.tag == ';' || look.tag == Tag.ELSE || look.tag == Tag.END) {
            //per produzione di epsilon, controllo insiemi guida
        } else {
            error("errore in assignlistp: mi aspettavo [ o assign o print o read o for o if o else o end o ; o {");
        }
    }

    public void idlist(int lprev_idlist, int ldc, boolean insert, boolean read, boolean stored) {
        if (look.tag == Tag.ID) {
            int id_addr = st.lookupAddress(((Word) look).lexeme);
            boolean nuovo = false;
            if (id_addr == -1) {
                nuovo = true;
                id_addr = count;
                st.insert(((Word) look).lexeme, count++);
            }
            match(Tag.ID);
            if (nuovo || stored) {
                code.emit(OpCode.istore, id_addr);
            } else {
                if (ldc == -3)//attenzione all'istore o iload
                {
                    code.emit(OpCode.istore, id_addr);
                } else {
                    code.emit(OpCode.iload, id_addr);
                }
            }
            idlistp(ldc, insert, read, stored);
        } else {
            error("errore in idlist: mi aspettavo ID");
        }
    }

    public void idlistp(int ldc, boolean insert, boolean read, boolean stored) {
        if (look.tag == ',') {
            match(',');
            if (read) {//booleano read == boleano che indica una read con più parametri, quindi ad ogni variabile corrisponde una lettura
                //se true allora abbiamo sullo stack già un valore da leggere
                code.emit(OpCode.invokestatic, 0);//quindi invokestatic read
            }
            int id_addr = st.lookupAddress(((Word) look).lexeme);
            boolean nuovo = false;
            if (id_addr == -1) {
                nuovo = true;
                id_addr = count;
                st.insert(((Word) look).lexeme, count++);
            }
            if (insert) {
                code.emit(OpCode.ldc, ldc);//rimetto la costante sulla pila
            }
            match(Tag.ID);
            if (nuovo || stored) {
                code.emit(OpCode.istore, id_addr);
            } else {
                if (ldc == -3)//attenzione all'istore o iload
                {
                    code.emit(OpCode.istore, id_addr);
                } else {
                    code.emit(OpCode.iload, id_addr);
                }
            }
            idlistp(ldc, insert, read, stored);
        } else if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{' || look.tag == ';' || look.tag == Tag.ELSE || look.tag == Tag.END || look.tag == ']' || look.tag == ')') {

        } else {
            error("errore in idlistp : mi aspettavo , o assign o print o read o for o if o { o ; o else o end o ] e invece ho " + look.tag);
        }

    }

    public void bexpr(int lprev_bexpr, int else_label) {
        /*
        Nel primo IF gestiamo le espressioni booleane semplici,
        Nell'else if gestiamo il 5.2 per espressioni booleano complesse.
        */
        if (look.tag == Tag.RELOP) {
            Word tmp = (Word) look;
            match(Tag.RELOP);
            expr(lprev_bexpr, false);
            expr(lprev_bexpr, false);
            switch (((Word) tmp).lexeme) {
                case "<":
                    code.emit(OpCode.if_icmpge, else_label);
                    break;
                case ">":
                    code.emit(OpCode.if_icmple, else_label);
                    break;
                case "==":
                    code.emit(OpCode.if_icmpne, else_label);
                    break;
                case "<=":
                    code.emit(OpCode.if_icmpgt, else_label);
                    break;
                case "!=":
                    code.emit(OpCode.if_icmpeq, else_label);
                    break;
                case ">=":
                    code.emit(OpCode.if_icmplt, else_label);
                    break;
                case "<>":
                    code.emit(OpCode.if_icmpeq, else_label);
                    break;
            }
        } else if(look.tag == Tag.OR || look.tag == Tag.AND || look.tag == Token.not.tag){
            switch (look.tag) {
                case Tag.OR:
                    match(Tag.OR);
                    int label_or2 = code.newLabel();//label per secondo statement dell'or
                    bexpr(lprev_bexpr,label_or2);
                    code.emit(OpCode.GOto,lprev_bexpr);//else-label -1 == IF label, con gli statement dell'if
                    code.emitLabel(label_or2);
                    bexpr(lprev_bexpr,else_label);
                    code.emit(OpCode.GOto,lprev_bexpr);//else-label -1 == IF label, con gli statement dell'if
                    break;
                case Tag.AND:
                    match(Tag.AND);
                    int label_and2 = code.newLabel();//label per il secondo statement dell'and
                    bexpr(lprev_bexpr,else_label);
                    code.emit(OpCode.GOto, label_and2);//goto Label_and2, qua siamo nel caso dove la prima condizione è verificata, bisogna verificare ancora la seconda
                    code.emitLabel(label_and2);//stampiamo la seconda label
                    bexpr(lprev_bexpr,else_label);
                    code.emit(OpCode.GOto,lprev_bexpr);//else-label - 1 == if_label
                    break;
                default:
                    match(Token.not.tag);
                    bexpr(lprev_bexpr,else_label-1);//invertiamo gli operandi, passiamo a bexpr quindi if al posto di else
                    code.emit(OpCode.GOto, else_label);
                    break;
            }
        } else {
            error("errore in bexpr: mi aspettavo RELOP");
        }

    }

    public int expr(int lprev_expr, boolean increment) {
        int ret = -1;
        int i = 1;
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist(lprev_expr, false);
                match(')');
                do {
                    code.emit(OpCode.iadd);
                    this.count_expr--;
                } while (i <= this.count_expr);
                this.count_expr = 0;
                break;
            case '-':
                match('-');
                expr(lprev_expr, increment);
                expr(lprev_expr, increment);
                do {
                    code.emit(OpCode.isub);
                    this.count_expr--;
                } while (i <= this.count_expr);
                this.count_expr = 0;

                break;
            case '*':
                match('*');
                match('(');
                exprlist(lprev_expr, false);
                match(')');
                do {
                    code.emit(OpCode.imul);
                    this.count_expr--;
                } while (i <= this.count_expr);
                this.count_expr = 0;
                break;
            case '/':
                match('/');
                expr(lprev_expr, increment);
                expr(lprev_expr, increment);
                do {
                    code.emit(OpCode.idiv);
                    this.count_expr--;
                } while (i <= this.count_expr);
                this.count_expr = 0;
                break;
            case Tag.NUM:
                int num = ((NumberTok) look).digit;
                code.emit(OpCode.ldc, num);
                ret = ((NumberTok) look).digit;
                match(Tag.NUM);
                if (increment) {
                    this.count_expr++;
                }
                break;
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                boolean nuovo = false;
                if (id_addr == -1) {
                    nuovo = true;
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                if (nuovo || ret == -3) {//se ret == -3 allora sulla pila c'è una costante da assegnare alla variabile, quindi istore
                    code.emit(OpCode.istore, id_addr);
                } else {//attenzione all'istore o iload
                    ret = -3;//attenzione all'istore o iload
                    code.emit(OpCode.iload, id_addr);//attenzione all'istore o iload
                }
                if (increment) {
                    this.count_expr++;
                }
                break;
            case -1:
                break;
            default:
                error("errore in expr : mi aspettavo + - / * NUM ID O EOF");
                break;
        }
        return ret;
    }

    public void exprlist(int lprev_exprlist, boolean print) {
        if (look.tag == Token.plus.tag || look.tag == Token.minus.tag || look.tag == Token.mult.tag || look.tag == Token.div.tag || look.tag == Tag.NUM || look.tag == Tag.ID) {
            expr(lprev_exprlist, false);
            if (print) {//se print == true allora sulla pila c'è qualcosa da stampare
                code.emit(OpCode.invokestatic, 1);
            }
            exprlistp(lprev_exprlist, true, print);

        } else {
            error("errore in exprlist : mi aspettavo + / - * o NUM o ID");
        }

    }

    public void exprlistp(int lprev_exprlistp, boolean increment_expr, boolean print) {
        if (look.tag == ',') {
            match(',');
            expr(lprev_exprlistp, increment_expr);
            if (print) {
                code.emit(OpCode.invokestatic, 1);
            }
            exprlistp(lprev_exprlistp, increment_expr, print);
        } else if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{' || look.tag == ';' || look.tag == Tag.ELSE || look.tag == Tag.END || look.tag == ')') {

        } else {
            error("errore in exprlistp : mi aspettavo , assign o print o read o for o if o { o ; o else o end o )");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "C:\\Users\\andre\\Documents\\NetBeansProjects\\Tester\\src\\tester\\prova.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
