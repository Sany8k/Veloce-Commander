package ui.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import presentation.FilePaneViewModel;

import java.util.Objects;

public class FilePaneHeaderView extends VBox {

  public FilePaneHeaderView(FilePaneViewModel viewModel) {
    FilePaneViewModel checkedViewModel = Objects.requireNonNull(
        viewModel,
        "viewModel must not be null"
    );

    Label titleLabel = new Label(checkedViewModel.getTitle());
    Label pathLabel = new Label("Location: " + checkedViewModel.getLocationText());
    getChildren().addAll(titleLabel, pathLabel);

    getStyleClass().add("file-pane-header");
    titleLabel.getStyleClass().add("file-pane-header-title-label");
    pathLabel.getStyleClass().add("file-pane-header-path-label");
  }
}
