package rahulb.pdftools.cmd;

import java.io.File;
import java.util.Map;
import rahulb.pdftools.core.AddWatermarkService;

final class AddWatermarkHandler extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
  private static final String ARG_WATERMARK_TEXT = "watermark-text";
  private static final String ARG_WATERMARK_FONT_SIZE = "watermark-font-size";
  private static final String ARG_OUTPUT_PDF_FILE = "output-pdf-file";
  private final AddWatermarkService service;

  AddWatermarkHandler(AddWatermarkService service) {
    this.service = service;
  }

  @Override
  void executeInternal(String... args) throws Exception {

    Map<?, ?> argMap =
        Map.of(
            ARG_INPUT_PDF_FILE, args[0],
            ARG_WATERMARK_TEXT, args[1],
            ARG_WATERMARK_FONT_SIZE, args[2],
            ARG_OUTPUT_PDF_FILE, args[3]);

    executeInternal(argMap);
  }

  @Override
  void executeInternal(Map<?, ?> args) throws Exception {

    File inputPdfFile = new File((String) args.get(ARG_INPUT_PDF_FILE));
    String watermarkText = (String) args.get(ARG_WATERMARK_TEXT);
    int fontSize = Integer.parseInt((String) args.get(ARG_WATERMARK_FONT_SIZE));
    File outputPdfFile = new File((String) args.get(ARG_OUTPUT_PDF_FILE));

    service.addWatermark(inputPdfFile, watermarkText, fontSize, outputPdfFile);
  }
}
