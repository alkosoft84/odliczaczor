package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pl.alkosoft.odliczaczor.Odliczaczor;
import pl.alkosoft.odliczaczor.external_tools.GifDecoder;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;
import static pl.alkosoft.odliczaczor.data.Assets.FINISH_BACKGROUND;
import static pl.alkosoft.odliczaczor.data.Assets.FINISH_SCREEN_ANIMATION_GIF;

class FinishScreen implements Screen {

    private final Odliczaczor app;
    private Stage stage;

    private TextButton oneMoreTime;
    private Animation<TextureRegion> finishAnimation;
    private float elapsed;

    public FinishScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        finishAnimation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(FINISH_SCREEN_ANIMATION_GIF.getPath()).read());
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        if(!app.getMusicManager().getFinishThemeSong().isPlaying()){
            app.getMusicManager().getFinishThemeSong().play();
            app.getMusicManager().getFinishThemeSong().setLooping(true);
        }

        Image finishBackground = app.getScreenHelper().createImageActor(FINISH_BACKGROUND.getPath(),0,0);
        createOneMoreTimeButton();

        stage.addActor(finishBackground);
        stage.addActor(oneMoreTime);
    }

    private void createOneMoreTimeButton() {
        oneMoreTime = app.getScreenHelper().createDefaultButton("Try Again, or DON'T YOU'RE OUT NOW !!!",
                app.getButtonDefaultColor(), app.getSkin(), 1200, 100, -1, HEIGHT/2 -450);
        oneMoreTime.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.getMusicManager().getFinishThemeSong().stop();
                app.setScreen(app.getScreenManager().getScreen(Screens.MENU_SCREEN));
            }
        });
        oneMoreTime.addAction(sequence(alpha(0f), fadeIn(1.5f)));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsed +=delta;
        stage.act(delta);
        stage.draw();
        app.getBatch().begin();
        app.getBatch().draw(finishAnimation.getKeyFrame(elapsed), WIDTH/2 - 450, HEIGHT/2 - 250);
        app.getBatch().end();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resize(int width, int height){
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
