package formulaTree;

public enum LogicOperation {
    CONSEQUENCE("=>"){
        public LogicOperation next() { return DISJUNCTION;}
    },
    DISJUNCTION("|"){
        public LogicOperation next() { return CONJUNCTION;}
    },
    CONJUNCTION("&"){
        public LogicOperation next() { return NEGATION;}
    },
    NEGATION("-"){
        public LogicOperation next() { return END_OP;}
    },
    END_OP("") {
        public LogicOperation next() { return null;}
    };

    LogicOperation(String val) {
        this.val = val;
    }
    String val;

    public String getStringVal() {
        return val;
    }

    public abstract LogicOperation next();

}
