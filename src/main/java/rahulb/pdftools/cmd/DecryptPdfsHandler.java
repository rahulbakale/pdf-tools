package rahulb.pdftools.cmd;

import java.nio.file.Paths;
import java.util.Map;
import rahulb.pdftools.core.DecryptPdfs;

final class DecryptPdfsHandler extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDFS_DIRECTORY = "input-pdfs-directory";
  private static final String ARG_OUTPUT_PDFS_DIRECTORY = "output-pdfs-directory";

  @Override
  void executeInternal(String... args) {

    Map<String, String> argMap =
        Map.of(
            ARG_INPUT_PDFS_DIRECTORY, args[0],
            ARG_OUTPUT_PDFS_DIRECTORY, args[1]);

    executeInternal(argMap);
  }

  @Override
  void executeInternal(Map<?, ?> args) {

    String inputPdfsDir = (String) args.get(ARG_INPUT_PDFS_DIRECTORY);
    String outputPdfsDir = (String) args.get(ARG_OUTPUT_PDFS_DIRECTORY);

    char[] docOpenPassword =
        System.console().readPassword("Enter the password required to open the document:");

    DecryptPdfs.decryptPdfs(Paths.get(inputPdfsDir), Paths.get(outputPdfsDir), docOpenPassword);
  }
}
