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

    private Properties properties;
    private List<LocalDate> holidays;
    private LocalTime startOfWork;
    private LocalTime endOfWork;

    public RemainingTimeService(Properties properties) {
        this.properties = properties;
        holidays = new Holidays().getHolidays();
        this.startOfWork = properties.getStartOfWork();
        this.endOfWork = properties.getEndOfWork();
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
        if (localTime.isBefore(endOfWork)) {
            if(localTime.isAfter(startOfWork)){
                todayWorkingHours = HOURS.between(localTime, endOfWork);
                todayWorkingMinutes = MINUTES.between(localTime, endOfWork);
                todayWorkingSeconds = SECONDS.between(localTime, endOfWork);
            }else{
                todayWorkingHours = HOURS.between(startOfWork, endOfWork);
                todayWorkingMinutes = MINUTES.between(startOfWork, endOfWork);
                todayWorkingSeconds = SECONDS.between(startOfWork, endOfWork);
            }

        }
        if ((localTime.isBefore(endOfWork) && localTime.isAfter(startOfWork) && isNotWeekend(currentDate))) {
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


    //getters & setters
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

    private int getMonthNumber(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }

    public LocalTime getStartOfWork() {
        return startOfWork;
    }

    public LocalTime getEndOfWork() {
        return endOfWork;
    }
}
