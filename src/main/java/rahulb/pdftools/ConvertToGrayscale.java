package rahulb.pdftools;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.util.Matrix;

final class ConvertToGrayscale {

  private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
  private static final String ARG_DPI = "dpi";
  private static final String ARG_OUTPUT_PAGE_SIZE = "output-page-size";
  private static final String ARG_OUTPUT_PDF_FILE = "output-pdf-file";

  private ConvertToGrayscale() {}

  static void convertToGrayscale(String... args)
      throws IOException, NoSuchFieldException, IllegalAccessException {

    Map<?, ?> argMap =
        Map.of(
            ARG_INPUT_PDF_FILE, args[0],
            ARG_DPI, args[1],
            ARG_OUTPUT_PAGE_SIZE, args[2],
            ARG_OUTPUT_PDF_FILE, args[3]);

    convertToGrayscale(argMap);
  }

  static void convertToGrayscale(Map<?, ?> args)
      throws IOException, NoSuchFieldException, IllegalAccessException {

    File inputPdfFile = new File((String) args.get(ARG_INPUT_PDF_FILE));
    float dpi = Float.parseFloat((String) args.get(ARG_DPI));
    String outputPageSize = (String) args.get(ARG_OUTPUT_PAGE_SIZE);
    File outputPdfFile = new File((String) args.get(ARG_OUTPUT_PDF_FILE));

    convertToGrayscale(inputPdfFile, dpi, outputPageSize, outputPdfFile);
  }

  private static void convertToGrayscale(
      File inputPdfFile, float dpi, String outputPageSize, File outputPdfFile)
      throws IOException, NoSuchFieldException, IllegalAccessException {

    try (var inDoc = PDDocument.load(inputPdfFile);
        var outDoc = new PDDocument()) {

      convertToGrayscale(dpi, outputPageSize, inDoc, outDoc);

      //noinspection ResultOfMethodCallIgnored
      outputPdfFile.getParentFile().mkdirs();

      outDoc.save(outputPdfFile);
    }
  }

  private static void convertToGrayscale(
      float dpi, String outputPageSize, PDDocument inDoc, PDDocument outDoc)
      throws IllegalAccessException, NoSuchFieldException, IOException {

    PDRectangle requiredPageSize =
        (PDRectangle) PDRectangle.class.getField(outputPageSize).get(null);

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
                requiredWidth / pageImage.getWidth(), requiredHeight / pageImage.getHeight()));
        contentStream.drawImage(imageXObject, 0, 0);
      }
    }
  }
}
