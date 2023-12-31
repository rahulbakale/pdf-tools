package rahulb.pdftools.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;

public final class PdfToImageService {

  public void pdfToImage(
      File inputPdfFile, File outputDir, List<Integer> pageNumbers, int dpi, String imageFormat)
      throws IOException {

    try (var document = PDDocument.load(inputPdfFile)) {
      pdfToImage(inputPdfFile, outputDir, pageNumbers, dpi, imageFormat, document);
    }
  }

  private static void pdfToImage(
      File inputPdfFile,
      File outputDir,
      List<Integer> pageNumbers,
      int dpi,
      String imageFormat,
      PDDocument document)
      throws IOException {

    Files.createDirectories(outputDir.toPath());

    PdfPageImageWriter imageWriter = new PdfPageImageWriter(document);

    imageWriter.writePages(pageNumbers, dpi, imageFormat, outputDir, inputPdfFile.getName());
  }
}
