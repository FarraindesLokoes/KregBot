package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Emote;
import nukeologist.kregbot.KregBot;
import nukeologist.kregbot.util.Constants;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

/**
 * @author Nukeologist
 */
public class RPGCommands {

    private static final Random RANDOM = new Random();

    @Command("roll")
    public static void rollCommand(Context ctx) {
        final String[] words = ctx.getWords();

        if (words.length == 1)
            ctx.reply("rolled " + roll(1, 20));
        else if (words.length > 1) {
            final StringBuilder builder = new StringBuilder("rolled ");
            int sum = 0;
            boolean atLeastOne = false;
            for (String word : words) {
                if (word.equalsIgnoreCase("roll")) continue;
                final Matcher oneRoll = Constants.ONEROLL.matcher(word);
                final Matcher anyRoll = Constants.ANYROLL.matcher(word);
                if (oneRoll.matches()) { // d20, d6, etc
                    int diceNumber = Integer.parseInt(Constants.GETINTEGER.matcher(word).replaceAll("$1"));
                    final String roll = roll(1, diceNumber);
                    sum += getSumFromString(roll);
                    builder.append(roll);
                    builder.append(" + ");
                    atLeastOne = true;
                } else if (anyRoll.matches()) { //matches <N>d<M> where N and M are ints
                    atLeastOne = true;
                    final int dice = Integer.parseInt(Constants.GETINTEGER.matcher(word).replaceAll("$1")); //parses int before d
                    final int diceNumber = Integer.parseInt(word.replaceFirst("\\d+d", ""));
                    String roll = roll(dice, diceNumber);
                    sum += getSumFromString(roll);
                    builder.append(roll);
                    builder.append(" + ");
                } else {
                    try {
                        final int numb = Integer.parseInt(word);
                        sum += numb;
                        builder.append("( ").append(numb).append(" ) = ").append(sum);
                        builder.append(" + ");
                    } catch (final NumberFormatException e) {
                        //ignored
                    }
                }
            }
            if (atLeastOne) {
                builder.deleteCharAt(builder.length() - 1).deleteCharAt(builder.length() - 1);
                builder.append("**Totals ").append(sum).append(".**");   //could use message helper, but already in builder context.
                ctx.reply(builder.toString());
            } else {
                ctx.reply("I do not understand...");
            }
        }
    }

    @CommandHelp("roll")
    public static void helpRoll(Context ctx) {
        final EmbedBuilder embed = new EmbedBuilder();
        final MessageBuilder msg = new MessageBuilder();
        embed.setColor((int) (Math.random() * 0xFFFFFF)); // now can be red and white, thanks to SpicyFerret
        embed.setDescription("Rolls dice for you! Here's a little help:\nUsage: [!roll ndm] where n is the number of dice and m the number of sides\nYou can chain call these with spaces in between too.");
        ctx.send(msg.setEmbed(embed.build()).build());
    }

    @Command("character")
    public static void character(Context ctx) {
        var sb = new StringBuilder("\n(");
        for (int i = 0; i < 6; i++) {
            var t = new int[]{RANDOM.nextInt(6) + 1, RANDOM.nextInt(6) + 1, RANDOM.nextInt(6) + 1, RANDOM.nextInt(6) + 1};
            sb.append(t[0]).append(" ").append(t[1]).append(" ").append(t[2]).append(" ").append(t[3]).append(") = ");
            Arrays.sort(t);
            sb.append(t[1] + t[2] + t[3]).append("\n(");
        }
        sb.setLength(sb.length() - 1);
        ctx.reply(sb.toString());

    }

    private static String roll(int diceAmount, int faceAmount) {
        final StringBuilder builder = new StringBuilder("(");
        int sum = 0;
        for (int i = 0; i < diceAmount; i++) {
            int next = RANDOM.nextInt(faceAmount) + 1;
            builder.append(next);
            sum += next;
            if (i != diceAmount - 1)
                builder.append(" + ");
        }
        builder.append(") = ").append(sum);
        return builder.toString();
    }

    private static int rollWithoutString(int diceAmount, int faceAmount) {
        int sum = 0;
        for (int i = 0; i < diceAmount; i++)
            sum += RANDOM.nextInt(faceAmount) + 1;
        return sum;
    }

    /*This is ugly, but we do not need a new object to hold two values...*/
    private static int getSumFromString(String str) {
        final StringBuilder builder = new StringBuilder(str);
        builder.delete(0, str.indexOf('=') + 2);
        return Integer.parseInt(builder.toString());
    }

}
