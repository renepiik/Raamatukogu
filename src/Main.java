import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean runProgram = true;
        ArrayList<Library> libraries = new ArrayList<>();

        while (runProgram) {
            Scanner sc = new Scanner(System.in);
            Map<String, String> commands = new HashMap<>();

            // siin peaks toimuma olemasolevate raamatukogude sisselugemine listi

            commands.put("ll", "list libraries");
            commands.put("nl", "new library");
            commands.put("dl", "delete library");
            commands.put("sl", "select library");
            commands.put("quit", "quit program");

            System.out.println("Sisesta käsklus või 'help' abi jaoks: ");
            String kasutajaSisend = sc.nextLine();

            switch (kasutajaSisend) {
                case "help": {
                    System.out.println(commands);
                    break;
                }
                case "quit": {
                    System.out.println("Do you wish to quit the program? (y/n) ");
                    if (sc.nextLine().equals("y") || sc.nextLine().equals("Y")) {
                        runProgram = false;
                    }
                    break;
                }
                case "ll": { // list libraries
                    System.out.println(libraries);
                    break;
                }
                case "nl": { // new library
                    boolean canCreate = true;

                    System.out.println("Enter a name for the library: ");
                    String libName = sc.nextLine();

                    // check if library with same name already exists
                    for (Library library : libraries) {
                        if (library.getName().equals(libName)) {
                            canCreate = false;
                            break;
                        }
                    }

                    if (canCreate) {
                        libraries.add(new Library(libName));
                        System.out.println("New library with name "+libName+" created");
                    } else {
                        System.out.println("Operation failed. Library with given name already exists");
                    }
                    break;
                }
                case "dl": { // delete library
                    System.out.println("Enter name of the library to delete: ");
                    String libName = sc.nextLine();
                    System.out.println("Do you wish to delete the library '"+libName+"'? (y/n)");
                    if (sc.nextLine().equals("y") || sc.nextLine().equals("Y")) {
                        libraries.removeIf(library -> library.getName().equals(libName));
                    }
                    break;
                }
                case "sl": { // select library
                    break;
                }
            }
        }

        // pärast while loopi tuleks kõik uus info sisse lugeda mõnda faili ja see salvestada
    }
}