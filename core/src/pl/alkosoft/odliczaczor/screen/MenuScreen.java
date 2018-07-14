package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pl.alkosoft.odliczaczor.Odliczaczor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;
import static pl.alkosoft.odliczaczor.screen.Screens.PREPARE_SCREEN;

class MenuScreen implements Screen {

    private final Odliczaczor app;
    private Stage stage;
    private Skin skin;
    private Image menuBackground;
    private Image logo;

    private TextButton buttonStart, buttonExit, buttonMute;

    public MenuScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        this.stage = new Stage(new StretchViewport(WIDTH, HEIGHT, app.camera));
        app.menuThemeSong = app.assetManager.get("sfx/reunion.mp3");
        Texture menuBackgroundTexture = app.assetManager.get("gdx/app_background.jpg");
        Texture splashLogoTexture = app.assetManager.get("gdx/splash_logo.png");
        Gdx.input.setInputProcessor(stage);
        if(!app.menuThemeSong.isPlaying() && (buttonMute ==null)){
            app.menuThemeSong.setVolume(.3f);
            app.menuThemeSong.play();
            app.menuThemeSong.setLooping(true);
        }
        if(skin==null){
            prepareSkin();
        }
        if(buttonMute==null){
            initButtons();
        }

        menuBackground = new Image(menuBackgroundTexture);
        logo = new Image(splashLogoTexture);
        stage.addActor(menuBackground);
        stage.addActor(logo);

        menuBackground.setPosition(0, 0);
        logo.setWidth(1200);
        logo.setHeight(120);
        logo.setPosition(WIDTH / 2 - logo.getWidth() / 2, 800);

        buttonStart.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        stage.addActor(buttonStart);
        buttonMute.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        stage.addActor(buttonMute);
        buttonExit.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        stage.addActor(buttonExit);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();
    }

    private void update(float delta) {
        stage.act(delta);
    }

    private void prepareSkin() {
        this.skin = app.assetManager.get("ui/uiskin.json", Skin.class);
    }

    private void initButtons() {
        createStartButton();
        createMuteButton();
        createExitButton();
    }

    private void createStartButton() {
        buttonStart = new TextButton("Start", skin, "default");
        buttonStart.setColor(.44f, .41f, .41f, 1f);
        buttonStart.setSize(400, 100);
        buttonStart.setPosition(WIDTH / 2 - buttonStart.getWidth() / 2, HEIGHT / 2 + 100);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.screenManager.getScreen(PREPARE_SCREEN));
            }
        });
    }

    private void createMuteButton() {
        buttonMute = new TextButton("Music: ON", skin, "default");
        buttonMute.setColor(.44f, .41f, .41f, 1f);
        buttonMute.setSize(400, 100);
        buttonMute.setPosition(WIDTH / 2 - buttonMute.getWidth() / 2, HEIGHT / 2 - 50);
        buttonMute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (app.menuThemeSong.isPlaying()) {
                    app.menuThemeSong.pause();
                    buttonMute.setStyle(buttonMute.getSkin().get("toggle", TextButton.TextButtonStyle.class));
                    buttonMute.setText("Music: OFF");
                } else {
                    app.menuThemeSong.play();
                    buttonMute.setStyle(buttonMute.getSkin().get("default", TextButton.TextButtonStyle.class));
                    buttonMute.setText("Music: ON");
                }
            }
        });
    }

    private void createExitButton() {
        buttonExit = new TextButton("Exit", skin, "default");
        buttonExit.setColor(.44f, .41f, .41f, 1f);
        buttonExit.setSize(400, 100);
        buttonExit.setPosition(WIDTH / 2 - buttonExit.getWidth() / 2, HEIGHT / 2 - 200);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
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

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
