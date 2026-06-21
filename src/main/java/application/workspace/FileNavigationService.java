package application.workspace;

import application.fs.DirectoryReader;
import domain.fs.DirectoryListing;
import domain.fs.DirectoryReadException;
import domain.workspace.FilePaneState;
import domain.workspace.PaneId;
import domain.workspace.WorkspaceState;

import java.nio.file.Path;
import java.util.Objects;

public class FileNavigationService {

  private final DirectoryReader directoryReader;

  public FileNavigationService(DirectoryReader directoryReader) {
    this.directoryReader = Objects.requireNonNull(
        directoryReader,
        "directoryReader must not be null"
    );
  }

  public void openDirectory(WorkspaceState state, PaneId paneId, Path directory)
      throws DirectoryReadException {
    Objects.requireNonNull(state, "state must not be null");
    Objects.requireNonNull(paneId, "paneId must not be null");
    Objects.requireNonNull(directory, "directory must not be null");

    FilePaneState pane = state.getPanes().stream()
        .filter(candidate -> Objects.equals(candidate.getId(), paneId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Pane not found: " + paneId));

    DirectoryListing listing = directoryReader.read(directory);
    pane.setCurrentPath(listing.directory());
    pane.setLocationText(listing.directory().toString());
    pane.setEntries(listing.entries());
  }
}
