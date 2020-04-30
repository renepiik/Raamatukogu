package com.sarec.components;

public class Book {
    private String title;
    private String authorName;
    private String publicationDate;
    private String genre;
    private ISBN ISBN;
    private Status status;

    @Override
    public String toString() {
        // status, title, authorName, publicationDate, genre, objects.ISBN: isbn

        StringBuilder sb = new StringBuilder();

        sb.append(status.toString().toLowerCase());
        sb.append(", ");
        sb.append(title);
        sb.append(", ");
        sb.append(authorName);

        if (!this.publicationDate.equals("")) sb.append(", ").append(this.publicationDate);
        if (!this.genre.equals("")) sb.append(", ").append(this.genre);
        if (!this.ISBN.toString().equals("")) sb.append(", objects.ISBN: ").append(this.ISBN);

        return sb.toString();
    }

    // constructors
    public Book(String title, String authorName) {
        this.title = title;
        this.authorName = authorName;
        this.status = Status.DEFAULT;
        this.genre = "";
        this.publicationDate = "";
        this.ISBN = new ISBN();
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

    public void setISBN(String ISBN) throws Exception {
        this.ISBN = ISBN.equals("") ? new ISBN() : new ISBN(ISBN);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
