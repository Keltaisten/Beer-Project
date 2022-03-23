package controller;

import controller.validator.Validator;
import controller.validator.ValidatorImplementation;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorImplementationTest {

    Validator validator = new ValidatorImplementation();

    @Test
    void validateNameTest() {
        assertTrue(validator.validateName("John"));
        assertFalse(validator.validateName(""));
    }

    @Test
    void validateNumbersTest() {
        assertTrue(validator.validateNumbers("3", Arrays.asList(1,2,3,4)));
        assertFalse(validator.validateNumbers("22",Arrays.asList(1,2,3,4)));
    }

    @Test
    void validateTypeTest() {
        assertTrue(validator.validateType("brown"));
        assertFalse(validator.validateType("notIncluded"));
    }

    @Test
    void validateIngredientTest() {
        assertTrue(validator.validateIngredient("salt"));
        assertFalse(validator.validateIngredient("brown sugar"));
    }

    @Test
    void validateInputTest(){
        assertTrue(validator.validateName("Data"));
        assertFalse(validator.validateName(""));
    }

    @Test
    void validateFillingDataBaseInputTest() {
        assertTrue(validator.validateFillingDataBaseInput("yes"));
        assertTrue(validator.validateFillingDataBaseInput("NO"));
        assertFalse(validator.validateFillingDataBaseInput("maybe"));
    }
}