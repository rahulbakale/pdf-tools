package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

class RemovePagesTest {

    @Test
    void removePages() throws URISyntaxException, IOException {

        File inputPdfFile = new File(RemovePagesTest.class.getResource("/RemovePages/input.pdf").toURI());

        try (var pdfDoc = PDDocument.load(inputPdfFile);
             InputStream expectedOutputPdfInputStream = IoUtils.getResourceAsStream("/RemovePages/expected-output.pdf")) {

            RemovePages.removePages(
                    pdfDoc,
                    "1,2,3,5,8,13"
            );

            byte[] actualOutputPdfBytes = PdfUtils.toBytes(pdfDoc);

            Assertions.assertFalse(PdfUtils.pdfEquals(inputPdfFile, actualOutputPdfBytes));

            Assertions.assertTrue(PdfUtils.pdfEquals(expectedOutputPdfInputStream, actualOutputPdfBytes));
        }
    }
}