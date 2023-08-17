package rahulb.pdftools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.pdfbox.pdmodel.PDDocument;

final class Utils {

  private Utils() {}

  static void saveDocument(PDDocument document, File outputFile) throws IOException {

    Path outputDirPath = outputFile.toPath().getParent();

    if (outputDirPath == null) {
      throw new IllegalArgumentException(
          String.format(
              "Output file path '%s' is invalid because it does not have a parent directory.",
              outputFile));
    }

    Files.createDirectories(outputDirPath);

    document.save(outputFile);
  }
}
