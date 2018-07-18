package pl.alkosoft.odliczaczor.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import static pl.alkosoft.odliczaczor.data.Assets.*;
import static pl.alkosoft.odliczaczor.entity.BombStates.*;

public class Bomb implements Entity {

    private AssetManager assetManager;
    private Vector2 position;
    private Animation<TextureRegion> bombAnimation;
    private Animation<TextureRegion> bombReadyToExplode;
    private Animation<TextureRegion> explosionAnimation;
    private float bombRuntime;
    private float explosionRuntime;
    private float bombReadyToExplodeRuntime;

    private BombStates bombState;

    public Bomb(AssetManager assetManager, Vector2 position) {
        this.assetManager = assetManager;
        TextureAtlas bombTA = this.assetManager.get(BOMBS_PACK.getPath());
        TextureAtlas bombReadyToExplodeTA = assetManager.get(BOMB_EXPLODING_PACK.getPath());
        TextureAtlas explosionTA = assetManager.get(EXPLOSION_PACK.getPath());
        this.position = position;
        this.bombState = NORMAL;
        this.bombRuntime = 0f;
        this.bombReadyToExplodeRuntime = 0f;
        this.explosionRuntime = 0f;
        this.bombAnimation = new Animation<>(2f, bombTA.getRegions());
        this.bombReadyToExplode = new Animation<>(2f, bombReadyToExplodeTA.getRegions());
        this.explosionAnimation = new Animation<>(2f, explosionTA.getRegions());
    }

    @Override
    public void update() {
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.bombState.equals(NORMAL)) {
            sb.draw(bombAnimation.getKeyFrame(this.bombRuntime, true), position.x, position.y);
            this.bombRuntime += 0.2;
        } else if (this.bombState.equals(READY_TO_EXPLODE)) {
            sb.draw(bombReadyToExplode.getKeyFrame(this.bombReadyToExplodeRuntime, true), position.x, position.y);
            this.bombReadyToExplodeRuntime += 0.2;
        } else if (this.bombState.equals(EXPLODED)) {
            sb.draw(explosionAnimation.getKeyFrame(this.explosionRuntime), position.x, position.y);
            this.explosionRuntime += 0.3;
        }

    }

    @Override
    public void setState(BombStates state) {
        this.bombState = state;
    }

    @Override
    public BombStates getState() {
        return this.bombState;
    }
}
