package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import pl.alkosoft.odliczaczor.Odliczaczor;
import pl.alkosoft.odliczaczor.data.ExplosionTimes;
import pl.alkosoft.odliczaczor.data.Months;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.utils.Align.center;
import static pl.alkosoft.odliczaczor.Odliczaczor.HEIGHT;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;
import static pl.alkosoft.odliczaczor.data.Months.JANUARY;
import static pl.alkosoft.odliczaczor.screen.Screens.APP_SCREEN;

class PrepareScreen implements Screen {

    private final Odliczaczor app;
    private Stage stage;
    private Skin skin;
    private Texture prepareScreenBackgroundTexture;

    private SelectBox<String> selectBoxYear, selectBoxMonth, selectBoxDay, selectBoxExplodeTime;
    private TextButton buttonStart, buttonExit;
    private BitmapFont fontGameOver136;

    public PrepareScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        fontGameOver136 = app.assetManager.get("font/game_over.ttf", BitmapFont.class);
        this.stage = new Stage(new StretchViewport(WIDTH, HEIGHT, app.camera));
        if (skin == null) {
            prepareSkin();
        }
        if (buttonExit == null) {
            initComponents();
        }

        prepareScreenBackgroundTexture = app.assetManager.get("gdx/app_background.jpg");
        Gdx.input.setInputProcessor(stage);
        selectBoxYear.addAction(sequence(alpha(0f), fadeIn(1f)));
        stage.addActor(selectBoxYear);
        selectBoxMonth.addAction(sequence(alpha(0f), fadeIn(1f)));
        stage.addActor(selectBoxMonth);
        selectBoxDay.addAction(sequence(alpha(0f), fadeIn(1f)));
        stage.addActor(selectBoxDay);
        buttonStart.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        stage.addActor(buttonStart);
        buttonExit.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        stage.addActor(buttonExit);
        selectBoxExplodeTime.addAction(sequence(alpha(0f), fadeIn(1.5f)));
        stage.addActor(selectBoxExplodeTime);
    }

    private void initComponents() {
        Array<String> years = new Array<>();
        years.add("2018");
        selectBoxYear = createSelectBox(years, 200, 780);

        Array<String> monthsList = new Array<>();
        for (Months month : Months.values()) {
            monthsList.add(month.getValue());
        }
        selectBoxMonth = createSelectBox(monthsList, 800, 780);
        selectBoxMonth.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Array<String> days = addAllDaysFromMonth(Months.valueOf(selectBoxMonth.getSelected().toUpperCase()).getDaysCount());
                selectBoxDay.setItems(days);
            }
        });
        Array<String> days = addAllDaysFromMonth(JANUARY.getDaysCount());
        selectBoxDay = createSelectBox(days, 1400, 780);

        Array<String> explosionTimes = new Array<>();
        for (ExplosionTimes explosionTime : ExplosionTimes.values()) {
            explosionTimes.add(explosionTime.getName());
        }
        selectBoxExplodeTime = createSelectBox(explosionTimes, 800, 400);

        createStartButton();
        createExitButton();
    }

    private void createStartButton() {
        buttonStart = new TextButton("Start", skin, "default");
        buttonStart.setColor(.44f, .41f, .41f, 1f);
        buttonStart.setSize(300, 100);
        buttonStart.setPosition(1180, 80);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isValidDate = app.getRemainingTimeService().validateDate(LocalDate.now(), selectBoxYear.getSelected(),
                        selectBoxMonth.getSelected(), selectBoxDay.getSelected());
                if (!isValidDate) {
                    displayErrorDialog();
                } else {
                    app.menuThemeSong.stop();
                    app.getRemainingTimeService().updateRemainingTimesInProperties(LocalDateTime.now(), selectBoxYear.getSelected(),
                            selectBoxMonth.getSelected(), selectBoxDay.getSelected());
                    app.getRemainingTimeService().setHowOftenBombBlow(selectBoxExplodeTime.getSelected());
                    app.setScreen(app.screenManager.getScreen(APP_SCREEN));
                }
            }
        });
    }

    private void displayErrorDialog() {
        Dialog dialog = new Dialog("Upsss", skin, "dialog");
        dialog.text("Date must be in the future");
        dialog.button("Ok", true);
        dialog.getButtonTable().align(center);
        dialog.getButtonTable().setPosition(100,200);
        dialog.getBackground().setLeftWidth(50);
        dialog.getBackground().setTopHeight(90);
        dialog.getBackground().setBottomHeight(20);
        dialog.getBackground().setRightWidth(50);
        dialog.getBackground().setMinWidth(500);
        dialog.getBackground().setMinHeight(250);
        dialog.getTitleLabel().setFontScale(.8f);
        dialog.getTitleLabel().setAlignment(center);
        dialog.show(stage);
    }

    private void createExitButton() {
        buttonExit = new TextButton("Back", skin, "default");
        buttonExit.setColor(.44f, .41f, .41f, 1f);
        buttonExit.setSize(300, 100);
        buttonExit.setPosition(1550, 80);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.screenManager.getScreen(Screens.MENU_SCREEN));
            }
        });
    }

    private Array<String> addAllDaysFromMonth(int daysCount) {
        Array<String> days = new Array<>();
        for (Integer i = 1; i <= daysCount; i++) {
            days.add(i.toString());
        }
        return days;
    }

    private SelectBox<String> createSelectBox(Array<String> items, int x, int y) {
        SelectBox<String> selectBox = new SelectBox<>(skin, "default");
        selectBox.setItems(items);
        selectBox.setColor(.44f, .41f, .41f, .8f);
        selectBox.setAlignment(center);
        selectBox.setWidth(400);
        selectBox.setHeight(120);
        selectBox.setPosition(x, y);
        return selectBox;
    }

    private void prepareSkin() {
        this.skin = app.assetManager.get("ui/uiskin.json", Skin.class);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.batch.begin();
        app.batch.draw(prepareScreenBackgroundTexture, 0, 0);
        drawLabel("Choose when You leave NGV:", fontGameOver136, 1000, WHITE);
        drawLabel("Choose how often bomb will explode:", fontGameOver136, 620, WHITE);
        app.batch.end();
        stage.act(delta);
        stage.draw();


    }

    private void drawLabel(String value, BitmapFont font, int y, Color color) {
        font.setColor(color);
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, value);
        font.draw(app.batch, glyphLayout, WIDTH / 2 - glyphLayout.width / 2, y);

    }

    private void update(float delta) {

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
