package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import nukeologist.kregbot.BotManager;
import nukeologist.kregbot.MessageHelper;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandContainer;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.KregBot;

import java.util.List;

/**
 * @author Nukeologist
 */
public class HelpCommands {

    private static List<CommandContainer> commands;

    @Command("about")
    public static void about(Context ctx) {
        ctx.send(MessageHelper.makeBold("KregBot | Reborn") + "\nWritten by: Nukeologist\nVersion: " + KregBot.INSTANCE.getVersion());
    }

    @Command("help")
    public static void help(Context ctx) {
        ctx.send("Not implemented yet, however you can view a list of commands using\n!commands");
    }

    @Command("commands")
    public static void displayCommands(Context ctx) {
        ctx.send("Displaying all commands:\n");
        EmbedBuilder eb = new EmbedBuilder();
        StringBuilder label = new StringBuilder();
        StringBuilder context = new StringBuilder();
        StringBuilder bot = new StringBuilder();
        eb.setTitle("Command List");
        for (CommandContainer command : getCommands()) {
            label.append("!").append(command.getLabel()).append("\n");
            context.append(command.getContextType().toString()).append("\n");
            bot.append(command.canBeCalledByBot()).append("\n");
        }
        eb.addField("Command", label.toString(), true);
        eb.addField("Guild or Private", context.toString(), true);
        eb.addField("Bots can call this", bot.toString(), true);
        MessageBuilder msg = new MessageBuilder();
        msg.setEmbed(eb.build());
        ctx.send(msg.build());
    }

    /*Lazy initializes*/
    private static List<CommandContainer> getCommands() {
        if (commands == null) commands = BotManager.getCommands();
        return commands;
    }

}
