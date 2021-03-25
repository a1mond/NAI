import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        Classifier classifier = new Classifier(args);
        classifier.assignOutputSet();
        System.out.println("\nAccuracy is " + classifier.calc_mistake() + "%");
        Scanner sc = new Scanner(System.in);
        String line;
        while (true) {
            System.out.println("Type in the parameters in form of 'x1,x2,...,xN', or just 'quit'");
            line = sc.nextLine();
            if (line.equals("quit")) break;
            List<Double> list = Arrays.stream(line.split(","))
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());
            Iris iris = classifier.classify(new Iris(list));
            if (!iris.getName().equals("Unclassifiable")) System.out.println("Your Iris was classified as " + iris.getName());
        }
    }
}
