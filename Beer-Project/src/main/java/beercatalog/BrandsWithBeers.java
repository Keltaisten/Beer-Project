package beercatalog;

import java.util.List;
import java.util.Objects;

public class BrandsWithBeers {
    private String brand;
    private List<String> beers;

    public BrandsWithBeers() {}

    public BrandsWithBeers(String brand, List<String> beers) {
        this.brand = brand;
        this.beers = beers;
    }

    public void addBeer(String beer){
        beers.add(beer);
    }

    @Override
    public String toString() {
        return "BrandsWithBeers{" +
                "brand='" + brand + '\'' +
                ", beers=" + beers +
                '}';
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getBeers() {
        return beers;
    }

    public void setBeers(List<String> beers) {
        this.beers = beers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandsWithBeers that = (BrandsWithBeers) o;
        return Objects.equals(brand, that.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand);
    }
}
