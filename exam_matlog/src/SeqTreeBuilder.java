import formulaTree.FormulaTree;
import formulaTree.Node;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SeqTreeBuilder {

    public static final int NEGATIVE_MODE = 0;
    public static final int POSITIVE_MODE = 1;

    private static final int LEFT_BRANCH = 0;
    private static final int RIGHT_BRANCH = 1;
    private static final int NEGATIVE_BRANCH = 0;


    public SeqTreeBuilder() {

    }

    public static void build(FormulaTree tree, String outputFileName) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph {\n");

        build(new Sequence(tree), sb);
        sb.append("}\n");

        saveToFile(sb, outputFileName);
    }

    private static void build(Sequence sequence, StringBuilder sb) {

        int mode;

        Node tree = sequence.getFirstPositive();

        if (tree != null) {
            mode = POSITIVE_MODE;
        } else {
            tree = sequence.getFirstNegative();
            mode = NEGATIVE_MODE;

            if (tree == null) {
                return;
            }
        }

        step(tree, sequence, mode, sb);
    }

    private static void step(Node tree, Sequence sequence, int mode, StringBuilder sb) {


        switch (tree.getOp()) {
            case CONSEQUENCE:
                consequenceRuleApplication(sb, mode, sequence, tree);
                break;
            case DISJUNCTION:
                disjunctionRuleApplication(sb, mode, sequence, tree);
                break;
            case CONJUNCTION:
                conjunctionRuleApplication(sb, mode, sequence, tree);
                break;
            case NEGATION:
                negationRuleApplication(sb, mode, sequence, tree);
                break;
        }
    }

    private static void consequenceRuleApplication(StringBuilder sb, int mode, Sequence sequence, Node tree) {
        FormulaTree[] br = tree.getBranches();

        if (mode == POSITIVE_MODE) {
            String str = sequence.toString();
            sequence.removeFormula(POSITIVE_MODE, tree);

            Sequence s1 = new Sequence(sequence);
            s1.addToPositive(br[RIGHT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);

            Sequence s2 = new Sequence(sequence);
            s2.addToNegative(br[LEFT_BRANCH]);
            write(sb, str, s2.toString());
            build(s2, sb);
        } else {
            String str = sequence.toString();
            sequence.removeFormula(NEGATIVE_MODE, tree);
            Sequence s1 = new Sequence(sequence);
            s1.addToPositive(br[LEFT_BRANCH]);
            s1.addToNegative(br[RIGHT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);
        }
    }

    private static void disjunctionRuleApplication(StringBuilder sb, int mode, Sequence sequence, Node tree) {
        FormulaTree[] br = tree.getBranches();

        if (mode == POSITIVE_MODE) {
            String str = sequence.toString();
            sequence.removeFormula(POSITIVE_MODE, tree);

            Sequence s1 = new Sequence(sequence);
            s1.addToPositive(br[LEFT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);

            Sequence s2 = new Sequence(sequence);
            s2.addToPositive(br[RIGHT_BRANCH]);
            write(sb, str, s2.toString());
            build(s2, sb);
        } else {
            String str = sequence.toString();
            sequence.removeFormula(NEGATIVE_MODE, tree);
            Sequence s1 = new Sequence(sequence);
            s1.addToNegative(br[LEFT_BRANCH]);
            s1.addToNegative(br[RIGHT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);
        }
    }

    private static void conjunctionRuleApplication(StringBuilder sb, int mode, Sequence sequence, Node tree) {
        FormulaTree[] br = tree.getBranches();

        if (mode == NEGATIVE_MODE) {
            String str = sequence.toString();
            sequence.removeFormula(NEGATIVE_MODE, tree);

            Sequence s1 = new Sequence(sequence);
            s1.addToNegative(br[LEFT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);

            Sequence s2 = new Sequence(sequence);
            s2.addToNegative(br[RIGHT_BRANCH]);
            write(sb, str, s2.toString());
            build(s2, sb);
        } else {
            String str = sequence.toString();
            sequence.removeFormula(POSITIVE_MODE, tree);
            Sequence s1 = new Sequence(sequence);
            s1.addToPositive(br[LEFT_BRANCH]);
            s1.addToPositive(br[RIGHT_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);
        }
    }

    private static void negationRuleApplication(StringBuilder sb, int mode, Sequence sequence, Node tree) {
        FormulaTree[] br = tree.getBranches();

        if (mode == POSITIVE_MODE) {
            String str = sequence.toString();
            sequence.removeFormula(POSITIVE_MODE, tree);

            Sequence s1 = new Sequence(sequence);
            s1.addToNegative(br[NEGATIVE_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);

        } else {
            String str = sequence.toString();
            sequence.removeFormula(NEGATIVE_MODE, tree);
            Sequence s1 = new Sequence(sequence);
            s1.addToPositive(br[NEGATIVE_BRANCH]);
            write(sb, str, s1.toString());
            build(s1, sb);
        }
    }

    private static void write(StringBuilder sb, String s1, String s2) {
        sb.append('"').append(s1).append('"').append(" -> ").append('"').append(s2).append("\"\n");
    }

    private static void saveToFile(StringBuilder sb, String outputFileName) {
        try {
            FileWriter writer = new FileWriter(new File(outputFileName + ".dot"));

            writer.write(sb.toString());

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
