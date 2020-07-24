package edu.txstate.bss64;

/**
 * @author Borislav S. Sabotinov
 */
public class Main {
    public static void main(String...args) {
        if (args.length != 1 || args[0].equals("help")) {
            printHelp();
        } else {
            if (args[0].equals("1")) {

            } else if (args[0].equals("2")) {

            } else {
                printHelp();
                throw new IllegalArgumentException("\nOption code not supported!");
            }
        }
    }

    private static void printHelp() {
        System.out.print("Enter 1 for standard message server, 2 for command execution server:\n" +
                "java -jar server.jar <OPT_CODE>\n");
    }
}
