package controller;

import controller.validator.Validator;
import controller.validator.ValidatorImplementation;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorImplementationTest {

    Validator validator = new ValidatorImplementation();

    @Test
    void validateNameTrueTest() {
        assertTrue(validator.validateName("John"));
    }

    @Test
    void validateNameFalseTest() {
        assertFalse(validator.validateName(""));
    }

    @Test
    void validateNumbersTrueTest() {
        assertTrue(validator.validateNumbers("3", Arrays.asList(1,2,3,4)));
    }

    @Test
    void validateNumbersFalseTest() {
        assertFalse(validator.validateNumbers("22",Arrays.asList(1,2,3,4)));
    }

    @Test
    void validateTypeTrueTest() {
        assertTrue(validator.validateType("brown"));
    }

    @Test
    void validateTypeFalseTest() {
        assertFalse(validator.validateType("stout"));
    }

    @Test
    void validateIngredientTrueTest() {
        assertTrue(validator.validateIngredient("salt"));
    }

    @Test
    void validateIngredientFalseTest() {
        assertFalse(validator.validateIngredient("brown sugar"));
    }

    @Test
    void validateInputTrueTest(){
        assertTrue(validator.validateName("Data"));
    }

    @Test
    void validateInputFalseTest(){
        assertFalse(validator.validateName(""));
    }

    @Test
    void validateFillingDataBaseInputTrueTest() {
        assertTrue(validator.validateFillingDataBaseInput("yes"));
        assertTrue(validator.validateFillingDataBaseInput("NO"));
    }

    @Test
    void validateFillingDataBaseInputFalseTest() {
        assertFalse(validator.validateFillingDataBaseInput("maybe"));
    }
}