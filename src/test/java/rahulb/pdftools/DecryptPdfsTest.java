package rahulb.pdftools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DecryptPdfsTest {

    @Test
    void test_decryptPdfs() throws IOException {

        Path inputPdfsDirPath = Paths.get("src/test/resources/DecryptPdfs/input-1");
        Path outputPdfsDirPath = Paths.get("target/test/DecryptPdfs/output-1");
        Path expectedOutputPdfsDirPath = Paths.get("src/test/resources/DecryptPdfs/expected-output-1");

        DecryptPdfs.decryptPdfs(inputPdfsDirPath, outputPdfsDirPath, "Password0#");

        Set<Path> inputDirContentsRelativePaths = getDirContentsRelativePaths(inputPdfsDirPath);
        Set<Path> outputDirContentsRelativePaths = getDirContentsRelativePaths(outputPdfsDirPath);

        Assertions.assertFalse(inputDirContentsRelativePaths.isEmpty());
        Assertions.assertFalse(outputDirContentsRelativePaths.isEmpty());

        Assertions.assertEquals(inputDirContentsRelativePaths, outputDirContentsRelativePaths);

        outputDirContentsRelativePaths
                .stream()
                .filter(path -> outputPdfsDirPath.resolve(path).toFile().isFile())
                .forEach(path -> assertPdfsEqual(path, expectedOutputPdfsDirPath, outputPdfsDirPath));
    }

    private static void assertPdfsEqual(Path path, Path expectedPdfsDirPath, Path actualPdfsDirPath) {

        Path expectedPdfPath = expectedPdfsDirPath.resolve(path);
        Path actualPdfPath = actualPdfsDirPath.resolve(path);
        Assertions.assertTrue(
                PdfUtils.pdfEquals(
                        expectedPdfPath,
                        actualPdfPath
                ),
                String.format("The following PDFs are not equal: '%s', '%s'", expectedPdfPath, actualPdfPath)
        );
    }

    private static Set<Path> getDirContentsRelativePaths(Path dirPath) throws IOException {

        try (Stream<Path> dirContents = Files.walk(dirPath, FileVisitOption.FOLLOW_LINKS)) {
            return dirContents.map(dirPath::relativize).collect(Collectors.toSet());
        }
    }
}
