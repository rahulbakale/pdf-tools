package rahulb.pdftools;

import de.redsix.pdfcompare.CompareResultImpl;
import de.redsix.pdfcompare.PdfComparator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

class RemovePagesTest {

    @Test
    void removePages() throws IOException {

        File inputPdfFile = new File(System.getProperty("user.dir"), "src/test/resources/RemovePages/input.pdf");
        File outputPdfFile = new File("target/test/RemovePages/output.pdf");

        RemovePages.removePages(
                inputPdfFile,
                IntStream.of(2, 4),
                outputPdfFile
        );

        CompareResultImpl comparisonResult = new PdfComparator<>(
                new File(System.getProperty("user.dir"), "src/test/resources/RemovePages/expected-output.pdf"),
                outputPdfFile
        ).compare();

        Assertions.assertTrue(comparisonResult.isEqual());
    }
}