public class Main {

    public static void main(String[] args) {
//        Operation op = new Operation(args[0]);
        Operation op = new Operation("input.txt");
        TypeFinder finder = new TypeFinder(op);
        System.out.println(finder.getType());
    }
}
