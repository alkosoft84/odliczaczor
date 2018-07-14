package pl.alkosoft.odliczaczor.data;

public enum Months {
    JANUARY("January",31),
    FEBRUARY("February",28),
    MARCH("March",31),
    APRIL("April",30),
    MAY("May",31),
    JUNE("June",30),
    JULY("July",31),
    AUGUST("August",31),
    SEPTEMBER("September",30),
    OCTOBER("October",31),
    NOVEMBER("November",30),
    DECEMBER("December",31);

    private final String value;
    private final Integer daysCount;

    Months(String value, Integer daysCount) {
        this.value = value;
        this.daysCount = daysCount;
    }

    public String getValue(){
        return value;
    }

    public Integer getDaysCount() {
        return daysCount;
    }
}
