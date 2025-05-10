package pl.fundraising.charity.util;

import wiremock.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class TestUtil {
    public static String getJsonFromFile(final String path) throws IOException{
        final File file = new File(TestUtil.class.getResource(path).getFile());
        final String data = FileUtils.readFileToString(file,"UTF-8");
        return data;
    }
}
