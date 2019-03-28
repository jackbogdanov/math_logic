import java.io.*;


public class FormulaGenerator {

    public static final int FIRST_DISCHARGE_SHIFT = 81;
    public static final int SECOND_DISCHARGE_SHIFT = 9;
    public static final int FIELD_SIZE = 9;
    public static final int NUM_COUNT = 9;
    public static final int VARIABLES_COUNT = 729;

    private StringBuilder formulaBuilder;
    private int clausesCount;
    String outputFileName;


    public FormulaGenerator() {
        formulaBuilder = new StringBuilder();
        outputFileName = "SAT.cnf";
        clausesCount = 0;
    }


    public String generate(String inputFilePath) {
        inputAdd(inputFilePath);
        fistRule();
        secondRule();
        thirdRule();
        forthRule();
        fifthRule();
        sixthRule();
        seventhRule();
        saveFile();
        return outputFileName;
    }

    private void inputAdd(String inputFilePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            String buff = "";
            char[] characters;
            int i = 0;

            while ((buff = reader.readLine()) != null) {
                characters = buff.toCharArray();
                for (int j = 0; j < characters.length; j++) {
                    if (characters[j] != ' ') {
                        addNum(i, j, Integer.parseInt(String.valueOf(characters[j])) - 1);
                        endConjunct();
                    }
                }
                i++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fistRule() {
        for (int k = 0; k < NUM_COUNT; k++) {
            for (int i = 0; i < FIELD_SIZE; i++) {
                for (int j = 0; j < FIELD_SIZE; j++) {
                    for (int t = 0; t < NUM_COUNT; t++) {
                        if (t != k) {
                            formulaBuilder.append('-');
                            addNum(i, j, k);
                            formulaBuilder.append(" -");
                            addNum(i, j, t);
                            endConjunct();
                        }
                    }
                }
            }
        }
    }

    private void secondRule() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                for (int k = 0; k < NUM_COUNT; k++) {
                    addNum(i, j, k);
                    if (k != NUM_COUNT - 1) {
                        formulaBuilder.append(' ');
                    }
                }
                endConjunct();
            }
        }
    }

    private void thirdRule() {
        for (int k = 0; k < NUM_COUNT; k++) {
            for (int i = 0; i < FIELD_SIZE; i++) {
                for (int j = 0; j < FIELD_SIZE; j++) {
                    for (int t = 0; t < NUM_COUNT; t++) {
                        if (t != j) {
                            formulaBuilder.append('-');
                            addNum(i, j, k);
                            formulaBuilder.append(" -");
                            addNum(i, t, k);
                            endConjunct();
                        }
                    }
                }
            }
        }
    }

    private void forthRule() {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int k = 0; k < NUM_COUNT; k++) {
                for (int j = 0; j < FIELD_SIZE; j++) {
                    addNum(i, j, k);
                    if (j != NUM_COUNT - 1) {
                        formulaBuilder.append(' ');
                    }
                }
                endConjunct();
            }
        }
    }

    private void fifthRule() {
        for (int k = 0; k < NUM_COUNT; k++) {
            for (int i = 0; i < FIELD_SIZE; i++) {
                for (int j = 0; j < FIELD_SIZE; j++) {
                    for (int t = 0; t < NUM_COUNT; t++) {
                        if (t != i) {
                            formulaBuilder.append('-');
                            addNum(i, j, k);
                            formulaBuilder.append(" -");
                            addNum(t, j, k);
                            endConjunct();
                        }
                    }
                }
            }
        }
    }

    private void sixthRule() {
        for (int k = 0; k < NUM_COUNT; k++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                for (int i = 0; i < FIELD_SIZE; i++) {
                    addNum(i, j, k);
                    if (i != NUM_COUNT - 1) {
                        formulaBuilder.append(' ');
                    }
                }
                endConjunct();
            }
        }
    }

    private void seventhRule() {
        for (int a = 0; a < FIELD_SIZE / 3; a++) {
            for (int b = 0; b < FIELD_SIZE / 3; b++) {
                for (int i = 0; i < FIELD_SIZE / 3; i++) {
                    for (int j = 0; j < FIELD_SIZE / 3; j++) {
                        for (int k = 0; k < NUM_COUNT; k++) {
                            for (int n = 0; n < FIELD_SIZE / 3; n++) {
                                for (int m = 0; m < FIELD_SIZE / 3; m++) {
                                    if (!(i == n && j == m)) {
                                        formulaBuilder.append('-');
                                        addNum(3*a + i, 3*b + j, k);
                                        formulaBuilder.append(" -");
                                        addNum(3*a + n, 3*b + m, k);
                                        endConjunct();
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void endConjunct() {
        formulaBuilder.append(" 0\n");
        clausesCount++;
    }

    private void addNum(int i, int j, int k) {
        formulaBuilder.append(String.valueOf(i*FIRST_DISCHARGE_SHIFT + j * SECOND_DISCHARGE_SHIFT + (k + 1)));
    }

    private void saveFile() {
        System.out.println(clausesCount);
        try {
            FileWriter writer = new FileWriter(new File(outputFileName));

            writer.write("p cnf " + VARIABLES_COUNT + " " + clausesCount +"\n");
            writer.write(formulaBuilder.toString());

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
