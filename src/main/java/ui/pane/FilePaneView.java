package ui.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class FilePaneView extends BorderPane {

  public FilePaneView(String title) {
    FilePaneHeaderView filePaneHeaderView = new FilePaneHeaderView(title);
    Label placeholderLabel = new Label("No directory loaded");
    Label statusLabel = new Label("0 selected");
    this.setTop(filePaneHeaderView);
    this.setCenter(placeholderLabel);
    this.setBottom(statusLabel);

    this.getStyleClass().add("file-pane");
    placeholderLabel.getStyleClass().add("file-pane-placeholder");
    statusLabel.getStyleClass().add("file-pane-status");
  }
}
