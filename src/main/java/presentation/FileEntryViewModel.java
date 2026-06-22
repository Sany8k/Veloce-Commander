package presentation;

import domain.fs.FileEntry;
import domain.fs.FileType;

import java.nio.file.Path;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public record FileEntryViewModel(
    String name,
    String typeText,
    String sizeText,
    String modifiedText,
    String attributesText,
    Path path,
    boolean directory
) {

  private static final DateTimeFormatter MODIFIED_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());

  public FileEntryViewModel {
    Objects.requireNonNull(name, "name must not be null");
    Objects.requireNonNull(typeText, "typeText must not be null");
    Objects.requireNonNull(sizeText, "sizeText must not be null");
    Objects.requireNonNull(modifiedText, "modifiedText must not be null");
    Objects.requireNonNull(attributesText, "attributesText must not be null");
    Objects.requireNonNull(path, "path must not be null");
  }

  public static FileEntryViewModel from(FileEntry entry) {
    Objects.requireNonNull(entry, "entry must not be null");

    boolean directory = entry.type() == FileType.DIRECTORY;
    Path path = entry.path();
    return new FileEntryViewModel(
        entry.name(),
        entry.type().name(),
        formatSize(entry),
        MODIFIED_FORMATTER.format(entry.lastModified()),
        formatAttributes(entry),
        path,
        directory
    );
  }

  private static String formatSize(FileEntry entry) {
    if (entry.type() != FileType.FILE && entry.size() == 0) {
      return "";
    }

    long bytes = entry.size();
    if (bytes < 1024) {
      return bytes + " B";
    }

    double value = bytes / 1024.0;
    String unit = "KB";
    if (value >= 1024) {
      value = value / 1024.0;
      unit = "MB";
    }
    if (value >= 1024) {
      value = value / 1024.0;
      unit = "GB";
    }

    return String.format(Locale.ROOT, "%.1f %s", value, unit);
  }

  private static String formatAttributes(FileEntry entry) {
    StringBuilder attributes = new StringBuilder();
    if (entry.hidden()) {
      attributes.append("H");
    }
    if (entry.readable()) {
      attributes.append("R");
    }
    if (entry.writable()) {
      attributes.append("W");
    }
    return attributes.toString();
  }
}
