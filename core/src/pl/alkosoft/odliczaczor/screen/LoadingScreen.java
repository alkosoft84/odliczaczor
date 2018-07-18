package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;
import pl.alkosoft.odliczaczor.Odliczaczor;

import static com.badlogic.gdx.graphics.Color.*;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.data.Assets.*;
import static pl.alkosoft.odliczaczor.screen.Screens.SPLASH_SCREEN;

class LoadingScreen implements Screen {

    private final Odliczaczor app;
    private ShapeRenderer shapeRenderer;
    private float progress;
    private Stage stage;

    private static final String FONT_SUFFIX = ".ttf";
    private static final String FONT_TEMPLATE = "default-font";

    public LoadingScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        this.stage = new Stage();
        this.shapeRenderer = new ShapeRenderer();
        this.progress = 0f;
        loadLoadingImages();
        app.getAssetManager().finishLoading();
        Image backgroundImg = app.getScreenHelper().createImageActor(LOADING_SCREEN_BACKGROUND.getPath(), 0, 0);
        Image odliczaczorImg = app.getScreenHelper().createImageActor(LOADING_SCREEN_ODLICZACZOR_CAPTION.getPath(), -1, HEIGHT / 2 + 50);
        Image loadingImg = app.getScreenHelper().createImageActor(LOADING_SCREEN_LOADING_CAPTION.getPath(), -1, HEIGHT / 2 - 80);
        stage.addActor(backgroundImg);
        stage.addActor(odliczaczorImg);
        stage.addActor(loadingImg);
        queueAssets();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();

        shapeRenderer.begin(Filled);
        shapeRenderer.setColor(BLACK);
        shapeRenderer.rect(400, app.getCamera().viewportHeight / 2 - 8, app.getCamera().viewportWidth - 800, 24);

        shapeRenderer.setColor(GREEN);
        shapeRenderer.rect(400, app.getCamera().viewportHeight / 2 - 6, progress * (app.getCamera().viewportWidth - 800), 20);
        shapeRenderer.end();
    }

    private void update(float delta) {
        stage.act(delta);
        progress = MathUtils.lerp(progress, app.getAssetManager().getProgress(), .3f);
        if (app.getAssetManager().update() && progress >= app.getAssetManager().getProgress() - .001f) {
            app.getMusicManager().init(app);
            app.setSkin(app.getAssetManager().get(UI_SKIN_JSON.getPath()));
            app.setScreen(app.getScreenManager().getScreen(SPLASH_SCREEN));
        }

    }

    private void queueAssets() {
        loadFonts();
        loadSkins();
        loadGraphics();
        loadMusic();
    }

    private void loadFonts() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        app.getAssetManager().setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        app.getAssetManager().setLoader(BitmapFont.class, FONT_SUFFIX, new FreetypeFontLoader(resolver));
        FreeTypeFontLoaderParameter myNormalFont = createFreeTypeFontLoaderParameter(FONT_ARCON.getPath(), 20, WHITE);
        FreeTypeFontLoaderParameter myGameFont = createFreeTypeFontLoaderParameter(FONT_GAME_OVER.getPath(), 136, WHITE);
        app.getAssetManager().load(myNormalFont.fontFileName, BitmapFont.class, myNormalFont);
        app.getAssetManager().load(myGameFont.fontFileName, BitmapFont.class, myGameFont);
    }

    private FreeTypeFontLoaderParameter createFreeTypeFontLoaderParameter(String fontPath, int size, Color color) {
        FreeTypeFontLoaderParameter font = new FreeTypeFontLoaderParameter();
        font.fontFileName = fontPath;
        font.fontParameters.size = size;
        font.fontParameters.color = color;
        return font;
    }

    private void loadSkins() {
        ObjectMap<String, Object> fontMap = createUiSkin(136, WHITE);
        app.getAssetManager().load(UI_SKIN_JSON.getPath(), Skin.class, new SkinParameter(UI_SKIN_ATLAS.getPath(), fontMap));
    }

    private ObjectMap<String, Object> createUiSkin(int size, Color color) {
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(FONT_GAME_OVER.getPath()));
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = size;
        fontParameter.color = color;
        BitmapFont bitmapFont = freeTypeFontGenerator.generateFont(fontParameter);
        ObjectMap<String, Object> fontMap = new ObjectMap<>();
        fontMap.put(FONT_TEMPLATE, bitmapFont);
        return fontMap;
    }

    private void loadGraphics() {
        app.getAssetManager().load(SPLASH_SCREEN_BACKGROUND.getPath(), Texture.class);
        app.getAssetManager().load(SPLASH_SCREEN_LOGO.getPath(), Texture.class);
        app.getAssetManager().load(SPLASH_SCREEN_ESCAPE_CAPTION.getPath(), Texture.class);
        app.getAssetManager().load(APP_BACKGROUND.getPath(), Texture.class);
        app.getAssetManager().load(FINISH_BACKGROUND.getPath(), Texture.class);
        app.getAssetManager().load(BOMBS_PACK.getPath(), TextureAtlas.class);
        app.getAssetManager().load(BOMB_EXPLODING_PACK.getPath(), TextureAtlas.class);
        app.getAssetManager().load(EXPLOSION_PACK.getPath(), TextureAtlas.class);
    }

    private void loadLoadingImages() {
        app.getAssetManager().load(LOADING_SCREEN_BACKGROUND.getPath(), Texture.class);
        app.getAssetManager().load(LOADING_SCREEN_ODLICZACZOR_CAPTION.getPath(), Texture.class);
        app.getAssetManager().load(LOADING_SCREEN_LOADING_CAPTION.getPath(), Texture.class);
    }

    private void loadMusic() {
        app.getAssetManager().load(MENU_THEME_SONG.getPath(), Music.class);
        app.getAssetManager().load(APP_THEME_SONG.getPath(), Music.class);
        app.getAssetManager().load(CHICKEN_EXPLODE_SFX.getPath(), Music.class);
        app.getAssetManager().load(FINISH_SCREEN_THEME_SONG.getPath(), Music.class);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
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
