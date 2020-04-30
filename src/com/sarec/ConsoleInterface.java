package com.sarec;

import com.sarec.components.Book;
import com.sarec.components.Library;
import com.sarec.components.OperationType;
import com.sarec.components.Status;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// singleton class for com.sarec.ConsoleInterface because there only ever needs to be one instance of the class
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

    public static ConsoleInterface getInstance() {
        if (instance == null) instance = new ConsoleInterface();
        return instance;
    }

    private void updatePath(String newPath, OperationType newType) {
        this.operationType = newType;

        // if operationtype is set to a library
        if (newType == OperationType.DEFAULT) {
            this.currentPath = newPath;
        } else if (newType == OperationType.LIBRARY) {
            this.currentPath = newPath;
        } else if (newType == OperationType.BOOK && selectedLibrary != null) {
            this.currentPath = this.selectedLibrary.getName()+"/"+newPath;
        }
    }

    public String getCommand(String query) {
        if (!query.equals("")) System.out.println(query);
        System.out.print("~/"+this.currentPath+"$ ");
        return scanner.nextLine();
    }

    public boolean save() throws IOException {
        // siin salvestatakse kõik sisestatud info
        for (Library library : this.libraries) {
            library.save();
        }
        return true;
    }

    public boolean quit() throws IOException {
        return this.save();
    }

    public void list() {
        if (this.operationType != OperationType.DEFAULT) {
            listBooks();
        }
        else {
            System.out.println(getLibraries());
        }
    }

    public Library getSelectedLibrary() {
        return selectedLibrary;
    }

    public void setSelectedLibrary(Library selectedLibrary) {
        this.selectedLibrary = selectedLibrary;
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
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

    public ArrayList<Library> getLibraries() {
        return this.libraries;
    }

    // these methods can only be accessed when this.selectedLibrary has been set

    public void deleteLibrary() {
        if (this.operationType == OperationType.LIBRARY) {
            String deleteCommand = this.getCommand("Kas te soovite eemaldada raamatukogu '"+this.selectedLibrary.getName()+"'? (jah/ei)");

            if (deleteCommand.toLowerCase().equals("jah")) {
                // kustuta info
                this.libraries.removeIf(library -> library.getName().equals(this.selectedLibrary.getName()));
                // kustuta fail
                if (Desktop.getDesktop().isSupported(Desktop.Action.MOVE_TO_TRASH)) Desktop.getDesktop().moveToTrash(new File(Vars.libsPath+this.selectedLibrary.getName()+".csv"));
                // muuda programmi töörežiim
                updatePath("", OperationType.DEFAULT);
                this.selectedLibrary = null;
            }
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

    public void createBook() {
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

    public void createBookWithTitleAndAuthor(String title, String author) {
        if (this.operationType == OperationType.LIBRARY) {
            this.selectedLibrary.addBook(new Book(title, author));
            System.out.println("Uus raamat pealkirjaga "+title+" ja autoriga "+author+" on loodud");
        }
    }

    public void listBooks() {
        if (this.operationType == OperationType.LIBRARY) {
            // status, title, authorName, publicationDate, genre, objects.ISBN: isbn

            // loon uue ArrayListi String[] arraydest, et arvutada välja printimiseks sobilike tulpade laiusi
            ArrayList<String[]> data = new ArrayList<>();

            // header
            data.add(new String[]{"Staatus", "Pealkiri", "Autor", "Ilmumisaeg", "Žanr", "ISBN"});

            // iga raamat on üks data rida
            for (Book book : this.selectedLibrary.getBooks()) {
                String[] row = new String[]{book.getStatus().toString(), book.getTitle(), book.getAuthorName(), book.getPublicationDate(), book.getGenre(), book.getISBN().toString()};
                data.add(row);
            }

            // rea pikkus ja ridade arv
            int col = data.get(0).length;

            // Array, kuhu lisatakse iga tulba suurim pikkus
            int[] maxWidth = new int[col];

            // data käiakse läbi ridahaaval, iga rea juures kontrollitakse laiusi maxWidth[i] vastu
            for (String[] rowData : data) {
                for (int i = 0; i < col; i++) {
                    if (maxWidth[i] < rowData[i].length()) maxWidth[i] = rowData[i].length();
                }
            }

            // väljundi formattimine
            StringBuilder format = new StringBuilder();

            for (int x : maxWidth) format.append("%-").append(x + 2).append("s ");
            format.append("%n");

            for(String[] rowData : data)
                System.out.printf(format.toString(), (Object[]) rowData);
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

            // objects.ISBN
            String newISBN = this.getCommand("Sisesta objects.ISBN: ");
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
