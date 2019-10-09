package nukeologist.kregbot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import nukeologist.kregbot.api.Context;
import org.jetbrains.annotations.NotNull;

public class CommonMessagesReplays {

    /** Replay a syntax error message
     *
     * @param context the context of the error
     * @param observation a observation String to add on the error message
     * @param commandHelp interface with the help command to be run after the message example: ()-> commandHelp(context)
     * */
    public static void syntaxError(@NotNull Context context, String observation, Runnable commandHelp) {
        context.reply("Something went wrong" + (observation != null ? ": " + observation : "") + "!");
        if (commandHelp != null) {
            commandHelp.run();
        }
    }
    public static void syntaxError(Context context) {
        syntaxError(context, null, null);
    }
    public static void syntaxError(Context context, Runnable commandHelp) {
        syntaxError(context, null, commandHelp);
    }
    public static void syntaxError(Context context, String observation) {
        syntaxError(context, observation, null);
    }


    /** Send an embed message
     * */
    public static void embedMessage(@NotNull Context context, String message) {
        EmbedBuilder embed = new EmbedBuilder();
        MessageBuilder msg = new MessageBuilder();
        embed.setColor((int) (Math.random() * 16777215)); // now cam be red and white, thanks to SpicyFerret
        embed.setDescription(message);
        context.send(msg.setEmbed(embed.build()).build());
    }
}
