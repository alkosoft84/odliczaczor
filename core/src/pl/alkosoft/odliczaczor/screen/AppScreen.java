package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import pl.alkosoft.odliczaczor.Odliczaczor;
import pl.alkosoft.odliczaczor.entity.BombsManager;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.badlogic.gdx.graphics.Color.RED;
import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;
import static pl.alkosoft.odliczaczor.data.Assets.*;
import static pl.alkosoft.odliczaczor.screen.Screens.FINISH_SCREEN;

class AppScreen implements Screen {

    private final Odliczaczor app;
    private Stage stage;
    private BombsManager bombsManager;

    private TextButton buttonFinish;
    private BitmapFont fontGameOver136;
    private BitmapFont fontArcon20;

    private float timer;
    private float bombTimer;
    private Long remaininingWorkingDays;
    private Long remaininingWorkingHours;
    private Long remaininingWorkingMinutes;
    private Long remaininingWorkingSeconds;
    private int howOftenBombBlow;
    private boolean isWorkingHours;
    private Texture appBackgroundTexture;


    public AppScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        fontGameOver136 = app.getAssetManager().get(FONT_GAME_OVER.getPath(), BitmapFont.class);
        fontArcon20 = app.getAssetManager().get(FONT_ARCON.getPath(), BitmapFont.class);
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        bombTimer = 0;
        timer = 0;

        if (!app.getMusicManager().getAppThemeSong().isPlaying() && !app.getMusicManager().isMuteOn()) {
            app.getMusicManager().getAppThemeSong().setVolume(.3f);
            app.getMusicManager().getAppThemeSong().play();
            app.getMusicManager().getAppThemeSong().setLooping(true);
        }
        loadTimeProperties();
        howOftenBombBlow = app.getRemainingTimeService().getHowOftenBombBlow();
        isWorkingHours = app.getRemainingTimeService().isWorkingHours();

        this.appBackgroundTexture = new Texture(APP_BACKGROUND.getPath());
        createFinishButton();

        bombsManager = new BombsManager(app, (int) (remaininingWorkingSeconds / howOftenBombBlow));

        stage.addActor(buttonFinish);

        drawLabel("Remainings: ", "remainingLbl", fontGameOver136, WIDTH / 2 - 100, 1000, RED, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("Working days: " + remaininingWorkingDays, "daysLbl", fontGameOver136, 100, 880, WHITE, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("Working hours: " + remaininingWorkingHours, "hoursLbl", fontGameOver136, 100, 800, WHITE, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("Working minutes: " + remaininingWorkingMinutes, "minutesLbl", fontGameOver136, 100, 720, WHITE, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("Working seconds: " + remaininingWorkingSeconds, "secondsLbl", fontGameOver136, 100, 640, WHITE, sequence(alpha(0f), fadeIn(1.5f)));
        drawLabel("...waiting for 8:00AM to countdown...", "workingHoursLbl", fontArcon20, WIDTH / 2 - 120, 960, Color.GREEN, forever(sequence(alpha(0f), fadeIn(1f), fadeOut(1f))));
    }

    private void loadTimeProperties() {
        remaininingWorkingDays = app.getRemainingTimeService().getRemainingWorkingDays();
        remaininingWorkingHours = app.getRemainingTimeService().getRemainingWorkingHours();
        remaininingWorkingMinutes = app.getRemainingTimeService().getRemainingWorkingMinutes();
        remaininingWorkingSeconds = app.getRemainingTimeService().getRemainingWorkingSeconds();
    }

    private void createFinishButton() {
        buttonFinish = app.getScreenHelper().createDefaultButton("Menu", app.getButtonDefaultColor(), app.getSkin(), 300, 100, 1550, 80);
        buttonFinish.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.getMusicManager().getAppThemeSong().stop();
                app.setScreen(app.getScreenManager().getScreen(Screens.MENU_SCREEN));
            }
        });
        buttonFinish.addAction(sequence(alpha(0f), fadeIn(1.5f)));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timer += delta;
        bombTimer += delta;
        app.getBatch().begin();
        app.getBatch().draw(appBackgroundTexture, 0, 0);
        app.getBatch().end();
        update(delta);
        stage.draw();

        handleWorkingHours();

        if (isWorkingHours) {
            assignCaptionToWorkingHourLabel("");
            handleRemainingWorkingHours();
            handleRemainingWorkingMinutes();
            handleRemainingWorkingSeconds();
            drawBombs();
        } else {
            assignCaptionToWorkingHourLabel("...waiting for 8:00AM to countdown...");
        }

        handleBombDrawing();
    }

    private void drawBombs() {
        app.getBatch().begin();
        bombsManager.render(app.getBatch(), bombTimer);
        app.getBatch().end();
    }

    private void handleBombDrawing() {
        if (!bombsManager.isAnyBombLeft()) {
            if ((remaininingWorkingSeconds / howOftenBombBlow) > 0) {
                bombsManager = new BombsManager(app, (int) (remaininingWorkingSeconds / howOftenBombBlow));
            } else {
                app.getMusicManager().getAppThemeSong().stop();
                stage.addAction(sequence(delay(1.5f), run(() -> app.setScreen(app.getScreenManager().getScreen(FINISH_SCREEN)))));
            }
        }
    }

    private void handleRemainingWorkingHours() {
        if (remaininingWorkingMinutes % 60 == 0 && timer > 1) {
            remaininingWorkingHours--;
            Label lbl = stage.getRoot().findActor("hoursLbl");
            lbl.setText("Working hours: " + remaininingWorkingHours);
        }
    }

    private void handleRemainingWorkingMinutes() {
        if (remaininingWorkingSeconds % 60 == 0 && timer > 1) {
            remaininingWorkingMinutes--;
            Label lbl = stage.getRoot().findActor("minutesLbl");
            lbl.setText("Working minutes: " + remaininingWorkingMinutes);
        }
    }

    private void handleRemainingWorkingSeconds() {
        if (timer > 1) {
            remaininingWorkingSeconds--;
            Label lbl = stage.getRoot().findActor("secondsLbl");
            lbl.setText("Working seconds: " + remaininingWorkingSeconds);
            timer = 0;
        }
    }

    private void assignCaptionToWorkingHourLabel(String caption) {
        Label waitingForWorkingHoursLbl = stage.getRoot().findActor("workingHoursLbl");
        waitingForWorkingHoursLbl.setText(caption);
    }

    private void handleWorkingHours() {
        LocalTime localTime = LocalTime.now();
        LocalDate localtDate = LocalDate.now();

        if (nowAreWorkingHours(localTime, localtDate)) {
            isWorkingHours = true;
        } else {
            isWorkingHours = false;
        }
        app.getRemainingTimeService().setIsWorkingHours(isWorkingHours);
    }

    private boolean nowAreWorkingHours(LocalTime localTime, LocalDate localtDate) {
        return localTime.isBefore(app.getRemainingTimeService().getEndOfWork()) &&
                localTime.isAfter(app.getRemainingTimeService().getStartOfWork()) &&
                !(localtDate.getDayOfWeek().equals(SATURDAY) || localtDate.getDayOfWeek().equals(SUNDAY));
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
