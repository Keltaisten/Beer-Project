package beercatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BeerTest {
    Beer beer1 = new Beer("ccw-1", "Coding Challenge White", "Coding Challenge Brewery",
            "White", 4645, 0.119,
            Arrays.asList(
                    new Ingredient("salt", 0.002),
                    new Ingredient("sugar", 0.014),
                    new Ingredient("barley", 0.025),
                    new Ingredient("wheat", 0.014),
                    new Ingredient("corn", 0)));

    @BeforeEach
    void init() {

    }


    @Test
    void checkIfIngredientNotInclude() {
    }

    @Test
    void addIngredient() {

    }

    @Test
    void getName() {
        assertEquals("Coding Challenge White",beer1.getName());
    }

    @Test
    void getBrand() {
        assertEquals("Coding Challenge Brewery",beer1.getBrand());
    }

    @Test
    void getType() {
        assertEquals("White",beer1.getType());
    }

    @Test
    void getPrice() {
        assertEquals(4645,beer1.getPrice());
    }
}