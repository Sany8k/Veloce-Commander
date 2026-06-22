package ui.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import presentation.FileEntryViewModel;
import presentation.FilePaneViewModel;

import java.util.Objects;
import java.util.function.Consumer;

public class FilePaneView extends BorderPane {

  public FilePaneView(
      FilePaneViewModel viewModel,
      Consumer<FileEntryViewModel> onEntryOpen,
      Runnable onPaneFocus
  ) {
    FilePaneViewModel checkedViewModel = Objects.requireNonNull(
        viewModel,
        "viewModel must not be null"
    );
    Consumer<FileEntryViewModel> checkedOnEntryOpen = Objects.requireNonNull(
        onEntryOpen,
        "onEntryOpen must not be null"
    );
    Runnable checkedOnPaneFocus = Objects.requireNonNull(
        onPaneFocus,
        "onPaneFocus must not be null"
    );

    FilePaneHeaderView header = new FilePaneHeaderView(checkedViewModel);
    FileTableView fileTableView = new FileTableView(
        checkedViewModel.getEntryViewModels(),
        checkedOnEntryOpen,
        checkedOnPaneFocus
    );
    Label statusLabel = new Label(checkedViewModel.getStatusText());
    setTop(header);
    setCenter(fileTableView);
    setBottom(statusLabel);

    getStyleClass().add("file-pane");
    setActive(checkedViewModel.isActive());
    statusLabel.getStyleClass().add("file-pane-status");
    addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> checkedOnPaneFocus.run());
  }

  public void setActive(boolean active) {
    if (active) {
      if (!getStyleClass().contains("file-pane-active")) {
        getStyleClass().add("file-pane-active");
      }
      return;
    }
    getStyleClass().remove("file-pane-active");
  }
}
