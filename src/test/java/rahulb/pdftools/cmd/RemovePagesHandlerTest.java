package rahulb.pdftools.cmd;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rahulb.pdftools.core.RemovePagesService;

class RemovePagesHandlerTest {

  @Test
  void test_service_invoked_with_correct_arguments() throws Exception {

    String inputPdfFileArg = "one/two.pdf";
    String pagesToRemoveArg = "keep:3,7,9";
    String outputPdfFileArg = "three/four.pdf";

    RemovePagesService mockService = Mockito.mock(RemovePagesService.class);

    new RemovePagesHandler(mockService)
        .executeInternal(inputPdfFileArg, pagesToRemoveArg, outputPdfFileArg);

    Mockito.verify(mockService)
        .removePages(
            Mockito.argThat(inputPdfFile -> new File(inputPdfFileArg).equals(inputPdfFile)),
            Mockito.argThat(pagesToRemoveArg::equals),
            Mockito.argThat(outputPdfFile -> new File(outputPdfFileArg).equals(outputPdfFile)));
  }
}
