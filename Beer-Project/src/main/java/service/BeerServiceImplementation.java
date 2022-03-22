package service;

import beercatalog.Beer;
import beercatalog.BeerIdWithIngredientRatio;
import beercatalog.BrandsWithBeers;
import beercatalog.BeerAndPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import repository.BeerRepo;
import repository.BeerRepoImplementation;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BeerServiceImplementation implements BeerService {
    private List<Beer> beers = new ArrayList<>();
    private FileService fileManager;
    private List<BrandsWithBeers> brandsWithBeers = new ArrayList<>();
    private BeerRepo beerRepo = new BeerRepoImplementation();

    public BeerServiceImplementation() {
    }

    public BeerServiceImplementation(String path) {
        fileManager = new FileServiceImplementation();
        beers = fileManager.readJsonFile(path);
    }

    public void saveDataToDb() {
        beerRepo.saveBeers(beers);
    }

    public void addBeer(Beer beer) {
        beers.add(beer);
    }

    public void addBrandsWithBeers(BrandsWithBeers bwb) {
        brandsWithBeers.add(bwb);
    }

    @Override
    public List<BrandsWithBeers> groupBeersByBrand(int outputFormat) {
        List<BrandsWithBeers> brandAndBeers = beerRepo.groupBeersByBrandDb();
        String nameOfTheTask = "group_beers_by_brand";
        convertToJson(brandAndBeers, 1, outputFormat, nameOfTheTask);
        return brandAndBeers;
    }

    @Override
    public List<String> filterBeersByBeerType(String type, int outputFormat) {
        List<String> beerIds = beerRepo.filterBeersByBeerTypeDb(type).get();
        String nameOfTheTask = "filter_beers_by_type";
        convertToJson(beerIds, 2, outputFormat, nameOfTheTask);
        return beerIds;
    }

    @Override
    public String getTheCheapestBrand(int outputFormat) {
        List<BeerAndPrice> brandsWithPrices = beerRepo.getTheCheapestBrandDb()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));

        String cheapestBrand = brandsWithPrices.stream()
                .collect(Collectors.toMap(BeerAndPrice::getBeer, BeerAndPrice::getPrice, Integer::sum))
                .entrySet().stream()
                .sorted(Comparator.comparing(k -> k.getValue())).map(k -> k.getKey()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));
        String nameOfTheTask = "the_cheapest_brand";
        convertToJson(cheapestBrand, 3, outputFormat, nameOfTheTask);
        return cheapestBrand;
    }

    @Override
    public List<String> getIdsThatLackSpecificIngredient(String ingredient, int outputFormat) {
        List<String> idsWithoutSpecificIngredient = beerRepo.getIdsThatLackSpecificIngredientDb(ingredient)
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));
        String nameOfTheTask = "get_ids_that_lack_from_specific_ingredient";
        convertToJson(idsWithoutSpecificIngredient, 4, outputFormat, nameOfTheTask);
        return idsWithoutSpecificIngredient;
    }

    @Override
    public List<String> sortAllBeersByRemainingIngredientRatio(int outputFormat) {
        List<String> result = beerRepo.sortAllBeersByRemainingIngredientRatioDb()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list")).stream()
                .collect(Collectors.toMap(BeerIdWithIngredientRatio::getId, BeerIdWithIngredientRatio::getRatio, Double::sum))
                .entrySet().stream()
                .sorted(Comparator.comparing(k -> k.getValue()))
                .map(k -> k.getKey())
                .collect(Collectors.toList());
        String nameOfTheTask = "sort_all_beers_by_remaining_ingredient_ratio";
        convertToJson(result, 5, outputFormat, nameOfTheTask);
        return result;
    }

    @Override
    public Map<Integer, List<String>> listBeersBasedOnTheirPriceWithATip(int outputFormat) {
        List<BeerAndPrice> beersAndPrices = new ArrayList<>(beerRepo.listBeersBasedOnTheirPriceWithATipDb()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list")));

        Map<Integer, List<String>> beersAndRoundedPrices = new TreeMap<>();
        for (BeerAndPrice bap : beersAndPrices) {
            int roundedPrice = roundPrice(bap.getPrice());
            List<String> tempBeers = beersAndRoundedPrices.computeIfAbsent(roundedPrice, k -> new ArrayList<>());
            tempBeers.add(bap.getBeer());
        }
        String nameOfTheTask = "list_beers_based_on_their_price_with_a_tip";
        convertToJson(beersAndRoundedPrices, 6, outputFormat, nameOfTheTask);
        return beersAndRoundedPrices;
    }

    private Integer roundPrice(int price) {
        return ((price + 99) / 100) * 100;
    }

    public String convertToJson(Object o, int taskNumber, int outputFormat, String nameOfTheTask) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        String result;
        try {
            result = objectMapper.writeValueAsString(o);
            if (outputFormat == 1) {
                System.out.println(result);
            } else if (outputFormat == 2) {
                objectMapper.writeValue(new File(taskNumber + ". " + nameOfTheTask + ".json"), o);
            }
            return result;
        } catch (IOException ioe) {
            throw new IllegalStateException("Cannot write file", ioe);
        }
    }

    public List<Beer> getBeers() {
        return beers;
    }

    public FileService getFileManager() {
        return fileManager;
    }

    public List<BrandsWithBeers> getBrandsWithBeers() {
        return brandsWithBeers;
    }

}
