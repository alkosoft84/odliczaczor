package pl.alkosoft.odliczaczor.screen;

import com.badlogic.gdx.Screen;
import pl.alkosoft.odliczaczor.Odliczaczor;

public class ScreenManager {

    private final LoadingScreen loadingScreen;
    private final MenuScreen menuScreen;
    private final SplashScreen splashScreen;
    private final PrepareScreen prepareScreen;
    private final AppScreen appScreen;
    private final FinishScreen finishScreen;


    public ScreenManager(Odliczaczor app) {
        loadingScreen = new LoadingScreen(app);
        splashScreen = new SplashScreen(app);
        menuScreen = new MenuScreen(app);
        prepareScreen = new PrepareScreen(app);
        appScreen = new AppScreen(app);
        finishScreen = new FinishScreen(app);
    }

    public Screen getScreen(Screens screen) {
        switch (screen) {
            case LOADING_SCREEN:
                return loadingScreen;
            case SPLASH_SCREEN:
                return splashScreen;
            case MENU_SCREEN:
                return menuScreen;
            case PREPARE_SCREEN:
                return prepareScreen;
            case APP_SCREEN:
                return appScreen;
            case FINISH_SCREEN:
                return finishScreen;
            default:
                return loadingScreen;

        }
    }

}
