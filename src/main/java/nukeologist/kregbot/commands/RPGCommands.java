package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import nukeologist.kregbot.util.Patterns;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;

import java.util.Random;
import java.util.regex.Matcher;

/**
 * @author Nukeologist
 */
public class RPGCommands {

    private static final Random RANDOM = new Random();

    @Command("roll")
    public static void rollCommand(Context ctx) {
        String[] words = ctx.getWords();
        if (words.length == 1)
            ctx.reply("rolled " + roll(1, 20));
        else if (words.length > 1) {
            StringBuilder builder = new StringBuilder("rolled ");
            int sum = 0;
            boolean atLeastOne = false;
            for (String word : words) {
                if (word.equalsIgnoreCase("roll")) continue;
                Matcher oneRoll = Patterns.ONEROLL.matcher(word);
                Matcher anyRoll = Patterns.ANYROLL.matcher(word);
                if (oneRoll.matches()) { // d20, d6, etc
                    int diceNumber = Integer.parseInt(word.replaceAll(Patterns.GETINTEGER.toString(), "$1"));
                    String roll = roll(1, diceNumber);
                    sum += getSumFromString(roll);
                    builder.append(roll);
                    builder.append(" + ");
                    atLeastOne = true;
                } else if (anyRoll.matches()) { //matches <N>d<M> where N and M are ints
                    atLeastOne = true;
                    int dice = Integer.parseInt(word.replaceAll(Patterns.GETINTEGER.toString(), "$1")); //parses int before d
                    int diceNumber = Integer.parseInt(word.replaceFirst("\\d+d", ""));
                    String roll = roll(dice, diceNumber);
                    sum += getSumFromString(roll);
                    builder.append(roll);
                    builder.append(" + ");
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
        EmbedBuilder embed = new EmbedBuilder();
        MessageBuilder msg = new MessageBuilder();
        embed.setColor((int) (Math.random() * 16777215)); // now cam be red and white, thanks to SpicyFerret
        embed.setDescription("Rolls dice for you! Here's a little help:\nUsage: [!roll ndm] where n is the number of dice and m the number of sides\nYou can chain call these with spaces in between too.");
        ctx.send(msg.setEmbed(embed.build()).build());
    }

    @Command("character")
    public static void character(Context ctx) {

    }

    private static String roll(int diceAmount, int faceAmount) {
        StringBuilder builder = new StringBuilder("(");
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
        StringBuilder builder = new StringBuilder(str);
        builder.delete(0, str.indexOf('=') + 2);
        return Integer.parseInt(builder.toString());
    }

}
