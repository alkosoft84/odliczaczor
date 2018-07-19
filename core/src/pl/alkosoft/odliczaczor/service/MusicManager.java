package pl.alkosoft.odliczaczor.service;

import com.badlogic.gdx.audio.Music;
import pl.alkosoft.odliczaczor.Odliczaczor;

import static pl.alkosoft.odliczaczor.data.Assets.*;

public class MusicManager {

    private Music menuThemeSong;
    private Music finishThemeSong;
    private Music appThemeSong;
    private Music chickenExplodeSfx;
    private boolean muteOn;

    public void init(Odliczaczor app){
        this.menuThemeSong = app.getAssetManager().get(MENU_THEME_SONG.getPath());
        this.appThemeSong = app.getAssetManager().get(APP_THEME_SONG.getPath());
        this.chickenExplodeSfx = app.getAssetManager().get(CHICKEN_EXPLODE_SFX.getPath());
        this.finishThemeSong = app.getAssetManager().get(FINISH_SCREEN_THEME_SONG.getPath());
        this.muteOn=false;
    }

    public Music getMenuThemeSong() {
        return menuThemeSong;
    }

    public Music getFinishThemeSong() {
        return finishThemeSong;
    }

    public Music getAppThemeSong() {
        return appThemeSong;
    }

    public Music getChickenExplodeSfx() {
        return chickenExplodeSfx;
    }

    public boolean isMuteOn() {
        return muteOn;
    }

    public void setMuteOn(boolean muteOn) {
        this.muteOn = muteOn;
    }
}
