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

final class Pipeline extends AbstractCommandHandler {

  Pipeline() {}

  @Override
  void executeInternal(String... args) throws Exception {

    executePipeline(Paths.get(args[0]));
  }

  @Override
  void executeInternal(Map<?, ?> args) {

    // Nested pipelines are not permitted for now.
    throw new UnsupportedOperationException();
  }

  private static void executePipeline(Path pipelineSpecFile) throws IOException {

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

        Command transformation = Command.valueOf(transformationType);

        if (transformation == Command.Pipeline) {

          // Nested pipelines are not permitted for now.

          throw new RuntimeException(
              String.format(
                  "Pipeline spec is invalid: Invalid transformation type '%s'",
                  transformationType));
        }

        transformation.obtainCommandHandler().execute(transformationArgs);
      }
    }
  }
}
