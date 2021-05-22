package CodeRank.app.src.main.impl.asm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private static String propertiesFileName;
    private static String[] packages = new String[0];

    public Configuration() throws IOException {
        InputStream input = new FileInputStream(propertiesFileName);
        Properties properties = new Properties();
        properties.load(input);

        String packagesList = properties.getProperty("process-packages");
        String deliminator = "[,][ ]";
        packages = packagesList.split(deliminator);
    }

    public static boolean processPackage(String name) {
        for (String token : packages) {
            if (name.startsWith(token) || name.startsWith(token.replace('/', '.'))) {
                return true;
            }
        }
        return false;
    }

    public static void setConfigProperty(String propertiesFileName) {
        Configuration.propertiesFileName = propertiesFileName;
    }
}
