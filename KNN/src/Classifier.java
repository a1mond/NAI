import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Classifier {
    private int k;
    private final String METHOD;
    private final HashSet<Iris> train_set;
    private final HashSet<Iris> test_set;
    private final HashSet<Iris> output_set;

    public Classifier(String[] args) throws Exception {
        File train_file;
        File test_file;
        if (args.length > 3) {
            k = 5;
            METHOD = args[0];
            train_file = new File(args[1]);
            test_file = new File(args[2]);
        } else {
            throw new Exception("""
                    Number of arguments should be exactly 4 where
                    1) Euclidean or Manhattan ('eu' or 'mh')
                    2) Path to training data
                    3) Path to test data
                    4) Type of GUI (kplot, splot)""");
        }

        train_set = Reader.read(train_file);
        test_set = Reader.read(test_file);
        output_set = new HashSet<>();
    }
    public void assignOutputSet() {
        for (Iris i : test_set) {
            Iris iris = classify(i);
            output_set.add(iris);
        }
    }

    public Iris classify(Iris iris) {
        // Assigning new map, where key is value of distance to given iris,
        // and second value of iris ot which it was calculated
        HashMap<Iris, Double> map = new HashMap<>();
        int num_s = 0, num_v = 0, num_vg = 0;

        // Calculating those distances
        if (METHOD.equals("eu")) {
            for (Iris i : train_set)
                map.put(i, eu_distance(i, iris));
        } else {
            for (Iris i : train_set)
                map.put(i, mh_distance(i, iris));
        }
        // Finding irises which are the most close to given iris
        HashMap<Iris, Double> min_map = find_mins(map);

        // Based on those minimums, find names to which this is most closely placed
        for (Map.Entry<Iris, Double> pair : min_map.entrySet()) {
            switch (pair.getKey().getName()) {
                case "Iris-setosa" -> num_s++;
                case "Iris-versicolor" -> num_v++;
                case "Iris-virginica" -> num_vg++;
            }
        }
        // Find maximum
        int max = Math.max(Math.max(num_s, num_v), num_vg);
        if (max == num_s) return new Iris(iris, "Iris-setosa");
        else if (max == num_v) return new Iris(iris, "Iris-versicolor");
        else return new Iris(iris, "Iris-virginica");
    }
    public int calc_mistake() {
        double correct = 0;
        for (Iris i : test_set) {
            if (output_set.contains(i)) correct++;
        }
        return (int) Math.round((correct / test_set.size()) * 100);
    }
    private HashMap<Iris, Double> find_mins(HashMap<Iris, Double> entry_map) {
        HashMap<Iris, Double> new_map = new HashMap<>();
        for (int i = 0; i < k; i++) {
            Double min = Collections.min(entry_map.values());
            Iris iris = null;
            for (Map.Entry<Iris, Double> entry : entry_map.entrySet()) {
                if (entry.getValue().equals(min)) {
                    iris = entry.getKey();
                }
            }
            new_map.put(iris, min);
            entry_map.remove(iris);
        }
        return new_map;
    }
    private double eu_distance(Iris i1, Iris i2) {
        return Math.sqrt(
                Math.pow(i1.getX1() - i2.getX1(), 2) +
                Math.pow(i1.getX2() - i2.getX2(), 2) +
                Math.pow(i1.getX3() - i2.getX3(), 2) +
                Math.pow(i1.getX4() - i2.getX4(), 2)
        );
    }
    private double mh_distance(Iris i1, Iris i2) {
        return (
                Math.abs(i1.getX1() - i2.getX1()) +
                Math.abs(i1.getX2() - i2.getX2()) +
                Math.abs(i1.getX3() - i2.getX3()) +
                Math.abs(i1.getX4() - i2.getX4())
                );
    }

    public HashSet<Iris> getOutputSet() {
        return output_set;
    }

    public void setK(int k) {
        this.k = k;
    }
}
