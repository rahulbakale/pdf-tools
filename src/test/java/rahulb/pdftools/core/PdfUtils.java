package rahulb.pdftools.core;

import de.redsix.pdfcompare.PdfComparator;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

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

  private static boolean pdfEquals(InputStream pdf1, InputStream pdf2) throws IOException {

    var pdfcomparator = new PdfComparator<>(pdf1, pdf2);
    return pdfcomparator.compare().isEqual();
  }
}
