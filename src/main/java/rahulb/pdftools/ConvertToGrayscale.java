package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.io.IOException;

final class ConvertToGrayscale {

    static void convertToGrayscale(File inputPdfFile, float dpi, String outputPageSize, File outputPdfFile) throws IOException, NoSuchFieldException, IllegalAccessException {

        try (var inDoc = PDDocument.load(inputPdfFile);
             var outDoc = new PDDocument()) {

            convertToGrayscale(dpi, outputPageSize, inDoc, outDoc);

            //noinspection ResultOfMethodCallIgnored
            outputPdfFile.getParentFile().mkdirs();

            outDoc.save(outputPdfFile);
        }
    }

    private static void convertToGrayscale(float dpi, String outputPageSize, PDDocument inDoc, PDDocument outDoc) throws IllegalAccessException, NoSuchFieldException, IOException {

        PDRectangle requiredPageSize = (PDRectangle) PDRectangle.class.getField(outputPageSize).get(null);

        float requiredWidth = requiredPageSize.getWidth();
        float requiredHeight = requiredPageSize.getHeight();

        var pdfRenderer = new PDFRenderer(inDoc);

        for (int pageIndex = 0; pageIndex < inDoc.getNumberOfPages(); pageIndex++) {

            var pageImage = pdfRenderer.renderImageWithDPI(pageIndex, dpi, ImageType.GRAY);

            // In one experiment, replacing LosslessFactory with JPEGFactory
            // reduced file size by about 70%.
            PDImageXObject imageXObject = JPEGFactory.createFromImage(outDoc, pageImage);

            var outPage = new PDPage(requiredPageSize);
            outDoc.addPage(outPage);

            try (var contentStream = new PDPageContentStream(outDoc, outPage)) {
                contentStream.transform(
                        Matrix.getScaleInstance(
                                requiredWidth / pageImage.getWidth(),
                                requiredHeight / pageImage.getHeight())
                );
                contentStream.drawImage(imageXObject, 0, 0);
            }
        }
    }
}
