package pl.alkosoft.odliczaczor.entity;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import pl.alkosoft.odliczaczor.Odliczaczor;

import static pl.alkosoft.odliczaczor.data.Assets.CHICKEN_EXPLODE_SFX;
import static pl.alkosoft.odliczaczor.entity.BombStates.EXPLODED;
import static pl.alkosoft.odliczaczor.entity.BombStates.READY_TO_EXPLODE;

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
        this.chickenExplodeSound = app.getMusicManager().getChickenExplodeSfx();
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
            addEntity(new Bomb(app.getAssetManager(), new Vector2(800 + counterX * 100, 890 - counterY * 100)));
            counterX++;
        }
        if (bombs.size > 0) {
            bombs.get(0).setState(READY_TO_EXPLODE);
        }
    }

    public void update() {
        for (Entity bomb : bombs) {
            bomb.update();
        }
    }

    public void render(SpriteBatch sb, float timePassed) {
        int timePassedInSeconds = (int) timePassed % bombsExplosionTimer;
        if (bombShouldBlow(timePassedInSeconds)) {
            chickenExplodeSound.setVolume(0.2f);
            chickenExplodeSound.play();
            bombs.get(bombsExplosionCounter).setState(EXPLODED);
            bombsExplosionCounter++;
            if (bombsExplosionCounter < bombs.size) {
                bombs.get(bombsExplosionCounter).setState(READY_TO_EXPLODE);
            }
            bombSafetySwitch = 1;
        }
        if (timePassedInSeconds == 1) {
            bombSafetySwitch = 0;
        }

        for (Entity bomb : bombs) {
            bomb.render(sb);
        }
    }

    private boolean bombShouldBlow(int timePassedInSeconds) {
        return timePassedInSeconds == 0 && timePassedInSeconds == bombSafetySwitch && bombsExplosionCounter < bombs.size;
    }

    public boolean isAnyBombLeft() {
        for (Bomb bomb : bombs) {
            if (bomb.getState() != EXPLODED) {
                return true;
            }
        }
        return false;
    }

    private void addEntity(Bomb bomb) {
        bombs.add(bomb);
    }

}
