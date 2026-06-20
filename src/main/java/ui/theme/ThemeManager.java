package ui.theme;

import javafx.application.Application;
import javafx.scene.Scene;

import java.util.Objects;

public class ThemeManager {

  private AppTheme currentTheme;

  public void applyDefaultTheme() {
    applyTheme(AppTheme.PRIMER_DARK);
  }

  public void applyTheme(AppTheme theme) {
    Objects.requireNonNull(theme, "theme must not be null");

    Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
    currentTheme = theme;
  }

  public void installAppStyles(Scene scene) {
    Objects.requireNonNull(scene, "scene must not be null");

    String stylePath = Objects.requireNonNull(ThemeManager.class.getResource("/styles/app.css"),
        "styles/app.css was not found on the classpath").toExternalForm();

    scene.getStylesheets().add(stylePath);
  }

  public AppTheme getCurrentTheme() {
    return currentTheme;
  }
}
