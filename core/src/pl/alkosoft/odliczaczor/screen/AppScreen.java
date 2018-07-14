package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pl.alkosoft.odliczaczor.Odliczaczor;
import pl.alkosoft.odliczaczor.entity.BombsManager;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;
import static pl.alkosoft.odliczaczor.screen.Screens.FINISH_SCREEN;

class AppScreen implements Screen {

    private final Odliczaczor app;
    private final static LocalTime END_OF_WORK = LocalTime.of(16, 0, 0);
    private final static LocalTime START_OF_WORK = LocalTime.of(8, 0, 0);
    private Stage stage;
    private Skin skin;
    private Texture appScreenBackgroundTexture;
    private BombsManager bombsManager;

    private TextButton buttonFinish;
    private BitmapFont fontGameOver136;
    private BitmapFont fontArcon20;

    private float timeSeconds = 0f;
    private float bombTimer = 0f;
    private Long remaininingWorkingDays;
    private Long remaininingWorkingHours;
    private Long remaininingWorkingMinutes;
    private Long remaininingWorkingSeconds;
    private int howOftenBombBlow;
    private Music backgroundMusic;

    public AppScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        this.stage = new Stage(new StretchViewport(WIDTH, HEIGHT, app.camera));
        if (skin == null) {
            prepareSkin();
        }
        if (buttonFinish == null) {
            initComponents();
        }
        backgroundMusic = app.assetManager.get("sfx/albatros.mp3");

        if(!backgroundMusic.isPlaying()){
            backgroundMusic.setVolume(.3f);
            backgroundMusic.play();
            backgroundMusic.setLooping(true);
        }

        Gdx.input.setInputProcessor(stage);
        appScreenBackgroundTexture = app.assetManager.get("gdx/app_background.jpg");
        fontGameOver136 = app.assetManager.get("font/game_over.ttf", BitmapFont.class);
        fontArcon20 = app.assetManager.get("font/Arcon.ttf", BitmapFont.class);
        buttonFinish.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        stage.addActor(buttonFinish);
        remaininingWorkingDays = app.getRemainingTimeService().getRemainingWorkingDays();
        remaininingWorkingHours = app.getRemainingTimeService().getRemainingWorkingHours();
        remaininingWorkingMinutes = app.getRemainingTimeService().getRemainingWorkingMinutes();
        remaininingWorkingSeconds = app.getRemainingTimeService().getRemainingWorkingSeconds();
        howOftenBombBlow = app.getRemainingTimeService().getHowOftenBombBlow();

        bombsManager = new BombsManager(app, (int) (remaininingWorkingSeconds / howOftenBombBlow));
        //bombsManager = new BombsManager(app, 5);
        bombTimer=0;

        drawLabel("Remainings: ", "remainingLbl", fontGameOver136, WIDTH / 2 - 100, 1000, RED, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("Working days: " + remaininingWorkingDays, "daysLbl", fontGameOver136, 100, 880, WHITE, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("Working hours: " + remaininingWorkingHours, "hoursLbl", fontGameOver136, 100, 800, WHITE, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("Working minutes: " + remaininingWorkingMinutes, "minutesLbl", fontGameOver136, 100, 720, WHITE, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("Working seconds: " + remaininingWorkingSeconds, "secondsLbl", fontGameOver136, 100, 640, WHITE, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("...waiting for 8:00AM to countdown...", "workingHoursLbl", fontArcon20, WIDTH / 2 - 120, 960, Color.GREEN, forever(sequence(alpha(0f), fadeIn(1f), fadeOut(1f))));
    }

    private void initComponents() {
        createFinishButton();
    }

    private void createFinishButton() {
        buttonFinish = new TextButton("Menu", skin, "default");
        buttonFinish.setColor(.44f, .41f, .41f, 1f);
        buttonFinish.setSize(300, 100);
        buttonFinish.setPosition(1550, 80);
        buttonFinish.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backgroundMusic.stop();
                app.setScreen(app.screenManager.getScreen(Screens.MENU_SCREEN));
            }
        });
    }

    private void prepareSkin() {
        this.skin = app.assetManager.get("ui/uiskin.json", Skin.class);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeSeconds += delta;
        bombTimer+= delta;
        app.batch.begin();
        app.batch.draw(appScreenBackgroundTexture, 0, 0);
        bombsManager.render(app.batch, bombTimer);
        app.batch.end();
        update(delta);
        stage.draw();


        LocalTime localTime = LocalTime.now();
        LocalDate localtDate = LocalDate.now();

        if ((localTime.isBefore(END_OF_WORK) && localTime.isAfter(START_OF_WORK) && !(localtDate.getDayOfWeek().equals(SATURDAY) || localtDate.getDayOfWeek().equals(SUNDAY)))) {
            app.getRemainingTimeService().setIsWorkingHours(true);
        } else {
            app.getRemainingTimeService().setIsWorkingHours(false);
        }

        if (app.getRemainingTimeService().isWorkingHours()) {
            Label waiting = stage.getRoot().findActor("workingHoursLbl");
            waiting.setText("");
            if (remaininingWorkingMinutes % 60 == 0 && timeSeconds > 1) {
                remaininingWorkingHours--;
                Label lbl = stage.getRoot().findActor("hoursLbl");
                lbl.setText("Working hours: " + remaininingWorkingHours);
            }
            if (remaininingWorkingSeconds % 60 == 0 && timeSeconds > 1) {
                remaininingWorkingMinutes--;
                Label lbl = stage.getRoot().findActor("minutesLbl");
                lbl.setText("Working minutes: " + remaininingWorkingMinutes);
            }
            if (timeSeconds > 1) {
                remaininingWorkingSeconds--;
                Label lbl = stage.getRoot().findActor("secondsLbl");
                lbl.setText("Working seconds: " + remaininingWorkingSeconds);
                timeSeconds = 0;
            }
        } else {
            Label waiting = stage.getRoot().findActor("workingHoursLbl");
            waiting.setText("...waiting for 8:00AM to countdown...");
        }

        if(!bombsManager.isAnyBombLeft()){
            if((remaininingWorkingSeconds / howOftenBombBlow) != 0){
                bombsManager = new BombsManager(app, (int) (remaininingWorkingSeconds / howOftenBombBlow));
            }else {
                stage.addAction(sequence(delay(1.5f), run(() -> app.setScreen(app.screenManager.getScreen(FINISH_SCREEN)))));
            }
            backgroundMusic.stop();
            //stage.addAction(sequence(delay(1.5f), run(() -> app.setScreen(app.screenManager.getScreen(FINISH_SCREEN)))));
        }
    }

    private void drawLabel(String value, String name, BitmapFont font, int x, int y, Color color, Action action) {
        LabelStyle labelStyle = new LabelStyle(font, color);
        Label label = new Label(value, labelStyle);
        label.setName(name);
        label.setPosition(x, y);
        label.addAction(action);
        stage.addActor(label);
    }

    private void update(float delta) {
        stage.act(delta);
        bombsManager.update();
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
    }

}
