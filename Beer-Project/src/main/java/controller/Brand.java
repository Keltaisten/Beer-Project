package controller;

public enum Brand {
    CCB("Coding Challenge Brewery"), LUB("Level Up Brewery"), BSB("Beer Sans Brewery"),
    CSB("Coding Sans Brewery"), SHA("Share");

    private final String name;

    Brand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
