package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

final class PdfPageImageWriter {

    private final PDDocument document;
    private final PDFRenderer renderer;

    PdfPageImageWriter(PDDocument document) {
        this.document = document;
        this.renderer = new PDFRenderer(document);
    }

    void writeAllPages(float dpi, String imageFormat, File outputDir, String outputFileNamePrefix) {
        writePages(i -> true, dpi, imageFormat, outputDir, outputFileNamePrefix);
    }

    void writeFirstPage(float dpi, String imageFormat, File outputDir, String outputFileNamePrefix) {
        IntStream pageNumbers = IntStream.of(1);
        createPageImages(pageNumbers, dpi, imageFormat, outputDir, outputFileNamePrefix);
    }

    void writeLastPage(float dpi, String imageFormat, File outputDir, String outputFileNamePrefix) {
        IntStream pageNumbers = IntStream.of(document.getNumberOfPages());
        createPageImages(pageNumbers, dpi, imageFormat, outputDir, outputFileNamePrefix);
    }

    void writePages(IntPredicate pageNumberPredicate, float dpi, String imageFormat, File outputDir, String outputFileNamePrefix) {
        IntStream pageNumbers = IntStream.rangeClosed(1, document.getNumberOfPages()).filter(pageNumberPredicate);
        writePages(pageNumbers, dpi, imageFormat, outputDir, outputFileNamePrefix);
    }

    void writePages(IntStream pageNumbers, float dpi, String imageFormat, File outputDir, String outputFileNamePrefix) {
        createPageImages(pageNumbers, dpi, imageFormat, outputDir, outputFileNamePrefix);
    }

    private void createPageImages(IntStream pageNumbers, float dpi, String imageFormat, File outputDir, String outputFileNamePrefix) {
        pageNumbers.forEach(pageNumber -> createPageImage(renderer, pageNumber, dpi, imageFormat, outputDir, outputFileNamePrefix));
    }

    private void createPageImage(PDFRenderer renderer, int pageNumber, float dpi, String imageFormat, File outputDir, String pdfFileName) {
        try {
            var pageIndex = pageNumber - 1;
            BufferedImage image = renderer.renderImageWithDPI(pageIndex, dpi, ImageType.RGB);
            var imageFile = new File(outputDir, pdfFileName + '.' + pageNumber + '.' + imageFormat);
            boolean success = ImageIO.write(image, imageFormat, imageFile);
            if (!success) {
                throw new RuntimeException("No appropriate writer found for image format '" + imageFormat + "'");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
