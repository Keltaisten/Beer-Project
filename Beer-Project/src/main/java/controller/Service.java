package controller;

public enum Service {
    GROUP_BY_BRAND(1), FILTER_BEERS_BY_TYPE(2), THE_CHEAPEST_BRAND(3),
            GET_IDS_THAT_LACK_FROM_SPEC_INGR(4),
    SORT_BEERS_BY_REMAINING_INGR_RATIO(5),LIST_BEERS_BASED_ON_THE_PRICE_AND_TIP(6),
    BACK(7),EXIT(8);

    private final int number;

    Service(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
