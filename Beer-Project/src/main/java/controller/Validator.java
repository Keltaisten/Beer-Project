package controller;

import java.util.List;

public interface Validator {
    boolean validateName(String nextLine);
    boolean validateNumbers(String nextLine, String numbers);
    boolean validateType(String nextLine, List<String> types);
    boolean validateIngredient(String nextLine, List<String> ingredients);
    boolean validateInput(String nextLine, List<String> parameter);
}
