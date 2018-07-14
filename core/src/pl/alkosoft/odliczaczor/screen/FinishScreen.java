package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pl.alkosoft.odliczaczor.Odliczaczor;
import pl.alkosoft.odliczaczor.external_tools.GifDecoder;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;

class FinishScreen implements Screen {

    private final Odliczaczor app;
    private Stage stage;

    private BitmapFont fontArcon20;
    private Image splashBackground;
    private TextButton oneMoreTime;
    private Skin skin;
    private Animation<TextureRegion> finishAnimation;
    float elapsed;
    private Music finishSong;

    public FinishScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        if (skin == null) {
            prepareSkin();
        }
        finishAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("gdx/finish.gif").read());
        this.stage = new Stage(new StretchViewport(WIDTH, HEIGHT, app.camera));
        Gdx.input.setInputProcessor(stage);
        fontArcon20 = app.assetManager.get("font/Arcon.ttf", BitmapFont.class);
        Texture splashBackgroundTex = app.assetManager.get("gdx/finish.jpg");
        splashBackground = new Image(splashBackgroundTex);
        stage.addActor(splashBackground);
        createOneMoreTimeButton();
        oneMoreTime.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        stage.addActor(oneMoreTime);
        finishSong = app.assetManager.get("sfx/freedom.mp3");
        if(!finishSong.isPlaying()){
            finishSong.play();
            finishSong.setLooping(true);
        }
    }

    private void prepareSkin() {
        this.skin = app.assetManager.get("ui/uiskin.json", Skin.class);
    }

    private void createOneMoreTimeButton() {
        oneMoreTime = new TextButton("Try Again, or DON'T YOU'RE OUT NOW !!!", skin, "default");
        oneMoreTime.setColor(.44f, .41f, .41f, 1f);
        oneMoreTime.setSize(1200, 100);
        oneMoreTime.setPosition(WIDTH/2 - oneMoreTime.getWidth()/2, HEIGHT/2 -450);
        oneMoreTime.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                finishSong.stop();
                app.setScreen(app.screenManager.getScreen(Screens.MENU_SCREEN));
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsed +=delta;
        update(delta);
        stage.draw();
        app.batch.begin();
        app.batch.draw(finishAnimation.getKeyFrame(elapsed), WIDTH/2 - 450, HEIGHT/2 - 250);
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
