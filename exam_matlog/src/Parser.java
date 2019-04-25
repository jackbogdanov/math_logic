import formulaTree.Leaf;
import formulaTree.LogicOperation;
import formulaTree.Node;
import formulaTree.FormulaTree;

import java.util.HashMap;
import java.util.Map;
public class Parser {

    public Parser() {

    }

    public static FormulaTree parseWithParentheses(String input) {
        int openBr = 0;
        int i = 0;
        int len = input.length();
        char key = 'A';
        Map<Character, FormulaTree> map = new HashMap<Character, FormulaTree>();

        int[] indexes = new int[input.length()];
        int lastOpenBrPoint = -1;

        while (i < len) {
            if (input.charAt(i) == '(') {
                openBr++;
                lastOpenBrPoint++;
                indexes[lastOpenBrPoint] = i;
            }

            if (input.charAt(i) == ')') {
                if (openBr == 0) {
                    System.out.println("Parse error!");
                    return null;
                } else {
                    map.put(key, parse(input.substring(indexes[lastOpenBrPoint] + 1, i)));
                    input = input.substring(0, indexes[lastOpenBrPoint]) + key + input.substring(i + 1);
                    len = input.length();

                    i = indexes[lastOpenBrPoint];
                    lastOpenBrPoint--;
                    key++;
                }
            }

            i++;
        }

        FormulaTree mainTree = parse(input);

        return treeBuild(mainTree, map);
    }

    private static FormulaTree treeBuild(FormulaTree mainTree, Map<Character, FormulaTree> map) {
        if (mainTree instanceof Node) {
            mainTree.changeStringForm(correctStringForms(mainTree.getStringForm(), map));
            FormulaTree[] br = ((Node) mainTree).getBranches();
            for (int i = 0; i < br.length; i++) {
                if (br[i] instanceof Leaf){
                    String var = ((Leaf) br[i]).getVar();
                    if (var.matches("[A-Z]")) {
                        br[i] = map.get(var.charAt(0));
                    }
                }

                treeBuild(br[i], map);
            }
        } else {
            String var = ((Leaf) mainTree).getVar();
            if (var.matches("[A-Z]")) {
                mainTree = map.get(var.charAt(0));
                mainTree.changeStringForm(correctStringForms(mainTree.getStringForm(), map));
                treeBuild(mainTree, map);
            }
        }

        return mainTree;
    }

    private static String correctStringForms(String form, Map<Character, FormulaTree> map) {
        int i = 0;
        boolean flag = true;

        while(flag) {
            flag = false;
            while (i < form.length()) {
                char ch = form.charAt(i);
                if (ch >= 'A' && ch <= 'Z') {
                    String s1 = form.substring(0, i);
                    String s2 = form.substring(i + 1);
                    form = s1 + "(" + map.get(ch).getStringForm() + ")" + s2;
                    i--;
                    flag = true;
                }
                i++;
            }
        }

        return form;
    }

    public static FormulaTree parse(String input) {
        return parse(input, LogicOperation.CONSEQUENCE);
    }

    private static FormulaTree parse(String input, LogicOperation durOp) {

        int index = input.indexOf(durOp.getStringVal());

        while (durOp != LogicOperation.END_OP) {
            index = input.indexOf(durOp.getStringVal());
            if (index != -1) break;
            durOp = durOp.next();
        }

        switch (durOp) {
            case CONJUNCTION:
            case DISJUNCTION:
            case CONSEQUENCE:
                FormulaTree left = parse(input.substring(0, index), durOp);
                FormulaTree right = parse(input.substring(index + durOp.getStringVal().length()), durOp);

                return new Node(new FormulaTree[]{left,right},durOp, input.trim());
            case NEGATION:
                return new Node(new FormulaTree[]{parse(input.substring(index + 1), durOp)},durOp, input.trim());
            default:
                return new Leaf(input.trim());
        }
    }

    public static void printTree(FormulaTree tree) {
        System.out.println(tree.getStringForm());

        if (tree instanceof  Node) {
            for (FormulaTree tr : ((Node) tree).getBranches()) {
                printTree(tr);
            }
        }
    }
}
