package rahulb.pdftools;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.Console;
import java.io.File;
import java.io.IOException;

public class EncryptPdf {

    //See https://pdfbox.apache.org/2.0/cookbook/encryption.html
    public static void main(String[] args) throws IOException {

        var inputPdfFile = new File("/tmp/sample-input.pdf");
        var outputPdfFile = new File("/tmp/sample-output.pdf");


        encryptPdf(inputPdfFile, outputPdfFile);
    }

    static void encryptPdf(File inputPdfFile, File outputPdfFile) throws IOException {

        Console console = System.console();
        var docOpenPassword = console.readPassword("Enter the password required to open the document:");
        var permissionsChangePassword = console.readPassword("Enter the password required to change the permissions of the document:");

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

        var protectionPolicy = new StandardProtectionPolicy(String.valueOf(permissionsChangePassword), String.valueOf(docOpenPassword), permissions);

        //The following parameters are based on Adobe Acrobat documentation.
        // https://helpx.adobe.com/in/acrobat/using/securing-pdfs-passwords.html
        protectionPolicy.setPreferAES(true);
        protectionPolicy.setEncryptionKeyLength(256);

        try (var document = PDDocument.load(inputPdfFile)) {

            document.protect(protectionPolicy);

            //noinspection ResultOfMethodCallIgnored
            outputPdfFile.getParentFile().mkdirs();

            document.save(outputPdfFile);
        }
    }
}
