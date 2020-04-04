import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Console {
    public static void main(String[] args) throws Exception{
        boolean runProgram = true;
        ArrayList<Library> libraries = new ArrayList<>();
        Map<String, String> commands = new HashMap<>();
        ConsoleInterface console = ConsoleInterface.getInstance();

        // ingliskeelsed juhised programmi kasutamiseks
        commands.put("list, ls", "list based on context");
        commands.put("ll", "list libraries");
        commands.put("sl <name>", "select library");
        commands.put("cl <name>", "create library");
        commands.put("dl", "delete library");
        commands.put("ul", "update library");
        commands.put("lb", "list books");
        commands.put("sb", "select book");
        commands.put("cb <title> -a <author>", "create book");
        commands.put("db", "delete book");
        commands.put("rb", "random book");
        commands.put("ub", "update book");
        commands.put("vb", "view book");
        commands.put("quit", "quit program");

        while (runProgram) {
            String[] userInput = console.getCommand("Sisesta käsklus või 'help' abi jaoks: ").split(" ");

            switch (userInput[0]) {
                case "help": {
                    // kuvab iga käskluse uuel real
                    for (Map.Entry<String, String> stringStringEntry : commands.entrySet()) {
                        System.out.println(stringStringEntry.getKey()+": "+stringStringEntry.getValue());
                    }
                    break;
                }
                case "quit": {
                    if (console.quit()) {
                        runProgram = false;
                        break;
                    }
                    break;
                }
                case "ll": { // list libraries
                    console.listLibraries();
                    break;
                }
                case "cl": { // create library
                    if (userInput.length > 1) {
                        StringBuilder name = new StringBuilder();
                        for (int i = 1; i < userInput.length; i++) {
                            name.append(userInput[i]).append(" ");
                        }
                        name.deleteCharAt(name.length()-1);
                        console.createLibraryWithName(name.toString());
                    }
                    else console.createLibrary();
                    break;
                }
                case "dl": { // delete library
                    console.deleteLibrary();
                    break;
                }
                case "sl": { // select library
                    if (userInput.length > 1) {
                        StringBuilder name = new StringBuilder();
                        for (int i = 1; i < userInput.length; i++) {
                            name.append(userInput[i]).append(" ");
                        }
                        name.deleteCharAt(name.length()-1);
                        console.selectLibraryWithName(name.toString());
                    }
                    else console.selectLibrary();
                    break;
                }
                case "ul": {
                    console.updateLibrary();
                    break;
                }
                case "sb": {
                    console.selectBook();
                    break;
                }
                case "cb": {
                    if (userInput.length > 1) {
                        // find the index of String "-a" in userInput[]
                        int cIndex = 1;

                        for (int i = 1; i < userInput.length; i++) {
                            if (userInput[i].equals("-a")) cIndex = i;
                        }

                        // join the Strings between 'cb' and '-' to title
                        StringBuilder title = new StringBuilder();

                        for (int i = 1; i < cIndex; i++) {
                            title.append(userInput[i]).append(" ");
                        }
                        title.deleteCharAt(title.length()-1);

                        // join the Strings after '-' to author
                        StringBuilder authorName = new StringBuilder();

                        for (int i = cIndex+1; i < userInput.length; i++) {
                            authorName.append(userInput[i]).append(" ");
                        }
                        authorName.deleteCharAt(authorName.length()-1);

                        // create book
                        console.createBookWithTitleAndAuthor(title.toString(), authorName.toString());
                    }
                    else console.createBook();
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
                case "rb": {
                    console.getRandomBook();
                    break;
                }
                case "ub": {
                    console.updateBook();
                    break;
                }
                case "list":
                case "ls": {
                    console.list();
                    break;
                }
                case "vb": {
                    console.displayBookInfo();
                    break;
                }
                default: {
                    System.out.println("Sellist käsklust pole.");
                    break;
                }
            }
        }
    }
}
