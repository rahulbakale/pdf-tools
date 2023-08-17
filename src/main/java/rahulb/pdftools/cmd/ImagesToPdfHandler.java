package rahulb.pdftools.cmd;

import java.io.File;
import java.util.Map;
import rahulb.pdftools.core.ImagesToPdf;

final class ImagesToPdfHandler extends AbstractCommandHandler {

  private static final String ARG_INPUT_IMAGES_DIRECTORY = "input-images-directory";
  private static final String ARG_OUTPUT_PAGE_SIZE = "output-page-size";
  private static final String ARG_IMAGE_POSITION = "image-position";
  private static final String ARG_PAGE_MARGINS = "page-margins";
  private static final String ARG_OUTPUT_PDF_FILE_PATH = "output-pdf-file";

  @Override
  void executeInternal(String... args) throws Exception {

    Map<?, ?> argMap =
        Map.of(
            ARG_INPUT_IMAGES_DIRECTORY, args[0],
            ARG_OUTPUT_PAGE_SIZE, args[1],
            ARG_IMAGE_POSITION, args[2],
            ARG_PAGE_MARGINS, args[3],
            ARG_OUTPUT_PDF_FILE_PATH, args[4]);

    executeInternal(argMap);
  }

  @Override
  void executeInternal(Map<?, ?> args) throws Exception {

    File inputImagesDir = new File((String) args.get(ARG_INPUT_IMAGES_DIRECTORY));
    String outputPageSize = (String) args.get(ARG_OUTPUT_PAGE_SIZE);
    String imagePosition = (String) args.get(ARG_IMAGE_POSITION);
    String pageMargins = (String) args.get(ARG_PAGE_MARGINS);
    File outputPdfFile = new File((String) args.get(ARG_OUTPUT_PDF_FILE_PATH));

    ImagesToPdf.imagesToPdf(
        inputImagesDir, outputPageSize, imagePosition, pageMargins, outputPdfFile);
  }
}
