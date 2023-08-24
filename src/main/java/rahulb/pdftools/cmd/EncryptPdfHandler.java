package rahulb.pdftools.cmd;

import java.io.File;
import java.util.Map;
import rahulb.pdftools.core.EncryptPdfService;

final class EncryptPdfHandler extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
  private static final String ARG_OUTPUT_PDF_FILE = "output-pdf-file";
  private final EncryptPdfService service;

  EncryptPdfHandler(EncryptPdfService service) {
    this.service = service;
  }

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

    char[] docOpenPassword = readDocOpenPassword();

    char[] permissionsChangePassword = readPermissionsChangePassword();

    service.encryptPdf(
        new File(inputPdfFile),
        docOpenPassword,
        permissionsChangePassword,
        new File(outputPdfFile));
  }

  char[] readPermissionsChangePassword() {

    return System.console()
        .readPassword(
            "Enter the password required to change the accessPermission of the document:");
  }

  char[] readDocOpenPassword() {

    return System.console().readPassword("Enter the password required to open the document:");
  }
}
