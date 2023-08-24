package rahulb.pdftools.cmd;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rahulb.pdftools.core.DecryptPdfsService;

class DecryptPdfsHandlerTest {

  @Test
  void test_service_invoked_with_correct_arguments() throws Exception {

    String inputPdfsDirPathArg = "one/two";
    String outputPdfsDirPathArg = "three/four";
    char[] docOpenPasswordInput = "Pass123#".toCharArray();

    DecryptPdfsService mockService = Mockito.mock(DecryptPdfsService.class);

    DecryptPdfsHandler mockHandler =
        Mockito.mock(DecryptPdfsHandler.class, Mockito.withSettings().useConstructor(mockService));
    Mockito.doCallRealMethod().when(mockHandler).executeInternal(Mockito.any(String[].class));
    Mockito.doCallRealMethod().when(mockHandler).executeInternal(Mockito.any(Map.class));
    Mockito.when(mockHandler.readDocOpenPassword()).thenReturn(docOpenPasswordInput);

    mockHandler.executeInternal(inputPdfsDirPathArg, outputPdfsDirPathArg);

    Mockito.verify(mockService)
        .decryptPdfs(
            Mockito.argThat(
                inputPdfsDirPath -> Paths.get(inputPdfsDirPathArg).equals(inputPdfsDirPath)),
            Mockito.argThat(
                outputPdfsDirPath -> Paths.get(outputPdfsDirPathArg).equals(outputPdfsDirPath)),
            Mockito.argThat(
                docOpenPassword -> Arrays.equals(docOpenPasswordInput, docOpenPassword)));
  }
}
