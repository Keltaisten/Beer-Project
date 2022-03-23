package controller.enums;

public enum Ingredients {
    BARLEY("barley"), CORN("corn"), SALT("salt"),
    SUGAR("sugar"), WHEAT("wheat");

    private final String name;

    Ingredients(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
