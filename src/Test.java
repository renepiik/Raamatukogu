public class Test {
    public static void main(String[] args) throws Exception {
	    Book TJ = new Book("Tõde ja õigus IV", "Anton Hansen Tammsaare");

	    TJ.setISBN("9789949664504");
	    TJ.setStatus(Status.READING);
        TJ.setGenre("Draama");
        TJ.setPublicationDate("1965");
        System.out.println(TJ);

        Library minuRaamatukogu = new Library("Minu Raamatukogu");

        minuRaamatukogu.addBook(TJ);
        System.out.println(minuRaamatukogu);
    }
}
