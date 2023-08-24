package rahulb.pdftools.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImagesToPdfServiceTest {

  @Test
  void test_imagesToPdf__page_size_A4__image_position_center__page_margins_none()
      throws ReflectiveOperationException, IOException {

    Path inputImagesDirPath = Paths.get("src/test/resources/ImagesToPdf/input-1");
    Path outputPdfFilePath = Paths.get("target/test/ImagesToPdf/output-1/output.pdf");
    Path expectedOutputPdfFilePath =
        Paths.get("src/test/resources/ImagesToPdf/expected-output-1/output.pdf");

    new ImagesToPdfService()
        .imagesToPdf(
            inputImagesDirPath.toFile(), "A4", "center", "none", outputPdfFilePath.toFile());

    Assertions.assertTrue(
        PdfUtils.pdfEquals(expectedOutputPdfFilePath, outputPdfFilePath),
        String.format(
            "The following PDFs are not equal: '%s', '%s'",
            expectedOutputPdfFilePath, outputPdfFilePath));
  }

  @Test
  void test_imagesToPdf__page_size_LEGAL__image_position_top_left__page_margins_none()
      throws ReflectiveOperationException, IOException {

    Path inputImagesDirPath = Paths.get("src/test/resources/ImagesToPdf/input-2");
    Path outputPdfFilePath = Paths.get("target/test/ImagesToPdf/output-2/output.pdf");
    Path expectedOutputPdfFilePath =
        Paths.get("src/test/resources/ImagesToPdf/expected-output-2/output.pdf");

    new ImagesToPdfService()
        .imagesToPdf(
            inputImagesDirPath.toFile(), "LEGAL", "top-left", "none", outputPdfFilePath.toFile());

    Assertions.assertTrue(
        PdfUtils.pdfEquals(expectedOutputPdfFilePath, outputPdfFilePath),
        String.format(
            "The following PDFs are not equal: '%s', '%s'",
            expectedOutputPdfFilePath, outputPdfFilePath));
  }

  @Test
  void test_imagesToPdf__page_size_LEGAL__image_position_top_left__page_margins_standard()
      throws ReflectiveOperationException, IOException {

    Path inputImagesDirPath = Paths.get("src/test/resources/ImagesToPdf/input-3");
    Path outputPdfFilePath = Paths.get("target/test/ImagesToPdf/output-3/output.pdf");
    Path expectedOutputPdfFilePath =
        Paths.get("src/test/resources/ImagesToPdf/expected-output-3/output.pdf");

    new ImagesToPdfService()
        .imagesToPdf(
            inputImagesDirPath.toFile(),
            "LEGAL",
            "top-left",
            "standard",
            outputPdfFilePath.toFile());

    Assertions.assertTrue(
        PdfUtils.pdfEquals(expectedOutputPdfFilePath, outputPdfFilePath),
        String.format(
            "The following PDFs are not equal: '%s', '%s'",
            expectedOutputPdfFilePath, outputPdfFilePath));
  }

  @Test
  void test_imagesToPdf__exception_is_thrown_if_pages_margins_argument_is_invalid() {

    Path inputImagesDirPath = Paths.get("src/test/resources/ImagesToPdf/input-4");
    Path outputPdfFilePath = Paths.get("target/test/ImagesToPdf/output-4/output.pdf");
    String pageMargins = "foo";

    Exception exception =
        Assertions.assertThrowsExactly(
            IllegalArgumentException.class,
            () ->
                new ImagesToPdfService()
                    .imagesToPdf(
                        inputImagesDirPath.toFile(),
                        "A4",
                        "top-left",
                        pageMargins,
                        outputPdfFilePath.toFile()));

    Assertions.assertEquals(
        String.format("Invalid page margins: '%s'", pageMargins),
        exception.getMessage(),
        "The error message is not as expected.");
  }

  @Test
  void test_imagesToPdf__exception_is_thrown_if_image_position_argument_is_invalid() {

    Path inputImagesDirPath = Paths.get("src/test/resources/ImagesToPdf/input-4");
    Path outputPdfFilePath = Paths.get("target/test/ImagesToPdf/output-4/output.pdf");
    String imagePosition = "foo";

    Exception exception =
        Assertions.assertThrowsExactly(
            IllegalArgumentException.class,
            () ->
                new ImagesToPdfService()
                    .imagesToPdf(
                        inputImagesDirPath.toFile(),
                        "A4",
                        imagePosition,
                        "standard",
                        outputPdfFilePath.toFile()));

    Assertions.assertEquals(
        String.format("Invalid image position: '%s'", imagePosition),
        exception.getMessage(),
        "The error message is not as expected.");
  }
}
