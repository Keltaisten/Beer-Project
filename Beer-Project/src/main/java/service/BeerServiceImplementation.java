package service;

import beercatalog.Beer;
import beercatalog.BeerIdWithIngredientRatio;
import beercatalog.BrandsWithBeers;
import beercatalog.BeerAndPrice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import repository.BeerRepo;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class BeerServiceImplementation implements BeerService {
    private List<Beer> beers = new ArrayList<>();
    private FileService fileManager;
    private List<BrandsWithBeers> brandsWithBeers = new ArrayList<>();
    private BeerRepo beerRepo = new BeerRepo();

    public BeerServiceImplementation() {
    }

    public BeerServiceImplementation(String path) {
        fileManager = new FileServiceImplementation();
        beers = fileManager.readJsonFile(path);
//        beerRepo.init();
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
//        brandAndBeers = beers.stream().collect(Collectors.groupingBy(Beer::getBrand)).entrySet().stream()
//                .map(r -> new BrandsWithBeers(r.getKey(), r.getValue().stream().map(Beer::getId).toList()))
//                .toList();
//        String stringFromJsonArrayResult = String.valueOf(addKeysToBrandAndBeers(brandAndBeers));
//        String nameOfTheTask = "groupBeersByBrand";
//        writeSolutionToJsonFile(stringFromJsonArrayResult, nameOfTheTask);
//        System.out.println(stringFromJsonArrayResult);
//        String nameOfTheTask = "groupBeersByBrand";
        brandAndBeers = beerRepo.groupBeersByBrandDb();
        String nameOfTheTask = "group_beers_by_brand";

        convertToJson(brandAndBeers, 1, outputFormat, nameOfTheTask);
        return brandAndBeers;
    }

    @Override
    public List<String> filterBeersByBeerType(String type, int outputFormat) {
//        List<String> beerIds = beers.stream()
//                .filter(k -> k.getType().toLowerCase().equals(type))
//                .map(Beer::getId)
//                .collect(Collectors.toList());
////        String nameOfTheTask = "filterBeersByBeerType";
        List<String> beerIds = beerRepo.filterBeersByBeerTypeDb(type).get();
        String nameOfTheTask = "filter_beers_by_type";
        convertToJson(beerIds, 2, outputFormat, nameOfTheTask);
        return beerIds;
    }

    @Override
    public String getTheCheapestBrand(int outputFormat) {
//        Map<String, BrandsWithPrices> tempBrandsAndPrices = new HashMap<>();

        List<BeerAndPrice> brandsWithPrices = beerRepo.getTheCheapestBrandDb()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));

        String cheapestBrand = brandsWithPrices.stream()
                .collect(Collectors.toMap(BeerAndPrice::getBeer, BeerAndPrice::getPrice, Integer::sum))
                .entrySet().stream()
                .sorted(Comparator.comparing(k -> k.getValue())).map(k -> k.getKey()).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));

//        for (BrandsWithPrices actual : brandsWithPrices) {
//            String brand = actual.getBrandName();
//            BrandsWithPrices tempBrandsWithPrices = tempBrandsAndPrices
//                    .computeIfAbsent(brand, k -> new BrandsWithPrices(brand));
//            tempBrandsWithPrices.incrementPrice(actual.getAllPrice());
//        }
//
//        BrandsWithPrices bwp = tempBrandsAndPrices.values().stream()
//                .min(Comparator.comparing(BrandsWithPrices::getAveragePrice))
//                .orElseThrow(() -> new IllegalArgumentException("No data in the list"));
////        String nameOfTheTask = "getTheCheapestBrand";
        String nameOfTheTask = "the_cheapest_brand";
        convertToJson(cheapestBrand, 3, outputFormat, nameOfTheTask);
        return cheapestBrand;
    }

    @Override
    public List<String> getIdsThatLackSpecificIngredient(String ingredient, int outputFormat) {
//        List<String> idsWithoutSpecificIngredient = new ArrayList<>();
//        for (Beer actual : beers) {
//            if (actual.checkIfIngredientNotInclude(ingredient)) {
//                idsWithoutSpecificIngredient.add(actual.getId());
//            }
//        }
//
////        String nameOfTheTask = "getIdsThatLackSpecificIngredient";
        List<String> idsWithoutSpecificIngredient = beerRepo.getIdsThatLackSpecificIngredientDb(ingredient).get();
        String nameOfTheTask = "get_ids_that_lack_from_specific_ingredient";
        convertToJson(idsWithoutSpecificIngredient, 4, outputFormat, nameOfTheTask);
        return idsWithoutSpecificIngredient;
    }

    @Override
    public List<String> sortAllBeersByRemainingIngredientRatio(int outputFormat) {
//        beers.forEach(Beer::setWaterIngredient);
//        List<String> beerIds = beers.stream()
//                .sorted(Comparator.comparing(Beer::getWaterIngredient).thenComparing(Beer::getId))
//                .map(Beer::getId)
//                .toList();
        List<String> result = beerRepo.sortAllBeersByRemainingIngredientRatioDb()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list")).stream()
                .collect(Collectors.toMap(BeerIdWithIngredientRatio::getId, BeerIdWithIngredientRatio::getRatio, Double::sum))
                .entrySet().stream()
                .sorted(Comparator.comparing(k -> k.getValue()))
                .map(k -> k.getKey())
                .collect(Collectors.toList());

//        String nameOfTheTask = "sortAllBeersByRemainingIngredientRatio";
                String nameOfTheTask = "sort_all_beers_by_remaining_ingredient_ratio";
        convertToJson(result, 5, outputFormat, nameOfTheTask);
        return result;
    }

    @Override
    public Map<Integer, List<String>> listBeersBasedOnTheirPriceWithATip(int outputFormat) {
        Map<Integer, List<String>> beersAndPrices = new TreeMap<>(beerRepo.listBeersBasedOnTheirPriceWithATipDb()
                .orElseThrow(() -> new IllegalArgumentException("No data in the list"))
                .stream()
                .collect(Collectors.groupingBy()));

//        Map<Integer, List<String>> beersAndPrices = new TreeMap<>(beerRepo.listBeersBasedOnTheirPriceWithATipDb()
//                .orElseThrow(() -> new IllegalArgumentException("No data in the list"))
//                .stream()
//                .collect(Collectors.toMap()));

        Map<Integer, List<String>> beersAndRoundedPrices = new TreeMap<>();
        for (Beer bc : beers) {
            int roundedPrice = ((bc.getPrice() + 99) / 100) * 100;
            List<String> tempBeers = beersAndRoundedPrices.computeIfAbsent(roundedPrice, k -> new ArrayList<>());
            tempBeers.add(bc.getId());
        }

//        String nameOfTheTask = "listBeersBasedOnTheirPriceWithATip";
        String nameOfTheTask = "list_beers_based_on_their_price_with_a_tip";
        convertToJson(beersAndRoundedPrices, 6, outputFormat, nameOfTheTask);
        return beersAndRoundedPrices;
    }

    private int roundPrice(int price){
        return 
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
            } else if (outputFormat == 3) {
//                BeerRepository beerRepository = new BeerRepository();
//                beerRepository.init();
//                beerRepository.separate(o, taskNumber, nameOfTheTask);
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

    //    private void selectOutPut(Object o, ObjectMapper objectMapper, int taskNumber, int outputFormat) {
//        String result;
//        result = objectMapper.writeValueAsString(o);
//        if (outputFormat == 1) {
//            WriteToConsole writeToConsole = new WriteToConsole();
//            writeToConsole.writeToConsole(task);
//        } else if (outputFormat == 2) {
//            WriteJson writeJson = new WriteJson();
//            writeJson.writeToJsonFile(task);
//        } else if(outputFormat == 3){
//            BeerRepository beerRepository = new BeerRepository();
//            beerRepository.writeToDatabase(task);
//        }
//    }
}
