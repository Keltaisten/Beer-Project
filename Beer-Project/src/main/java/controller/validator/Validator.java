package controller.validator;

import java.util.List;

public interface Validator {
    boolean validateName(String nextLine);
    boolean validateNumbers(String nextLine, List<Integer> numbers);
    boolean validateType(String nextLine);
    boolean validateIngredient(String nextLine);
    //    boolean validateInput(String nextLine, List<String> parameter);
    boolean validateFillingDataBaseInput(String nextLine);
}
