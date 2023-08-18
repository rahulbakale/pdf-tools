package rahulb.pdftools.core;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.pdfbox.pdmodel.PDDocument;

public final class DecryptPdfs {

  private DecryptPdfs() {}

  public static void decryptPdfs(
      Path inputPdfsDirPath, Path outputPdfsDirPath, char[] docOpenPassword) throws IOException {

    try (Stream<Path> inputDirContents =
        Files.walk(inputPdfsDirPath, FileVisitOption.FOLLOW_LINKS)) {

      String docOpenPasswordStr = String.valueOf(docOpenPassword);

      inputDirContents
          .filter(path -> path.toFile().isFile())
          .forEach(
              file -> decryptPdf(file, docOpenPasswordStr, inputPdfsDirPath, outputPdfsDirPath));
    }
  }

  private static void decryptPdf(
      Path inputPdfPath, String docOpenPassword, Path inputPdfsDirPath, Path outputPdfsDirPath) {

    try (var doc = PDDocument.load(inputPdfPath.toFile(), docOpenPassword)) {

      doc.setAllSecurityToBeRemoved(true);

      var outputPdfPath = getOutputPdfPath(inputPdfPath, inputPdfsDirPath, outputPdfsDirPath);

      Utils.saveDocument(doc, outputPdfPath.toFile());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static Path getOutputPdfPath(
      Path inputPdfPath, Path inputPdfsDirPath, Path outputPdfsDirPath) {

    var relativePath = inputPdfsDirPath.relativize(inputPdfPath);
    return outputPdfsDirPath.resolve(relativePath);
  }
}
