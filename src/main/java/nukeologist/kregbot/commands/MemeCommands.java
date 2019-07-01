package nukeologist.kregbot.commands;

import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.Context;

/**
 * @author Nukeologist
 */
public class MemeCommands {

    @Command("vitor")
    public static void otario(Context ctx) {
        ctx.send("viadao da porra");
    }

    @Command("60")
    public static void senta(Context ctx) {
        ctx.send("Pare com essa viadagem.");
    }
}
