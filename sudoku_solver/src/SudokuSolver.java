import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import java.io.IOException;

public class SudokuSolver {


    private FormulaGenerator generator;

    public SudokuSolver() {
        generator = new FormulaGenerator();
    }

    public void solve(String inputFilePath) {
        ISolver solver = SolverFactory. newDefault ();
        solver . setTimeout (3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
// CNF filename is given on the command line
        try {
            IProblem problem = reader.parseInstance(generator.generate(inputFilePath));
            if (problem.isSatisfiable()) {
                System.out.println (" Satisfiable !");
                printResult(problem.model());
            } else {
                System . out . println (" Unsatisfiable !");
            }

        } catch ( ParseFormatException | IOException ignored) {
        } catch ( ContradictionException e) {
            System .out . println (" Unsatisfiable ( trivial )!");
        } catch ( TimeoutException e) {
            System .out . println (" Timeout , sorry !");
        }
    }

    private void printResult(int[] res) {
        for (int i = 0; i < FormulaGenerator.FIELD_SIZE; i++) {
            for (int j = 0; j < FormulaGenerator.FIELD_SIZE; j++) {
                for (int k = 0; k < FormulaGenerator.NUM_COUNT; k++) {
                    int index = i*FormulaGenerator.FIRST_DISCHARGE_SHIFT + j*FormulaGenerator.SECOND_DISCHARGE_SHIFT + k;
                    if (res[index] > 0) {
                        System.out.print((k + 1) + " ");
                    }
                }
            }
            System.out.println();
        }
    }

}

