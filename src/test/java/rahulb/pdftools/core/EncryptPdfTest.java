package rahulb.pdftools.core;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EncryptPdfTest {

  @Test
  void test_document_is_encrypted_properly() throws IOException {

    Path inputPdfPath = Paths.get("src/test/resources/EncryptPdf/input-1.pdf");
    Path outputPdfPath = Paths.get("target/test/EncryptPdf/output-1.pdf");

    String docOpenPassword = "Password0#";
    String permissionsChangePassword = "Password1#";

    EncryptPdf.encryptPdf(
        inputPdfPath.toFile(),
        docOpenPassword.toCharArray(),
        permissionsChangePassword.toCharArray(),
        outputPdfPath.toFile());

    assertPdfCantBeOpenedWithoutPassword(outputPdfPath);
    assertPdfCantBeOpenedWithWrongPassword(outputPdfPath, "WrongPassword0#");
    assertPdfCanBeOpenedWithCorrectPassword(outputPdfPath, docOpenPassword);

    assertOutputPdfContentIsEqualToInputPdfContent(outputPdfPath, docOpenPassword, inputPdfPath);
  }

  private static void assertPdfCantBeOpenedWithoutPassword(Path pdfPath) {

    InvalidPasswordException exceptionThrown =
        Assertions.assertThrowsExactly(
            InvalidPasswordException.class, () -> tryOpenPdfWithoutPassword(pdfPath));

    Assertions.assertEquals(
        "Cannot decrypt PDF, the password is incorrect", exceptionThrown.getMessage());
  }

  private static void assertPdfCantBeOpenedWithWrongPassword(
      Path pdfPath, String wrongDocOpenPassword) {

    InvalidPasswordException exceptionThrown =
        Assertions.assertThrowsExactly(
            InvalidPasswordException.class,
            () -> tryOpenPdfWithPassword(pdfPath, wrongDocOpenPassword));

    Assertions.assertEquals(
        "Cannot decrypt PDF, the password is incorrect", exceptionThrown.getMessage());
  }

  private static void assertPdfCanBeOpenedWithCorrectPassword(
      Path pdfPath, String correctDocOpenPassword) {

    Assertions.assertDoesNotThrow(() -> tryOpenPdfWithPassword(pdfPath, correctDocOpenPassword));
  }

  private static void assertOutputPdfContentIsEqualToInputPdfContent(
      Path outputPdfPath, String docOpenPassword, Path inputPdfPath) throws IOException {

    try (PDDocument docWithoutEncryption = removePdfEncryption(outputPdfPath, docOpenPassword)) {

      Assertions.assertTrue(
          PdfUtils.pdfEquals(inputPdfPath, docWithoutEncryption),
          "The decrypted version of the output PDF is not equal to the input PDF.");
    }
  }

  private static void tryOpenPdfWithoutPassword(Path pdfPath) throws IOException {

    PDDocument ignored = PDDocument.load(pdfPath.toFile());
    ignored.close();
  }

  private static void tryOpenPdfWithPassword(Path pdfPath, String docOpenPassword)
      throws IOException {

    PDDocument ignored = PDDocument.load(pdfPath.toFile(), docOpenPassword);
    ignored.close();
  }

  private static PDDocument removePdfEncryption(Path pdfPath, String docOpenPassword)
      throws IOException {

    PDDocument doc = PDDocument.load(pdfPath.toFile(), docOpenPassword);

    try {
      doc.setAllSecurityToBeRemoved(true); // remove encryption

    } catch (Exception encryptionRemovalError) {

      try {
        doc.close();
      } catch (Throwable docClosingError) {
        encryptionRemovalError.addSuppressed(docClosingError);
      }

      throw encryptionRemovalError;
    }

    return doc;
  }
}
