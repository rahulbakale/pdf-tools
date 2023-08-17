package rahulb.pdftools.core;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.apache.pdfbox.pdmodel.PDDocument;

public final class DecryptPdfs {

  private DecryptPdfs() {}

  public static void decryptPdfs(Path inputPdfsDirPath, Path outputPdfsDirPath) {

    String docOpenPassword =
        String.valueOf(
            System.console().readPassword("Enter the password required to open the document:"));

    decryptPdfs(inputPdfsDirPath, outputPdfsDirPath, docOpenPassword);
  }

  static void decryptPdfs(Path inputPdfsDirPath, Path outputPdfsDirPath, String docOpenPassword) {

    try (Stream<Path> inputDirContents =
        Files.walk(inputPdfsDirPath, FileVisitOption.FOLLOW_LINKS)) {

      inputDirContents
          .filter(path -> path.toFile().isFile())
          .forEach(file -> decryptPdf(file, docOpenPassword, inputPdfsDirPath, outputPdfsDirPath));

    } catch (IOException e) {
      throw new RuntimeException(e);
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
