public class Book {
    private String title;
    private String authorName;
    private String publicationDate;
    private String genre;
    private ISBN ISBN;
    private Status status;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(status.toString().toLowerCase()).append(": ").append(title).append(", ").append(authorName);
        if (this.publicationDate != null || this.genre != null || this.ISBN != null) {
            sb.append("(");
            if (this.publicationDate != null) sb.append(this.publicationDate);
            if (this.genre != null) sb.append(", ").append(this.genre);
            if (this.ISBN != null) sb.append(", ISBN: ").append(this.ISBN);
            sb.append(")");
        }

        return sb.toString();
    }

    // constructors
    public Book(String title, String authorName) {
        this.title = title;
        this.authorName = authorName;
        this.status = Status.DEFAULT;
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
