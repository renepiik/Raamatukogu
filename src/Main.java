public class Main {

    public static void main(String[] args) throws Exception {
	    Book TÕ = new Book("Tõde ja õigus IV", "Anton Hansen Tammsaare");

	    TÕ.setISBN("9789949664504");
	    TÕ.setStatus(Status.READING);
        TÕ.setGenre("Draama");
        TÕ.setPublicationDate("1964");
        System.out.println(TÕ);
    }
}
