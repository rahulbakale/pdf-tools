package rahulb.pdftools.cmd;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rahulb.pdftools.core.AddWatermarkService;

class AddWatermarkHandlerTest {

  @Test
  void test_service_invoked_with_correct_arguments() throws Exception {

    String inputPdfFileArg = "one/two.pdf";
    String watermarkTextArg = "CLASSIFIED";
    String fontSizeArg = "13";
    String outputPdfFileArg = "three/four.pdf";

    AddWatermarkService mockService = Mockito.mock(AddWatermarkService.class);

    new AddWatermarkHandler(mockService)
        .executeInternal(inputPdfFileArg, watermarkTextArg, fontSizeArg, outputPdfFileArg);

    Mockito.verify(mockService)
        .addWatermark(
            Mockito.argThat(inputPdfFile -> new File(inputPdfFileArg).equals(inputPdfFile)),
            Mockito.argThat(watermarkTextArg::equals),
            Mockito.intThat(fontSize -> Integer.parseInt(fontSizeArg) == fontSize),
            Mockito.argThat(outputPdfFile -> new File(outputPdfFileArg).equals(outputPdfFile)));
  }
}
