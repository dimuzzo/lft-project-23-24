public class NumberTok extends Token {
    public int digit;//valore del num
    public NumberTok(int n) { super(Tag.NUM); this.digit=n; }//un solo costruttore, il tag è sempre lo stesso (256)
    public String toString() { return "<" + super.tag + ", " + digit + ">"; }//toString()
}