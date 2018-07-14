package pl.alkosoft.odliczaczor.entity;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import pl.alkosoft.odliczaczor.Odliczaczor;

public class BombsManager {

    private Array<Bomb> bombs;
    private Odliczaczor app;
    private final static int MAX_AMOUNT = 70;
    private int bombsExplosionTimer;
    private int bombsExplosionCounter;
    private int bombSafetySwitch;
    private Music chickenExplodeSound;

    public BombsManager(Odliczaczor app, int amount) {
        this.app = app;
        this.bombs = new Array<>();
        this.bombSafetySwitch = 1;
        this.bombsExplosionTimer = app.getRemainingTimeService().getHowOftenBombBlow();
        this.bombsExplosionCounter = 0;
        this.chickenExplodeSound = app.assetManager.get("sfx/chicken_explode.mp3");
        initBombs(amount);

    }

    private void initBombs(int amount) {
        int finalAmount = amount > MAX_AMOUNT ? MAX_AMOUNT : amount;
        int cols = 10;
        int counterX = 0;
        int counterY = 0;

        for (int i = 0; i < finalAmount; i++) {
            if (i != 0 && i % cols == 0) {
                counterX = 0;
                counterY++;
            }
            addEntity(new Bomb(app.assetManager, new Vector2(800 + counterX * 100, 890 - counterY * 100)));
            counterX++;
        }
        if (bombs.size > 0) {
            bombs.get(0).setState("readyToExplode");
        }
    }

    public void update() {
        for (Entity bomb : bombs) {
            bomb.update();
        }

    }

    public void render(SpriteBatch sb, float timePassed) {
        int timePassedInSeconds = (int) timePassed % bombsExplosionTimer;
            if (timePassedInSeconds == 0 && timePassedInSeconds == bombSafetySwitch && bombsExplosionCounter < bombs.size) {
                chickenExplodeSound.setVolume(0.3f);
                chickenExplodeSound.play();
                bombs.get(bombsExplosionCounter).setState("exploded");
                bombsExplosionCounter++;
                if(bombsExplosionCounter < bombs.size){
                    bombs.get(bombsExplosionCounter).setState("readyToExplode");
                }
                bombSafetySwitch=1;
            }
            if(timePassedInSeconds==1){
                bombSafetySwitch=0;
            }

        for (Entity bomb : bombs) {
            bomb.render(sb);
        }
    }

    public boolean isAnyBombLeft() {

        for (Bomb bomb : bombs) {
            if (bomb.getState() != "exploded") {
                return true;
            }
        }
        return false;
    }

    private void addEntity(Bomb bomb) {
        bombs.add(bomb);
    }

}
