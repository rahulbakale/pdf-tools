package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

final class RemovePages {

    private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
    private static final String ARG_PAGES_TO_REMOVE = "pages-to-remove";
    private static final String ARG_OUTPUT_PDF_FILE = "output-pdf-file";

    static void removePages(String... args) throws IOException {

        Map<?, ?> argMap = Map.of(
                ARG_INPUT_PDF_FILE, args[0],
                ARG_PAGES_TO_REMOVE, args[1],
                ARG_OUTPUT_PDF_FILE, args[2]
        );

        removePages(argMap);
    }

    static void removePages(Map<?,?> args) throws IOException {

        File inputPdfFile = new File((String) args.get(ARG_INPUT_PDF_FILE));
        IntStream pageNumbers = Arrays.stream(((String) args.get(ARG_PAGES_TO_REMOVE)).split(",")).mapToInt(Integer::parseInt);
        File outputPdfFile = new File((String) args.get(ARG_OUTPUT_PDF_FILE));

        removePages(inputPdfFile, pageNumbers, outputPdfFile);
    }

    private static void removePages(File inputPdfFile, IntStream pageNumbers, File outputPdfFile) throws IOException {

        try (var document = PDDocument.load(inputPdfFile)) {

            removePages(pageNumbers, document);

            //noinspection ResultOfMethodCallIgnored
            outputPdfFile.getParentFile().mkdirs();

            document.save(outputPdfFile);
        }
    }

    private static void removePages(IntStream pageNumbers, PDDocument document) {

        List<PDPage> pagesToRemove = pageNumbers
                .distinct()
                .mapToObj(pageNumber -> document.getPage(pageNumber - 1))
                .toList();

        pagesToRemove.forEach(document::removePage);
    }
}
