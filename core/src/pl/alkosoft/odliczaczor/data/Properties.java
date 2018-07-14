package pl.alkosoft.odliczaczor.data;

public class Properties {

    private long remainingWorkingDays;
    private long remainingWorkingHours;
    private long remainingWorkingMinutes;
    private long remainingWorkingSeconds;

    private boolean workingHours;

    private int howOftenBombBlow;

    public boolean isWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(boolean workingHours) {
        this.workingHours = workingHours;
    }

    public long getRemainingWorkingDays() {
        return remainingWorkingDays;
    }

    public void setRemainingWorkingDays(long remainingWorkingDays) {
        this.remainingWorkingDays = remainingWorkingDays;
    }

    public long getRemainingWorkingHours() {
        return remainingWorkingHours;
    }

    public void setRemainingWorkingHours(long remainingWorkingHours) {
        this.remainingWorkingHours = remainingWorkingHours;
    }

    public long getRemainingWorkingMinutes() {
        return remainingWorkingMinutes;
    }

    public void setRemainingWorkingMinutes(long remainingWorkingMinutes) {
        this.remainingWorkingMinutes = remainingWorkingMinutes;
    }

    public long getRemainingWorkingSeconds() {
        return remainingWorkingSeconds;
    }

    public void setRemainingWorkingSeconds(long remainingWorkingSeconds) {
        this.remainingWorkingSeconds = remainingWorkingSeconds;
    }

    public int getHowOftenBombBlow() {
        return howOftenBombBlow;
    }

    public void setHowOftenBombBlow(int howOftenBombBlow) {
        this.howOftenBombBlow = howOftenBombBlow;
    }
}
