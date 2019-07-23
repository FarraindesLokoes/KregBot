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

    private static Runnable bomb = null;
    @Command("planting")
    public static void plantingBomb(Context context) {
        if (bomb != null) {
            context.reply("Bomb already planted.");
            return;
        }
        context.send("planting...");
        bomb = () -> {
            int count = 0;
            while (count < 5) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                context.send( ".".repeat(Math.max(1, count)));
            }
            context.send("@here\nThe bomb has been planted!");

            if (context.getWords().length == 1) {
                try {
                    Thread.sleep(3600000);
                    context.send("@everyone\n" +
                            "TERRORIST WINS!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else if (context.getWords()[1].chars().allMatch(Character::isDigit)) {
                try {
                    Thread.sleep(Long.parseLong(context.getWords()[1]) * 1000);
                    context.send("@everyone\n" +
                            "TERRORIST WINS!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                context.send("The bom was defective...");
            }
            bomb = null;
        };
        new Thread(bomb).start();
    }
}
