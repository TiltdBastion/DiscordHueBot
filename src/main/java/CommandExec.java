import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;

/**
 * @author TiltdBastion
 */
public class CommandExec {

    private final static double X_WHITE = 0.32273606538012656;
    private final static double Y_WHITE = 0.3290324363790471;

    private HueClient hueClient;

    private PropertiesReader propertiesReader;

    public CommandExec(HueClient hueClient){
        this.hueClient = hueClient;
        propertiesReader = new PropertiesReader("strings.properties");
    }

    public void list(MessageChannel messageChannel){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(propertiesReader.readProperty("lights"));
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.addField(propertiesReader.readProperty("available"), hueClient.getLights(),false);
        messageChannel.sendMessage(embedBuilder.build()).queue();
    }

    public void showNotification(){
        hueClient.blinkGroup();
    }

    public void colorloop(){
        hueClient.setGroupColor(hueClient.getAllLightsGroupId(), X_WHITE, Y_WHITE);
        hueClient.startGroupColorloop(hueClient.getAllLightsGroupId());
    }

    public void light(String id, String red, String green, String blue){
        double[] xy = hueClient.rgbToXY(Double.parseDouble(red), Double.parseDouble(green), Double.parseDouble(blue));
        hueClient.stopColorloop(id);
        hueClient.setColor(id,xy[0],xy[1]);
    }

    public void reset(){
        hueClient.stopGroupColorloop(hueClient.getAllLightsGroupId());
        hueClient.setGroupColor(hueClient.getAllLightsGroupId(), X_WHITE, Y_WHITE);
    }

    public void help(String message, MessageChannel messageChannel){
        if(message.equals("help")){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(propertiesReader.readProperty("help"));
            embedBuilder.setColor(Color.ORANGE);
            embedBuilder.addField(propertiesReader.readProperty("listCommand"),propertiesReader.readProperty("listDescription"),false);
            embedBuilder.addField(propertiesReader.readProperty("lightCommand"),propertiesReader.readProperty("lightDescription"),false);
            embedBuilder.addField(propertiesReader.readProperty("colorloopCommand"),propertiesReader.readProperty("colorloopDescription"),false);
            embedBuilder.addField(propertiesReader.readProperty("helpCommand"),propertiesReader.readProperty("helpDescription"),false);
           messageChannel.sendMessage(embedBuilder.build()).queue();
        }else if(message.startsWith("light",6)){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(propertiesReader.readProperty("lightHelp"));
            embedBuilder.setColor(Color.ORANGE);
            embedBuilder.addField(propertiesReader.readProperty("lightCommand"),propertiesReader.readProperty("lightShortDescription"),false);
            embedBuilder.addField(propertiesReader.readProperty("id"),propertiesReader.readProperty("idDescription"),false);
            embedBuilder.addField(propertiesReader.readProperty("red"),propertiesReader.readProperty("redDescription"),false);
            embedBuilder.addField(propertiesReader.readProperty("green"),propertiesReader.readProperty("greenDescription"),false);
            embedBuilder.addField(propertiesReader.readProperty("blue"),propertiesReader.readProperty("blueDescription"),false);
            messageChannel.sendMessage(embedBuilder.build()).queue();
        }else if(message.startsWith("list",6)){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(propertiesReader.readProperty("listHelp"));
            embedBuilder.setColor(Color.ORANGE);
            embedBuilder.addField(propertiesReader.readProperty("listCommand"),propertiesReader.readProperty("listDescription"),false);
            messageChannel.sendMessage(embedBuilder.build()).queue();
        }else{
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(propertiesReader.readProperty("noHelpAvailable"));
            embedBuilder.setColor(Color.RED);
            messageChannel.sendMessage(embedBuilder.build()).queue();
        }
    }

    public void commandNotRecognized(MessageChannel messageChannel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(propertiesReader.readProperty("commandNotRecognized"));
        embedBuilder.setColor(Color.RED);
        messageChannel.sendMessage(embedBuilder.build()).queue();
    }
}
