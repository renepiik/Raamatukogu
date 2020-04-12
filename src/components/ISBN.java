package components;

public class ISBN {
    private String isbn;

    public ISBN(String isbn) throws Exception {
        if (checkISBN10(isbn) || checkISBN13(isbn)) this.isbn = isbn;
        else throw new Exception();
    }

    public ISBN() {
        this.isbn = "";
    }

    @Override
    public String toString() {
        return isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (checkISBN10(isbn) || checkISBN13(isbn)) this.isbn = isbn;
    }

    // returns true for valid objects.ISBN-10
    private boolean checkISBN10(String ISBN_number) {
        int s = 0;
        int t = 0;

        for (int i=0; i<10; i++) {
            t += ISBN_number.charAt(i);
            s += t;
        }

        return (s % 11 == 0);
    }

    // returns true for valid objects.ISBN-13
    private boolean checkISBN13(String ISBN_number) {
        int s = 0;

        // for loop is offset to start at 1 for checking %2
        for (int i=1; i<=13; i++) {
            // every other number
            if (i%2 == 0) {
                // multiply the number at [i-1] by 3 and add to s
                s += 3*ISBN_number.charAt(i-1);
            } else {
                // add the number at [i-1] to s
                s += ISBN_number.charAt(i-1);
            }
        }

        return (s % 10 == 0);
    }
}
