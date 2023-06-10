package rahulb.pdftools;

import de.redsix.pdfcompare.PdfComparator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;

class RemovePagesTest {

    @Test
    void removePages() throws URISyntaxException, IOException {

        File inputPdfFile = new File(RemovePagesTest.class.getResource("/RemovePages/input.pdf").toURI());

        byte[] pdfBytes;
        try (var pdfDoc = PDDocument.load(inputPdfFile)) {

            RemovePages.removePages(
                    pdfDoc,
                    "1,2,3,5,8,13"
            );

            pdfBytes = toBytes(pdfDoc);
        }

        try (InputStream expectedOutputPdfInputStream = RemovePagesTest.class.getResourceAsStream("/RemovePages/expected-output.pdf")) {

            var pdfcomparator = new PdfComparator<>(
                    expectedOutputPdfInputStream,
                    new ByteArrayInputStream(pdfBytes));

            Assertions.assertTrue(pdfcomparator.compare().isEqual());
        }
    }

    private static byte[] toBytes(PDDocument pdfDoc) throws IOException {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            pdfDoc.save(baos);
            return baos.toByteArray();
        }
    }
}