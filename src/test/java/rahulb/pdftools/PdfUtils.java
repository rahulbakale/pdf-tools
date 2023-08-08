package rahulb.pdftools;

import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

final class PdfUtils {

    static byte[] toBytes(PDDocument pdfDoc) throws IOException {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            pdfDoc.save(baos);
            return baos.toByteArray();
        }
    }

    static boolean pdfEquals(Path pdf1, Path pdf2) {

        try {
            return pdfEquals(
                    new ByteArrayInputStream(Files.readAllBytes(pdf1)),
                    new ByteArrayInputStream(Files.readAllBytes(pdf2))
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean pdfEquals(File pdf1, byte[] pdf2) throws IOException {

        return pdfEquals(
                new ByteArrayInputStream(Files.readAllBytes(pdf1.toPath())),
                new ByteArrayInputStream(pdf2)
        );
    }

    static boolean pdfEquals(byte[] pdf1, byte[] pdf2) throws IOException {

        return pdfEquals(
                new ByteArrayInputStream(pdf1),
                new ByteArrayInputStream(pdf2)
        );
    }

    static boolean pdfEquals(InputStream pdf1, byte[] pdf2) throws IOException {

        return pdfEquals(
                pdf1,
                new ByteArrayInputStream(pdf2)
        );
    }

    private static boolean pdfEquals(InputStream pdf1, InputStream pdf2) throws IOException {

        var pdfcomparator = new PdfComparator<>(pdf1, pdf2);
        return pdfcomparator.compare().isEqual();
    }
}
