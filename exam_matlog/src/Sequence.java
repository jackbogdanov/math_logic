import formulaTree.FormulaTree;
import formulaTree.Node;

import java.util.ArrayList;

public class Sequence {

    private ArrayList<FormulaTree> positive;
    private ArrayList<FormulaTree> negative;

    public Sequence(FormulaTree start) {
        negative = new ArrayList<>();
        negative.add(start);
        positive = new ArrayList<>();
    }

    public Sequence(Sequence sequence) {
        negative = new ArrayList<>(sequence.getNegative());
        positive = new ArrayList<>(sequence.getPositive());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (FormulaTree t : positive) {
            sb.append(t.getStringForm()).append("; ");
        }
        cleanUp(sb);

        sb.append("||| ");

        for (FormulaTree t : negative) {
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

    public ArrayList<FormulaTree> getPositive() {
        return positive;
    }

    public ArrayList<FormulaTree> getNegative() {
        return negative;
    }

    public void removeFormula(int mode, FormulaTree tree) {
        if (mode == SeqTreeBuilder.NEGATIVE_MODE) {
            negative.remove(tree);
        } else {
            positive.remove(tree);
        }
    }

    public void addToNegative(FormulaTree tree) {
        negative.add(tree);
    }

    public void addToPositive(FormulaTree tree) {
        positive.add(tree);
    }

    public Node getFirstPositive() {

        for (int i = 0; i < positive.size(); i++) {
            FormulaTree tree = positive.get(i);
            if (!tree.getStringForm().matches("[a-z]")) {
                return (Node) tree;
            }
        }

        return null;
    }

    public Node getFirstNegative() {
        for (int i = 0; i < negative.size(); i++) {
            FormulaTree tree = negative.get(i);
            if (!tree.getStringForm().matches("[a-z]")) {
                return (Node) tree;
            }
        }

        return null;
    }
}
