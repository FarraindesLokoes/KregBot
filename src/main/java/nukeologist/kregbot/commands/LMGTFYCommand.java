package nukeologist.kregbot.commands;

import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Nukeologist
 */
public class LMGTFYCommand {

    @Command("lmgtfy")
    public static void lmgtfy(Context ctx) {
        String[] words = ctx.getWords();
        if (words.length == 1) {
            ctx.send("Need one param at least");
            return;
        }
        StringBuilder builder = new StringBuilder(ctx.getMessage().getContentRaw());
        StringBuilder url = new StringBuilder("<http://lmgtfy.com/?iie=").append(1).append("&q=");
        builder.delete(0, 7);
        ctx.send(url.append(URLEncoder.encode(builder.toString(), StandardCharsets.UTF_8)).append(">").toString());
    }

    @CommandHelp("lmgtfy")
    public static void help(Context ctx) {
        ctx.send("Helps someone understand they can use Google. Needs at least one parameter.");
    }
}
