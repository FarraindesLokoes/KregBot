package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.api.ContextType;
import nukeologist.kregbot.listeners.ReplacerListener;
import nukeologist.kregbot.util.MessageHelper;

/**
 * @author SpicyFerret
 * */
public class ReplaceWordCommand {

    @Command(value = "replace", type = ContextType.GUILD)
    public static void replace(Context context) {
        String[] words = context.getWords();

        if (words.length < 3) {
            context.reply(", Syntax error: !replace <original> <replacer>");
        } else {
            ReplacerListener.VALUES.add(context.getMember().getGuild().getIdLong(), words[1], MessageHelper.collapse(words, 2));
            context.reply(", okay, " + words[1] + " will now be replaced by " + MessageHelper.collapse(words, 2));
        }
    }

    @CommandHelp("replace")
    public static void helpReplace(Context context) {
        EmbedBuilder embed = new EmbedBuilder();
        MessageBuilder msg = new MessageBuilder();
        embed.setColor((int) (Math.random() * 16777215)); // now cam be red and white, thanks to SpicyFerret
        embed.setDescription("Use !replace <word> <word or sentence>\n" +
                "messages containing the first parameter will be replaced by a new one containing the second parameter instead of the original word.");
        context.send(msg.setEmbed(embed.build()).build());
    }
}
