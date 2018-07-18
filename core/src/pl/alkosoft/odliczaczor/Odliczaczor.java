package pl.alkosoft.odliczaczor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import pl.alkosoft.odliczaczor.data.Properties;
import pl.alkosoft.odliczaczor.data.RGBColor;
import pl.alkosoft.odliczaczor.screen.ScreenManager;
import pl.alkosoft.odliczaczor.service.MusicManager;
import pl.alkosoft.odliczaczor.service.RemainingTimeService;
import pl.alkosoft.odliczaczor.service.ScreenUtils;

import static pl.alkosoft.odliczaczor.data.Assets.UI_SKIN_JSON;
import static pl.alkosoft.odliczaczor.screen.Screens.LOADING_SCREEN;

public class Odliczaczor extends Game {

    public static final int HEIGHT = 1080;
    public static final int WIDTH = 1920;
    public static final float VERSION = 0.1f;
    public static final String TITLE = "Odliczaczor";

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private AssetManager assetManager;
    private ScreenManager screenManager;
    private MusicManager musicManager;
    private ScreenUtils screenHelper;
    private RemainingTimeService remainingTimeService;
    private RGBColor buttonDefaultColor;
    private Skin skin;

    @Override
    public void create() {
        assetManager = new AssetManager();
        screenManager = new ScreenManager(this);
        musicManager = new MusicManager();
        remainingTimeService = new RemainingTimeService(new Properties());
        screenHelper = new ScreenUtils(this);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        batch = new SpriteBatch();
        buttonDefaultColor = new RGBColor(.44f, .41f, .41f, 1f);
        skin = new Skin();
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


    //GETTERS


    public MusicManager getMusicManager() {
        return musicManager;
    }

    public RemainingTimeService getRemainingTimeService() {
        return remainingTimeService;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public ScreenManager getScreenManager() {
        return screenManager;
    }

    public ScreenUtils getScreenHelper() {
        return screenHelper;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public RGBColor getButtonDefaultColor() {
        return buttonDefaultColor;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }
}
