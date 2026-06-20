package ui.theme;

import javafx.application.Application;

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

  public AppTheme getCurrentTheme() {
    return currentTheme;
  }
}
