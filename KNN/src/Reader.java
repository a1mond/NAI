import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Reader {
    public static HashSet<Iris> read(File file) {
        HashSet<Iris> set = new HashSet<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                set.add(new Iris(
                        Double.parseDouble(lines[0]),
                        Double.parseDouble(lines[1]),
                        Double.parseDouble(lines[2]),
                        Double.parseDouble(lines[3]),
                        lines[4]
                ));
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }
}
