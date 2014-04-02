package de.macbury.botlogic.ui;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.macbury.botlogic.core.GameManager;

import javax.swing.*;

/**
 * Created by macbury on 27.03.14.
 */
public class EditorFrame extends JFrame {
  private GameEditorForm form;

  public EditorFrame() {
    super("Botlogic");

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run () {
        Runtime.getRuntime().halt(0); // Because fuck you, Swing shutdown hooks.
      }
    });

    this.form = new GameEditorForm();
    this.setContentPane(form.getRootPanel());
    this.pack();
    this.setSize(1360, 768);
    this.setResizable(true);
    this.setJMenuBar(form.getMenu());

    final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.title = "Loading...";
    config.width = 512;
    config.height = 512;
    config.vSyncEnabled = true;
    config.samples = 8;
    config.useGL30 = true;

    this.setVisible(true);
    final GameManager game = new GameManager();

    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          form.bind(EditorFrame.this, game, config);
        } catch(Exception e) {
          e.printStackTrace();
          JOptionPane.showMessageDialog(EditorFrame.this,
                  e.getMessage(),
                  "Runtime Error",
                  JOptionPane.ERROR_MESSAGE);
          System.exit(1);
        }

      }
    });


  }
}
