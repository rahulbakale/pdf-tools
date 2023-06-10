package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class RemovePagesTest {

    @Test
    void test_removePages_simple_predicate() throws IOException {

        byte[] inputPdfBytes = IoUtils.getResourceAsBytes("/RemovePages/input.pdf");

        try (var pdfDoc = PDDocument.load(inputPdfBytes);
             var expectedOutputPdfInputStream = IoUtils.getResourceAsStream("/RemovePages/expected-output.pdf")) {

            RemovePages.removePages(
                    pdfDoc,
                    "1,2,3,5,8,13"
            );

            byte[] actualOutputPdfBytes = PdfUtils.toBytes(pdfDoc);

            Assertions.assertFalse(PdfUtils.pdfEquals(inputPdfBytes, actualOutputPdfBytes));

            Assertions.assertTrue(PdfUtils.pdfEquals(expectedOutputPdfInputStream, actualOutputPdfBytes));
        }
    }

    @Test
    void test_removePages_not_predicate() throws IOException {

        byte[] inputPdfBytes = IoUtils.getResourceAsBytes("/RemovePages/input.pdf");

        try (var pdfDoc = PDDocument.load(inputPdfBytes);
             var expectedOutputPdfInputStream = IoUtils.getResourceAsStream("/RemovePages/expected-output.pdf")) {

            RemovePages.removePages(
                    pdfDoc,
                    "keep:4,6,7,9,10,11,12,14,15,16,17,18,19,20"
            );

            byte[] actualOutputPdfBytes = PdfUtils.toBytes(pdfDoc);

            Assertions.assertFalse(PdfUtils.pdfEquals(inputPdfBytes, actualOutputPdfBytes));

            Assertions.assertTrue(PdfUtils.pdfEquals(expectedOutputPdfInputStream, actualOutputPdfBytes));
        }
    }
}