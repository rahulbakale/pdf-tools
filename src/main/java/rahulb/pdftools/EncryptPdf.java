package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;

final class EncryptPdf {

    //See https://pdfbox.apache.org/2.0/cookbook/encryption.html
    static void encryptPdf(File inputPdfFile, File outputPdfFile) throws IOException {

        try (var document = PDDocument.load(inputPdfFile)) {

            encryptPdf(document);

            //noinspection ResultOfMethodCallIgnored
            outputPdfFile.getParentFile().mkdirs();

            document.save(outputPdfFile);
        }
    }

    private static void encryptPdf(PDDocument document) throws IOException {

        StandardProtectionPolicy protectionPolicy = prepareProtectionPolicy();

        document.protect(protectionPolicy);
    }

    private static StandardProtectionPolicy prepareProtectionPolicy() {

        var console = System.console();
        char[] docOpenPassword = console.readPassword("Enter the password required to open the document:");
        char[] permissionsChangePassword = console.readPassword("Enter the password required to change the accessPermission of the document:");

        var accessPermission = prepareAccessPermission();
        var protectionPolicy = new StandardProtectionPolicy(String.valueOf(permissionsChangePassword), String.valueOf(docOpenPassword), accessPermission);

        //The following parameters are based on Adobe Acrobat documentation.
        // https://helpx.adobe.com/in/acrobat/using/securing-pdfs-passwords.html
        protectionPolicy.setPreferAES(true);
        protectionPolicy.setEncryptionKeyLength(256);

        return protectionPolicy;
    }

    private static AccessPermission prepareAccessPermission() {

        AccessPermission permissions = new AccessPermission();

        //insert/rotate/delete pages
        permissions.setCanAssembleDocument(false);

        //extract content from the document
        permissions.setCanExtractContent(false);

        //extract content from the document for accessibility purposes
        permissions.setCanExtractForAccessibility(false);

        //fill in interactive form fields (including signature fields)
        permissions.setCanFillInForm(false);

        //modify the document
        permissions.setCanModify(false);

        //add or modify text annotations and fill in interactive forms fields and,
        // if canModify() returns true, create or modify interactive form fields (including signature fields)
        permissions.setCanModifyAnnotations(false);

        //print the document
        permissions.setCanPrint(true);

        //print the document in a faithful format
        permissions.setCanPrintFaithful(true);

        //Locks the access permission read only
        permissions.setReadOnly();

        return permissions;
    }
}
