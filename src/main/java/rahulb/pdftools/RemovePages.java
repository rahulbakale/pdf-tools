package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

final class RemovePages {

    static void removePages(File inputPdfFile, IntStream pageNumbers, File outputPdfFile) throws IOException {

        try (var document = PDDocument.load(inputPdfFile)) {

            List<PDPage> pagesToRemove = pageNumbers
                    .distinct()
                    .mapToObj(pageNumber -> document.getPage(pageNumber - 1))
                    .toList();

            pagesToRemove.forEach(document::removePage);

            //noinspection ResultOfMethodCallIgnored
            outputPdfFile.getParentFile().mkdirs();

            document.save(outputPdfFile);
        }
    }
}
