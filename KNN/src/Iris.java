import java.util.Objects;

public class Iris {
    private final double x1, x2, x3, x4;
    private final String name;

    public Iris(double x1, double x2, double x3, double x4, String name) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.name = name;
    }

    public Iris(double x1, double x2, double x3, double x4) {
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
        this.x4 = x4;
        this.name = null;
    }

    public Iris(Iris i, String name) {
        this.x1 = i.x1;
        this.x2 = i.x2;
        this.x3 = i.x3;
        this.x4 = i.x4;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getX3() {
        return x3;
    }

    public double getX4() {
        return x4;
    }

    @Override
    public String toString() {
        return "" + x1 + "," + x2 + "," + x3 + "," + x4 + "," + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Iris)) return false;
        Iris iris = (Iris) o;
        return Double.compare(iris.x1, x1) == 0 && Double.compare(iris.x2, x2) == 0 && Double.compare(iris.x3, x3) == 0 && Double.compare(iris.x4, x4) == 0 && Objects.equals(name, iris.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, x2, x3, x4, name);
    }
}
