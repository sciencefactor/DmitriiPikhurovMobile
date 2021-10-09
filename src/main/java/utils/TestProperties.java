package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.testng.annotations.Test;

public class TestProperties {

//    @Test
//    public void test() {
//        TestProperties.loadProperties("web", "private");
//        System.out.println(TestProperties.get("searchEngine"));
//        System.out.println(TestProperties.getURL("testToken"));
//    }

    private static final Properties properties = new Properties();

    public static String get(String property) {
        return properties.getProperty(property);
    }

    public static int getInt(String property) {
        return Integer.parseInt(properties.getProperty(property));
    }

    public static String getURL(String property) {
        String rawText = properties.getProperty(property);
        return rawText.replace("/", "%2F");
    }

    public static void loadProperties(String... types) {
        for (String type : types) {
            String path = String.format("src/test/resources/properties/%s.properties", type);
            try (InputStream inputStream = new FileInputStream(path)) {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
