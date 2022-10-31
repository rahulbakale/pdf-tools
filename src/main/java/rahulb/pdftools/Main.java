package rahulb.pdftools;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        String command = args == null || args.length == 0 ? null : args[0];

        if (command == null) {
            throw new IllegalArgumentException("Command not specified");
        }

        switch (CommandName.valueOf(command)) {

            case EncryptPdf -> {
                String inputPdfFile = args[1];
                String outputPdfFile = args[2];
                EncryptPdf.encryptPdf(new File(inputPdfFile), new File(outputPdfFile));
            }

            default -> throw new IllegalArgumentException(String.format("Invalid command '%s'", command));
        }
    }
}
