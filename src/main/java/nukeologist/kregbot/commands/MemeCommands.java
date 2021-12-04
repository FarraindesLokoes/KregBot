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

    @Command("lewin")
    public static void fdp(Context ctx) {
        ctx.send("Aquele jogador de WoW? Viado.");
    }

    @Command("60")
    public static void senta(Context ctx) {
        ctx.send("Pare com essa viadagem.");
    }

    @Command("lisboa")
    public static void fodase(Context ctx) {
        ctx.send("FODA-SE");
    }

}
