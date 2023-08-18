package rahulb.pdftools.core;

import de.redsix.pdfcompare.PdfComparator;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.pdfbox.pdmodel.PDDocument;

final class PdfUtils {

  static boolean pdfEquals(Path pdf1, Path pdf2) {

    try {
      return pdfEquals(
          new ByteArrayInputStream(Files.readAllBytes(pdf1)),
          new ByteArrayInputStream(Files.readAllBytes(pdf2)));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  static boolean pdfEquals(Path pdf1, PDDocument pdf2) throws IOException {

    return pdfEquals(
        new ByteArrayInputStream(Files.readAllBytes(pdf1)),
        new ByteArrayInputStream(pdDocumentToBytes(pdf2)));
  }

  private static boolean pdfEquals(InputStream pdf1, InputStream pdf2) throws IOException {

    var pdfcomparator = new PdfComparator<>(pdf1, pdf2);
    return pdfcomparator.compare().isEqual();
  }

  private static byte[] pdDocumentToBytes(PDDocument pdDocument) throws IOException {

    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      pdDocument.save(baos);
      return baos.toByteArray();
    }
  }
}
