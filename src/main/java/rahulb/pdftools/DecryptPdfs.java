package rahulb.pdftools;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.pdfbox.pdmodel.PDDocument;

final class DecryptPdfs extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDFS_DIRECTORY = "input-pdfs-directory";
  private static final String ARG_OUTPUT_PDFS_DIRECTORY = "output-pdfs-directory";

  DecryptPdfs() {}

  @Override
  void executeInternal(String... args) {

    decryptPdfs(args);
  }

  @Override
  void executeInternal(Map<?, ?> args) {

    decryptPdfs(args);
  }

  private static void decryptPdfs(String... args) {

    Map<String, String> argMap =
        Map.of(
            ARG_INPUT_PDFS_DIRECTORY, args[0],
            ARG_OUTPUT_PDFS_DIRECTORY, args[1]);

    decryptPdfs(argMap);
  }

  static void decryptPdfs(Map<?, ?> args) {

    String inputPdfsDir = (String) args.get(ARG_INPUT_PDFS_DIRECTORY);
    String outputPdfsDir = (String) args.get(ARG_OUTPUT_PDFS_DIRECTORY);

    decryptPdfs(Paths.get(inputPdfsDir), Paths.get(outputPdfsDir));
  }

  private static void decryptPdfs(Path inputPdfsDirPath, Path outputPdfsDirPath) {

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
