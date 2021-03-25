import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Classifier {
    private int k;
    private final HashSet<Iris> train_set;
    private final HashSet<Iris> test_set;
    private final HashSet<Iris> output_set;

    public Classifier(String[] args) throws Exception {
        File train_file;
        File test_file;
        if (args.length == 2) {
            k = 5;
            train_file = new File(args[0]);
            test_file = new File(args[1]);
        } else {
            throw new Exception("""
                    Number of arguments should be exactly 3 where
                    2) Path to training data
                    3) Path to test data
                    4) Type of GUI (kplot, console)""");
        }

        train_set = Reader.read(train_file);
        test_set = Reader.read(test_file);
        output_set = new HashSet<>();
    }
    public void assignOutputSet() {
        output_set.clear();
        for (Iris i : test_set)
            output_set.add(classify(i));
    }

    public Iris classify(Iris iris) {
        HashMap<Iris, Double> map = new HashMap<>();

        for (Iris i : train_set) {
            double distance = eu_distance(i, iris);
            if (distance != -1) map.put(i, eu_distance(i, iris));
        }

        HashMap<Iris, Double> min_map = find_mins(map);
        if (min_map.isEmpty()) {
            System.out.println("Cannot classify " + iris + ", no elements matching this in train set");
            return new Iris(iris, "Unclassifiable");
        } else {
            HashMap<String, Long> counts = new HashMap<>(min_map.keySet().stream().collect(Collectors.groupingBy(Iris::getName, Collectors.counting())));

            Map.Entry<String, Long> max = Collections.max(counts.entrySet(), Map.Entry.comparingByValue());
            return new Iris(iris, max.getKey());
        }
    }
    public int calc_mistake() {
        double correct = 0;
        for (Iris test_i : test_set) {
            if (output_set.contains(test_i)) correct++;
        }
        return (int) Math.round((correct / test_set.size()) * 100);
    }
    private HashMap<Iris, Double> find_mins(HashMap<Iris, Double> entry_map) {
        HashMap<Iris, Double> new_map = new HashMap<>();
        for (int i = 0; i < k; i++) {
            if (entry_map.isEmpty()) return new_map;
            Double min = Collections.min(entry_map.values());
            Iris iris = null;
            for (Map.Entry<Iris, Double> entry : entry_map.entrySet()) {
                if (entry.getValue().equals(min) && min != -1) {
                    iris = entry.getKey();
                }
            }
            new_map.put(iris, min);
            entry_map.remove(iris);
        }
        return new_map;
    }
    private double eu_distance(Iris i1, Iris i2) {
        if (i1.getCoords().size() != i2.getCoords().size()) return -1;
        double sum = 0;
        Iterator<Double> it1 = i1.getCoords().iterator();
        Iterator<Double> it2 = i2.getCoords().iterator();
        while (it1.hasNext() && it2.hasNext()) {
            sum += Math.pow(it1.next() - it2.next(), 2);
        }
        return Math.sqrt(sum);
    }

    public HashSet<Iris> getOutputSet() {
        return output_set;
    }

    public void setK(int k) {
        this.k = k;
    }
}
