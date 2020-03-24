import java.util.ArrayList;

public class Test {
    public static void main(String[] args) throws Exception{

        ArrayList<Library> allLibs = Startup.Initialize();

        for (Library library : allLibs) {
            System.out.println(library);
            for (Book book : library.getBooks()) {
                System.out.println(book);
            }
        }



    }
}
