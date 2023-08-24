package rahulb.pdftools.cmd;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rahulb.pdftools.core.ConvertToGrayscaleService;

class ConvertToGrayscaleHandlerTest {

  @Test
  void test_service_invoked_with_correct_arguments() throws Exception {

    String inputPdfFileArg = "one/two.pdf";
    String dpiArg = "300";
    String outputPageSizeArg = "A4";
    String outputPdfFileArg = "three/four.pdf";

    ConvertToGrayscaleService mockService = Mockito.mock(ConvertToGrayscaleService.class);

    new ConvertToGrayscaleHandler(mockService)
        .executeInternal(inputPdfFileArg, dpiArg, outputPageSizeArg, outputPdfFileArg);

    Mockito.verify(mockService)
        .convertToGrayscale(
            Mockito.argThat(inputPdfFile -> new File(inputPdfFileArg).equals(inputPdfFile)),
            Mockito.floatThat(dpi -> Float.parseFloat(dpiArg) == dpi),
            Mockito.argThat(outputPageSizeArg::equals),
            Mockito.argThat(outputPdfFile -> new File(outputPdfFileArg).equals(outputPdfFile)));
  }
}
