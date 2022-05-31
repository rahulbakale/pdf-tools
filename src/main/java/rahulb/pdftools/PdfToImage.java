package rahulb.pdftools;

import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

public final class PdfToImage {

    public static void main(String[] args) throws IOException {

        var inputPdfFile = new File("/tmp/sample.pdf");
        var outputDir = new File("/tmp/");
        IntStream pageNumbers = IntStream.of(1);
        int dpi = 300;
        String imageFormat = "jpeg";

        outputDir.mkdirs();

        try (PdfPageImageWriter imageWriter = new PdfPageImageWriter(inputPdfFile)) {
            imageWriter.writePages(pageNumbers, dpi, imageFormat, outputDir, inputPdfFile.getName());
        }
    }
}