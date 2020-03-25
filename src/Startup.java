import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class Startup {

    public static ArrayList<Library> Initialize() throws Exception{
        //Loeb .csv failid sisse, moodustab nende sisudest Library objektid ja koondab need ArrayListi
        //Väljastab Library objektide ArrayListi


        //.csv failide lugemine
        List<String> csvFiles = new ArrayList<>();

        File dir = new File("./libs/");
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.getName().endsWith((".csv"))) {
                csvFiles.add(file.getName());
            }
        }

        ArrayList<Library> allLibs = new ArrayList<>();

        //Library objektide genereerimine Arraylisti
        for (String csvFile : csvFiles) {
            try {
                File fail = new File("./libs/"+csvFile);
                Scanner scanner = new Scanner(fail);

                Library tempLib = new Library(scanner.nextLine());//Library nimi
                List<Book> tempBooks = new ArrayList<>();

                while (scanner.hasNextLine()) {
                    String rida = scanner.nextLine();
                    String[] data = rida.split(";");

                    //Raamatu parameetrite määramine
                    Book tempBook = new Book(data[0].substring(1), data[1]);
                    if (!data[2].equals("null")) tempBook.setPublicationDate(data[2]);
                    if (!data[3].equals("null")) tempBook.setGenre(data[3]);
                    if (!data[4].equals("null")) tempBook.setISBN(data[4]);
                    if (!data[5].equals("null")) tempBook.setStatus(Status.valueOf(data[5]));
                    tempBooks.add(tempBook);//Lisamine raamatute Listi
                }
                scanner.close();

                //Raaamtute lisamine Librarysse, Library lisamine ArrayListi
                tempLib.addBooks(tempBooks);
                allLibs.add(tempLib);

            } catch (FileNotFoundException e) {
                System.out.println("Tekkis viga, faili ei leitud");
                e.printStackTrace();
            }

        }//iter genereerimine

        return allLibs;
    }//Initialize
}//class