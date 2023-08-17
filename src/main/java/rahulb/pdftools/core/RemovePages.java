package rahulb.pdftools.core;

import java.io.File;
import java.io.IOException;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;
import org.apache.pdfbox.pdmodel.PDDocument;

public final class RemovePages {

  private RemovePages() {}

  public static void removePages(File inputPdfFile, String pagesToRemove, File outputPdfFile)
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