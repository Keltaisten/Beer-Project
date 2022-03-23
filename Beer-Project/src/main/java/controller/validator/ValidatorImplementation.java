package controller.validator;

import controller.enums.Ingredients;
import controller.enums.Types;

import java.util.Arrays;
import java.util.List;

public class ValidatorImplementation implements Validator {

    @Override
    public boolean validateName(String nextLine) {
        if (nextLine == null || nextLine.trim().equals("")) {
            System.out.println("Name not correct.\nPlease add your name!");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean validateNumbers(String nextLine, List<Integer> numbers) {
        String line;
        if (nextLine == null || nextLine.trim().equals("")) {
            System.out.println("No given parameter");
            return false;
        }
        line = nextLine.trim();
        if (line.length() != 1) {
            System.out.println("Given parameter is not ok");
            return false;
        } else if (!Character.isDigit(line.charAt(0))) {
            System.out.println("Given parameter is not a number");
            return false;
        } else if (numbers.contains(Integer.parseInt(line))) {
            return true;
        }
        System.out.println("Given parameter is not ok");
        return false;
    }

    @Override
    public boolean validateType(String nextLine) {
        if (nextLine == null || nextLine.trim().equals("")) {
            System.out.println("No given type");
            return false;
        } else if(Arrays.stream(Types.values()).map(Types::getName).toList().contains(nextLine)){
            return true;
        }
        else {
            System.out.println(nextLine + " is not included. Select from the list.");
            return false;
        }
    }

    @Override
    public boolean validateIngredient(String nextLine) {
        if (nextLine == null || nextLine.trim().equals("")) {
            System.out.println("No given parameter");
            return false;
        } else if(Arrays.stream(Ingredients.values()).map(Ingredients::getName).toList().contains(nextLine)){
            return true;
        }
        else {
            System.out.println(nextLine + " is not included. Select from the list.");
            return false;
        }
    }

    @Override
    public boolean validateInput(String nextLine) {
        if (nextLine == null || nextLine.trim().equals("")) {
            System.out.println("No given parameter");
            return false;
        }
        return true;
    }

    @Override
    public boolean validateFillingDataBaseInput(String nextLine) {
        String line = nextLine.toLowerCase();
        if (nextLine == null || nextLine.trim().equals("")) {
            System.out.println("No given parameter");
            return false;
        } else if("yes".equals(line) || "no".equals(line)){
            return true;
        }
        else {
            System.out.println("Given parameter is not ok: " + nextLine);
            return false;
        }
    }
}
