public class Book {
    private String title;
    private String publicationDate;
    private String authorName;
    private String genre;
    private ISBN ISBN;
    private Status status;

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author_name='" + authorName + '\'' +
                ", publication_date='" + publicationDate + '\'' +
                ", genre='" + genre + '\'' +
                ", ISBN=" + ISBN +
                ", status=" + status +
                '}';
    }

    // constructors
    public Book(String title, String authorName) {
        this.title = title;
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public ISBN getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) throws Exception { this.ISBN = new ISBN(ISBN); }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
