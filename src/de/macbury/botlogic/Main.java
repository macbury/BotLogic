package de.macbury.botlogic;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.macbury.botlogic.core.GameManager;
import de.macbury.botlogic.ui.EditorFrame;

import javax.swing.*;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
      LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
      config.title = "BotLogic";
      config.width = 1024;
      config.height = 768;
      config.vSyncEnabled = true;
      config.useGL30 = true;
      config.resizable = false;
      LwjglApplication app = new LwjglApplication(new GameManager(), config);
    }
}
