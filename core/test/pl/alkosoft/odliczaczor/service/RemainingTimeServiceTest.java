package pl.alkosoft.odliczaczor.service;

import org.junit.Before;
import org.junit.Test;
import pl.alkosoft.odliczaczor.data.Properties;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class RemainingTimeServiceTest {

    private final static LocalDateTime CURRENT_DATE = LocalDateTime.of(2018, 8, 1, 14, 50, 0);
    private final static LocalDateTime CURRENT_DATE_AFTER_WORKING_HOURS = LocalDateTime.of(2018, 8, 1, 16, 50, 0);
    private RemainingTimeService remainingTimeService;

    @Before
    public void setUp() {
        remainingTimeService = new RemainingTimeService(new Properties());

    }

    @Test
    public void should_return_one_day_between_these_dates() {
        //given
        //when
        remainingTimeService.updateRemainingTimesInProperties(CURRENT_DATE, "2018", "AUGUST", "3");
        //then
        long remainingWorkingDays = remainingTimeService.getRemainingWorkingDays();
        assertThat(remainingWorkingDays).isEqualTo(1);
    }

    @Test
    public void should_return_five_days_between_dates_when_weekend_is_included() {
        //given
        //when
        remainingTimeService.updateRemainingTimesInProperties(CURRENT_DATE, "2018", "AUGUST", "9");
        //then
        long remainingWorkingDays = remainingTimeService.getRemainingWorkingDays();
        assertThat(remainingWorkingDays).isEqualTo(5);
    }

    @Test
    public void should_return_10_days_between_dates_when_weekend_and_holiday_are_included() {
        //given
        //when
        remainingTimeService.updateRemainingTimesInProperties(CURRENT_DATE, "2018", "AUGUST", "17");
        //then
        long remainingWorkingDays = remainingTimeService.getRemainingWorkingDays();
        assertThat(remainingWorkingDays).isEqualTo(10);
    }

    @Test
    public void should_translate_workingDays_to_workingHours_and_add_today_hours() {
        //given
        //when
        remainingTimeService.updateRemainingTimesInProperties(CURRENT_DATE, "2018", "AUGUST", "3");
        //then
        long remainingWorkingHours = remainingTimeService.getRemainingWorkingHours();
        assertThat(remainingWorkingHours).isEqualTo(9);
    }

    @Test
    public void should_translate_workingDays_to_workingMinutes_and_add_today_minutes() {
        //given
        //when
        remainingTimeService.updateRemainingTimesInProperties(CURRENT_DATE, "2018", "AUGUST", "3");
        //then
        long remainingWorkingMinutes = remainingTimeService.getRemainingWorkingMinutes();
        assertThat(remainingWorkingMinutes).isEqualTo(550);
    }

    @Test
    public void should_translate_workingDays_to_workingSeconds_and_add_today_seconds() {
        //given
        //when
        remainingTimeService.updateRemainingTimesInProperties(CURRENT_DATE, "2018", "AUGUST", "3");
        //then
        long remainingWorkingSeconds = remainingTimeService.getRemainingWorkingSeconds();
        assertThat(remainingWorkingSeconds).isEqualTo(33000);
    }

    @Test
    public void should_translate_workingDays_to_workingSeconds_and_add_0_seconds_when_currentDate_is_after_work() {
        //given
        //when
        remainingTimeService.updateRemainingTimesInProperties(CURRENT_DATE_AFTER_WORKING_HOURS, "2018", "AUGUST", "3");
        //then
        long remainingWorkingSeconds = remainingTimeService.getRemainingWorkingSeconds();
        assertThat(remainingWorkingSeconds).isEqualTo(28800);
    }

    @Test
    public void should_return_TRUE_if_date_is_in_the_future() {
        //given
        //when
        boolean isValidDate = remainingTimeService.validateDate(LocalDate.from(CURRENT_DATE),  "2018", "AUGUST", "3");
        //then
        assertThat(isValidDate).isTrue();
    }

    @Test
    public void should_return_FALSE_if_date_is_NOT_in_the_future() {
        //given
        //when
        boolean isValidDate = remainingTimeService.validateDate(LocalDate.from(CURRENT_DATE),  "2018", "JUNE", "3");
        //then
        assertThat(isValidDate).isFalse();
    }


}