package pl.alkosoft.odliczaczor.service;

import pl.alkosoft.odliczaczor.data.ExplosionTimes;
import pl.alkosoft.odliczaczor.data.Holidays;
import pl.alkosoft.odliczaczor.data.Properties;

import java.time.*;
import java.util.List;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.ChronoUnit.*;

public class RemainingTimeService {

    private final static LocalTime END_OF_WORK = LocalTime.of(16, 0, 0);
    private final static LocalTime START_OF_WORK = LocalTime.of(7, 23, 0);
    private Properties properties;
    private List<LocalDate> holidays;

    public RemainingTimeService(Properties properties) {
        this.properties = properties;
        holidays = new Holidays().getHolidays();
    }

    public boolean validateDate(LocalDate currentDate, String selectedYear, String selectedMonth, String selectedDay) {
        LocalDate chosenDate = createLocalDateFromSelectedValues(selectedYear, selectedMonth, selectedDay);
        return currentDate.isBefore(chosenDate) ? true : false;
    }

    public void updateRemainingTimesInProperties(LocalDateTime currentDateTime, String selectedYear, String selectedMonth, String selectedDay) {
        LocalDate chosenDate = createLocalDateFromSelectedValues(selectedYear, selectedMonth, selectedDay);
        LocalDate currentDate = LocalDate.from(currentDateTime);
        LocalTime localTime = LocalTime.from(currentDateTime);
        long remainingWorkingDays = countWorkingDaysBetween(currentDate, chosenDate);
        properties.setRemainingWorkingDays(remainingWorkingDays);
        long todayWorkingHours = 0;
        long todayWorkingMinutes = 0;
        long todayWorkingSeconds = 0;
        if (localTime.isBefore(END_OF_WORK)) {
            if(localTime.isAfter(START_OF_WORK)){
                todayWorkingHours = HOURS.between(localTime, END_OF_WORK);
                todayWorkingMinutes = MINUTES.between(localTime, END_OF_WORK);
                todayWorkingSeconds = SECONDS.between(localTime, END_OF_WORK);
            }else{
                todayWorkingHours = HOURS.between(START_OF_WORK, END_OF_WORK);
                todayWorkingMinutes = MINUTES.between(START_OF_WORK, END_OF_WORK);
                todayWorkingSeconds = SECONDS.between(START_OF_WORK, END_OF_WORK);
            }

        }
        if ((localTime.isBefore(END_OF_WORK) && localTime.isAfter(START_OF_WORK))) {
            properties.setWorkingHours(true);
        } else {
            properties.setWorkingHours(false);
        }

        properties.setRemainingWorkingHours((remainingWorkingDays * 8) + todayWorkingHours);
        properties.setRemainingWorkingMinutes((remainingWorkingDays * 8 * 60) + todayWorkingMinutes);
        properties.setRemainingWorkingSeconds((remainingWorkingDays * 8 * 60 * 60) + todayWorkingSeconds);
    }

    private LocalDate createLocalDateFromSelectedValues(String selectedYear, String selectedMonth, String selectedDay) {
        int year = Integer.parseInt(selectedYear);
        int month = getMonthNumber(selectedMonth);
        int days = Integer.parseInt(selectedDay);

        return LocalDate.of(year, month, days);
    }

    public long getRemainingWorkingDays() {
        return properties.getRemainingWorkingDays();
    }

    public long getRemainingWorkingHours() {
        return properties.getRemainingWorkingHours();
    }

    public long getRemainingWorkingMinutes() {
        return properties.getRemainingWorkingMinutes();
    }

    public long getRemainingWorkingSeconds() {
        return properties.getRemainingWorkingSeconds();
    }

    public boolean isWorkingHours() {
        return properties.isWorkingHours();
    }

    public void setIsWorkingHours(boolean isWorkingHours){
        this.properties.setWorkingHours(isWorkingHours);
    }

    public int getHowOftenBombBlow() {
        return this.properties.getHowOftenBombBlow();
    }

    public void setHowOftenBombBlow(String howOftenBombBlow) {
        this.properties.setHowOftenBombBlow(ExplosionTimes.findByName(howOftenBombBlow).getExplosionTimeInSecond());
    }

    private long countWorkingDaysBetween(LocalDate currentDate, LocalDate chosenDate) {
        int dayCounter = 0;
        currentDate = currentDate.plusDays(1);
        while (currentDate.isBefore(chosenDate)) {
            if (isNotWeekend(currentDate) && isNotHoliday(currentDate)) {
                dayCounter++;
            }
            currentDate = currentDate.plusDays(1);
        }
        return dayCounter;
    }

    private boolean isNotWeekend(LocalDate currentDate) {
        return !(currentDate.getDayOfWeek().equals(SATURDAY) || currentDate.getDayOfWeek().equals(SUNDAY));
    }

    private boolean isNotHoliday(LocalDate currentDate) {
        for (LocalDate holiday : holidays) {
            if (currentDate.isEqual(holiday)) {
                return false;
            }
        }
        return true;
    }

    private int getMonthNumber(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }
}
