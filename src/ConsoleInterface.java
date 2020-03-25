import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// singleton class for ConsoleInterface because there only ever needs to be one instance of the class
// used for setting and retrieving console path, setting accessible methods accordingly

public class ConsoleInterface {

    private String currentPath;
    private OperationType operationType = OperationType.DEFAULT;
    private String currentLibraryName;
    private String currentBookName;

    private ArrayList<Library> libraries;
    private Library selectedLibrary;
    private Book selectedBook;

    private static ConsoleInterface instance = null;
    private static Scanner scanner;

    private ConsoleInterface() throws Exception {
        this.currentPath = "";
        scanner = new Scanner(System.in);
        // should read libraries from a local file here
        // if libraries are not read, creates new empty list of libraries
        this.libraries = Startup.Initialize();
    }

    public static ConsoleInterface getInstance() throws Exception {
        if (instance == null) instance = new ConsoleInterface();
        return instance;
    }

    private void updatePath(String newPath, OperationType newType) {
        this.operationType = newType;

        // if operationtype is set to a library
        if (newType == OperationType.LIBRARY) {
            this.currentLibraryName = newPath;
            this.currentPath = newPath;
        } else if (newType == OperationType.BOOK && selectedLibrary != null) {
            this.currentPath = this.selectedLibrary.getName()+"/"+newPath;
            this.currentBookName = newPath.substring(newPath.lastIndexOf('/')+1);
        }
    }

    public String getCommand(String query) {
        System.out.println(query);
        System.out.print("~/"+this.currentPath+" ");
        return scanner.nextLine();
    }

    public boolean quit() throws IOException {
        String input = this.getCommand("Do you wish to quit the program? (y/n) ");
        if (input.equals("y") || input.equals("Y")) {
            // siin salvestatakse kõik sisestatud info
            for (Library library : this.libraries) {
                library.save();
            }

            return true;
        }
        return false;
    }

    /*
    LIBRARY METHODS

    Methods accessible from root:
        - selectLibrary()
        - createLibrary()
        - listLibraries()

    Methods accessible when this.selectedLibrary has been set:
        - updateLibrary()
        - deleteLibrary()

     */

    public void selectLibrary() {
        boolean canOpen = false;
        String libName = this.getCommand("Enter the name of the library: ");
        Library selectedLibrary = null;

        // check if library with given name exists
        for (Library library : this.libraries) {
            if (library.getName().equals(libName)) {
                canOpen = true;
                selectedLibrary = library;
                break;
            }
        }

        if (canOpen) {
            updatePath(libName, OperationType.LIBRARY);
            this.selectedLibrary = selectedLibrary;
            this.selectedBook = null;
        }
    }

    public void createLibrary() {
        boolean canCreate = true;

        String libName = this.getCommand("Enter a name for the library: ");

        // check if library with same name already exists
        for (Library library : this.libraries) {
            if (library.getName().equals(libName)) {
                canCreate = false;
                break;
            }
        }

        if (canCreate) {
            this.libraries.add(new Library(libName));
            System.out.println("New library with name "+libName+" created");
        } else {
            System.out.println("Operation failed. Library with given name already exists");
        }
    }

    public void listLibraries() {
        System.out.println(this.libraries);
    }

    // these methods can only be accessed when this.selectedLibrary has been set

    public void deleteLibrary() {
        if (this.selectedLibrary != null) {
            String deleteCommand = this.getCommand("Do you wish to delete the library '"+this.selectedLibrary.getName()+"'? (y/n)");

            if (deleteCommand.equals("y") || deleteCommand.equals("Y")) {
                this.libraries.removeIf(library -> library.getName().equals(this.selectedLibrary.getName()));
            }

            updatePath("", OperationType.DEFAULT);
            this.selectedLibrary = null;
        }
    }

    // TODO
    public void updateLibrary() {
        if (this.selectedLibrary != null) {

        }
    }

    /*
    BOOK METHODS

    Methods accessible when this.selectedLibrary has been set:
        - selectLibrary()
        - createLibrary()
        - listLibraries()

    Methods accessible when this.selectedBook has been set:
        - updateLibrary()
        - deleteLibrary()
     */

    // asks for book title, then gives a selection of all the books with given title
    // user enters a number corresponding to the book to select it
    public void selectBook() {
        if (this.selectedLibrary != null) {
            boolean canOpen = false;
            String bookName = this.getCommand("Enter the title of the book: ");
            Book selectedBook;
            ArrayList<Book> booksWithSameTitle = new ArrayList<>();

            // check if book with given name exists
            for (Book book : this.selectedLibrary.getBooks()) {
                if (book.getTitle().equals(bookName)) {
                    canOpen = true;
                    booksWithSameTitle.add(book);
                }
            }

            if (canOpen) {
                if (booksWithSameTitle.size() == 1) {
                    updatePath(bookName, OperationType.BOOK);
                }
                else {
                    System.out.println("Which of these books do you wish to open? ");
                    for (int i = 0; i < booksWithSameTitle.size(); i++) {
                        System.out.println(i +": "+booksWithSameTitle.get(i));
                    }

                    int bookIndex = Integer.parseInt(this.getCommand("Write the number in front of the book here: "));
                    selectedBook = booksWithSameTitle.get(bookIndex);
                    this.selectedBook = selectedBook;
                    this.updatePath(selectedBook.getTitle(), OperationType.BOOK);
                }
            }
        }
    }

    public void createBook() throws Exception {
        if (this.selectedLibrary != null) {
            boolean canCreate = true;

            String bookName = this.getCommand("Enter a title for the new Book: ");

            // check if book with same name already exists
            for (Book book : this.selectedLibrary.getBooks()) {
                if (book.getTitle().equals(bookName)) {
                    canCreate = false;
                    break;
                }
            }

            String authorName = this.getCommand("Enter the author's name: ");

            if (canCreate) {
                this.selectedLibrary.addBook(new Book(bookName, authorName));
                System.out.println("New book with title "+bookName+" and author "+authorName+" created");
            } else {
                System.out.println("Operation failed. Book with given title already exists");
            }
        }
    }

    public void listBooks() {
        if (this.selectedLibrary != null) {
            for (Book book : this.selectedLibrary.getBooks()) {
                System.out.println(book);
            }
        }
    }

    public void deleteBook() {
        if (this.selectedLibrary != null && this.selectedBook != null) {
            boolean canDelete = false;
            ArrayList<Book> booksWithSameTitle = new ArrayList<>();
            String bookName = this.getCommand("Enter title of the book to delete: ");

            // check if book with given name exists
            for (Book book : this.selectedLibrary.getBooks()) {
                if (book.getTitle().equals(bookName)) {
                    canDelete = true;
                    booksWithSameTitle.add(book);
                }
            }

            if (canDelete) {
                if (booksWithSameTitle.size() == 1) {
                    String deleteCommand = this.getCommand("Do you wish to delete the book '"+bookName+"'? (y/n)");

                    if (deleteCommand.equals("y") || deleteCommand.equals("Y")) {
                        this.selectedLibrary.getBooks().removeIf(book -> book.getTitle().equals(bookName));
                    }
                }
                else {
                    System.out.println("Which of these books do you wish to delete? ");
                    for (int i = 0; i < booksWithSameTitle.size(); i++) {
                        System.out.println(i +": "+booksWithSameTitle.get(i));
                    }

                    int bookIndex = Integer.parseInt(this.getCommand("Write the number in front of the book here: "));
                    String deleteCommand = this.getCommand("Do you wish to delete the book '"+bookIndex+": "+booksWithSameTitle.get(bookIndex)+"'? (y/n)");

                    if (deleteCommand.equals("y") || deleteCommand.equals("Y")) {
                        this.selectedLibrary.removeBook(booksWithSameTitle.get(bookIndex));
                        this.selectedBook = null;
                        this.updatePath(this.selectedLibrary.getName(), OperationType.LIBRARY);
                    }
                }
            } else {
                System.out.println("Book with given name does not exist.");
            }
        }
    }

    // TODO
    public void updateBook() {

    }

    // Math.Random funktsiooni kasutus
    public Book getRandomBook() throws Exception {
        if (this.selectedLibrary != null) {
            int bookIndex = (int) Math.floor(Math.random() * this.selectedLibrary.getBooks().size());
            return this.selectedLibrary.getBooks().get(bookIndex);
        }
        return new Book("", "");
    }
}
