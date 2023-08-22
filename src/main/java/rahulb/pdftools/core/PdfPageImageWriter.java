package rahulb.pdftools.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

final class PdfPageImageWriter {

  private final PDFRenderer renderer;

  PdfPageImageWriter(PDDocument document) {
    this.renderer = new PDFRenderer(document);
  }

  void writePages(
      IntStream pageNumbers,
      float dpi,
      String imageFormat,
      File outputDir,
      String outputFileNamePrefix) {
    createPageImages(pageNumbers, dpi, imageFormat, outputDir, outputFileNamePrefix);
  }

  private void createPageImages(
      IntStream pageNumbers,
      float dpi,
      String imageFormat,
      File outputDir,
      String outputFileNamePrefix) {
    pageNumbers.forEach(
        pageNumber ->
            createPageImage(
                renderer, pageNumber, dpi, imageFormat, outputDir, outputFileNamePrefix));
  }

  private void createPageImage(
      PDFRenderer renderer,
      int pageNumber,
      float dpi,
      String imageFormat,
      File outputDir,
      String pdfFileName) {
    try {
      var pageIndex = pageNumber - 1;
      BufferedImage image = renderer.renderImageWithDPI(pageIndex, dpi, ImageType.RGB);
      var imageFile = new File(outputDir, pdfFileName + '.' + pageNumber + '.' + imageFormat);
      boolean success = ImageIO.write(image, imageFormat, imageFile);
      if (!success) {
        throw new RuntimeException(
            "No appropriate writer found for image format '" + imageFormat + "'");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
