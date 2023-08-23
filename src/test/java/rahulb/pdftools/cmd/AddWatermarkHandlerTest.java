package rahulb.pdftools.cmd;

import java.io.File;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rahulb.pdftools.core.AddWatermarkService;

class AddWatermarkHandlerTest {

  @Test
  void test_service_invoked_with_correct_arguments() throws Exception {

    AddWatermarkService mockService = Mockito.mock(AddWatermarkService.class);

    String inputPdfFileArg = "one/two.pdf";
    String watermarkTextArg = "CLASSIFIED";
    String fontSizeArg = "13";
    String outputPdfFileArg = "three/four.pdf";

    new AddWatermarkHandler(mockService)
        .executeInternal(inputPdfFileArg, watermarkTextArg, fontSizeArg, outputPdfFileArg);

    ArgumentCaptor<File> inputPdfFileCaptor = ArgumentCaptor.forClass(File.class);
    ArgumentCaptor<String> watermarkTextCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> fontSizeCaptor = ArgumentCaptor.forClass(int.class);
    ArgumentCaptor<File> outputPdfFileCaptor = ArgumentCaptor.forClass(File.class);

    Mockito.verify(mockService)
        .addWatermark(
            inputPdfFileCaptor.capture(),
            watermarkTextCaptor.capture(),
            fontSizeCaptor.capture(),
            outputPdfFileCaptor.capture());

    Assertions.assertEquals(new File(inputPdfFileArg), inputPdfFileCaptor.getValue());
    Assertions.assertEquals(watermarkTextArg, watermarkTextCaptor.getValue());
    Assertions.assertEquals(Integer.parseInt(fontSizeArg), fontSizeCaptor.getValue());
    Assertions.assertEquals(new File(outputPdfFileArg), outputPdfFileCaptor.getValue());
  }
}
