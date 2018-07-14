package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pl.alkosoft.odliczaczor.Odliczaczor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;
import static pl.alkosoft.odliczaczor.screen.Screens.MENU_SCREEN;

class SplashScreen implements Screen {

    private final Odliczaczor app;
    private Stage stage;

    private BitmapFont fontArcon20;
    private Image splashBackground;
    private Image splashImg;
    private Image splashLogo;

    public SplashScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        this.stage = new Stage(new StretchViewport(WIDTH, HEIGHT, app.camera));
        Gdx.input.setInputProcessor(stage);
        fontArcon20 = app.assetManager.get("font/Arcon.ttf", BitmapFont.class);
        Texture splashBackgroundTex = app.assetManager.get("gdx/splashScreen.png");
        Texture splashImgTex = app.assetManager.get("gdx/escape_odlicz.png");
        Texture splashLogoTex = app.assetManager.get("gdx/splash_logo.png");
        splashBackground = new Image(splashBackgroundTex);
        splashImg = new Image(splashImgTex);
        splashLogo = new Image(splashLogoTex);
        stage.addActor(splashBackground);
        stage.addActor(splashImg);
        stage.addActor(splashLogo);
        splashBackground.setPosition(WIDTH / 2 - splashBackground.getWidth()/2, HEIGHT / 2 - splashBackground.getHeight() / 2);
        splashLogo.setPosition(WIDTH / 2 - splashLogo.getWidth() / 2, HEIGHT / 2 - splashLogo.getHeight() / 2 + 100);
        splashImg.setPosition(WIDTH / 2 - splashImg.getWidth() / 2, HEIGHT / 2 - 200);
        splashBackground.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        splashLogo.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        splashImg.addAction(sequence(alpha(0f), delay(1f), fadeIn(2), delay(1f), run(() -> app.setScreen(app.screenManager.getScreen(MENU_SCREEN)))));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();

        app.batch.begin();
        fontArcon20.setColor(Color.WHITE);
        fontArcon20.draw(app.batch, "* All assets were download from google and music was download from Youtube." +
                " This app was created only for peronal use. " +
                "Distribution of this app is illegal.",
                280, 60);
        app.batch.end();
    }

    private void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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

    @Override
    public void dispose() {
        stage.dispose();
    }
}
