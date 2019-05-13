import java.util.Arrays;

public class TypeFinder {

    private Operation op;
    private int[] args;
    private int neutral;

    public TypeFinder(Operation op) {
        this.op = op;
        args = op.getElements();
        neutral = 0;
    }

    public Type getType() {
        if(!op.isClosed()){
            if(isAssociative()){
                if(isContainNeutralElem()){
                    if(isEachHasReverseElement()){
                        if(isCommutative()){
                            return Type.ABELIAN_GROUP;
                        }
                        return Type.GROUP;
                    }
                    if(isCommutative()){
                        return Type.COMMUTATIVE_MONOID;
                    }
                    return Type.MONOID;
                }
                return Type.SEMI_GROUP;
            }
            return Type.MAGMA;
        }
        return Type.NOTHING;
    }

    public boolean isAssociative() {
        for (int arg : args) {
            for (int arg1 : args) {
                for (int arg2 : args) {
                    if (op.calculate(op.calculate(arg, arg1), arg2) !=
                            op.calculate(arg, op.calculate(arg1, arg2))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean isCommutative() {
        for (int arg : args) {
            for (int arg1 : args) {
                if (op.calculate(arg, arg1) != op.calculate(arg1, arg)) {
                    return false;
                }

            }
        }

        return true;
    }

    public boolean isContainNeutralElem() {
        for (int arg : args) {
            int[] column = op.getTableColumn(arg);
            if (Arrays.equals(column, args)){
                int[] raw = op.getTableRaw(arg);
                if (Arrays.equals(raw,args)) {
                    neutral = arg;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isEachHasReverseElement() {
        boolean flag = true;

        for (int arg : args) {
            int[] column = op.getTableColumn(arg);

            for (int i : column) {
                if (i == neutral) {
                    flag = false;
                }
            }

            if (flag) {
                return false;
            }

            flag = true;
        }

        return true;
    }

}
