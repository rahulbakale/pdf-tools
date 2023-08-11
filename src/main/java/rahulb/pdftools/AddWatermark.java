package rahulb.pdftools;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.pdfbox.multipdf.Overlay;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;

final class AddWatermark {

  private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
  private static final String ARG_WATERMARK_TEXT = "watermark-text";
  private static final String ARG_WATERMARK_FONT_SIZE = "watermark-font-size";
  private static final String ARG_OUTPUT_PDF_FILE = "output-pdf-file";

  private AddWatermark() {}

  static void addWatermark(String... args) throws IOException {

    Map<?, ?> argMap =
        Map.of(
            ARG_INPUT_PDF_FILE, args[0],
            ARG_WATERMARK_TEXT, args[1],
            ARG_WATERMARK_FONT_SIZE, args[2],
            ARG_OUTPUT_PDF_FILE, args[3]);

    addWatermark(argMap);
  }

  static void addWatermark(Map<?, ?> args) throws IOException {

    File inputPdfFile = new File((String) args.get(ARG_INPUT_PDF_FILE));
    String watermarkText = (String) args.get(ARG_WATERMARK_TEXT);
    int fontSize = Integer.parseInt((String) args.get(ARG_WATERMARK_FONT_SIZE));
    File outputPdfFile = new File((String) args.get(ARG_OUTPUT_PDF_FILE));

    addWatermark(inputPdfFile, watermarkText, fontSize, outputPdfFile);
  }

  private static void addWatermark(
      File inputPdfFile, String watermarkText, int fontSize, File outputPdfFile)
      throws IOException {

    try (var document = PDDocument.load(inputPdfFile)) {

      addWatermark(watermarkText, fontSize, document);

      //noinspection ResultOfMethodCallIgnored
      outputPdfFile.getParentFile().mkdirs();

      document.save(outputPdfFile);
    }
  }

  private static void addWatermark(String watermarkText, int fontSize, PDDocument document)
      throws IOException {

    PDType1Font font = PDType1Font.HELVETICA_BOLD;
    Color color = Color.LIGHT_GRAY;

    float watermarkTextWidth = (font.getStringWidth(watermarkText) / 1000) * fontSize;
    float watermarkTextHeight = (font.getFontDescriptor().getCapHeight() / 1000) * fontSize;
    float watermarkGapX =
        ((font.getSpaceWidth() / 1000) * fontSize) * 3; /*Gap of three characters*/
    float watermarkGapY = watermarkTextHeight * 1.5f;

    PDExtendedGraphicsState exGraphicsState;
    {
      // Transparency

      // https://issues.apache.org/jira/browse/PDFBOX-1176
      // https://github.com/TomRoush/PdfBox-Android/issues/73

      exGraphicsState = new PDExtendedGraphicsState();
      exGraphicsState.setNonStrokingAlphaConstant(0.5f);
    }

    for (var page : document.getPages()) {

      float pageWidth = page.getMediaBox().getWidth();
      float pageHeight = page.getMediaBox().getHeight();

      try (var contentStream =
          new PDPageContentStream(
              document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {

        contentStream.setFont(font, fontSize);
        contentStream.setNonStrokingColor(color);
        contentStream.setGraphicsStateParameters(exGraphicsState); // Transparency

        float tx = 0;
        float ty = 0;

        while (ty < pageHeight) {

          contentStream.beginText();

          Matrix textMatrix = Matrix.getRotateInstance(Math.toRadians(45), tx, ty);
          contentStream.setTextMatrix(textMatrix);
          contentStream.newLineAtOffset(0, 0);
          // contentStream.newLineAtOffset(tx, ty);
          contentStream.showText(watermarkText);
          contentStream.endText();

          tx = tx + watermarkTextWidth + watermarkGapX;

          if (tx > pageWidth) {
            ty = ty + watermarkTextHeight + watermarkGapY;
            tx = 0;
          }
        }

        /*
            To place the watermark at the center of the page:

            float watermarkTextX = (pageWidth - watermarkTextWidth) / 2;
            float watermarkTextY = (pageHeight - watermarkTextHeight) / 2;
            contentStream.beginText();
            contentStream.setFont(font, fontSize);
            contentStream.setNonStrokingColor(Color.DARK_GRAY);
            contentStream.newLineAtOffset(watermarkTextX, watermarkTextY);
            contentStream.showText(watermarkText);
            contentStream.endText();
        */
      }
    }
  }

  // TODO - Provide a command line interface for this capability
  static void addWatermark(File inputPdfFile, File watermarkPdfFile, File outputPdfFile)
      throws IOException {

    try (var overlayDoc = PDDocument.load(watermarkPdfFile);
        var document = PDDocument.load(inputPdfFile)) {

      Map<Integer, PDDocument> overlayDocs =
          IntStream.rangeClosed(1, document.getNumberOfPages())
              .boxed()
              .collect(Collectors.toMap(pageNumber -> pageNumber, pageNumber -> overlayDoc));

      try (var overlay = new Overlay()) {
        overlay.setInputPDF(document);
        overlay.setOverlayPosition(Overlay.Position.FOREGROUND);
        overlay.overlayDocuments(overlayDocs).save(outputPdfFile);
      }
    }
  }
}
