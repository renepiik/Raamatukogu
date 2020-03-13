public class Test {
    public static void main(String[] args) throws Exception{

        Book TJ = new Book("Tõde ja õigus IV", "Anton Hansen Tammsaare");
        TJ.setISBN("9789949664504");
        TJ.setStatus(Status.READING);
        TJ.setGenre("Draama");
        TJ.setPublicationDate("1964");
        System.out.println(TJ);

        Book Witcher = new Book("Witcher: Lady of the Lake", "Andrzej Sapkowski");
        //Witcher.setStatus(Status.LENT_OUT);
        //Witcher.setGenre("Action");
        //Witcher.setPublicationDate("1999");
        System.out.println(Witcher);


        Library minuRaamatukogu = new Library("Minu Raamatukogu");

        minuRaamatukogu.addBook(TJ);
        minuRaamatukogu.addBook(Witcher);
        System.out.println(minuRaamatukogu);

    }
}
