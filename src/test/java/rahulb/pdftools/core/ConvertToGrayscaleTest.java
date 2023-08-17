package rahulb.pdftools.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ConvertToGrayscaleTest {

  @Test
  void test_conversion_to_grayscale_is_proper()
      throws IOException, NoSuchFieldException, IllegalAccessException {

    Path inputPdfPath = Paths.get("src/test/resources/ConvertToGrayscale/input-1.pdf");
    Path outputPdfPath = Paths.get("target/test/ConvertToGrayscale/output-1.pdf");
    Path expectedOutputPdfPath =
        Paths.get("src/test/resources/ConvertToGrayscale/expected-output-1.pdf");

    ConvertToGrayscale.convertToGrayscale(
        inputPdfPath.toFile(), 300.0f, "A4", outputPdfPath.toFile());

    Assertions.assertTrue(
        PdfUtils.pdfEquals(expectedOutputPdfPath, outputPdfPath),
        String.format(
            "The following PDFs are not equal: '%s', '%s'", expectedOutputPdfPath, outputPdfPath));
  }
}
