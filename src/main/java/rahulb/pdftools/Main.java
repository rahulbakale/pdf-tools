package rahulb.pdftools;

import java.io.File;
import java.util.Arrays;
import java.util.stream.IntStream;

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

            case PdfToImage -> {
                File inputPdfFile = new File(args[1]);
                File outputDir = new File(args[2]);
                IntStream pageNumbers = Arrays.stream(args[3].split(",")).mapToInt(Integer::parseInt);
                int dpi = Integer.parseInt(args[4]);
                String imageFormat = args[5];

                PdfToImage.pdfToImage(inputPdfFile, outputDir, pageNumbers, dpi, imageFormat);
            }

            case RemovePages ->  {
                File inputPdfFile = new File(args[1]);
                IntStream pageNumbers = Arrays.stream(args[2].split(",")).mapToInt(Integer::parseInt);
                File outputPdfFile = new File(args[3]);

                RemovePages.removePages(inputPdfFile, pageNumbers, outputPdfFile);
            }

            case AddWatermark -> {
                File inputPdfFile = new File(args[1]);
                String watermarkText = args[2];
                int fontSize = Integer.parseInt(args[3]);
                File outputPdfFile = new File(args[4]);

                AddWatermark.addWatermark(inputPdfFile, watermarkText, fontSize, outputPdfFile);
            }

            default -> throw new IllegalArgumentException(String.format("Invalid command '%s'", command));
        }
    }
}
