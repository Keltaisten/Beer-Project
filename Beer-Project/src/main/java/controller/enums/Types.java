package controller.enums;

public enum Types {
    BROWN("brown"), CORN("corn"), PALE_ALE("pale ale"),
    WHEAT("wheat"), WHITE("white");

    private final String name;

    Types(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
