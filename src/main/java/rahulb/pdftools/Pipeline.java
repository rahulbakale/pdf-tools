package rahulb.pdftools;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

final class Pipeline {

  private Pipeline() {}

  static void execute(String... args) throws Exception {

    execute(Paths.get(args[0]));
  }

  private static void execute(Path pipelineSpecFile)
      throws IOException, ReflectiveOperationException {

    try (Reader reader = Files.newBufferedReader(pipelineSpecFile, UTF_8)) {

      Yaml yaml = new Yaml();
      Map<String, Object> pipelineSpec = yaml.load(reader);

      List<Map<?, ?>> transformationsSpec = (List<Map<?, ?>>) pipelineSpec.get("transformations");

      if (transformationsSpec == null || transformationsSpec.isEmpty()) {
        throw new RuntimeException("Pipeline spec is invalid: no transformations specified");
      }

      for (int i = 0; i < transformationsSpec.size(); i++) {
        Map<?, ?> transformationSpec = transformationsSpec.get(i);

        String transformationType = (String) transformationSpec.get("type");

        if (transformationType == null) {
          throw new RuntimeException(
              String.format(
                  "Pipeline spec is invalid: transformation type is not specified for transformation number '%d'",
                  (i + 1)));
        }

        Map<?, ?> transformationArgs = (Map<?, ?>) transformationSpec.get("args");

        switch (CommandName.valueOf(transformationType)) {
          case EncryptPdf -> EncryptPdf.encryptPdf(transformationArgs);
          case DecryptPdfs -> DecryptPdfs.decryptPdfs(transformationArgs);
          case PdfToImage -> PdfToImage.pdfToImage(transformationArgs);
          case RemovePages -> RemovePages.removePages(transformationArgs);
          case AddWatermark -> AddWatermark.addWatermark(transformationArgs);
          case ConvertToGrayscale -> ConvertToGrayscale.convertToGrayscale(transformationArgs);
          case ImagesToPdf -> ImagesToPdf.imagesToPdf(transformationArgs);

          default -> throw new RuntimeException(
              String.format(
                  "Pipeline spec is invalid: Invalid transformation type '%s'",
                  transformationType));
        }
      }
    }
  }
}
