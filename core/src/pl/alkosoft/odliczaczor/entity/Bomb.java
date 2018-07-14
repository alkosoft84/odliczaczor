package pl.alkosoft.odliczaczor.entity;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import static pl.alkosoft.odliczaczor.data.Textures.BOMB_EXPLOSION_TEXTURE_ATLAS;
import static pl.alkosoft.odliczaczor.data.Textures.BOMB_TEXTURE_ATLAS;
import static pl.alkosoft.odliczaczor.data.Textures.EXPLOSION_TEXTURE_ATLAS;

public class Bomb implements Entity {

    private AssetManager assetManager;
    private TextureAtlas bombTA;
    private TextureAtlas bombReadyToExplodeTA;
    private TextureAtlas explosionTA;
    private Vector2 position;
    private Animation<TextureRegion> bombAnimation;
    private Animation<TextureRegion> bombReadyToExplode;
    private Animation<TextureRegion> explosionAnimation;
    private float bombRuntime;
    private float explosionRuntime;
    private float bombReadyToExplodeRuntime;

    private String bombState;

    public Bomb(AssetManager assetManager, Vector2 position) {
        this.assetManager = assetManager;
        this.bombTA = assetManager.get(BOMB_TEXTURE_ATLAS.getUrl());
        this.bombReadyToExplodeTA = assetManager.get(BOMB_EXPLOSION_TEXTURE_ATLAS.getUrl());
        this.explosionTA = assetManager.get(EXPLOSION_TEXTURE_ATLAS.getUrl());
        this.position = position;
        this.bombState="normal";
        this.bombRuntime=0f;
        this.bombReadyToExplodeRuntime=0f;
        this.explosionRuntime=0f;
        this.bombAnimation = new Animation<>(2f, this.bombTA.getRegions());
        this.bombReadyToExplode = new Animation<>(2f, this.bombReadyToExplodeTA.getRegions());
        this.explosionAnimation = new Animation<>(2f, this.explosionTA.getRegions());
    }

    @Override
    public void update() {
    }

    @Override
    public void render(SpriteBatch sb){
        if(this.bombState.equals("normal")) {
            sb.draw(bombAnimation.getKeyFrame(this.bombRuntime, true), position.x, position.y);
            this.bombRuntime += 0.2;
        }
        else if(this.bombState.equals("readyToExplode")){
                sb.draw(bombReadyToExplode.getKeyFrame( this.bombReadyToExplodeRuntime, true), position.x, position.y);
                this.bombReadyToExplodeRuntime+=0.2;
        }
        else if(this.bombState.equals("exploded")){
            sb.draw(explosionAnimation.getKeyFrame(this.explosionRuntime), position.x, position.y);
            this.explosionRuntime+=0.3;
        }

    }

    @Override
    public void setState(String state) {
        this.bombState=state;
    }

    @Override
    public String getState() {
        return this.bombState;
    }
}
