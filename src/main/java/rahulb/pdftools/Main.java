package rahulb.pdftools;

import java.util.Arrays;

public class Main {

  public static void main(String[] args) throws Exception {

    String commandName = args == null || args.length == 0 ? null : args[0];

    if (commandName == null) {
      throw new IllegalArgumentException("Command name not specified");
    }

    String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

    Command.valueOf(commandName).obtainCommandHandler().execute(commandArgs);
  }
}
