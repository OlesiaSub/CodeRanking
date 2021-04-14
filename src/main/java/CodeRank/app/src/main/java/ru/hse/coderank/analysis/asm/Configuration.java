package CodeRank.app.src.main.java.ru.hse.coderank.analysis.asm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private Properties properties;
    private static String propertiesFileName;
    private static String[] packages = new String[0];

    public Configuration() throws IOException {
        InputStream input = new FileInputStream(propertiesFileName);
        properties = new Properties();
        properties.load(input);

        String packagesList = properties.getProperty("process-packages");
        String deliminator = "[,][ ]";
        packages = packagesList.split(deliminator);
    }

    public static boolean processPackage(String name) {
        for (String token : packages) {
            if (name.startsWith(token)) {
                return true;
            }
        }
        return false;
    }

    public static void setConfigProperty(String propertiesFileName) {
        Configuration.propertiesFileName = propertiesFileName;
    }
}
