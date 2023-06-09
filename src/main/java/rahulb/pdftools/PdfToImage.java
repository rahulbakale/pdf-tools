package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;

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

        pdfToImage(inputPdfFile, outputDir, pageNumbers, dpi, imageFormat);
    }

    static void pdfToImage(File inputPdfFile, File outputDir, IntStream pageNumbers, int dpi, String imageFormat) throws IOException {

        try (var document = PDDocument.load(inputPdfFile)) {
            pdfToImage(inputPdfFile, outputDir, pageNumbers, dpi, imageFormat, document);
        }
    }

    private static void pdfToImage(File inputPdfFile, File outputDir, IntStream pageNumbers, int dpi, String imageFormat, PDDocument document) {

        //noinspection ResultOfMethodCallIgnored
        outputDir.mkdirs();

        PdfPageImageWriter imageWriter = new PdfPageImageWriter(document);

        imageWriter.writePages(pageNumbers, dpi, imageFormat, outputDir, inputPdfFile.getName());
    }
}