public class Book {
    private String title;
    private String authorName;
    private String publicationDate;
    private String genre;
    private ISBN ISBN;
    private Status status;

    @Override
    public String toString() {
        return status+": "+title+", "+authorName+"\n("+publicationDate+", "+genre+", ISBN: "+ISBN+")";
    }

    // constructors
    public Book(String title, String authorName) throws Exception {
        this.title = title;
        this.authorName = authorName;
        this.status = Status.DEFAULT;
        this.genre = "Romaan";
        this.publicationDate = "0000";
        this.ISBN = new ISBN("0000000000");
    }

    public Book(String title, String authorName, String publicationDate, String genre, ISBN ISBN, Status status) {
        this.title = title;
        this.authorName = authorName;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.ISBN = ISBN;
        this.status = status;
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
