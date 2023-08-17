package rahulb.pdftools;

import java.util.Map;

abstract class AbstractCommandHandler {

  void execute(String... args) {

    try {
      executeInternal(args);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  void execute(Map<?, ?> args) {

    try {
      executeInternal(args);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  abstract void executeInternal(String... args) throws Exception;

  abstract void executeInternal(Map<?, ?> args) throws Exception;
}
