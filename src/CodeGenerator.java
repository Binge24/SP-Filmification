
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Binge2/Ryuusei
 */
public class CodeGenerator {

    private String filmSelected = "MultistageNetwork";

    public CodeGenerator(String filmSelected) {
        this.filmSelected = filmSelected;
    }

    public void overwriteFile(List<String> list, String rows,
            String columns, String iterations, String fileName) throws IOException {

        List<String> newList = new ArrayList<String>();

        if (filmSelected == "JacobiRelaxation") {
            for (int i = 0; i < list.size(); i++) {
                if (i == 22) {
                    newList.add("\trows dw " + rows + "\t\t;Added from Filmification v1.0");
                } else if (i == 23) {
                    newList.add("\tcols dw " + columns + "\t\t;Added from Filmification v1.0");
                } else if (i == 26) {
                    newList.add("\titersLimit dw " + iterations + "\t\t;Added from Filmification v1.0");
                } else {
                    newList.add(list.get(i));
                }
            }
        } else if (filmSelected == "MultistageNetwork"){
            for (int i = 0; i < list.size(); i++) {
                if (i == 16) {
                    newList.add("\tUsize db " + rows + "h\t\t;Added from Filmification v1.0");
                } else if (i < 388 && i > 379) {
                    newList.add("");
                } else {
                    newList.add(list.get(i));
                }
            }
        }

        Path file = Paths.get(fileName + ".flm");
        Files.write(file, newList, Charset.forName("UTF-8"));
        file = Paths.get(fileName + ".asm");
        Files.write(file, newList, Charset.forName("UTF-8"));
    }

    public List<String> readFile(String path) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(path));
        String str;

        List<String> list = new ArrayList<String>();

        while ((str = in.readLine()) != null) {
            list.add(str);
        }

        return list;
    }
}
