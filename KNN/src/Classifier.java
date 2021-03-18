import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Classifier {
    private static int k;
    private static String METHOD;

    private static HashSet<Iris> train_set;
    private static HashSet<Iris> test_set;
    private static HashSet<Iris> output_set;

    public static void main(String[] args) throws Exception {
        File train_file;
        File test_file;
        if (args.length == 4) {
            k = Integer.parseInt(args[0]);
            METHOD = args[1];
            train_file = new File(args[2]);
            test_file = new File(args[3]);
        } else {
            throw new Exception("""
                    Number of arguments should be exactly 4 where
                    1) value for K
                    2) Euclidean or Manhattan ('eu' or 'mh')
                    3) Path to training data
                    4) Path to test data""");
        }

        train_set = Reader.read(train_file);
        test_set = Reader.read(test_file);
        output_set = new HashSet<>();

        for (Iris i : test_set) {
            Iris iris = classify(i);
            output_set.add(iris);
            System.out.println(iris);
        }
        System.out.println("\nCorrectness of KNN for k-value " + k + " is " + calc_mistake());
    }
    private static Iris classify(Iris iris) {
        HashMap<Double, Iris> map = new HashMap<>();
        int num_s = 0, num_v = 0, num_vg = 0;

        if (METHOD.equals("eu")) {
            for (Iris i : train_set)
                map.put(eu_distance(i, iris), i);
        } else {
            for (Iris i : train_set)
                map.put(mh_distance(i, iris), i);
        }

        HashMap<Double, Iris> min_map = find_mins(map);
        for (Map.Entry<Double, Iris> pair : min_map.entrySet()) {
            switch (pair.getValue().getName()) {
                case "Iris-setosa" -> num_s++;
                case "Iris-versicolor" -> num_v++;
                case "Iris-virginica" -> num_vg++;
            }
        }
        int max = Math.max(Math.max(num_s, num_v), num_vg);
        if (max == num_s) return new Iris(iris, "Iris-setosa");
        else if (max == num_v) return new Iris(iris, "Iris-versicolor");
        else return new Iris(iris, "Iris-virginica");
    }
    private static String calc_mistake() {
        double correct = 0;
        for (Iris i : test_set) {
            if (output_set.contains(i)) correct++;
        }
        return "" + Math.round((correct / test_set.size()) * 100) + "%";
    }
    @SuppressWarnings("")
    private static HashMap<Double, Iris> find_mins(HashMap<Double, Iris> entry_map) {
        HashMap<Double, Iris> new_map = new HashMap<>();
        for (int i = 0; i < k; i++) {
            double min = -1;
            for (Map.Entry<Double, Iris> pair : entry_map.entrySet()) {
                if (min != -1) {
                    if (pair.getKey() < min)
                        min = pair.getKey();
                } else {
                    min = pair.getKey();
                }
            }
            new_map.put(min, entry_map.get(min));
            entry_map.remove(min);
        }
        return new_map;
    }
    private static double eu_distance(Iris i1, Iris i2) {
        return Math.sqrt(
                Math.pow(i1.getX1() - i2.getX1(), 2) +
                Math.pow(i1.getX2() - i2.getX2(), 2) +
                Math.pow(i1.getX3() - i2.getX3(), 2) +
                Math.pow(i1.getX4() - i2.getX4(), 2)
        );
    }
    private static double mh_distance(Iris i1, Iris i2) {
        return (
                Math.abs(i1.getX1() - i2.getX1()) +
                Math.abs(i1.getX2() - i2.getX2()) +
                Math.abs(i1.getX3() - i2.getX3()) +
                Math.abs(i1.getX4() - i2.getX4())
                );
    }
}
