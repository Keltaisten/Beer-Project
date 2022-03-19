package beercatalog;

public class Ingredient {
    private String name;
    private double ratio;

    public Ingredient() {
    }

    public Ingredient(String name, double ratio) {
        this.name = name;
        this.ratio = ratio;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", ratio=" + ratio +
                '}';
    }

    public String getName() {
        return name;
    }

    public double getRatio() {
        return ratio;
    }
}
