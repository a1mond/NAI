import java.util.List;
import java.util.Objects;

public class Iris {
    private final List<Double> coords;
    private final String name;

    public Iris(List<Double> coords, String name) {
        this.coords = coords;
        this.name = name;
    }

    public Iris(List<Double> coords) {
        this.coords = coords;
        this.name = null;
    }
    public Iris(Iris iris, String name) {
        this.coords = iris.coords;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Double> getCoords() {
        return coords;
    }

    @Override
    public String toString() {
        return "" + coords + " -- " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Iris)) return false;
        Iris iris = (Iris) o;
        return Objects.equals(coords, iris.coords) && Objects.equals(name, iris.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coords, name);
    }
}
