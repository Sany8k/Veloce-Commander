package application.workspace;

import application.fs.DirectoryReader;
import domain.fs.DirectoryListing;
import domain.fs.FileEntry;
import domain.fs.FileType;
import domain.workspace.FilePaneState;
import domain.workspace.PaneId;
import domain.workspace.WorkspaceState;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileNavigationServiceTest {

  @Test
  void openDirectoryWritesListingToPaneState() throws Exception {
    Path directory = Path.of("directory");
    FileEntry entry = new FileEntry(
        directory.resolve("file.txt"),
        "file.txt",
        FileType.FILE,
        12,
        Instant.EPOCH,
        false,
        true,
        true
    );
    DirectoryReader directoryReader = path -> new DirectoryListing(path, List.of(entry));
    FileNavigationService service = new FileNavigationService(directoryReader);
    WorkspaceState state = new WorkspaceState();
    PaneId paneId = PaneId.random();
    FilePaneState pane = new FilePaneState(paneId, "Pane 1", "Not loaded", true);
    state.addPane(pane);
    state.setActivePaneId(paneId);

    service.openDirectory(state, paneId, directory);

    assertThat(pane.getCurrentPath()).isEqualTo(directory);
    assertThat(pane.getLocationText()).isEqualTo(directory.toString());
    assertThat(pane.getEntries()).containsExactly(entry);
  }

  @Test
  void openDirectoryThrowsWhenPaneDoesNotExist() {
    DirectoryReader directoryReader = path -> new DirectoryListing(path, List.of());
    FileNavigationService service = new FileNavigationService(directoryReader);
    WorkspaceState state = new WorkspaceState();
    PaneId missingPaneId = PaneId.random();

    assertThatThrownBy(() -> service.openDirectory(state, missingPaneId, Path.of("directory")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Pane not found: " + missingPaneId);
  }
}
