package service;

import beercatalog.Beer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import repository.BeerRepo;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileServiceImplementation implements FileService {
    private static final String PATH = "src/main/resources/demo.json";
    private BeerService beerService;
    private BeerRepo beerRepo;

//    public FileServiceImplementation() {
//    }

    public FileServiceImplementation(BeerService beerService) {
        this.beerService = beerService;
    }

    public FileServiceImplementation(BeerRepo beerRepo) {
        this.beerRepo = beerRepo;
    }

    public FileServiceImplementation() {
    }

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
