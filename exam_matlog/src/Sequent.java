import formulaTree.FormulaTree;
import formulaTree.Node;

import java.util.ArrayList;

public class Sequent {

    private ArrayList<FormulaTree> antecedent;
    private ArrayList<FormulaTree> succedent;

    public Sequent(FormulaTree start) {
        succedent = new ArrayList<>();
        succedent.add(start);
        antecedent = new ArrayList<>();
    }

    public Sequent(Sequent sequent) {
        succedent = new ArrayList<>(sequent.getSuccedent());
        antecedent = new ArrayList<>(sequent.getAntecedent());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (FormulaTree t : antecedent) {
            sb.append(t.getStringForm()).append("; ");
        }
        cleanUp(sb);

        int turnstileSymbolCode = 0x22A2;
        sb.append(Character.toString((char) turnstileSymbolCode)).append(' ');

        for (FormulaTree t : succedent) {
            sb.append(t.getStringForm()).append("; ");
        }

        cleanUp(sb);
        return sb.toString().trim();
    }

    private void cleanUp(StringBuilder sb) {
        int index = sb.lastIndexOf(";");
        if (index != -1) {
            sb.deleteCharAt(index);
        }
    }

    public boolean isContainVar(String var) {
        for (FormulaTree node: antecedent) {
            if (node.getStringForm().equals(var)) {
                return true;
            }
        }

        for (FormulaTree node: succedent) {
            if (node.getStringForm().equals(var)) {
                return true;
            }
        }

        return false;
    }

    public ArrayList<FormulaTree> getAntecedent() {
        return antecedent;
    }

    public ArrayList<FormulaTree> getSuccedent() {
        return succedent;
    }

    public void removeFormula(int mode, FormulaTree tree) {
        if (mode == SeqTreeBuilder.NEGATIVE_MODE) {
            succedent.remove(tree);
        } else {
            antecedent.remove(tree);
        }
    }

    public void addToSuccedent(FormulaTree tree) {
        succedent.add(tree);
    }

    public void addToAntecedent(FormulaTree tree) {
        antecedent.add(tree);
    }

    public Node getFirstFromAntecedent() {

        for (FormulaTree tree : antecedent) {
            if (!tree.getStringForm().matches("[a-z]")) {
                return (Node) tree;
            }
        }

        return null;
    }

    public Node getFirstFromSuccedent() {
        for (FormulaTree tree : succedent) {
            if (!tree.getStringForm().matches("[a-z]")) {
                return (Node) tree;
            }
        }

        return null;
    }

    public boolean isAxiom() {
        for (FormulaTree tr_p : antecedent) {
            for (FormulaTree tr_n : succedent){
                if (tr_p.getStringForm().equals(tr_n.getStringForm())) {
                    return true;
                }
            }
        }

        return false;
    }
}
