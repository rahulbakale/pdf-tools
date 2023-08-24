package rahulb.pdftools.cmd;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rahulb.pdftools.core.PdfToImageService;

class PdfToImageHandlerTest {

  @Test
  void test_service_invoked_with_correct_arguments() throws Exception {

    String inputPdfFileArg = "one/two.pdf";
    String outputDirArg = "three/four";
    List<Integer> pageNumbersArg = List.of(4, 7, 9);
    String dpiArg = "300";
    String imageFormatArg = "JPEG";

    PdfToImageService mockService = Mockito.mock(PdfToImageService.class);

    new PdfToImageHandler(mockService)
        .executeInternal(
            inputPdfFileArg,
            outputDirArg,
            pageNumbersArg.stream().map(String::valueOf).collect(Collectors.joining(",")),
            dpiArg,
            imageFormatArg);

    Mockito.verify(mockService)
        .pdfToImage(
            Mockito.argThat(inputPdfFile -> new File(inputPdfFileArg).equals(inputPdfFile)),
            Mockito.argThat(outputDir -> new File(outputDirArg).equals(outputDir)),
            Mockito.argThat(pageNumbersArg::equals),
            Mockito.intThat(dpi -> Integer.parseInt(dpiArg) == dpi),
            Mockito.argThat(imageFormatArg::equals));
  }
}
