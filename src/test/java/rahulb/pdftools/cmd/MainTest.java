package rahulb.pdftools.cmd;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import rahulb.pdftools.IncompleteTestException;

class MainTest {

  @Test
  void test_exception_is_thrown_if_command_name_is_not_specified() {

    Exception exception =
        Assertions.assertThrowsExactly(
            IllegalArgumentException.class, () -> Main.main(new String[] {}));

    Assertions.assertEquals(
        "Command name not specified", exception.getMessage(), "Error message is not as expected.");
  }

  @Test
  void test_exception_is_thrown_if_command_name_is_invalid() {

    String commandName = "foo";

    Exception exception =
        Assertions.assertThrowsExactly(
            IllegalArgumentException.class,
            () -> Main.main(new String[] {commandName, "arg1", "arg2"}));

    Assertions.assertEquals(
        String.format("Invalid command name: '%s'", commandName),
        exception.getMessage(),
        "Error message is not as expected.");
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsFor_test_correct_command_is_executed")
  void test_correct_command_is_executed(String inputCommandName, Command expectedCommand) {

    Main mockMain = Mockito.mock(Main.class);
    Mockito.doCallRealMethod().when(mockMain).execute(Mockito.any(String[].class));

    mockMain.execute(new String[] {inputCommandName, "foo", "bar"});

    Mockito.verify(mockMain).executeCommand(expectedCommand, new String[] {"foo", "bar"});
  }

  @ParameterizedTest
  @MethodSource("provideArgumentsFor_test_correct_command_handler_is_executed")
  void test_correct_command_handler_is_executed(
      String commandName, Class<? extends AbstractCommandHandler> expectedHandlerType) {

    Main mockMain = Mockito.mock(Main.class);
    Mockito.doCallRealMethod().when(mockMain).execute(Mockito.any(String[].class));
    Mockito.doCallRealMethod()
        .when(mockMain)
        .executeCommand(Mockito.any(Command.class), Mockito.any(String[].class));
    Mockito.doCallRealMethod().when(mockMain).obtainCommandHandler(Mockito.any(Command.class));

    mockMain.execute(new String[] {commandName, "foo", "bar"});

    Mockito.verify(mockMain)
        .executeCommandHandler(Mockito.any(expectedHandlerType), Mockito.any(String[].class));
  }

  @Test
  void test_correct_arguments_are_passed_to_command_handler() {

    String[] commandArgs = {"foo", "bar"};

    Main mockMain = Mockito.mock(Main.class);
    Mockito.doCallRealMethod()
        .when(mockMain)
        .executeCommand(Mockito.any(Command.class), Mockito.any(String[].class));
    Mockito.doCallRealMethod()
        .when(mockMain)
        .executeCommandHandler(
            Mockito.any(AbstractCommandHandler.class), Mockito.any(String[].class));

    AbstractCommandHandler mockCommandHandler = Mockito.mock(AbstractCommandHandler.class);
    Mockito.when(mockMain.obtainCommandHandler(Mockito.any(Command.class)))
        .thenReturn(mockCommandHandler);

    mockMain.executeCommand(Command.Pipeline, commandArgs);

    ArgumentCaptor<String[]> commandArgsCaptor = ArgumentCaptor.forClass(String[].class);
    Mockito.verify(mockCommandHandler).execute(commandArgsCaptor.capture());
    String[] argsReceivedByCommandHandler = commandArgsCaptor.getAllValues().get(0);
    Assertions.assertArrayEquals(commandArgs, argsReceivedByCommandHandler);
  }

  private static Stream<Arguments> provideArgumentsFor_test_correct_command_is_executed() {

    return Arrays.stream(Command.values()).map(command -> Arguments.of(command.name(), command));
  }

  private static Stream<Arguments> provideArgumentsFor_test_correct_command_handler_is_executed() {

    List<Arguments> argsStream =
        List.of(
            Arguments.of("EncryptPdf", EncryptPdfHandler.class),
            Arguments.of("DecryptPdfs", DecryptPdfsHandler.class),
            Arguments.of("PdfToImage", PdfToImageHandler.class),
            Arguments.of("RemovePages", RemovePagesHandler.class),
            Arguments.of("AddWatermark", AddWatermarkHandler.class),
            Arguments.of("ConvertToGrayscale", ConvertToGrayscaleHandler.class),
            Arguments.of("ImagesToPdf", ImagesToPdfHandler.class),
            Arguments.of("Pipeline", PipelineHandler.class));

    Set<String> validCommandNames =
        Arrays.stream(Command.values()).map(Command::name).collect(Collectors.toSet());

    Set<String> testedCommandNames =
        argsStream.stream().map(args -> (String) args.get()[0]).collect(Collectors.toSet());

    Set<String> untestedCommandNames = new HashSet<>(validCommandNames);
    untestedCommandNames.removeAll(testedCommandNames);

    if (!untestedCommandNames.isEmpty()) {
      throw new IncompleteTestException(
          String.format(
              "The following command names are not being tested: '%s'", untestedCommandNames));
    }

    return argsStream.stream();
  }
}
