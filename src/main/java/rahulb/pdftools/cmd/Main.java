package rahulb.pdftools.cmd;

import java.util.Arrays;

public class Main {

  public static void main(String[] args) {

    new Main().execute(args);
  }

  void execute(String[] args) {

    String commandName = args == null || args.length == 0 ? null : args[0];

    if (commandName == null) {
      throw new IllegalArgumentException("Command name not specified");
    }

    String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

    Command command;
    try {
      command = Command.valueOf(commandName);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(String.format("Invalid command name: '%s'", commandName));
    }

    executeCommand(command, commandArgs);
  }

  void executeCommand(Command command, String[] commandArgs) {

    executeCommandHandler(obtainCommandHandler(command), commandArgs);
  }

  private static AbstractCommandHandler obtainCommandHandler(Command command) {

    return command.obtainCommandHandler();
  }

  void executeCommandHandler(AbstractCommandHandler commandHandler, String[] commandArgs) {

    commandHandler.execute(commandArgs);
  }
}
