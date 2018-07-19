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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import pl.alkosoft.odliczaczor.Odliczaczor;
import pl.alkosoft.odliczaczor.data.ExplosionTimes;
import pl.alkosoft.odliczaczor.data.Months;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.utils.Align.center;
import static pl.alkosoft.odliczaczor.Odliczaczor.WIDTH;
import static pl.alkosoft.odliczaczor.data.Assets.APP_BACKGROUND;
import static pl.alkosoft.odliczaczor.data.Assets.FONT_GAME_OVER;
import static pl.alkosoft.odliczaczor.data.Months.JANUARY;
import static pl.alkosoft.odliczaczor.screen.Screens.APP_SCREEN;

class PrepareScreen implements Screen {

    private final Odliczaczor app;
    private Stage stage;

    private SelectBox<String> selectBoxYear, selectBoxMonth, selectBoxDay, selectBoxExplodeTime;
    private TextButton buttonStart, buttonExit;
    private BitmapFont fontGameOver136;
    private Texture menuBackgroundTexture;

    public PrepareScreen(final Odliczaczor app) {
        this.app = app;
    }

    @Override
    public void show() {
        this.fontGameOver136 = app.getAssetManager().get(FONT_GAME_OVER.getPath(), BitmapFont.class);
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        this.menuBackgroundTexture = new Texture(APP_BACKGROUND.getPath());

        initComponents();

        stage.addActor(selectBoxYear);
        stage.addActor(selectBoxMonth);
        stage.addActor(selectBoxDay);
        stage.addActor(buttonStart);
        stage.addActor(buttonExit);
        stage.addActor(selectBoxExplodeTime);
    }

    private void initComponents() {
        createYearSelectBox();
        createMonthSelectBox();
        createDaySelectBox();
        createExplosionTimesSelectBox();
        createStartButton();
        createExitButton();
    }

    private void createYearSelectBox() {
        Array<String> years = new Array<>();
        years.add("2018");
        selectBoxYear = createSelectBox(years, 200, 780);
        selectBoxYear.addAction(sequence(alpha(0f), fadeIn(1f)));
    }

    private void createMonthSelectBox() {
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
        selectBoxMonth.addAction(sequence(alpha(0f), fadeIn(1f)));
    }

    private void createDaySelectBox() {
        Array<String> days = addAllDaysFromMonth(JANUARY.getDaysCount());
        selectBoxDay = createSelectBox(days, 1400, 780);
        selectBoxDay.addAction(sequence(alpha(0f), fadeIn(1f)));
    }

    private void createExplosionTimesSelectBox() {
        Array<String> explosionTimes = new Array<>();
        for (ExplosionTimes explosionTime : ExplosionTimes.values()) {
            explosionTimes.add(explosionTime.getName());
        }
        selectBoxExplodeTime = createSelectBox(explosionTimes, 800, 400);
        selectBoxExplodeTime.addAction(sequence(alpha(0f), fadeIn(1.5f)));
    }

    private void createStartButton() {
        buttonStart = app.getScreenHelper().createDefaultButton("Start", app.getButtonDefaultColor(), app.getSkin(),300, 100, 1180, 80);
        buttonStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateProperties();
            }
        });
        buttonStart.addAction(sequence(alpha(0f), fadeIn(1.5f)));
    }

    private void createExitButton() {
        buttonExit = app.getScreenHelper().createDefaultButton("Back", app.getButtonDefaultColor(), app.getSkin(), 300, 100, 1550, 80);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                app.setScreen(app.getScreenManager().getScreen(Screens.MENU_SCREEN));
            }
        });
        buttonExit.addAction(sequence(alpha(0f), fadeIn(1.5f)));
    }

    private void updateProperties() {
        boolean isValidDate = app.getRemainingTimeService().validateDate(LocalDate.now(), selectBoxYear.getSelected(),
                selectBoxMonth.getSelected(), selectBoxDay.getSelected());

        if (!isValidDate) {
            displayErrorDialog();
        } else {
            app.getMusicManager().getMenuThemeSong().stop();
            app.getRemainingTimeService().updateRemainingTimesInProperties(LocalDateTime.now(), selectBoxYear.getSelected(),
                    selectBoxMonth.getSelected(), selectBoxDay.getSelected());
            app.getRemainingTimeService().setHowOftenBombBlow(selectBoxExplodeTime.getSelected());
            app.setScreen(app.getScreenManager().getScreen(APP_SCREEN));
        }
    }

    private void displayErrorDialog() {
        Dialog dialog = new Dialog("Upsss", app.getSkin(), "dialog");
        dialog.text("Date must be in the future");
        dialog.button("Ok", true);
        dialog.getButtonTable().align(center);
        dialog.getButtonTable().setPosition(100, 200);
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

    private Array<String> addAllDaysFromMonth(int daysCount) {
        Array<String> days = new Array<>();
        for (Integer i = 1; i <= daysCount; i++) {
            days.add(i.toString());
        }
        return days;
    }

    private SelectBox<String> createSelectBox(Array<String> items, int x, int y) {
        SelectBox<String> selectBox = new SelectBox<>(app.getSkin(), "default");
        selectBox.setItems(items);
        selectBox.setColor(.44f, .41f, .41f, .8f);
        selectBox.setAlignment(center);
        selectBox.setWidth(400);
        selectBox.setHeight(120);
        selectBox.setPosition(x, y);
        return selectBox;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.getBatch().begin();
        app.getBatch().draw(menuBackgroundTexture, 0, 0);
        drawLabel("Choose when You leave NGV:", fontGameOver136, 1000, WHITE);
        drawLabel("Choose how often bomb will explode:", fontGameOver136, 620, WHITE);
        app.getBatch().end();
        stage.act(delta);
        stage.draw();
    }

    private void drawLabel(String value, BitmapFont font, int y, Color color) {
        font.setColor(color);
        GlyphLayout glyphLayout = new GlyphLayout();
        glyphLayout.setText(font, value);
        font.draw(app.getBatch(), glyphLayout, WIDTH / 2 - glyphLayout.width / 2, y);
    }

    @Override
    public void dispose() {
        stage.dispose();
        app.getSkin().dispose();
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
