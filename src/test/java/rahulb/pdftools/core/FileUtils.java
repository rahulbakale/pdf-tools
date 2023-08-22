package rahulb.pdftools.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class FileUtils {

  private FileUtils() {}

  static Set<Path> getDirContentPaths(Path dirPath) throws IOException {

    try (Stream<Path> pathStream = Files.walk(dirPath)) {

      return pathStream
          .filter(path -> !path.equals(dirPath))
          .map(dirPath::relativize)
          .collect(Collectors.toSet());
    }
  }
}
