package pl.alkosoft.odliczaczor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.alkosoft.odliczaczor.Odliczaczor;

import static pl.alkosoft.odliczaczor.Odliczaczor.*;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = TITLE + " v" + VERSION;
		config.width = WIDTH;
		config.height = HEIGHT;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.resizable = false;
		//config.fullscreen = true;
		//config.vSyncEnabled = true;
		new LwjglApplication(new Odliczaczor(), config);
	}
}
