import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TiltdBastion
 */
public class MessageListener extends ListenerAdapter {

    private final static String GUILD_ID = "672146029830995988";
    private final static String CHANNEL_ID = "672446509245005857";


    private String commandPrefix;
    private CommandExec commandExec;
    private List<String> adminIds;

    public MessageListener(HueClient hueClient, String commandPrefix, List<String> adminIds){
        this.commandExec = new CommandExec(hueClient);
        this.commandPrefix = commandPrefix;
        this.adminIds = new ArrayList<>();
        this.adminIds.addAll(adminIds);
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        super.onMessageReceived(event);
        if(event.getGuild().getId().equals(GUILD_ID) && !event.getAuthor().isBot() && event.getMessage().getContentRaw().startsWith(commandPrefix)){
            if(event.getChannel().getId().equals(CHANNEL_ID)){
                String message = event.getMessage().getContentRaw().substring(commandPrefix.length());
                String command = message.split(" ")[0];
                switch (command) {
                    case "list":
                        commandExec.list(event.getChannel());
                        break;
                    case "colorloop":
                        commandExec.colorloop();
                        break;
                    case "light":
                        String[] args = message.split(" ");
                        commandExec.light(args[1],args[2],args[3],args[4]);
                        break;
                    case "reset":
                        if (adminIds.contains(event.getAuthor().getId())){
                            commandExec.reset();
                        }
                        break;
                    case "help":
                        commandExec.help(message, event.getChannel());
                        break;
                    default:
                        commandExec.commandNotRecognized(event.getChannel());
                        break;
                }
            }
        }
        else if(event.getMessage().getMentionedMembers().size() > 0){
            event.getMessage().getMentionedMembers().forEach(member -> {
                if(member.getId().equals("196272593001644032")){ //Custom for owner
                    commandExec.showNotification();
                }
            });
        }
    }
}
