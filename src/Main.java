import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        boolean runProgram = true;
        ArrayList<Library> libraries = new ArrayList<>();

        while (runProgram) {
            Map<String, String> commands = new HashMap<>();
            ConsoleInterface console = ConsoleInterface.getInstance();

            // siin peaks toimuma olemasolevate raamatukogude sisselugemine listi
            // võib-olla liigutada see tulevikus ConsoleInterface constructorisse, aga pole veel tarvis

            commands.put("ll", "list libraries");
            commands.put("cl", "create library");
            commands.put("dl", "delete library");
            commands.put("sl", "select library");
            commands.put("quit", "quit program");
            commands.put("lb", "list books");
            commands.put("sb", "select book");
            commands.put("cb", "create book");
            commands.put("db", "delete book");

            String userInput = console.getCommand("Sisesta käsklus või 'help' abi jaoks: ");

            switch (userInput) {
                case "help": {
                    System.out.println(commands);
                    break;
                }
                case "quit": {
                    if (console.quit()) {
                        runProgram = false;
                        break;
                    }
                }
                case "ll": { // list libraries
                    console.listLibraries();
                    break;
                }
                case "cl": { // create library
                    console.createLibrary();
                    break;
                }
                case "dl": { // delete library
                    console.deleteLibrary();
                    break;
                }
                case "sl": { // select library
                    console.selectLibrary();
                    break;
                }
                case "sb": {
                    console.selectBook();
                    break;
                }
                case "lb": {
                    console.listBooks();
                    break;
                }
                case "db": {
                    console.deleteBook();
                    break;
                }
                case "cb": {
                    console.createBook();
                    break;
                }
            }
        }

        // pärast while loopi tuleks kõik uus info sisse lugeda mõnda faili ja see salvestada
    }
}