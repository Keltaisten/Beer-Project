package controller;

public enum OutputFormat {
    CONSOLE("console",1), JSON_FILE("jsonFile",2);

    private final String name;
    private final int number;

    OutputFormat(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }
}
