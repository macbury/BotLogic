package de.macbury.botlogic;

import de.macbury.botlogic.ui.EditorFrame;

import javax.swing.*;
import java.util.Locale;

public class Main {

    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
          } catch (Exception e) { /* Never happens */ }

          JComponent.setDefaultLocale(Locale.ENGLISH);
          EditorFrame ef = new EditorFrame();
        }
      });
    }
}
