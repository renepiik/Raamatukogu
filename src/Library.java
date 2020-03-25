import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private String name;

    // constructors
    public Library(List<Book> books, String name) {
        this.books = books;
        this.name = name;
    }

    public Library(String name) {
        this.books = new ArrayList<>();
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name+"{" +
                "numOfBooks=" + books.size() +
                '}';
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book newBook) {
        this.books.add(newBook);
    }

    public void addBooks(List<Book> newBooks) {
        this.books.addAll(newBooks);
    }

    public void removeBook(Book book) {
        this.books.remove(book);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() throws IOException {
        File libraryFile = new File("./libs/"+name + ".csv");
        PrintWriter writer = new PrintWriter(libraryFile, StandardCharsets.UTF_8);
        writer.println(name);
        for (Book book : this.books) {
            String data = "@" + //tähistab uue raamatu algust
                    book.getTitle() + ";" +
                    book.getAuthorName() + ";" +
                    book.getPublicationDate() + ";" +
                    book.getGenre() + ";" +
                    book.getISBN() + ";" +
                    book.getStatus(); //salvestab iga raamatu andmed hiljem hästi loetavasse formaati
            writer.println(data);
        }
        writer.close();
        System.out.println("Raamatukogu '" + name + "' salvestatud.");
    }
}
