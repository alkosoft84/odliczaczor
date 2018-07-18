package pl.alkosoft.odliczaczor.data;

public enum Assets {

    LOADING_SCREEN_BACKGROUND("gdx/loadingscreen/loadingScreen.png"),
    LOADING_SCREEN_LOADING_CAPTION("gdx/loadingscreen/loading.png"),
    LOADING_SCREEN_ODLICZACZOR_CAPTION("gdx/loadingscreen/odliczaczor.png"),
    FONT_ARCON("font/Arcon.ttf"),
    FONT_GAME_OVER("font/game_over.ttf"),
    UI_SKIN_ATLAS("ui/uiskin.atlas"),
    UI_SKIN_JSON("ui/uiskin.json"),
    SPLASH_SCREEN_BACKGROUND("gdx/splashscreen/splashScreen.png"),
    SPLASH_SCREEN_LOGO("gdx/splashscreen/splash_logo.png"),
    SPLASH_SCREEN_ESCAPE_CAPTION("gdx/splashscreen/escape.png"),
    APP_BACKGROUND("gdx/app_background.jpg"),
    BOMBS_PACK("gdx/characters/bombs.pack"),
    BOMB_EXPLODING_PACK("gdx/characters/explodingBombs.pack"),
    EXPLOSION_PACK("gdx/characters/explosion.pack"),
    MENU_THEME_SONG("sfx/reunion.mp3"),
    APP_THEME_SONG("sfx/albatros.mp3"),
    CHICKEN_EXPLODE_SFX("sfx/chicken_explode.mp3"),
    FINISH_SCREEN_THEME_SONG("sfx/freedom.mp3"),
    FINISH_SCREEN_ANIMATION_GIF("gdx/finishscreen/finish.gif"),
    FINISH_BACKGROUND("gdx/finishscreen/finish.jpg");

    private final String path;

    Assets(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
