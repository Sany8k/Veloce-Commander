package application.fs;

import domain.fs.DirectoryListing;
import domain.fs.DirectoryReadException;

import java.nio.file.Path;

public interface DirectoryReader {

  DirectoryListing read(Path directory) throws DirectoryReadException;
}
