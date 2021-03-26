import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Reader {
    public static HashSet<Iris> read(File file) {
        HashSet<Iris> set = new HashSet<>();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                String[] lines = line.split(",");
                List<Double> coords = new LinkedList<>();
                for (int i = 0; i < lines.length - 1; i++) {
                    coords.add(Double.parseDouble(lines[i]));
                    set.add(new Iris(coords, lines[lines.length - 1]));
                }
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }
}
