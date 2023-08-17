package rahulb.pdftools.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AddWatermarkTest {

  @Test
  void test_given_watermark_text_is_added_repetitively() throws IOException {

    Path inputPdfPath = Paths.get("src/test/resources/AddWatermark/input-1.pdf");
    Path outputPdfPath = Paths.get("target/test/AddWatermark/output-1.pdf");
    Path expectedOutputPdfPath = Paths.get("src/test/resources/AddWatermark/expected-output-1.pdf");

    AddWatermark.addWatermark(inputPdfPath.toFile(), "CONFIDENTIAL", 20, outputPdfPath.toFile());

    Assertions.assertTrue(
        PdfUtils.pdfEquals(expectedOutputPdfPath, outputPdfPath),
        String.format(
            "The following PDFs are not equal: '%s', '%s'", expectedOutputPdfPath, outputPdfPath));
  }
}
