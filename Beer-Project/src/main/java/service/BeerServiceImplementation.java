package service;

import beercatalog.Beer;
import beercatalog.BeerIdWithIngredientRatio;
import beercatalog.BrandsWithBeers;
import beercatalog.BeerAndPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import controller.Application;
import controller.enums.OutputFormat;
import repository.BeerRepo;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BeerServiceImplementation implements BeerService {
    private static final String PATH = "src/main/resources/demo.json";
    private List<Beer> beers = new ArrayList<>();
    private FileService fileManager;
    private List<BrandsWithBeers> brandsWithBeers = new ArrayList<>();
    private BeerRepo beerRepo;
    private Application application = new Application();

    public BeerServiceImplementation() {
    }

    public BeerServiceImplementation(BeerRepo beerRepo) {
        this.beerRepo = beerRepo;
        fileManager = new FileServiceImplementation();
//        beers = fileManager.readJsonFile(path);
    }

    @Override
    public void saveDataToDb(String path) {
        beers = fileManager.readJsonFile(path);
        beerRepo.saveBeers(beers);
    }

    @Override
    public void addBeer(Beer beer) {
        beers.add(beer);
    }

    @Override
    public void addBrandsWithBeers(BrandsWithBeers bwb) {
        brandsWithBeers.add(bwb);
    }

    @Override
    public List<BrandsWithBeers> groupBeersByBrand(OutputFormat outputFormat, String name) {
        List<BrandsWithBeers> brandAndBeers = beerRepo.groupBeersByBrandDb();
        String nameOfTheTask = "group_beers_by_brand";
        convertToJson(brandAndBeers, 1, outputFormat, nameOfTheTask, name);
        return brandAndBeers;
    }

    @Override
    public List<String> filterBeersByBeerType(String type, OutputFormat outputFormat, String name) {
        List<String> beerIds = beerRepo.filterBeersByBeerTypeDb(type).get();
        String nameOfTheTask = "filter_beers_by_type";
        convertToJson(beerIds, 2, outputFormat, nameOfTheTask, name);
        return beerIds;
    }

    @Override
    public String getTheCheapestBrand(OutputFormat outputFormat, String name) {
        String cheapestBrand = beerRepo.getBeersAndPricesForTheCheapestBrandDb()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"))
                .stream()
                .collect(Collectors.toMap(BeerAndPrice::getBeer, BeerAndPrice::getPrice, Integer::sum))
                .entrySet().stream()
                .sorted(Comparator.comparing(k -> k.getValue()))
                .map(k -> k.getKey()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));
        String nameOfTheTask = "the_cheapest_brand";
        convertToJson(cheapestBrand, 3, outputFormat, nameOfTheTask, name);
        return cheapestBrand;
    }

    @Override
    public List<String> getIdsThatLackSpecificIngredient(String ingredient, OutputFormat outputFormat, String name) {
        List<String> idsWithoutSpecificIngredient = beerRepo.getIdsThatLackSpecificIngredientDb(ingredient)
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));
        String nameOfTheTask = "get_ids_that_lack_from_specific_ingredient";
        convertToJson(idsWithoutSpecificIngredient, 4, outputFormat, nameOfTheTask, name);
        return idsWithoutSpecificIngredient;
    }

    @Override
    public List<String> sortAllBeersByRemainingIngredientRatio(OutputFormat outputFormat, String name) {
        List<String> result = beerRepo.getBeerIdsWithIngrRatioForsortAllBeersByRemainingIngredientRatioDb()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list")).stream()
                .collect(Collectors.toMap(BeerIdWithIngredientRatio::getId, BeerIdWithIngredientRatio::getRatio, Double::sum))
                .entrySet().stream()
                .sorted(Comparator.comparing(k -> k.getValue()))
                .map(k -> k.getKey())
                .collect(Collectors.toList());
        String nameOfTheTask = "sort_all_beers_by_remaining_ingredient_ratio";
        convertToJson(result, 5, outputFormat, nameOfTheTask, name);
        return result;
    }

    @Override
    public Map<Integer, List<String>> listBeersBasedOnTheirPriceWithATip(OutputFormat outputFormat, String name) {
        List<BeerAndPrice> beersAndPrices = new ArrayList<>(beerRepo.listBeersWithPriceForTipCalculationDb()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list")));
        Map<Integer, List<String>> beersAndRoundedPrices = new TreeMap<>();
        convertToMap(beersAndPrices, beersAndRoundedPrices);
        String nameOfTheTask = "list_beers_based_on_their_price_with_a_tip";
        convertToJson(beersAndRoundedPrices, 6, outputFormat, nameOfTheTask, name);
        return beersAndRoundedPrices;
    }

    private void convertToMap(List<BeerAndPrice> beersAndPrices, Map<Integer, List<String>> beersAndRoundedPrices) {
        for (BeerAndPrice bap : beersAndPrices) {
            int roundedPrice = roundPrice(bap.getPrice());
            List<String> tempBeers = beersAndRoundedPrices.computeIfAbsent(roundedPrice, k -> new ArrayList<>());
            tempBeers.add(bap.getBeer());
        }
    }

    @Override
    public void updatePrice() {
        beerRepo.selectAndUpdatePrice();
    }

    @Override
    public boolean deleteBeerById(String number) {
        return beerRepo.deleteBeerByIdDb(number);
    }

    @Override
    public Integer roundPrice(int price) {
        return ((price + 99) / 100) * 100;
    }

    @Override
    public String convertToJson(Object o, int taskNumber, OutputFormat outputFormat, String nameOfTheTask, String name) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String result;
        try {
            result = objectMapper.writeValueAsString(o);
            if (outputFormat == OutputFormat.CONSOLE) {
                application.writeServiceResultToConsole(result);
            } else if (outputFormat == OutputFormat.JSON_FILE) {
                objectMapper.writeValue(new File(new StringBuilder()
                                .append(taskNumber)
                                .append(". ")
                                .append(nameOfTheTask)
                                .append("_")
                                .append(name)
                                .append(".json")
                                .toString())
                        , o);
            }
            return result;
        } catch (IOException ioe) {
            throw new IllegalStateException("Cannot write file", ioe);
        }
    }

    @Override
    public List<Beer> getBeers() {
        return beers;
    }

    @Override
    public FileService getFileManager() {
        return fileManager;
    }

    @Override
    public List<BrandsWithBeers> getBrandsWithBeers() {
        return brandsWithBeers;
    }

    @Override
    public BeerRepo getBeerRepo() {
        return beerRepo;
    }
}
