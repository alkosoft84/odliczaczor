package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import pl.alkosoft.odliczaczor.Odliczaczor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.data.Assets.*;
import static pl.alkosoft.odliczaczor.screen.Screens.MENU_SCREEN;

class SplashScreen implements Screen {

    private final Odliczaczor app;
    private Stage stage;
    private BitmapFont fontArcon20;

    public SplashScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        fontArcon20 = app.getAssetManager().get(FONT_ARCON.getPath(), BitmapFont.class);
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Image splashBackground = app.getScreenHelper().createImageActor(SPLASH_SCREEN_BACKGROUND.getPath(), -1, -1);
        Image splashLogoImg = app.getScreenHelper().createImageActor(SPLASH_SCREEN_LOGO.getPath(), -1, HEIGHT / 2 + 20);
        Image splashEscapeImg = app.getScreenHelper().createImageActor(SPLASH_SCREEN_ESCAPE_CAPTION.getPath(), -1, HEIGHT / 2 - 200);
        splashBackground.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        splashLogoImg.addAction(sequence(alpha(0f), fadeIn(2f)));
        splashEscapeImg.addAction(sequence(alpha(0f), delay(1f), fadeIn(2), delay(1f),
                run(() -> app.setScreen(app.getScreenManager().getScreen(MENU_SCREEN)))));
        stage.addActor(splashBackground);
        stage.addActor(splashEscapeImg);
        stage.addActor(splashLogoImg);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();

        app.getBatch().begin();
        fontArcon20.setColor(Color.WHITE);
        fontArcon20.draw(app.getBatch(), "* All assets were download from google and music was download from Youtube." +
                        " This app was created only for peronal use. " +
                        "Distribution of this app is illegal.",
                280, 60);
        app.getBatch().end();
    }

    @Override
    public void dispose() {
        stage.dispose();
        fontArcon20.dispose();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
