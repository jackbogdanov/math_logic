package formulaTree;

public class Node implements FormulaTree {

    private FormulaTree[] branches;
    private LogicOperation op;
    private String stringForm;

    public Node(FormulaTree[] branches, LogicOperation op, String stringForm) {
        this.branches = branches;
        this.op = op;
        this.stringForm = stringForm;
    }

    public FormulaTree[] getBranches(){
        return branches;
    }

    public void setBranches(FormulaTree[] branches) {
        this.branches = branches;
    }

    @Override
    public String toString() {
        return op.getStringVal();
    }

    @Override
    public String getStringForm() {
        return stringForm;
    }

    public void changeStringForm(String newForm) {
        stringForm = newForm;
    }

    public LogicOperation getOp() {
        return op;
    }
}
