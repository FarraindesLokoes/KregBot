package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * @author Nukeologist
 */
public class WolframCommand {

    @Command("wolfram")
    public static void wolfram(Context ctx) {
        String[] words = ctx.getWords();
        if (words.length == 1) {
            ctx.send("Wolfram what?? (One mora parameter)");
        }
        String urlstring = "https://api.wolframalpha.com/v1/simple?appid=" + System.getenv("WOLFRAM") + "&i=";

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Wolfram|Alpha");
        eb.setDescription(ctx.getChannel().toString());
        eb.setTimestamp(Instant.now());
        eb.setAuthor(ctx.getAuthor().getName());

        StringBuilder builder = new StringBuilder(ctx.getMessage().getContentRaw());
        builder.delete(0, 8);
        urlstring += URLEncoder.encode(builder.toString().replace('+', '*'), StandardCharsets.US_ASCII); //for the memes
        MessageBuilder message = new MessageBuilder();

        eb.setImage(urlstring);
        message.setEmbed(eb.build());
        ctx.send(message.build());
    }

    @CommandHelp("wolfram")
    public static void helpWolfram(Context ctx) {
        ctx.send("Usage: !wolfram <anytext>");
    }
}
