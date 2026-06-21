package infrastructure.fs;

import application.fs.DirectoryReader;
import domain.fs.DirectoryListing;
import domain.fs.DirectoryReadError;
import domain.fs.DirectoryReadException;
import domain.fs.FileEntry;
import domain.fs.FileType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class NioDirectoryReader implements DirectoryReader {

  @Override
  public DirectoryListing read(Path directory) throws DirectoryReadException {
    Objects.requireNonNull(directory, "directory must not be null");

    if (Files.notExists(directory)) {
      throw new DirectoryReadException(DirectoryReadError.PATH_NOT_FOUND, directory);
    }

    if (!Files.isDirectory(directory)) {
      throw new DirectoryReadException(DirectoryReadError.NOT_A_DIRECTORY, directory);
    }

    try (Stream<Path> stream = Files.list(directory)) {
      List<FileEntry> entries = stream
          .map(this::toFileEntry)
          .sorted(entryComparator())
          .toList();

      return new DirectoryListing(directory, entries);
    } catch (AccessDeniedException exception) {
      throw new DirectoryReadException(DirectoryReadError.ACCESS_DENIED, directory);
    } catch (DirectoryIteratorException exception) {
      if (exception.getCause() instanceof AccessDeniedException) {
        throw new DirectoryReadException(DirectoryReadError.ACCESS_DENIED, directory);
      }
      throw new DirectoryReadException(DirectoryReadError.IO_ERROR, directory);
    } catch (UncheckedIOException exception) {
      if (exception.getCause() instanceof AccessDeniedException) {
        throw new DirectoryReadException(DirectoryReadError.ACCESS_DENIED, directory);
      }
      throw new DirectoryReadException(DirectoryReadError.IO_ERROR, directory);
    } catch (IOException exception) {
      throw new DirectoryReadException(DirectoryReadError.IO_ERROR, directory);
    }
  }

  private FileEntry toFileEntry(Path path) {
    FileType type = readType(path);

    return new FileEntry(
        path,
        readName(path),
        type,
        readSize(path, type),
        readLastModified(path),
        readHidden(path),
        Files.isReadable(path),
        Files.isWritable(path)
    );
  }

  private FileType readType(Path path) {
    if (Files.isSymbolicLink(path)) {
      return FileType.SYMLINK;
    }
    if (Files.isDirectory(path)) {
      return FileType.DIRECTORY;
    }
    if (Files.isRegularFile(path)) {
      return FileType.FILE;
    }
    return FileType.OTHER;
  }

  private String readName(Path path) {
    Path fileName = path.getFileName();
    if (fileName == null) {
      return path.toString();
    }
    return fileName.toString();
  }

  private long readSize(Path path, FileType type) {
    if (type != FileType.FILE) {
      return 0;
    }

    try {
      return Files.size(path);
    } catch (IOException exception) {
      return 0;
    }
  }

  private Instant readLastModified(Path path) {
    try {
      return Files.getLastModifiedTime(path).toInstant();
    } catch (IOException exception) {
      return Instant.EPOCH;
    }
  }

  private boolean readHidden(Path path) {
    try {
      return Files.isHidden(path);
    } catch (IOException exception) {
      return false;
    }
  }

  private Comparator<FileEntry> entryComparator() {
    return Comparator
        .comparingInt((FileEntry entry) -> typeOrder(entry.type()))
        .thenComparing(FileEntry::name, String.CASE_INSENSITIVE_ORDER)
        .thenComparing(FileEntry::name);
  }

  private int typeOrder(FileType type) {
    return switch (type) {
      case DIRECTORY -> 0;
      case SYMLINK -> 1;
      case FILE -> 2;
      case ARCHIVE -> 3;
      case DRIVE -> 4;
      case VIRTUAL -> 5;
      case OTHER -> 6;
    };
  }
}
