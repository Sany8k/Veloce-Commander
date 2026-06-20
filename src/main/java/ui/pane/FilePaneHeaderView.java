package ui.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class FilePaneHeaderView extends VBox {

  public FilePaneHeaderView(String title) {
    Label titleLabel = new Label(title);
    Label pathLabel = new Label("Location: Not loaded");
    this.getChildren().addAll(titleLabel, pathLabel);

    this.getStyleClass().add("file-pane-header");
    titleLabel.getStyleClass().add("file-pane-header-title-label");
    pathLabel.getStyleClass().add("file-pane-header-path-label");
  }
}
