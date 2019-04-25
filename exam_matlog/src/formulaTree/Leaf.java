package formulaTree;

public class Leaf implements FormulaTree {

    private String var;

    public Leaf(String var) {
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public String getStringForm() {
        return var;
    }

    @Override
    public void changeStringForm(String newVar) {
        var = newVar;
    }
}
