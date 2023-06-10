package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;

public final class PdfToImage {

    private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
    private static final String ARG_PAGE_NUMBERS = "page-numbers";
    private static final String ARG_DPI = "dpi";
    private static final String ARG_IMAGE_FORMAT = "image-format";
    private static final String ARG_OUTPUT_DIRECTORY = "output-directory";

    public static void main(String[] args) throws IOException {

        pdfToImage("/tmp/sample.pdf", "/tmp/", "1", "300", "jpeg");
    }

    static void pdfToImage(String... args) throws IOException {

        Map<?, ?> argMap = Map.of(
                ARG_INPUT_PDF_FILE, args[0],
                ARG_OUTPUT_DIRECTORY, args[1],
                ARG_PAGE_NUMBERS, args[2],
                ARG_DPI, args[3],
                ARG_IMAGE_FORMAT, args[4]
        );

        pdfToImage(argMap);
    }

    static void pdfToImage(Map<?,?> args) throws IOException {

        File inputPdfFile = new File((String) args.get(ARG_INPUT_PDF_FILE));
        File outputDir = new File((String) args.get(ARG_OUTPUT_DIRECTORY));
        IntStream pageNumbers = Arrays.stream(((String) args.get(ARG_PAGE_NUMBERS)).split(",")).mapToInt(Integer::parseInt);
        int dpi = Integer.parseInt((String) args.get(ARG_DPI));
        String imageFormat = (String) args.get(ARG_IMAGE_FORMAT);

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