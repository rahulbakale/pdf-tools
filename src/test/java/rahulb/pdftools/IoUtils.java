package rahulb.pdftools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

final class IoUtils {

    static InputStream getResourceAsStream(String resourceName) {
        return IoUtils.class.getResourceAsStream(resourceName);
    }

    static byte[] getResourceAsBytes(String resourceName) throws IOException {

        try (var is = getResourceAsStream(resourceName);
             var bis = new BufferedInputStream(is)) {

            return bis.readAllBytes();
        }
    }
}
