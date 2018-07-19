package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pl.alkosoft.odliczaczor.Odliczaczor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.data.Assets.APP_BACKGROUND;
import static pl.alkosoft.odliczaczor.data.Assets.SPLASH_SCREEN_LOGO;
import static pl.alkosoft.odliczaczor.screen.Screens.PREPARE_SCREEN;

class MenuScreen implements Screen {

    private final Odliczaczor app;
    private Stage stage;

    private TextButton buttonStart, buttonExit, buttonMute;

    public MenuScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        this.stage = new Stage();
        buttonStart = null;
        buttonMute = null;
        buttonExit = null;
        Gdx.input.setInputProcessor(stage);
        if (!app.getMusicManager().getMenuThemeSong().isPlaying() && !app.getMusicManager().isMuteOn()) {
            app.getMusicManager().getMenuThemeSong().setVolume(.3f);
            app.getMusicManager().getMenuThemeSong().play();
            app.getMusicManager().getMenuThemeSong().setLooping(true);
        }

        Image menuBackground = app.getScreenHelper().createImageActor(APP_BACKGROUND.getPath(), 0, 0);
        Image logo = app.getScreenHelper().createImageActor(SPLASH_SCREEN_LOGO.getPath(), -1, 800);
        logo.setWidth(1200);
        logo.setHeight(120);

        initButtons();

        stage.addActor(menuBackground);
        stage.addActor(logo);
        stage.addActor(buttonStart);
        stage.addActor(buttonMute);
        stage.addActor(buttonExit);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    private void initButtons() {
        createStartButton();
        createMuteButton();
        createExitButton();
    }

    private void createStartButton() {
        buttonStart = app.getScreenHelper().createDefaultButton("Start", app.getButtonDefaultColor(), app.getSkin(), 400, 100, -1, HEIGHT / 2 + 100);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.getScreenManager().getScreen(PREPARE_SCREEN));
            }
        });
        buttonStart.addAction(sequence(alpha(0f), fadeIn(1.5f)));
    }

    private void createMuteButton() {
        String name = "Music: ON";
        String style = "default";
        if (app.getMusicManager().isMuteOn()){
            name = "Music: OFF";
            style = "toggle";
        }
        buttonMute = app.getScreenHelper().createDefaultButton(name, app.getButtonDefaultColor(), app.getSkin(), 400, 100, -1, HEIGHT / 2 - 50);
        buttonMute.setStyle(buttonMute.getSkin().get(style, TextButton.TextButtonStyle.class));
        buttonMute.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleMuteToggle();
            }
        });
        buttonMute.addAction(sequence(alpha(0f), fadeIn(1.5f)));
    }

    private void createExitButton() {
        buttonExit = app.getScreenHelper().createDefaultButton("Exit", app.getButtonDefaultColor(), app.getSkin(), 400, 100, -1, HEIGHT / 2 - 200);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        buttonExit.addAction(sequence(alpha(0f), fadeIn(1.5f)));
    }

    private void handleMuteToggle() {
        if (!app.getMusicManager().isMuteOn()) {
            app.getMusicManager().getMenuThemeSong().pause();
            buttonMute.setStyle(buttonMute.getSkin().get("toggle", TextButton.TextButtonStyle.class));
            buttonMute.setText("Music: OFF");
            app.getMusicManager().setMuteOn(true);
        } else {
            app.getMusicManager().getMenuThemeSong().play();
            buttonMute.setStyle(buttonMute.getSkin().get("default", TextButton.TextButtonStyle.class));
            buttonMute.setText("Music: ON");
            app.getMusicManager().setMuteOn(false);
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
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
