package pl.alkosoft.odliczaczor.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Entity {

    void update();
    void render(SpriteBatch sb);
    void setState(String state);
    String getState();

}
