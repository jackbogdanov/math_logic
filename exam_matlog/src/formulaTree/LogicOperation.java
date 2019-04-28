package formulaTree;

public enum LogicOperation {
    IMPLICATION("->", 0x2192){
        public LogicOperation next() { return DISJUNCTION;}
    },
    DISJUNCTION("|", 0x2228){
        public LogicOperation next() { return CONJUNCTION;}
    },
    CONJUNCTION("&", 0x2227){
        public LogicOperation next() { return NEGATION;}
    },
    NEGATION("-", 0x00AC){
        public LogicOperation next() { return END_OP;}
    },
    END_OP("", 1) {
        public LogicOperation next() { return null;}
    };

    LogicOperation(String val, int unicodeVal) {
        this.val = val;
        this.unicodeVal = Character.toString((char) unicodeVal);
    }
    String val;
    String unicodeVal;

    public String getStringVal() {
        return val;
    }
    public String getUnicodeVal() {return unicodeVal;}

    public abstract LogicOperation next();

}
