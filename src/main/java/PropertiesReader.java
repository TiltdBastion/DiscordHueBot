import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author TiltdBastion
 */
public class PropertiesReader {

    private Properties properties;
    private String filename;

    public PropertiesReader(String filename){
        this.filename = filename;
        properties = new Properties();
    }

    public String readProperty(String propertyName){
        String property = null;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filename);
            if (inputStream != null){
                properties.load(inputStream);
                property = properties.getProperty(propertyName);
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return property;
    }

    

}
