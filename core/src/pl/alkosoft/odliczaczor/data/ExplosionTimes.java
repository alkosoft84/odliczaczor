package pl.alkosoft.odliczaczor.data;

public enum ExplosionTimes {
    SECONDS("10 Seconds",10),
    MINUTE("Minute", 60),
    HOUR("Hour", 3600);

    private final String name;
    private final int explosionTimeInSecond;

    ExplosionTimes(String name, int explosionTimeInSecond) {
        this.name = name;
        this.explosionTimeInSecond=explosionTimeInSecond;
    }

    public String getName() {
        return name;
    }

    public int getExplosionTimeInSecond() {
        return explosionTimeInSecond;
    }

    public static ExplosionTimes findByName(String name){
        for (ExplosionTimes explosionTime : ExplosionTimes.values()) {
            if(explosionTime.getName().equals(name)){
                return explosionTime;
            }

        }
        throw new RuntimeException("We don't have that time to explode our bombs");
    }
}
