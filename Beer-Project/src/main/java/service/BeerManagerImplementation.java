package service;

import beercatalog.Beer;
import beercatalog.BrandsWithBeers;
import beercatalog.BrandsWithPrices;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BeerManagerImplementation implements BeerManager {
    List<Beer> beers;
    FileManager fileManager;

    public BeerManagerImplementation(String path) {
        fileManager = new FileManagerImplementation();
        beers = fileManager.readJsonFile(path);
    }


    @Override
    public String groupBeersByBrand() {
//        List<String> beersByGroups;
//        Map<String, List<String>> brandAndBeers = new HashMap<>();
//
//        for (Beer bc : beers) {
//            beersByGroups = brandAndBeers.computeIfAbsent(bc.getBrand(), k -> new ArrayList<>());
//            beersByGroups.add(bc.getId());
//        }
        List<BrandsWithBeers> brandAndBeers;
//        Map<String, List<BrandsWithBeers>> tempBeer = new HashMap<>();
//        for (Beer beer : beers) {
////            for (BrandsWithBeers bwb : brandAndBeers) {
////                if (beer.getBrand().equals(bwb.getBrand())) {
////                    bwb.addBeer(beer.getName());
////                } else {
////                    brandAndBeers.add(new BrandsWithBeers(beer.getBrand(), new ArrayList<>()));
////                }
////            }
//            if(brandAndBeers.contains(beer.getBrand())){
//            }
//        }
        brandAndBeers = beers.stream().collect(Collectors.groupingBy(Beer::getBrand)).entrySet().stream()
                .map(r-> new BrandsWithBeers(r.getKey(), r.getValue().stream().map(Beer::getId).toList()))
                .toList();
//        String stringFromJsonArrayResult = String.valueOf(addKeysToBrandAndBeers(brandAndBeers));
//        String nameOfTheTask = "groupBeersByBrand";
//        writeSolutionToJsonFile(stringFromJsonArrayResult, nameOfTheTask);
//        System.out.println(stringFromJsonArrayResult);
        String nameOfTheTask = "groupBeersByBrand";
        return convertToJson(brandAndBeers, 1);
    }

    @Override
    public String filterBeersByBeerType(String type) {
        List<String> beerIds = beers.stream()
                .filter(k -> k.getType().equals(type))
                .map(Beer::getId)
                .collect(Collectors.toList());
        String nameOfTheTask = "filterBeersByBeerType";
        return convertToJson(beerIds, 2);
    }

    @Override
    public String getTheCheapestBrand() {
        Map<String, BrandsWithPrices> tempBrandsAndPrices = new HashMap<>();

        for (Beer bc : beers) {
            String brand = bc.getBrand();
            BrandsWithPrices tempBrandsWithPrices = tempBrandsAndPrices
                    .computeIfAbsent(brand, k -> new BrandsWithPrices(brand));
            tempBrandsWithPrices.incrementPrice(bc.getPrice());
        }

        BrandsWithPrices bwp = tempBrandsAndPrices.values().stream()
                .min(Comparator.comparing(BrandsWithPrices::getAveragePrice))
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));
        String nameOfTheTask = "getTheCheapestBrand";
        return convertToJson(bwp.getBrandName(), 3);
    }

    @Override
    public String getIdsThatLackSpecificIngredient(String ingredient) {
        List<String> idsWithoutSpecificIngredient = new ArrayList<>();
        for (Beer actual : beers) {
            if (actual.checkIfIngredientNotInclude(ingredient)) {
                idsWithoutSpecificIngredient.add(actual.getId());
            }
        }

        String nameOfTheTask = "getIdsThatLackSpecificIngredient";
        return convertToJson(idsWithoutSpecificIngredient, 4);
    }

    @Override
    public String sortAllBeersByRemainingIngredientRatio() {
        List<String> beerIds = beers.stream()
                .sorted(Comparator.comparing(Beer::getWaterIngredient).thenComparing(Beer::getId))
                .map(Beer::getId)
                .toList();
        String nameOfTheTask = "sortAllBeersByRemainingIngredientRatio";
        return convertToJson(beerIds, 5);
    }

    @Override
    public String listBeersBasedOnTheirPriceWithATip() {
        Map<Integer, List<String>> beersAndRoundedPrices = new TreeMap<>();
        for (Beer bc : beers) {
            int roundedPrice = ((bc.getPrice() + 99) / 100) * 100;
            List<String> tempBeers = beersAndRoundedPrices.computeIfAbsent(roundedPrice, k -> new ArrayList<>());
            tempBeers.add(bc.getId());
        }

        String nameOfTheTask = "listBeersBasedOnTheirPriceWithATip";
        return convertToJson(beersAndRoundedPrices, 6);
    }

    private String convertToJson(Object o, int taskNumber) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        String result;
        try {
            result = objectMapper.writeValueAsString(o);
        } catch (IOException ioe) {
            throw new IllegalArgumentException("Cannot write file", ioe);
        }
        return result;
    }
}
