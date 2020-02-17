package nukeologist.kregbot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import nukeologist.kregbot.api.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CommonMessagesReplays {

    /** Replay a syntax error message
     *
     * @param context the context of the error
     * @param observation a observation String to add on the error message
     * @param commandHelp interface with the help command to be run after the message example: ()-> commandHelp(context)
     * */
    public static void syntaxError(@NotNull Context context, String observation, @Nullable Runnable commandHelp) {
        context.reply("Something went wrong" + (observation != null ? ": " + observation : "") + "!");
        if (commandHelp != null) {
            commandHelp.run();
        }
    }

    public static void syntaxError(@NotNull Context context, String observation, @Nullable Consumer<Context> help) {
        context.reply("Something went wrong" + (observation != null ? ": " + observation : "") + "!");
        if (help != null) {
            help.accept(context);
        }
    }

    public static void syntaxError(Context context) {
        context.reply("Something went wrong!");
    }
    public static void syntaxError(Context context, Runnable commandHelp) {
        syntaxError(context, null, commandHelp);
    }
    public static void syntaxError(Context context, String observation) {
        context.reply("Something went wrong" + (observation != null ? ": " + observation : "") + "!");
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
