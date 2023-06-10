package rahulb.pdftools;

import java.io.InputStream;

final class IoUtils {

    static InputStream getResourceAsStream(String resourceName) {
        return IoUtils.class.getResourceAsStream(resourceName);
    }
}
