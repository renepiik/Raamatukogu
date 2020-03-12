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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
