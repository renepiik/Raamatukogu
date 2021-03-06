package com.sarec;

import com.sarec.components.Book;
import com.sarec.components.Library;
import com.sarec.components.Status;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

class Startup {

    public static ArrayList<Library> Initialize() {
        //Loeb .csv failid sisse, moodustab nende sisudest objects.Library objektid ja koondab need ArrayListi
        //Väljastab objects.Library objektide ArrayListi

        ArrayList<Library> allLibs = new ArrayList<>();

        //.csv failide lugemine
        List<String> csvFiles = new ArrayList<>();

        // failide lugemine try-catchi sees, et programm käivituks ka siis, kui libs kausta olemas pole
        // NullPointerExceptioni puhul luuakse kaust
        try {
            File dir = new File(Vars.libsPath);
            for (File file : Objects.requireNonNull(dir.listFiles())) {
                if (file.getName().endsWith((".csv"))) {
                    csvFiles.add(file.getName());
                }
            }

            //objects.Library objektide genereerimine Arraylisti
            for (String csvFile : csvFiles) {
                try {
                    String filePath = Vars.libsPath+csvFile;
                    File fail = new File(filePath);
                    Scanner scanner = new Scanner(fail);

                    Library tempLib = new Library(scanner.nextLine());//objects.Library nimi
                    List<Book> tempBooks = new ArrayList<>();

                    while (scanner.hasNextLine()) {
                        String rida = scanner.nextLine();
                        String[] data = rida.split(";");

                        //Raamatu parameetrite määramine
                        Book tempBook = new Book(data[0].substring(1), data[1]);
                        if (!data[2].equals("")) tempBook.setPublicationDate(data[2]);
                        if (!data[3].equals("")) tempBook.setGenre(data[3]);
                        if (!data[4].equals("")) tempBook.setISBN(data[4]);
                        if (!data[5].equals("")) tempBook.setStatus(Status.valueOf(data[5]));
                        tempBooks.add(tempBook);//Lisamine raamatute Listi
                    }
                    scanner.close();

                    //Raaamtute lisamine Librarysse, objects.Library lisamine ArrayListi
                    tempLib.addBooks(tempBooks);
                    allLibs.add(tempLib);

                } catch (FileNotFoundException e) {
                    System.out.println("Tekkis viga, faili ei leitud");
                    e.printStackTrace();
                }

            }//iter genereerimine

            return allLibs;
        } catch (Exception NullPointerException) {
            // loob libs/ directory kuna seda veel pole, tagastab tühja raamatukogude listi
            new File(Vars.libsPath).mkdirs();
            return allLibs;
        }
    }//Initialize
}//class
