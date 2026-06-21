package ui.pane;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import presentation.FileEntryViewModel;

import java.util.List;
import java.util.Objects;

public class FileTableView extends TableView<FileEntryViewModel> {

  public FileTableView(List<FileEntryViewModel> entries) {
    List<FileEntryViewModel> checkedEntries = List.copyOf(
        Objects.requireNonNull(entries, "entries must not be null")
    );

    getStyleClass().add("file-table-view");
    setPlaceholder(new Label("No directory loaded"));
    setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    TableColumn<FileEntryViewModel, String> nameColumn = createColumn(
        "Name",
        FileEntryViewModel::name
    );
    nameColumn.setPrefWidth(280);

    TableColumn<FileEntryViewModel, String> typeColumn = createColumn(
        "Type",
        FileEntryViewModel::typeText
    );
    typeColumn.setPrefWidth(90);

    TableColumn<FileEntryViewModel, String> sizeColumn = createColumn(
        "Size",
        FileEntryViewModel::sizeText
    );
    sizeColumn.setPrefWidth(90);

    TableColumn<FileEntryViewModel, String> modifiedColumn = createColumn(
        "Modified",
        FileEntryViewModel::modifiedText
    );
    modifiedColumn.setPrefWidth(130);

    TableColumn<FileEntryViewModel, String> attrColumn = createColumn(
        "Attr",
        FileEntryViewModel::attributesText
    );
    attrColumn.setPrefWidth(70);

    getColumns().add(nameColumn);
    getColumns().add(typeColumn);
    getColumns().add(sizeColumn);
    getColumns().add(modifiedColumn);
    getColumns().add(attrColumn);
    getItems().setAll(checkedEntries);
  }

  private TableColumn<FileEntryViewModel, String> createColumn(
      String title,
      EntryTextProvider textProvider
  ) {
    TableColumn<FileEntryViewModel, String> column = new TableColumn<>(title);
    column.setReorderable(false);
    column.setCellValueFactory(
        cellData -> new ReadOnlyStringWrapper(textProvider.getText(cellData.getValue()))
    );
    return column;
  }

  @FunctionalInterface
  private interface EntryTextProvider {

    String getText(FileEntryViewModel entry);
  }
}
