package domain.fs;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public record DirectoryListing(
    Path directory,
    List<FileEntry> entries
) {

  public DirectoryListing {
    Objects.requireNonNull(directory, "directory must not be null");
    Objects.requireNonNull(entries, "entries must not be null");
    entries = List.copyOf(entries);
  }
}
