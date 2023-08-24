package rahulb.pdftools.cmd;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import rahulb.pdftools.core.PdfToImageService;

final class PdfToImageHandler extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
  private static final String ARG_PAGE_NUMBERS = "page-numbers";
  private static final String ARG_DPI = "dpi";
  private static final String ARG_IMAGE_FORMAT = "image-format";
  private static final String ARG_OUTPUT_DIRECTORY = "output-directory";
  private final PdfToImageService service;

  PdfToImageHandler(PdfToImageService service) {
    this.service = service;
  }

  @Override
  void executeInternal(String... args) throws Exception {

    Map<?, ?> argMap =
        Map.of(
            ARG_INPUT_PDF_FILE, args[0],
            ARG_OUTPUT_DIRECTORY, args[1],
            ARG_PAGE_NUMBERS, args[2],
            ARG_DPI, args[3],
            ARG_IMAGE_FORMAT, args[4]);

    executeInternal(argMap);
  }

  @Override
  void executeInternal(Map<?, ?> args) throws Exception {

    File inputPdfFile = new File((String) args.get(ARG_INPUT_PDF_FILE));
    File outputDir = new File((String) args.get(ARG_OUTPUT_DIRECTORY));
    List<Integer> pageNumbers =
        Arrays.stream(((String) args.get(ARG_PAGE_NUMBERS)).split(","))
            .map(Integer::parseInt)
            .toList();
    int dpi = Integer.parseInt((String) args.get(ARG_DPI));
    String imageFormat = (String) args.get(ARG_IMAGE_FORMAT);

    service.pdfToImage(inputPdfFile, outputDir, pageNumbers, dpi, imageFormat);
  }
}
