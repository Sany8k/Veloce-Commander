package presentation;

import domain.workspace.FilePaneState;
import domain.workspace.PaneId;
import domain.fs.FileEntry;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class FilePaneViewModel {

  private final PaneId id;
  private final String title;
  private final String locationText;
  private final boolean active;
  private final Path currentPath;
  private final List<FileEntry> entries;

  public FilePaneViewModel(
      PaneId id,
      String title,
      String locationText,
      boolean active,
      Path currentPath,
      List<FileEntry> entries
  ) {
    this.id = Objects.requireNonNull(id, "Pane id must not be null");
    this.title = Objects.requireNonNull(title, "Title must not be null");
    this.locationText = Objects.requireNonNull(locationText, "Location text must not be null");
    this.active = active;
    this.currentPath = currentPath;
    this.entries = List.copyOf(Objects.requireNonNull(entries, "Entries must not be null"));
  }

  public static FilePaneViewModel from(FilePaneState state) {
    Objects.requireNonNull(state, "File pane state must not be null");

    return new FilePaneViewModel(
        state.getId(),
        state.getTitle(),
        state.getLocationText(),
        state.isActive(),
        state.getCurrentPath(),
        state.getEntries()
    );
  }

  public PaneId getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getLocationText() {
    return locationText;
  }

  public boolean isActive() {
    return active;
  }

  public Path getCurrentPath() {
    return currentPath;
  }

  public List<FileEntry> getEntries() {
    return entries;
  }

  public String getStatusText() {
    if (currentPath == null) {
      return "No directory loaded";
    }

    int entryCount = entries.size();
    if (entryCount == 0) {
      return "Directory is empty";
    }
    if (entryCount == 1) {
      return "1 item";
    }
    return entryCount + " items";
  }

  public List<FileEntryViewModel> getEntryViewModels() {
    return entries.stream()
        .map(FileEntryViewModel::from)
        .toList();
  }
}
