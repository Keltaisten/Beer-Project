package controller;

import java.util.List;

public interface Validator {
    boolean validateName(String nextLine);
    boolean validateNumbers(String nextLine, String numbers);
    boolean validateType(String nextLine);
    boolean validateIngredient(String nextLine);
    boolean validateInput(String nextLine, List<String> parameter);
}
