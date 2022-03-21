package service;

import beercatalog.Beer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileServiceImplementation implements FileService {

    @Override
    public List<Beer> readJsonFile(String path) {
        List<Beer> beers;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            beers = objectMapper.readValue(new File(path), new TypeReference<>() {
            });
        } catch (IOException ioe) {
            throw new IllegalStateException("Cannot read file", ioe);
        }
        return beers;
    }
}
