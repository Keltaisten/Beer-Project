package beercatalog;

public class BeerAndPrice {
    private String beer;
    private int price;

    public BeerAndPrice(String brandName, int allPrice) {
        this.beer = brandName;
        this.price = allPrice;
    }

    @Override
    public String toString() {
        return "BeerAndPrice{" +
                "beer='" + beer + '\'' +
                ", price=" + price +
                '}';
    }

    public String getBeer() {
        return beer;
    }

    public int getPrice() {
        return price;
    }
}
