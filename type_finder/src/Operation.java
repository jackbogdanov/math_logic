
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;

public class Operation {

    private int[] elements;
    private int[][] table;

    public Operation(String fileName) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            readElements(reader);
            readTable(reader, elements.length);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Input output exception!");
        }
    }

    private String[] prepareInput(String[] input) {
        ArrayList<String> preparedInput = new ArrayList<>();
        for (String s : input) {
            if (!s.equals("")) {
                preparedInput.add(s);
            }
        }

        return preparedInput.toArray(new String[preparedInput.size()]);
    }

    public int getIndex(int el) {
        int index = 0;
        for (int i : elements) {
            if (i != el) {
                index++;
            } else {
                return index;
            }
        }
        return -1;
    }

    private void readElements(BufferedReader reader) throws IOException {

        String[] elems = prepareInput(reader.readLine().split(" "));

        elements = new int[elems.length];

        for (int i = 0; i < elems.length; i++) {
            elements[i] = Integer.parseInt(elems[i]);
        }
    }

    private void readTable(BufferedReader reader, int size) throws IOException {
        table = new int[size][size];
        int j = 0;

        while (j != size) {
            String[] elems = prepareInput(reader.readLine().split(" "));

            for (int i = 1; i < elems.length; i++) {
                 table[j][i-1] = Integer.parseInt(elems[i]);
            }

            j++;
        }

    }

    public void print() {
        System.out.println(Arrays.toString(elements) + "\n");

        for (int i = 0; i < elements.length; i++) {
            System.out.println(Arrays.toString(table[i]));
        }
    }

    public int calculate(int arg1, int arg2) {
        int i = getIndex(arg1);
        int j = getIndex(arg2);

        if (i == -1 || j == -1) {
            System.out.println("Structure not closed!!!");
            System.exit(1);
            return 0;
        }
        return table[i][j];
    }

    public int[] getElements() {
        return elements;
    }

    public int[] getTableRow(int arg) {
        int index = getIndex(arg);
        int[] column = new int[elements.length];

        System.arraycopy(table[index], 0, column, 0, column.length);

        return column;
    }

    public int[] getTableColumn(int arg) {
        int index = getIndex(arg);
        int[] raw = new int[elements.length];

        for (int i = 0; i < raw.length; i++) {
            raw[i] = table[i][index];
        }

        return raw;
    }

    public boolean isClosed() {
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements.length; j++) {
                if (getIndex(table[i][j]) == -1) {
                    return false;
                }
            }
        }

        return true;
    }

}
