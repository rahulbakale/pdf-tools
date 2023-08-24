package rahulb.pdftools.cmd;

import java.io.File;
import java.util.Map;
import rahulb.pdftools.core.ConvertToGrayscaleService;

final class ConvertToGrayscaleHandler extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
  private static final String ARG_DPI = "dpi";
  private static final String ARG_OUTPUT_PAGE_SIZE = "output-page-size";
  private static final String ARG_OUTPUT_PDF_FILE = "output-pdf-file";
  private final ConvertToGrayscaleService service;

  ConvertToGrayscaleHandler(ConvertToGrayscaleService service) {
    this.service = service;
  }

  @Override
  void executeInternal(String... args) throws Exception {

    Map<?, ?> argMap =
        Map.of(
            ARG_INPUT_PDF_FILE, args[0],
            ARG_DPI, args[1],
            ARG_OUTPUT_PAGE_SIZE, args[2],
            ARG_OUTPUT_PDF_FILE, args[3]);

    executeInternal(argMap);
  }

  @Override
  void executeInternal(Map<?, ?> args) throws Exception {

    File inputPdfFile = new File((String) args.get(ARG_INPUT_PDF_FILE));
    float dpi = Float.parseFloat((String) args.get(ARG_DPI));
    String outputPageSize = (String) args.get(ARG_OUTPUT_PAGE_SIZE);
    File outputPdfFile = new File((String) args.get(ARG_OUTPUT_PDF_FILE));

    service.convertToGrayscale(inputPdfFile, dpi, outputPageSize, outputPdfFile);
  }
}
