package rahulb.pdftools;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {

        String command = args == null || args.length == 0 ? null : args[0];

        if (command == null) {
            throw new IllegalArgumentException("Command not specified");
        }

        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

        if ("Pipeline".equals(command)) {

            Pipeline.execute(commandArgs);

        } else {

            switch (CommandName.valueOf(command)) {

                case EncryptPdf -> EncryptPdf.encryptPdf(commandArgs);
                case PdfToImage -> PdfToImage.pdfToImage(commandArgs);
                case RemovePages -> RemovePages.removePages(commandArgs);
                case AddWatermark -> AddWatermark.addWatermark(commandArgs);
                case ConvertToGrayscale -> ConvertToGrayscale.convertToGrayscale(commandArgs);
                case ImagesToPdf -> ImagesToPdf.imagesToPdf(commandArgs);

                default -> throw new IllegalArgumentException(String.format("Invalid command '%s'", command));
            }
        }
    }
}
