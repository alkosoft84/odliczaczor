package pl.alkosoft.odliczaczor.service;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import pl.alkosoft.odliczaczor.Odliczaczor;
import pl.alkosoft.odliczaczor.data.RGBColor;

import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;

public class ScreenUtils {
    private final Odliczaczor app;

    public ScreenUtils(Odliczaczor app) {
        this.app = app;
    }

    //if you set x or y for "-1" value it will be Aligned center
    public Image createImageActor(String textureUrl, float x, float y) {
        Texture texture = app.getAssetManager().get(textureUrl);
        Image image = new Image(texture);
        if (x == -1) {
            x = WIDTH / 2 - image.getWidth() / 2;
        }
        if (y == -1) {
            y = HEIGHT / 2 - image.getHeight() / 2;
        }
        image.setPosition(x, y);
        return image;
    }

    //if you set x or y for "-1" value it will be Aligned center
    public TextButton createDefaultButton(String name, RGBColor color, Skin skin, float width, float height, float x, float y) {
        TextButton button = new TextButton(name, skin, "default");
        button.setColor(color.getRed(),color.getGreen(),color.getBlue(),color.getAlpha());
        button.setSize(width, height);
        if (x == -1) {
            x = WIDTH / 2 - button.getWidth() / 2;
        }
        if (y == -1) {
            y = HEIGHT / 2 - button.getHeight() / 2;
        }
        button.setPosition(x, y);
        return button;
    }
}
