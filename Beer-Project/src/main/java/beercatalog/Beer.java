package beercatalog;

import java.util.List;

public class Beer {
    private String id;
    private String name;
    private String brand;
    private String type;
    private int price;
    private double alcohol;
    private List<Ingredient> ingredients;
    private double waterIngredient;

    public boolean checkIfIngredientNotInclude(String ingredient) {
        for (Ingredient actual : ingredients) {
            if (actual.getName().equals(ingredient) && actual.getRatio() == 0) {
                return true;
            }
        }
        return false;
    }

    public Beer() {
    }

    public Beer(String id, String name, String brand, String type, int price, double alcohol, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.price = price;
        this.alcohol = alcohol;
        this.ingredients = ingredients;
    }

    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
    }

    @Override
    public String toString() {
        return "Beer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", alcohol=" + alcohol +
                ", ingredients=" + ingredients +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public double getAlcohol() {
        return alcohol;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public double getWaterIngredient() {
        return waterIngredient;
    }
}
