package rahulb.pdftools.cmd;

import java.io.File;
import java.util.Map;
import rahulb.pdftools.core.EncryptPdf;

final class EncryptPdfHandler extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
  private static final String ARG_OUTPUT_PDF_FILE = "output-pdf-file";

  @Override
  void executeInternal(String... args) throws Exception {

    Map<String, String> argMap =
        Map.of(
            ARG_INPUT_PDF_FILE, args[0],
            ARG_OUTPUT_PDF_FILE, args[1]);

    executeInternal(argMap);
  }

  @Override
  void executeInternal(Map<?, ?> args) throws Exception {

    String inputPdfFile = (String) args.get(ARG_INPUT_PDF_FILE);
    String outputPdfFile = (String) args.get(ARG_OUTPUT_PDF_FILE);

    EncryptPdf.encryptPdf(new File(inputPdfFile), new File(outputPdfFile));
  }
}
