package rahulb.pdftools.cmd;

import java.io.File;
import java.util.Map;
import rahulb.pdftools.core.RemovePagesService;

final class RemovePagesHandler extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDF_FILE_PATH = "input-pdf-file";
  private static final String ARG_PAGES_TO_REMOVE = "pages-to-remove";
  private static final String ARG_OUTPUT_PDF_FILE_PATH = "output-pdf-file";
  private final RemovePagesService service;

  RemovePagesHandler(RemovePagesService service) {
    this.service = service;
  }

  @Override
  void executeInternal(String... args) throws Exception {

    Map<?, ?> argMap =
        Map.of(
            ARG_INPUT_PDF_FILE_PATH, args[0],
            ARG_PAGES_TO_REMOVE, args[1],
            ARG_OUTPUT_PDF_FILE_PATH, args[2]);

    executeInternal(argMap);
  }

  @Override
  void executeInternal(Map<?, ?> args) throws Exception {

    File inputPdfFile = new File((String) args.get(ARG_INPUT_PDF_FILE_PATH));
    String pagesToRemove = (String) args.get(ARG_PAGES_TO_REMOVE);
    File outputPdfFile = new File((String) args.get(ARG_OUTPUT_PDF_FILE_PATH));

    service.removePages(inputPdfFile, pagesToRemove, outputPdfFile);
  }
}
