package us.codecraft.webmagic.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SSLVerifyUtils {

    public static InputStream getCertificate(String fileLocation) {
        File f = new File(fileLocation);

        FileInputStream fips = null;
        try {
            fips = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fips;
    }

}
