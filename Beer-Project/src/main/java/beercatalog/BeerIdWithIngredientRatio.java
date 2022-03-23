package beercatalog;

public class BeerIdWithIngredientRatio {
    private String id;
    private double ratio;

    public BeerIdWithIngredientRatio(String id, double ratio) {
        this.id = id;
        this.ratio = ratio;
    }

    public String getId() {
        return id;
    }

    public double getRatio() {
        return ratio;
    }

    @Override
    public String toString() {
        return "BeerIdWithIngredientRatio{" +
                "id='" + id + '\'' +
                ", ratio=" + ratio +
                '}';
    }
}
