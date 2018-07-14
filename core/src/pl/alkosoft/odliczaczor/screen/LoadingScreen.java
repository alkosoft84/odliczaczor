package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SkinLoader.SkinParameter;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pl.alkosoft.odliczaczor.Odliczaczor;

import static com.badlogic.gdx.graphics.Color.*;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;
import static pl.alkosoft.odliczaczor.screen.Screens.SPLASH_SCREEN;

class LoadingScreen implements Screen {

    private final Odliczaczor app;
    private ShapeRenderer shapeRenderer;
    private float progress;
    private Stage stage;
    private Image loadingImg;
    private Image loadingLogo;
    private Image loadingLogo2;

    public LoadingScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        this.stage = new Stage(new StretchViewport(WIDTH, HEIGHT, app.camera));
        this.shapeRenderer = new ShapeRenderer();
        this.progress = 0f;
        loadLoadingImages();
        app.assetManager.finishLoading();
        Texture loadingTex = app.assetManager.get("gdx/loadingScreen.png");
        Texture loadingLogoTex = app.assetManager.get("gdx/loading_logo.png");
        Texture loadingLogo2Tex = app.assetManager.get("gdx/loading_logo2.png");
        loadingImg = new Image(loadingTex);
        loadingLogo = new Image(loadingLogoTex);
        loadingLogo2 = new Image(loadingLogo2Tex);
        stage.addActor(loadingImg);
        stage.addActor(loadingLogo);
        stage.addActor(loadingLogo2);
        queueAssets();
        loadingImg.setPosition(0, 0);
        loadingLogo.setPosition(WIDTH / 2 - loadingLogo.getWidth() / 2, HEIGHT / 2 + 50);
        loadingLogo2.setPosition(WIDTH / 2 - loadingLogo2.getWidth() / 2, HEIGHT / 2 - 80);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();

        shapeRenderer.begin(Filled);
        shapeRenderer.setColor(BLACK);
        shapeRenderer.rect(400, app.camera.viewportHeight / 2 - 8, app.camera.viewportWidth - 800, 24);

        shapeRenderer.setColor(GREEN);
        shapeRenderer.rect(400, app.camera.viewportHeight / 2 - 6, progress * (app.camera.viewportWidth - 800), 20);
        shapeRenderer.end();
    }

    private void update(float delta) {
        stage.act(delta);
        progress = MathUtils.lerp(progress, app.assetManager.getProgress(), .3f);
        if (app.assetManager.update() && progress >= app.assetManager.getProgress() - .001f) {
            app.setScreen(app.screenManager.getScreen(SPLASH_SCREEN));
        }

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
        shapeRenderer.dispose();
        stage.dispose();
    }

    private void queueAssets() {
        loadFonts();
        loadSkins();
        loadGraphics();
        loadMusic();
    }

    private void loadFonts() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        app.assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        app.assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreeTypeFontLoaderParameter myNormalFont = new FreeTypeFontLoaderParameter();
        myNormalFont.fontFileName = "font/Arcon.ttf";
        myNormalFont.fontParameters.size = 20;
        myNormalFont.fontParameters.color = WHITE;
        FreeTypeFontLoaderParameter myGameFont = new FreeTypeFontLoaderParameter();
        myGameFont.fontFileName = "font/game_over.ttf";
        myGameFont.fontParameters.size = 136;
        myGameFont.fontParameters.color = WHITE;

        app.assetManager.load(myNormalFont.fontFileName, BitmapFont.class, myNormalFont);
        app.assetManager.load(myGameFont.fontFileName, BitmapFont.class, myGameFont);
    }

    private void loadSkins() {
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/game_over.ttf"));
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 136;
        fontParameter.color = WHITE;
        BitmapFont bitmapFont = freeTypeFontGenerator.generateFont(fontParameter);
        ObjectMap<String, Object> fontMap = new ObjectMap<>();
        fontMap.put("default-font", bitmapFont);
        app.assetManager.load("ui/uiskin.json", Skin.class, new SkinParameter("ui/uiskin.atlas", fontMap));
    }

    private void loadGraphics() {
        app.assetManager.load("gdx/splashScreen.png", Texture.class);
        app.assetManager.load("gdx/splash_logo.png", Texture.class);
        app.assetManager.load("gdx/escape_odlicz.png", Texture.class);
        app.assetManager.load("gdx/app_background.jpg", Texture.class);
        app.assetManager.load("gdx/finish.jpg", Texture.class);
        app.assetManager.load("gdx/bombs.pack", TextureAtlas.class);
        app.assetManager.load("gdx/explodingBombs.pack", TextureAtlas.class);
        app.assetManager.load("gdx/explosion.pack", TextureAtlas.class);
    }

    private void loadLoadingImages() {
        app.assetManager.load("gdx/loadingScreen.png", Texture.class);
        app.assetManager.load("gdx/loading_logo.png", Texture.class);
        app.assetManager.load("gdx/loading_logo2.png", Texture.class);
    }

    private void loadMusic() {
        app.assetManager.load("sfx/reunion.mp3", Music.class);
        app.assetManager.load("sfx/freedom.mp3", Music.class);
        app.assetManager.load("sfx/chicken_explode.mp3", Music.class);
        app.assetManager.load("sfx/albatros.mp3", Music.class);
    }
}
