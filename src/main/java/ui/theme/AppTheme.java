package ui.theme;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;

public enum AppTheme {
  PRIMER_LIGHT("primer-light", "Primer Light", false, new PrimerLight().getUserAgentStylesheet()),
  PRIMER_DARK("primer-dark", "Primer Dark", true, new PrimerDark().getUserAgentStylesheet());

  private final String id;
  private final String displayName;
  private final boolean isDark;
  private final String stylesheet;

  AppTheme(String id, String displayName, boolean isDark, String stylesheet) {
    this.id = id;
    this.displayName = displayName;
    this.isDark = isDark;
    this.stylesheet = stylesheet;
  }

  public String getId() {
    return id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public boolean isDark() {
    return isDark;
  }

  public String getUserAgentStylesheet() {
    return stylesheet;
  }
}
