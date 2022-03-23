package service;

import beercatalog.Beer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceImplementationTest {
    FileServiceImplementation fmi = new FileServiceImplementation();

    @Test
    void readJsonFileListSizeTest() {
        List<Beer> beers = fmi.readJsonFile("src/main/resources/demo.json");
        assertEquals(25, beers.size());
    }

    @Test
    void readJsonFileGetAlcoholOfAnElementTest() {
        List<Beer> beers = fmi.readJsonFile("src/main/resources/demo.json");
        assertEquals(0.141, beers.get(3).getAlcohol());
    }

    @Test
    void readJsonFileGetIngredientNameTest() {
        List<Beer> beers = fmi.readJsonFile("src/main/resources/demo.json");
        assertEquals("barley", beers.get(15).getIngredients().get(2).getName());
    }

    @Test
    void readJsonFileExceptionTest() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> fmi.readJsonFile("word\"/:?*!<>|"));
        assertEquals("Cannot read file", exception.getMessage());
    }
}