package rahulb.pdftools.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public final class ImagesToPdf {

  /** User space units per inch. See org.apache.pdfbox.pdmodel.common.PDRectangle. */
  private static final float POINTS_PER_INCH = 72;

  /** User space units per millimeter. See org.apache.pdfbox.pdmodel.common.PDRectangle. */
  // private static final float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;

  private ImagesToPdf() {}

  public static void imagesToPdf(
      File inputImagesDir,
      String outputPageSize,
      String imagePosition,
      String pageMargins,
      File outputPdfFile)
      throws IOException, ReflectiveOperationException {

    PDRectangle requiredPageSize =
        (PDRectangle) PDRectangle.class.getField(outputPageSize).get(null);

    try (var outDoc = new PDDocument()) {

      try (Stream<Path> dirContents = Files.list(inputImagesDir.toPath())) {
        dirContents
            .map(Path::toFile)
            .filter(File::isFile)
            .sorted(Comparator.comparing(File::getName))
            .forEach(
                file ->
                    createPageFromImage(
                        file, requiredPageSize, imagePosition, pageMargins, outDoc));
      }

      Utils.saveDocument(outDoc, outputPdfFile);
    }
  }

  private static void createPageFromImage(
      File file,
      PDRectangle requiredPageSize,
      String imagePosition,
      String pageMargins,
      PDDocument outDoc) {

    try {
      var imageObj = PDImageXObject.createFromFile(file.getAbsolutePath(), outDoc);

      float topMargin;
      float rightMargin;
      float bottomMargin;
      float leftMargin;

      switch (pageMargins) {
        case "none" -> {
          topMargin = 0;
          rightMargin = 0;
          bottomMargin = 0;
          leftMargin = 0;
        }
        case "standard" -> {
          topMargin = 1 /*inch*/ * POINTS_PER_INCH;
          rightMargin = 1 /*inch*/ * POINTS_PER_INCH;
          bottomMargin = 1 /*inch*/ * POINTS_PER_INCH;
          leftMargin = 1 /*inch*/ * POINTS_PER_INCH;
        }
        default -> throw new IllegalArgumentException(
            String.format("Invalid page margins: '%s'", pageMargins));
      }

      float pageWidthWithoutMargins = requiredPageSize.getWidth() - leftMargin - rightMargin;
      float pageHeightWithoutMargins = requiredPageSize.getHeight() - topMargin - bottomMargin;

      if (pageWidthWithoutMargins < 0) {
        throw new IllegalArgumentException(
            "Increase the page width and/or reduce the left and/or right margins");
      }

      if (pageHeightWithoutMargins < 0) {
        throw new IllegalArgumentException(
            "Increase the page height and/or reduce the top and/or bottom margins");
      }

      if (pageWidthWithoutMargins < imageObj.getWidth()) {
        throw new IllegalArgumentException(
            "Image too wide for the page. Increase the page width and/or reduce the left and/or right margins.");
      }

      if (pageHeightWithoutMargins < imageObj.getHeight()) {
        throw new IllegalArgumentException(
            "Image too tall for the page. Increase the page height and/or reduce the top and/or bottom margins.");
      }

      float finalImageWidth = Math.min(imageObj.getWidth(), pageWidthWithoutMargins);
      float finalImageHeight = Math.min(imageObj.getHeight(), pageHeightWithoutMargins);

      float x;
      float y;

      switch (imagePosition) {
        case "center" -> {
          x = (requiredPageSize.getWidth() - finalImageWidth) / 2;
          y = (requiredPageSize.getHeight() - finalImageHeight) / 2;
        }
        case "top-left" -> {
          x = 0 + leftMargin;
          y = (requiredPageSize.getHeight() - finalImageHeight) - topMargin;
        }
        default -> throw new IllegalArgumentException(
            String.format("Invalid image position: '%s'", imagePosition));
      }

      var page = new PDPage(requiredPageSize);

      try (var contentStream = new PDPageContentStream(outDoc, page)) {

        contentStream.drawImage(imageObj, x, y, finalImageWidth, finalImageHeight);
      }

      outDoc.addPage(page);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
