package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;

public final class PdfToImage {

    public static void main(String[] args) throws IOException {

        pdfToImage("/tmp/sample.pdf", "/tmp/", "1", "300", "jpeg");
    }

    static void pdfToImage(String... args) throws IOException {

        File inputPdfFile = new File(args[0]);
        File outputDir = new File(args[1]);
        IntStream pageNumbers = Arrays.stream(args[2].split(",")).mapToInt(Integer::parseInt);
        int dpi = Integer.parseInt(args[3]);
        String imageFormat = args[4];

        pdfToImage(inputPdfFile, outputDir, pageNumbers, dpi, imageFormat);
    }

    private static void pdfToImage(File inputPdfFile, File outputDir, IntStream pageNumbers, int dpi, String imageFormat) throws IOException {

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