package rahulb.pdftools;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import org.apache.pdfbox.pdmodel.PDDocument;

final class RemovePages extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDF_FILE_PATH = "input-pdf-file";
  private static final String ARG_PAGES_TO_REMOVE = "pages-to-remove";
  private static final String ARG_OUTPUT_PDF_FILE_PATH = "output-pdf-file";

  RemovePages() {}

  @Override
  void executeInternal(String... args) throws Exception {

    Map<?, ?> argMap =
        Map.of(
            ARG_INPUT_PDF_FILE_PATH, args[0],
            ARG_PAGES_TO_REMOVE, args[1],
            ARG_OUTPUT_PDF_FILE_PATH, args[2]);

    executeInternal(argMap);
  }

  @Override
  void executeInternal(Map<?, ?> args) throws Exception {

    File inputPdfFile = new File((String) args.get(ARG_INPUT_PDF_FILE_PATH));
    String pagesToRemove = (String) args.get(ARG_PAGES_TO_REMOVE);
    File outputPdfFile = new File((String) args.get(ARG_OUTPUT_PDF_FILE_PATH));

    removePages(inputPdfFile, pagesToRemove, outputPdfFile);
  }

  private static void removePages(File inputPdfFile, String pagesToRemove, File outputPdfFile)
      throws IOException {

    try (var document = PDDocument.load(inputPdfFile)) {

      removePages(document, pagesToRemove);

      Utils.saveDocument(document, outputPdfFile);
    }
  }

  static void removePages(PDDocument document, String pagesToRemove) {

    IntPredicate shouldRemovePageNumber = getPageNumberToRemovePredicate(pagesToRemove);

    int numberOfPages = document.getNumberOfPages();

    int[] pgsToRemove =
        IntStream.range(1, numberOfPages + 1).filter(shouldRemovePageNumber).toArray();

    for (int i = 0; i < pgsToRemove.length; i++) {
      int originalPageNumber = pgsToRemove[i];
      int pageIndexToRemove = originalPageNumber - (i + 1);
      document.removePage(pageIndexToRemove);
    }
  }

  private static IntPredicate getPageNumberToRemovePredicate(String pagesToRemove) {

    if (pagesToRemove.contains(":")) {

      int index = pagesToRemove.indexOf(":");
      String predicateType = pagesToRemove.substring(0, index);
      String pageNumbers = pagesToRemove.substring(index + 1);

      if ("keep".equals(predicateType)) {
        return createPredicate(pageNumbers).negate();
      } else {
        throw new IllegalArgumentException(
            String.format("Invalid predicate type: '%s'", predicateType));
      }

    } else {
      return createPredicate(pagesToRemove);
    }
  }

  private static IntPredicate createPredicate(String pageNumbers) {

    Set<Integer> pageNumberSet =
        Arrays.stream(pageNumbers.split(","))
            .mapToInt(Integer::parseInt)
            .collect(HashSet::new, HashSet::add, AbstractCollection::addAll);

    return pageNumberSet::contains;
  }
}
