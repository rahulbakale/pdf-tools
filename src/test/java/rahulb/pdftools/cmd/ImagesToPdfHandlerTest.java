package rahulb.pdftools.cmd;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rahulb.pdftools.core.ImagesToPdfService;

class ImagesToPdfHandlerTest {

  @Test
  void test_service_invoked_with_correct_arguments() throws Exception {

    String inputImagesDirArg = "one/two";
    String outputPageSizeArg = "A4";
    String imagePositionArg = "center";
    String pageMarginsArg = "standard";
    String outputPdfFileArg = "three/four.pdf";

    ImagesToPdfService mockService = Mockito.mock(ImagesToPdfService.class);

    new ImagesToPdfHandler(mockService)
        .executeInternal(
            inputImagesDirArg,
            outputPageSizeArg,
            imagePositionArg,
            pageMarginsArg,
            outputPdfFileArg);

    Mockito.verify(mockService)
        .imagesToPdf(
            Mockito.argThat(inputImagesDir -> new File(inputImagesDirArg).equals(inputImagesDir)),
            Mockito.argThat(outputPageSizeArg::equals),
            Mockito.argThat(imagePositionArg::equals),
            Mockito.argThat(pageMarginsArg::equals),
            Mockito.argThat(outputPdfFile -> new File(outputPdfFileArg).equals(outputPdfFile)));
  }
}
