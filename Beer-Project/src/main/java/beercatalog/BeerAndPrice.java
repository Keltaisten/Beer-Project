package beercatalog;

public class BeerAndPrice {
    private String beer;
    private int price;
//    private int numberOfBeersByBrand;
//    private Double averagePrice;
//
//    public BeerAndPrice(String brandName) {
//        this.brandName = brandName;
//    }

    public BeerAndPrice(String brandName, int allPrice) {
        this.beer = brandName;
        this.price = allPrice;
    }

//    public void incrementPrice(int price) {
//        this.price += price;
//        numberOfBeersByBrand++;
//        calculateAveragePrice();
//    }

//    public void calculateAveragePrice() {
//        averagePrice =  ((double) price / numberOfBeersByBrand);
//    }

    public String getBeer() {
        return beer;
    }

    public int getPrice() {
        return price;
    }

//    public double getAveragePrice() {
//        return averagePrice;
//    }

}
