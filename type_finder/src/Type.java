public enum Type {
    //                               elem    rev   comm    ass
    NOTHING           (new boolean[]{false, false, false, false}){
        public Type next() { return MAGMA;}
    },
    MAGMA             (new boolean[]{false, false, false, false}){
        public Type next() { return SEMI_GROUP;}
    },
    SEMI_GROUP        (new boolean[]{false, false, false, true}){
        public Type next() { return GROUP;}
    },
    GROUP             (new boolean[]{true,  true,  false, true}){
        public Type next() { return ABELIAN_GROUP;}
    },
    ABELIAN_GROUP     (new boolean[]{true,  true,  true,  true}){
        public Type next() { return MONOID;}
    },
    MONOID            (new boolean[]{true,  false, false, true}){
        public Type next() { return COMMUTATIVE_MONOID;}
    },
    COMMUTATIVE_MONOID(new boolean[]{true,  false, true,  true}){
        public Type next() { return null;}
    };

    Type(boolean[] label) {
        this.label = label;
    }

    boolean[]  label;

    public abstract Type next();

    public boolean[] getLabel() {
        return label;
    }
}
