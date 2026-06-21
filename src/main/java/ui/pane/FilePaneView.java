package ui.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import presentation.FilePaneViewModel;

import java.util.Objects;

public class FilePaneView extends BorderPane {

  public FilePaneView(FilePaneViewModel viewModel) {
    FilePaneViewModel checkedViewModel = Objects.requireNonNull(
        viewModel,
        "viewModel must not be null"
    );

    FilePaneHeaderView header = new FilePaneHeaderView(checkedViewModel);
    FileTableView fileTableView = new FileTableView(checkedViewModel.getEntryViewModels());
    Label statusLabel = new Label("0 selected");
    setTop(header);
    setCenter(fileTableView);
    setBottom(statusLabel);

    getStyleClass().add("file-pane");
    if (checkedViewModel.isActive()) {
      getStyleClass().add("file-pane-active");
    }
    statusLabel.getStyleClass().add("file-pane-status");
  }
}
