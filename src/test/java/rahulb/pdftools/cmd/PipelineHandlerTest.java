package rahulb.pdftools.cmd;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

class PipelineHandlerTest {

  @Test
  void test_appropriate_command_handlers_are_executed() throws Exception {

    String pipelineSpecFileArg = "src/test/resources/Pipeline/pipeline-1.yaml";

    EncryptPdfHandler encryptPdfHandler = Mockito.mock(EncryptPdfHandler.class);
    DecryptPdfsHandler decryptPdfsHandler = Mockito.mock(DecryptPdfsHandler.class);
    PdfToImageHandler pdfToImageHandler = Mockito.mock(PdfToImageHandler.class);
    RemovePagesHandler removePagesHandler = Mockito.mock(RemovePagesHandler.class);
    AddWatermarkHandler addWatermarkHandler = Mockito.mock(AddWatermarkHandler.class);
    ConvertToGrayscaleHandler convertToGrayscaleHandler =
        Mockito.mock(ConvertToGrayscaleHandler.class);
    ImagesToPdfHandler imagesToPdfHandler = Mockito.mock(ImagesToPdfHandler.class);
    PipelineHandler pipelineHandler = Mockito.mock(PipelineHandler.class);

    Mockito.doCallRealMethod().when(pipelineHandler).executeInternal(Mockito.any(String[].class));
    Mockito.doCallRealMethod().when(pipelineHandler).executePipeline(Mockito.any(Reader.class));

    Mockito.when(pipelineHandler.getCommandHandler(Mockito.any(Command.class)))
        .thenAnswer(
            answer -> {
              Command transformation = answer.getArgument(0);
              return switch (transformation) {
                case EncryptPdf -> encryptPdfHandler;
                case DecryptPdfs -> decryptPdfsHandler;
                case PdfToImage -> pdfToImageHandler;
                case RemovePages -> removePagesHandler;
                case AddWatermark -> addWatermarkHandler;
                case ConvertToGrayscale -> convertToGrayscaleHandler;
                case ImagesToPdf -> imagesToPdfHandler;
                case Pipeline -> pipelineHandler;
              };
            });

    pipelineHandler.executeInternal(pipelineSpecFileArg);

    InOrder orderVerifier =
        Mockito.inOrder(
            encryptPdfHandler,
            decryptPdfsHandler,
            pdfToImageHandler,
            removePagesHandler,
            addWatermarkHandler,
            convertToGrayscaleHandler,
            imagesToPdfHandler);

    orderVerifier
        .verify(imagesToPdfHandler)
        .execute(
            Map.of(
                "input-images-directory", "/tmp/images",
                "output-page-size", "A4",
                "image-position", "top-left",
                "page-margins", "standard",
                "output-pdf-file", "/tmp/intermediate.pdf"));

    orderVerifier
        .verify(removePagesHandler)
        .execute(
            Map.of(
                "input-pdf-file", "/tmp/input.pdf",
                "pages-to-remove", "1,3",
                "output-pdf-file", "/tmp/intermediate.pdf"));

    orderVerifier
        .verify(convertToGrayscaleHandler)
        .execute(
            Map.of(
                "input-pdf-file", "/tmp/intermediate.pdf",
                "dpi", "200.0",
                "output-page-size", "LEGAL",
                "output-pdf-file", "/tmp/intermediate.pdf"));

    orderVerifier
        .verify(addWatermarkHandler)
        .execute(
            Map.of(
                "input-pdf-file", "/tmp/intermediate.pdf",
                "watermark-text", "DUPLICATE",
                "watermark-font-size", "40",
                "output-pdf-file", "/tmp/intermediate.pdf"));

    orderVerifier
        .verify(encryptPdfHandler)
        .execute(
            Map.of(
                "input-pdf-file", "/tmp/intermediate.pdf",
                "output-pdf-file", "/tmp/encrypted/output.pdf"));

    orderVerifier
        .verify(decryptPdfsHandler)
        .execute(
            Map.of(
                "input-pdfs-directory", "/tmp/encrypted",
                "output-pdfs-directory", "/tmp/decrypted"));

    orderVerifier
        .verify(pdfToImageHandler)
        .execute(
            Map.of(
                "input-pdf-file", "/tmp/decrypted",
                "output-directory", "/tmp/images-2",
                "page-numbers", "1",
                "dpi", "300",
                "image-format", "JPEG"));

    orderVerifier.verifyNoMoreInteractions();
  }

  @Test
  void test_exception_is_thrown_if_transformation_type_is_not_specified_in_pipeline() {

    String pipelineSpec =
        """
                                transformations:
                                  - type: ImagesToPdf
                                    args:
                                      input-images-directory: '/tmp/images'
                                      output-page-size: 'A4'
                                      image-position: 'top-left'
                                      page-margins: 'standard'
                                      output-pdf-file: '/tmp/intermediate.pdf'

                                  - args:
                                      foo: one
                                      bar: two

                                  - type: ImagesToPdf
                                    args:
                                      input-images-directory: '/tmp/images'
                                      output-page-size: 'A4'
                                      image-position: 'top-left'
                                      page-margins: 'standard'
                                      output-pdf-file: '/tmp/intermediate.pdf'
                        """;

    Exception exception =
        Assertions.assertThrowsExactly(
            RuntimeException.class,
            () -> new PipelineHandler().executePipeline(new StringReader(pipelineSpec)));

    Assertions.assertEquals(
        "Pipeline spec is invalid: transformation type is not specified for transformation number '2'",
        exception.getMessage());
  }
}
