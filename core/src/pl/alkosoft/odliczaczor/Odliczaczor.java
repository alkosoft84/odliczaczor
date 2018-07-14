package pl.alkosoft.odliczaczor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pl.alkosoft.odliczaczor.data.Properties;
import pl.alkosoft.odliczaczor.data.Textures;
import pl.alkosoft.odliczaczor.screen.ScreenManager;
import pl.alkosoft.odliczaczor.service.RemainingTimeService;

import static pl.alkosoft.odliczaczor.screen.Screens.LOADING_SCREEN;
import static pl.alkosoft.odliczaczor.screen.Screens.MENU_SCREEN;

public class Odliczaczor extends Game {

    public static final int HEIGHT = 1080;
    public static final int WIDTH = 1920;
    public static final float VERSION = 0.1f;
    public static final String TITLE = "Odliczaczor HD";

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public AssetManager assetManager;
    public ScreenManager screenManager;
    private RemainingTimeService remainingTimeService;
    public Music menuThemeSong;

    @Override
    public void create() {
        assetManager = new AssetManager();
        screenManager = new ScreenManager(this);
        remainingTimeService = new RemainingTimeService(new Properties());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        batch = new SpriteBatch();

        this.setScreen(screenManager.getScreen(LOADING_SCREEN));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
    }

    public RemainingTimeService getRemainingTimeService() {
        return remainingTimeService;
    }
}
