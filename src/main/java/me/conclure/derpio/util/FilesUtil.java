package me.conclure.derpio.util;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FilesUtil {
  private static final Logger LOGGER = LoggerFactory.getLogger(FilesUtil.class);

  private FilesUtil() {
    throw new AssertionError();
  }

  public static void createDirsIfNotExist(Path path) throws IOException {
    if (Files.exists(path) && (Files.isDirectory(path) || Files.isSymbolicLink(path))) {
      return;
    }

    try {
      Files.createDirectories(path);
    } catch (FileAlreadyExistsException e) {
      LOGGER.error(e.getMessage(),e);
    }
  }

}
