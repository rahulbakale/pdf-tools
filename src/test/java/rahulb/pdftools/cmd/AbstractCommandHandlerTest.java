package rahulb.pdftools.cmd;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AbstractCommandHandlerTest {

  @Test
  void test_execute_string_array_args() throws Exception {

    String[] commandArgs = {"foo", "bar"};

    AbstractCommandHandler commandHandler = Mockito.mock(AbstractCommandHandler.class);
    Mockito.doCallRealMethod().when(commandHandler).execute(Mockito.any(String[].class));

    commandHandler.execute(commandArgs);

    Mockito.verify(commandHandler, Mockito.times(1)).executeInternal(commandArgs);
  }

  @Test
  void test_execute_map_of_args() throws Exception {

    Map<String, String> commandArgs =
        Map.of(
            "key1", "value1",
            "key2", "value2");

    AbstractCommandHandler commandHandler = Mockito.mock(AbstractCommandHandler.class);
    Mockito.doCallRealMethod().when(commandHandler).execute(Mockito.any(Map.class));

    commandHandler.execute(commandArgs);

    Mockito.verify(commandHandler, Mockito.times(1)).executeInternal(commandArgs);
  }
}
