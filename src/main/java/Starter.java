import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author TiltdBastion
 */
public class Starter extends ListenerAdapter {

    public static void main(String[] args) {
        try {
            PropertiesReader propertiesReader = new PropertiesReader("config.properties");
            HueClient hueClient = new HueClient(propertiesReader.readProperty("hue"), propertiesReader.readProperty("bridgeAddress"));
            ArrayList<String> adminIds = new ArrayList<>(Arrays.asList(propertiesReader.readProperty("adminId").split(",")));
            JDABuilder.createDefault(propertiesReader.readProperty("discord")).addEventListeners(new MessageListener(hueClient, propertiesReader.readProperty("commandPrefix"), adminIds)).build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}

