package controller;

import controller.validator.Validator;
import controller.validator.ValidatorImplementation;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorImplementationTest {

    Validator validator = new ValidatorImplementation();

    @Test
    void validateName() {
    }

    @Test
    void validateNumbers() {
        assertTrue(validator.validateNumbers("3", Arrays.asList(1,2,3,4)));
        assertFalse(validator.validateNumbers("22",Arrays.asList(1,2,3,4)));
    }

    @Test
    void validateType() {
        assertTrue(validator.validateType("brown"));
        assertFalse(validator.validateType("notIncluded"));
    }

    @Test
    void validateIngredient() {
        assertTrue(validator.validateIngredient("salt"));
        assertFalse(validator.validateIngredient("brown sugar"));
    }

    @Test
    void validateFillingDataBaseInput() {
        assertTrue(validator.validateFillingDataBaseInput("yes"));
        assertTrue(validator.validateFillingDataBaseInput("NO"));
        assertFalse(validator.validateFillingDataBaseInput("maybe"));
    }
}