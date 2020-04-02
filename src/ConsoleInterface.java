import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// singleton class for ConsoleInterface because there only ever needs to be one instance of the class
// used for setting and retrieving console path, setting accessible methods accordingly

public class ConsoleInterface {

    private String currentPath;
    private OperationType operationType = OperationType.DEFAULT;

    private ArrayList<Library> libraries;
    private Library selectedLibrary;
    private Book selectedBook;

    private static ConsoleInterface instance = null;
    private static Scanner scanner;

    private ConsoleInterface() {
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
            this.currentPath = newPath;
        } else if (newType == OperationType.BOOK && selectedLibrary != null) {
            this.currentPath = this.selectedLibrary.getName()+"/"+newPath;
        }
    }

    public String getCommand(String query) {
        System.out.println(query);
        System.out.print("~/"+this.currentPath+"$ ");
        return scanner.nextLine();
    }

    public boolean quit() throws IOException {
        String input = this.getCommand("Kas te soovite programmist lahkuda? (jah/ei)");
        if (input.toLowerCase().equals("jah")) {
            // siin salvestatakse kõik sisestatud info
            for (Library library : this.libraries) {
                library.save();
            }

            return true;
        }
        return false;
    }

    public void list() {
        if (this.operationType != OperationType.DEFAULT) {
            listBooks();
        }
        else {
            listLibraries();
        }
    }

    /*
    LIBRARY METHODS

    Methods accessible from root:
        - selectLibrary()
        - selectLibraryWithName(String libName)
        - createLibrary()
        - createLibraryWithName(String libName)
        - listLibraries()

    Methods accessible when this.selectedLibrary has been set:
        - updateLibrary()
        - deleteLibrary()

     */

    public void selectLibrary() {
        String libName = this.getCommand( "Sisesatage raamatukogu nimi: ");
        selectLibraryWithName(libName);
    }

    public void selectLibraryWithName(String libName) {
        boolean canOpen = false;
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
        String libName = this.getCommand("Sisestage loodava raamatukogu nimi: ");
        createLibraryWithName(libName);
    }

    public void createLibraryWithName(String libName) {
        boolean canCreate = true;

        // check if library with same name already exists
        for (Library library : this.libraries) {
            if (library.getName().equals(libName)) {
                canCreate = false;
                break;
            }
        }

        if (canCreate) {
            this.libraries.add(new Library(libName));
            System.out.println("Uus raamatukogu nimega "+libName+" on loodud");
        } else {
            System.out.println("Tekkis tõrge, sellise nimega raamatukogu on juba olemas.");
        }
    }

    public void listLibraries() {
        System.out.println(this.libraries);
    }

    // these methods can only be accessed when this.selectedLibrary has been set

    public void deleteLibrary() {
        if (this.operationType == OperationType.LIBRARY) {
            String deleteCommand = this.getCommand("Kas te soovite eemaldada raamatukogu '"+this.selectedLibrary.getName()+"'? (jah/ei)");

            if (deleteCommand.toLowerCase().equals("jah")) {
                this.libraries.removeIf(library -> library.getName().equals(this.selectedLibrary.getName()));
            }

            updatePath("", OperationType.DEFAULT);
            this.selectedLibrary = null;
        }
    }

    public void updateLibrary() {
        if (this.operationType == OperationType.LIBRARY) {
            String newName = this.getCommand("Sisesta uus nimi raamatukogule: ");

            boolean canChange = true;

            // check if library with same name already exists
            for (Library library : this.libraries) {
                if (library.getName().equals(newName)) {
                    canChange = false;
                    break;
                }
            }

            if (canChange) {
                this.selectedLibrary.setName(newName);
                this.updatePath(this.selectedLibrary.getName(), OperationType.LIBRARY);
                System.out.println("Raamatukogu uus nimi on "+newName);
            } else {
                System.out.println("Tekkis tõrge, sellise nimega raamatukogu on juba olemas.");
            }
        }
    }

    /*
    BOOK METHODS

    Methods accessible when this.selectedLibrary has been set:
        - selectBook()
        - selectBookWithTitle(String bookTitle)
        - createBook()
        - createBookWithTitleAndAuthor(String title, String author)
        - listBooks()
        - getRandomBook()

    Methods accessible when this.selectedBook has been set:
        - updateBook()
        - deleteBook()
        - displayBookInfo()
     */

    // asks for book title, then gives a selection of all the books with given title
    // user enters a number corresponding to the book to select it
    public void selectBook() {
        if (this.operationType == OperationType.LIBRARY) {
            String bookTitle = this.getCommand("Sisestage raamatu pealkiri: ");
            selectBookWithTitle(bookTitle);
        }
    }

    public void selectBookWithTitle(String bookTitle) {
        if (this.operationType == OperationType.LIBRARY) {
            boolean canOpen = false;
            Book selectedBook;
            ArrayList<Book> booksWithSameTitle = new ArrayList<>();

            // check if book with given name exists
            for (Book book : this.selectedLibrary.getBooks()) {
                if (book.getTitle().toLowerCase().equals(bookTitle.toLowerCase())) {
                    canOpen = true;
                    booksWithSameTitle.add(book);
                }
            }

            if (canOpen) {
                if (booksWithSameTitle.size() == 1) {
                    this.updatePath(bookTitle, OperationType.BOOK);
                    this.selectedBook = booksWithSameTitle.get(0);
                }
                else {
                    System.out.println("Millise raamatu te soovite avada? ");
                    for (int i = 0; i < booksWithSameTitle.size(); i++) {
                        System.out.println(i +": "+booksWithSameTitle.get(i));
                    }

                    int bookIndex = Integer.parseInt(this.getCommand("Sisestage raamatu eesolev number siia: "));
                    selectedBook = booksWithSameTitle.get(bookIndex);
                    this.selectedBook = selectedBook;
                    this.updatePath(selectedBook.getTitle(), OperationType.BOOK);
                }
            }
        }
    }

    public void createBook() throws Exception {
        if (this.operationType == OperationType.LIBRARY) {
            boolean create = true;
            String bookName = this.getCommand("Sisestage loodava raamatu pealkiri: ");

            // if there already is another book with the same title, asks for confirmation
            for (Book book : this.selectedLibrary.getBooks()) {
                if (book.getTitle().equals(bookName)) {
                    create = this.getCommand("Sellise pealkirjaga raamat on juba olemas. Kas soovid luua uut? (jah/ei)").toLowerCase().equals("jah");
                    break;
                }
            }

            String authorName = this.getCommand("Sisestage raamatu autori nimi: ");

            if (create) {
                this.createBookWithTitleAndAuthor(bookName, authorName);
            }
        }
    }

    public void createBookWithTitleAndAuthor(String title, String author) throws Exception {
        if (this.operationType == OperationType.LIBRARY) {
            this.selectedLibrary.addBook(new Book(title, author));
            System.out.println("Uus raamat pealkirjaga "+title+" ja autoriga "+author+" on loodud");
        }
    }

    public void listBooks() {
        if (this.operationType == OperationType.LIBRARY) {
            for (Book book : this.selectedLibrary.getBooks()) {
                System.out.println(book);
            }
        }
    }

    // Math.Random funktsiooni kasutus
    public void getRandomBook() {
        if (this.operationType == OperationType.LIBRARY) {
            int bookIndex = (int) Math.floor(Math.random() * this.selectedLibrary.getBooks().size());
            System.out.println(this.selectedLibrary.getBooks().get(bookIndex));
        }
        else {
            System.out.println("Raamatukogu pole valitud");
        }
    }

    // these methods can only be accessed when this.selectedBook has been set

    public void deleteBook() {
        if (this.operationType == OperationType.BOOK) {
            boolean canDelete = false;
            ArrayList<Book> booksWithSameTitle = new ArrayList<>();
            String bookName = this.getCommand("Sisestage raamatu pealkiri, mille soovite eemaldada: ");

            // check if book with given name exists
            for (Book book : this.selectedLibrary.getBooks()) {
                if (book.getTitle().equals(bookName)) {
                    canDelete = true;
                    booksWithSameTitle.add(book);
                }
            }

            if (canDelete) {
                if (booksWithSameTitle.size() == 1) {
                    String deleteCommand = this.getCommand("Kas te soovite eemaldada raamatu pealkirjaga '"+bookName+"'? (jah/ei)");

                    if (deleteCommand.toLowerCase().equals("jah")) {
                        this.selectedLibrary.getBooks().removeIf(book -> book.getTitle().equals(bookName));
                    }
                }
                else {
                    System.out.println("Millise raamatu te soovite eemaldada? ");
                    for (int i = 0; i < booksWithSameTitle.size(); i++) {
                        System.out.println(i +": "+booksWithSameTitle.get(i));
                    }

                    int bookIndex = Integer.parseInt(this.getCommand("Sisestage raamatu ees olev number siia: "));
                    String deleteCommand = this.getCommand("Kas te soovite eemaldada raamatu '"+bookIndex+": "+booksWithSameTitle.get(bookIndex)+"'? (jah/ei)");

                    if (deleteCommand.toLowerCase().equals("jah")) {
                        this.selectedLibrary.removeBook(booksWithSameTitle.get(bookIndex));
                        this.selectedBook = null;
                        this.updatePath(this.selectedLibrary.getName(), OperationType.LIBRARY);
                    }
                }
            } else {
                System.out.println("Sellise nimega raamatut ei ole olemas");
            }
        }
    }

    public void updateBook() throws Exception {
        if (this.operationType == OperationType.BOOK) {
            System.out.println("Iga küsimuse juures võid vajutada enter, kui soovid välja muutmata jätta.");

            // title
            String newTitle = this.getCommand("Sisesta pealkiri: ");
            if (!newTitle.equals("")) this.selectedBook.setTitle(newTitle);

            // authorName
            String newAuthorName = this.getCommand("Sisesta autori nimi: ");
            if (!newAuthorName.equals("")) this.selectedBook.setAuthorName(newAuthorName);

            // publicationDate
            String newPublicationDate = this.getCommand("Sisesta ilmumisaasta: ");
            if (!newPublicationDate.equals("")) this.selectedBook.setPublicationDate(newPublicationDate);

            // genre
            String newGenre = this.getCommand("Sisesta žanr: ");
            if (!newGenre.equals("")) this.selectedBook.setGenre(newGenre);

            // ISBN
            String newISBN = this.getCommand("Sisesta ISBN: ");
            if (!newISBN.equals("")) this.selectedBook.setISBN(newISBN);

            // status
            String newStatus = this.getCommand("Sisesta staatus (DEFAULT, LENT, READING): ");
            if (!newStatus.equals("")) this.selectedBook.setStatus(Status.valueOf(newStatus.toUpperCase()));

            System.out.println("Raamatu info uuendatud! ");
            System.out.println(this.selectedBook);
        }
        else {
            System.out.println("Vali enne raamat, mida soovid uuendada");
        }
    }

    public void displayBookInfo() {
        if (this.operationType == OperationType.BOOK) {
            System.out.println(this.selectedBook);
        }
    }

}
