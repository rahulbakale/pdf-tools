package rahulb.pdftools;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

final class EncryptPdf extends AbstractCommandHandler {

  private static final String ARG_INPUT_PDF_FILE = "input-pdf-file";
  private static final String ARG_OUTPUT_PDF_FILE = "output-pdf-file";

  EncryptPdf() {}

  // See https://pdfbox.apache.org/2.0/cookbook/encryption.html

  @Override
  void executeInternal(String... args) throws Exception {

    Map<String, String> argMap =
        Map.of(
            ARG_INPUT_PDF_FILE, args[0],
            ARG_OUTPUT_PDF_FILE, args[1]);

    executeInternal(argMap);
  }

  @Override
  void executeInternal(Map<?, ?> args) throws Exception {

    String inputPdfFile = (String) args.get(ARG_INPUT_PDF_FILE);
    String outputPdfFile = (String) args.get(ARG_OUTPUT_PDF_FILE);

    encryptPdf(new File(inputPdfFile), new File(outputPdfFile));
  }

  private static void encryptPdf(File inputPdfFile, File outputPdfFile) throws IOException {

    try (var document = PDDocument.load(inputPdfFile)) {

      encryptPdf(document);

      Utils.saveDocument(document, outputPdfFile);
    }
  }

  private static void encryptPdf(PDDocument document) throws IOException {

    StandardProtectionPolicy protectionPolicy = prepareProtectionPolicy();

    document.protect(protectionPolicy);
  }

  private static StandardProtectionPolicy prepareProtectionPolicy() {

    var console = System.console();
    char[] docOpenPassword =
        console.readPassword("Enter the password required to open the document:");
    char[] permissionsChangePassword =
        console.readPassword(
            "Enter the password required to change the accessPermission of the document:");

    var accessPermission = prepareAccessPermission();
    var protectionPolicy =
        new StandardProtectionPolicy(
            String.valueOf(permissionsChangePassword),
            String.valueOf(docOpenPassword),
            accessPermission);

    // The following parameters are based on Adobe Acrobat documentation.
    // https://helpx.adobe.com/in/acrobat/using/securing-pdfs-passwords.html
    protectionPolicy.setPreferAES(true);
    protectionPolicy.setEncryptionKeyLength(256);

    return protectionPolicy;
  }

  private static AccessPermission prepareAccessPermission() {

    AccessPermission permissions = new AccessPermission();

    // insert/rotate/delete pages
    permissions.setCanAssembleDocument(false);

    // extract content from the document
    permissions.setCanExtractContent(false);

    // extract content from the document for accessibility purposes
    permissions.setCanExtractForAccessibility(false);

    // fill in interactive form fields (including signature fields)
    permissions.setCanFillInForm(false);

    // modify the document
    permissions.setCanModify(false);

    // add or modify text annotations and fill in interactive forms fields and,
    // if canModify() returns true, create or modify interactive form fields (including signature
    // fields)
    permissions.setCanModifyAnnotations(false);

    // print the document
    permissions.setCanPrint(true);

    // print the document in a faithful format
    permissions.setCanPrintFaithful(true);

    // Locks the access permission read only
    permissions.setReadOnly();

    return permissions;
  }
}
