package pl.alkosoft.odliczaczor.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Holidays {

    private List<LocalDate> holidays;

    public Holidays() {
        holidays = new ArrayList<>();
        init();
    }

    public Holidays(List<LocalDate> holidays) {
        this.holidays = holidays;
    }

    private void init(){
        holidays.add(LocalDate.of(2018,8,15));
        holidays.add(LocalDate.of(2018,1,1));
        holidays.add(LocalDate.of(2018,11,11));
        holidays.add(LocalDate.of(2018,12,25));
        holidays.add(LocalDate.of(2018,12,26));
    }

    public List<LocalDate> getHolidays() {
        return holidays;
    }
}
