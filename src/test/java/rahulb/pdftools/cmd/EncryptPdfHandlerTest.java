package rahulb.pdftools.cmd;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import rahulb.pdftools.core.EncryptPdfService;

class EncryptPdfHandlerTest {

  @Test
  void test_service_invoked_with_correct_arguments() throws Exception {

    String inputPdfFileArg = "one/two.pdf";
    String outputPdfFileArg = "three/four.pdf";
    char[] docOpenPasswordInput = "Pass123#".toCharArray();
    char[] permissionsChangePasswordInput = "Pass456#".toCharArray();

    EncryptPdfService mockService = Mockito.mock(EncryptPdfService.class);

    EncryptPdfHandler mockHandler =
        Mockito.mock(EncryptPdfHandler.class, Mockito.withSettings().useConstructor(mockService));
    Mockito.doCallRealMethod().when(mockHandler).executeInternal(Mockito.any(String[].class));
    Mockito.doCallRealMethod().when(mockHandler).executeInternal(Mockito.any(Map.class));
    Mockito.when(mockHandler.readDocOpenPassword()).thenReturn(docOpenPasswordInput);
    Mockito.when(mockHandler.readPermissionsChangePassword())
        .thenReturn(permissionsChangePasswordInput);

    mockHandler.executeInternal(inputPdfFileArg, outputPdfFileArg);

    Mockito.verify(mockService)
        .encryptPdf(
            Mockito.argThat(inputPdfFile -> new File(inputPdfFileArg).equals(inputPdfFile)),
            Mockito.argThat(
                docOpenPassword -> Arrays.equals(docOpenPasswordInput, docOpenPassword)),
            Mockito.argThat(
                permissionsChangePassword ->
                    Arrays.equals(permissionsChangePasswordInput, permissionsChangePassword)),
            Mockito.argThat(outputPdfFile -> new File(outputPdfFileArg).equals(outputPdfFile)));
  }
}
