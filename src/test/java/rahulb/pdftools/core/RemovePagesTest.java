package rahulb.pdftools.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RemovePagesTest {

  @Test
  void test_removePages_simple_predicate() throws IOException {

    Path inputPdfPath = Paths.get("src/test/resources/RemovePages/input.pdf");
    Path outputPdfPath = Paths.get("target/test/RemovePages/output-1.pdf");
    Path expectedOutputPdfPath = Paths.get("src/test/resources/RemovePages/expected-output.pdf");

    RemovePages.removePages(inputPdfPath.toFile(), "1,2,3,5,8,13", outputPdfPath.toFile());

    Assertions.assertTrue(
        PdfUtils.pdfEquals(expectedOutputPdfPath, outputPdfPath),
        String.format(
            "The following PDFs are not equal: '%s', '%s'", expectedOutputPdfPath, outputPdfPath));
  }

  @Test
  void test_removePages_not_predicate() throws IOException {

    Path inputPdfPath = Paths.get("src/test/resources/RemovePages/input.pdf");
    Path outputPdfPath = Paths.get("target/test/RemovePages/output-2.pdf");
    Path expectedOutputPdfPath = Paths.get("src/test/resources/RemovePages/expected-output.pdf");

    RemovePages.removePages(
        inputPdfPath.toFile(),
        "keep:4,6,7,9,10,11,12,14,15,16,17,18,19,20",
        outputPdfPath.toFile());

    Assertions.assertTrue(
        PdfUtils.pdfEquals(expectedOutputPdfPath, outputPdfPath),
        String.format(
            "The following PDFs are not equal: '%s', '%s'", expectedOutputPdfPath, outputPdfPath));
  }

  @Test
  void test_exception_is_thrown_when_predicate_type_is_invalid() {

    Throwable thrownException =
        Assertions.assertThrowsExactly(
            IllegalArgumentException.class,
            () -> RemovePages.removePages(null, /*irrelevant*/ "remove:2,3", null /*irrelevant*/));

    Assertions.assertEquals(
        String.format("Invalid predicate type: '%s'", "remove"), thrownException.getMessage());
  }
}
