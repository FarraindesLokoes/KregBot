package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.api.ContextType;
import nukeologist.kregbot.listeners.ReplacerListener;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.util.Constants;

import java.util.Map;

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
            String str = MessageHelper.sanitizeEveryone(MessageHelper.collapse(words, 2));
            ReplacerListener.VALUES.add(context.getMember().getGuild().getIdLong(), words[1], str);
            context.reply(", okay, " + MessageHelper.sanitizeEveryone(words[1]) + " will now be replaced by " + str);
        }
    }

    @CommandHelp("replace")
    public static void helpReplace(Context context) {
        EmbedBuilder embed = new EmbedBuilder();
        MessageBuilder msg = new MessageBuilder();
        embed.setColor((int) (Math.random() * 16777215)); // now cam be red and white, thanks to SpicyFerret
        embed.setDescription("Use !replace <word A> <word or sentence B>\n" +
                "  Messages containing A will be replaced by B instead of the original word A.\n" +
                "Use !replaceRemove <word A>\n" +
                "  Messages containing A will no longer be replaced.\n" +
                "Use !replaceTable\n" +
                "  All messages and replacers will be listed.");
        context.send(msg.setEmbed(embed.build()).build());
    }

    @Command(value = "replaceRemove", type = ContextType.GUILD)
    public static void replaceRemove(Context context){
        String[] words = context.getWords();

        if (words.length < 2) {
            context.reply(", Syntax error: !replace <original>");
        } else {
           ReplacerListener.VALUES.remove(context.getMember().getGuild().getIdLong(), words[1]);
           context.getMessage().addReaction(Constants.Emotes.ItsApproved).queue();
        }
    }

    @Command(value = "replaceClear", type = ContextType.GUILD)
    public static void replaceClear(Context context) {
        if (context.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            ReplacerListener.VALUES.getMap().clear();
            context.reply(", you nazi!");
            return;
        }
        context.reply(", you does not have permission to do that, fucking cunt.");
    }

    @Command(value = "replaceTable", type = ContextType.GUILD)
    public static void replaceTable(Context context){
        Map<String, String> possible = ReplacerListener.VALUES.getMapOfGuild(context.getMember().getGuild().getIdLong());

        if (possible != null && possible.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder("Replace table:");


            for (Map.Entry<String, String> entry: possible.entrySet()) {
                stringBuilder.append("\n").append(entry.getKey()).append(" → ").append(entry.getValue());
                if (stringBuilder.length() > 1800) {
                    context.getChannel().sendMessage(MessageHelper.makeMultiCodeBlock(stringBuilder.toString())).queue();
                    stringBuilder = new StringBuilder();
                }
            }

            context.getChannel().sendMessage(MessageHelper.makeMultiCodeBlock(stringBuilder.toString())).queue();
            return;
        }
        context.reply("Missing table or not created yet.");
    }
}
