import formulaTree.FormulaTree;
import formulaTree.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SeqTreeBuilder {

    public static final int NEGATIVE_MODE = 0;
    public static final int POSITIVE_MODE = 1;

    private static final int LEFT_BRANCH = 0;
    private static final int RIGHT_BRANCH = 1;
    private static final int NEGATIVE_BRANCH = 0;

    private Sequent counterExample;
    private String inputString;

    public SeqTreeBuilder(String inputString) {
        counterExample = null;
        this.inputString = inputString;
    }

    public void build(FormulaTree tree, String outputFileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n");
        sb.append("rankdir = BT\n");

        build(new Sequent(tree), sb);
        sb.append("}\n");

        saveToFile(sb, outputFileName);

        if (counterExample == null) {
            System.out.println("CounterExample not found");
        } else {
            printResult(counterExample);

            String[] allVariables = inputString.split("[^a-z]");

            for (String var : allVariables) {
                if (!var.equals("")) {
                    if (!counterExample.isContainVar(var)) {
                        System.out.println(var + " = 0\n");
                    }
                }
            }

        }
    }

    private void build(Sequent sequent, StringBuilder sb) {

        int mode;

        Node tree = sequent.getFirstFromAntecedent();

        if (tree != null) {
            mode = POSITIVE_MODE;
        } else {
            tree = sequent.getFirstFromSuccedent();
            mode = NEGATIVE_MODE;

            if (tree == null) {

                if (sequent.isAxiom()) {
                    sb.append('\"').append(sequent).append('\"').
                            append("[style=\"filled\",fillcolor=\"red\"];\n");
                } else {
                    sb.append('\"').append(sequent).append('\"').
                            append("[style=\"filled\",fillcolor=\"green\"];\n");
                    if (counterExample == null) {
                        counterExample = sequent;
                    }
                }

                return;
            }
        }

        step(tree, sequent, mode, sb);
    }

    private void printResult(Sequent sequent) {
        ArrayList<FormulaTree> list = sequent.getAntecedent();

        for (FormulaTree f: list) {
            System.out.println(f + " = 1");
        }

        list = sequent.getSuccedent();

        for (FormulaTree f: list) {
            System.out.println(f + " = 0");
        }
    }

    private void step(Node tree, Sequent sequent, int mode, StringBuilder sb) {


        switch (tree.getOp()) {
            case IMPLICATION:
                implicationRuleApplication(sb, mode, sequent, tree);
                break;
            case DISJUNCTION:
                disjunctionRuleApplication(sb, mode, sequent, tree);
                break;
            case CONJUNCTION:
                conjunctionRuleApplication(sb, mode, sequent, tree);
                break;
            case NEGATION:
                negationRuleApplication(sb, mode, sequent, tree);
                break;
        }
    }

    private void implicationRuleApplication(StringBuilder sb, int mode, Sequent sequent, Node tree) {
        FormulaTree[] br = tree.getBranches();

        if (mode == POSITIVE_MODE) {
            String str = sequent.toString();
            sequent.removeFormula(POSITIVE_MODE, tree);

            Sequent s1 = new Sequent(sequent);
            s1.addToAntecedent(br[RIGHT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);

            Sequent s2 = new Sequent(sequent);
            s2.addToSuccedent(br[LEFT_BRANCH]);
            write(sb, str, s2.toString());
            build(s2, sb);
        } else {
            String str = sequent.toString();
            sequent.removeFormula(NEGATIVE_MODE, tree);
            Sequent s1 = new Sequent(sequent);
            s1.addToAntecedent(br[LEFT_BRANCH]);
            s1.addToSuccedent(br[RIGHT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);
        }
    }

    private void disjunctionRuleApplication(StringBuilder sb, int mode, Sequent sequent, Node tree) {
        FormulaTree[] br = tree.getBranches();

        if (mode == POSITIVE_MODE) {
            String str = sequent.toString();
            sequent.removeFormula(POSITIVE_MODE, tree);

            Sequent s1 = new Sequent(sequent);
            s1.addToAntecedent(br[LEFT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);

            Sequent s2 = new Sequent(sequent);
            s2.addToAntecedent(br[RIGHT_BRANCH]);
            write(sb, str, s2.toString());
            build(s2, sb);
        } else {
            String str = sequent.toString();
            sequent.removeFormula(NEGATIVE_MODE, tree);
            Sequent s1 = new Sequent(sequent);
            s1.addToSuccedent(br[LEFT_BRANCH]);
            s1.addToSuccedent(br[RIGHT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);
        }
    }

    private void conjunctionRuleApplication(StringBuilder sb, int mode, Sequent sequent, Node tree) {
        FormulaTree[] br = tree.getBranches();

        if (mode == NEGATIVE_MODE) {
            String str = sequent.toString();
            sequent.removeFormula(NEGATIVE_MODE, tree);

            Sequent s1 = new Sequent(sequent);
            s1.addToSuccedent(br[LEFT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);

            Sequent s2 = new Sequent(sequent);
            s2.addToSuccedent(br[RIGHT_BRANCH]);
            write(sb, str, s2.toString());
            build(s2, sb);
        } else {
            String str = sequent.toString();
            sequent.removeFormula(POSITIVE_MODE, tree);
            Sequent s1 = new Sequent(sequent);
            s1.addToAntecedent(br[LEFT_BRANCH]);
            s1.addToAntecedent(br[RIGHT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);
        }
    }

    private void negationRuleApplication(StringBuilder sb, int mode, Sequent sequent, Node tree) {
        FormulaTree[] br = tree.getBranches();

        if (mode == POSITIVE_MODE) {
            String str = sequent.toString();
            sequent.removeFormula(POSITIVE_MODE, tree);

            Sequent s1 = new Sequent(sequent);
            s1.addToSuccedent(br[NEGATIVE_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);

        } else {
            String str = sequent.toString();
            sequent.removeFormula(NEGATIVE_MODE, tree);
            Sequent s1 = new Sequent(sequent);
            s1.addToAntecedent(br[NEGATIVE_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);
        }
    }

    private void write(StringBuilder sb, String s1, String s2) {
        sb.append('"').append(s1).append('"').append(" -> ").append('"').append(s2).append("\"\n");
    }

    private void saveToFile(StringBuilder sb, String outputFileName) {
        try {
            FileWriter writer = new FileWriter(new File(outputFileName + ".dot"));

            writer.write(sb.toString());

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
