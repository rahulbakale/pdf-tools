package rahulb.pdftools.core;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PdfToImageTest {

  /** User space units per inch. See org.apache.pdfbox.pdmodel.common.PDRectangle. */
  private static final float POINTS_PER_INCH = 72;

  @Test
  void test_images_of_selected_pdf_pages_are_generated_successfully() throws Exception {

    Path inputPdfPath = Paths.get("src/test/resources/PdfToImage/input-1.pdf");
    Path outputDirPath = Paths.get("target/test/PdfToImage/output-1");
    Path expectedOutputDirPath = Paths.get("src/test/resources/PdfToImage/expected-output-1");

    String expectedImageFormat = "JPEG";
    int[] pageNumbers = {1, 3};
    int expectedImageDPI = 300;

    PdfToImage.pdfToImage(
        inputPdfPath.toFile(),
        outputDirPath.toFile(),
        IntStream.of(pageNumbers),
        expectedImageDPI,
        expectedImageFormat);

    Set<Path> outputDirContentPaths = FileUtils.getDirContentPaths(outputDirPath);

    // Verify that the output directory contains exactly the expected files.
    Assertions.assertEquals(
        IntStream.of(pageNumbers)
            .mapToObj(
                pageNum ->
                    String.format(
                        "%s.%d.%s", inputPdfPath.getFileName(), pageNum, expectedImageFormat))
            .collect(Collectors.toSet()),
        outputDirContentPaths.stream().map(Path::toString).collect(Collectors.toSet()));

    BigDecimal pageWidthInInches;
    BigDecimal pageHeightInInches;

    try (var pdDocument = PDDocument.load(inputPdfPath.toFile())) {

      PDRectangle pageMediaBox = pdDocument.getPage(1).getMediaBox();

      pageWidthInInches =
          BigDecimal.valueOf(pageMediaBox.getWidth())
              .divide(BigDecimal.valueOf(POINTS_PER_INCH), 10, RoundingMode.HALF_UP);
      pageHeightInInches =
          BigDecimal.valueOf(pageMediaBox.getHeight())
              .divide(BigDecimal.valueOf(POINTS_PER_INCH), 10, RoundingMode.HALF_UP);
    }

    BigDecimal expectedDPI = BigDecimal.valueOf(expectedImageDPI);
    BigDecimal BD_100 = BigDecimal.valueOf(100);
    BigDecimal dpiErrorTolerance = BigDecimal.valueOf(0.05);

    for (Path outputImageRelativePath : outputDirContentPaths) {

      Path outputImagePath = outputDirPath.resolve(outputImageRelativePath);

      // Verify that the generated images are of the expected format.
      ImageInfo outputImageInfo = Imaging.getImageInfo(outputImagePath.toFile());
      Assertions.assertEquals("JPEG", outputImageInfo.getFormat().getName());

      // Verify that the generated images have the expected DPI.
      Dimension imageSize = Imaging.getImageSize(outputImagePath.toFile());
      BigDecimal imageWidthDPI =
          BigDecimal.valueOf(imageSize.getWidth())
              .divide(pageWidthInInches, 10, RoundingMode.HALF_UP);
      BigDecimal imageHeightDPI =
          BigDecimal.valueOf(imageSize.getHeight())
              .divide(pageHeightInInches, 10, RoundingMode.HALF_UP);

      BigDecimal widthDpiErrorPercent =
          imageWidthDPI
              .subtract(expectedDPI, MathContext.DECIMAL32)
              .abs()
              .divide(expectedDPI, 10, RoundingMode.HALF_UP)
              .multiply(BD_100);

      BigDecimal heightDpiErrorPercent =
          imageHeightDPI
              .subtract(expectedDPI, MathContext.DECIMAL32)
              .abs()
              .divide(expectedDPI, 10, RoundingMode.HALF_UP)
              .multiply(BD_100);

      Assertions.assertTrue(widthDpiErrorPercent.compareTo(dpiErrorTolerance) < 1);
      Assertions.assertTrue(heightDpiErrorPercent.compareTo(dpiErrorTolerance) < 1);

      // Verify the content of generated images.
      Path expectedOutputImagePath = expectedOutputDirPath.resolve(outputImageRelativePath);
      Assertions.assertTrue(
          imageEquals(expectedOutputImagePath, outputImagePath),
          () -> "Expected output image is not same as the actual output image.");
    }
  }

  private static boolean imageEquals(Path image1Path, Path image2Path) throws IOException {

    BufferedImage image1 = ImageIO.read(image1Path.toFile());
    BufferedImage image2 = ImageIO.read(image2Path.toFile());

    // https://stackoverflow.com/questions/11006394/is-there-a-simple-way-to-compare-bufferedimage-instances

    if (image1.getHeight() != image2.getHeight() || image1.getWidth() != image2.getWidth()) {
      return false;
    }

    for (int x = 0; x < image1.getWidth(); x++) {
      for (int y = 0; y < image1.getHeight(); y++) {

        if (image1.getRGB(x, y) != image2.getRGB(x, y)) {
          return false;
        }
      }
    }

    return true;
  }
}
