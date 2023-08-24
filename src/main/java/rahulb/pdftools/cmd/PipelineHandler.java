package rahulb.pdftools.cmd;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

final class PipelineHandler extends AbstractCommandHandler {

  PipelineHandler() {}

  @Override
  void executeInternal(String... args) throws Exception {

    executePipeline(Paths.get(args[0]));
  }

  @Override
  void executeInternal(Map<?, ?> args) {

    // Nested pipelines are not permitted for now.
    throw new UnsupportedOperationException();
  }

  private void executePipeline(Path pipelineSpecFile) throws IOException {

    try (Reader reader = Files.newBufferedReader(pipelineSpecFile, UTF_8)) {
      executePipeline(reader);
    }
  }

  void executePipeline(Reader pipelineSpecReader) {

    Yaml yaml = new Yaml();
    Map<String, Object> pipelineSpec = yaml.load(pipelineSpecReader);

    List<Map<?, ?>> transformationsSpec = (List<Map<?, ?>>) pipelineSpec.get("transformations");

    validateTransformationsSpec(transformationsSpec);

    executeTransformations(transformationsSpec);
  }

  private static void validateTransformationsSpec(List<Map<?, ?>> transformationsSpec) {

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

      Command transformation;
      try {
        transformation = Command.valueOf(transformationType);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException(
            String.format(
                "Pipeline spec is invalid: Invalid transformation type '%s'", transformationType));
      }

      if (transformation == Command.Pipeline) {

        // Nested pipelines are not permitted for now.

        throw new RuntimeException(
            String.format(
                "Pipeline spec is invalid: Invalid transformation type '%s'", transformationType));
      }
    }
  }

  private void executeTransformations(List<Map<?, ?>> transformationsSpec) {

    for (Map<?, ?> transformationSpec : transformationsSpec) {

      String transformationType = (String) transformationSpec.get("type");
      Command transformation = Command.valueOf(transformationType);
      AbstractCommandHandler commandHandler = getCommandHandler(transformation);

      Map<?, ?> transformationArgs = (Map<?, ?>) transformationSpec.get("args");

      commandHandler.execute(transformationArgs);
    }
  }

  AbstractCommandHandler getCommandHandler(Command transformation) {

    return transformation.obtainCommandHandler();
  }
}
