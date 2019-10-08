package nukeologist.kregbot.commands.miniGames;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.util.MessageHelper;

import java.util.Random;

public class RockPaperScissor {

    private static final Random RANDOM = new Random();

    @Command("rps")
    public static void rockPaperScissor(Context context) {
        String[] words = context.getWords();

        if (words.length < 2) {
            context.reply("Something went wrong!");
            rockPaperScissorHelp(context);
        } else {
            int playerPlay;
            switch (words[1].charAt(0)) {
                case 'r':
                    playerPlay = 1;
                    break;
                case 'p':
                    playerPlay = 2;
                    break;
                case 's':
                    playerPlay = 3;
                    break;
                default:
                    context.reply("Something went wrong!");
                    rockPaperScissorHelp(context);
                    return;
            }
            int kregPlay = RANDOM.nextInt(3) + 1;

            context.getMessage().getChannel().sendMessage("I play " + getPlay(kregPlay) + "!").queue();

            if (playerPlay == kregPlay) { // Tie
                tieEvent(context);
            } else if (playerPlay == 3 && kregPlay == 1) { // Kreg Won
                wonEvent(context);
            } else if (playerPlay == 1 && kregPlay == 3) { // Kreg Lose
                loseEvent(context);
            } else if (playerPlay > kregPlay) { // Kreg lose
                loseEvent(context);
            } else { // Kreg Won
                wonEvent(context);
            }
        }

    }

    private static void wonEvent(Context context) {
        switch (RANDOM.nextInt(3)) {
            case 0:
                context.getMessage().getChannel().sendMessage("Nice try, " + context.getAuthor().getAsMention() + ", but looks like I won!").queue();
                break;
            case 1:
                context.getMessage().getChannel().sendMessage("Hahahaha, pathetic!\n" +
                        "It seems to me that you lack hate!").queue();
                break;
            case 2:
                context.getMessage().getChannel().sendMessage("And to think that you were a worthy opponent...\n" +
                        "Get out of here you wormy worm!").queue();
                break;
        }
    }

    private static void loseEvent(Context context) {
        switch (RANDOM.nextInt(3)) {
            case 0:
                context.getMessage().getChannel().sendMessage("Oh, you defeated me.\n" +
                        "What about a rematch?").queue();
                break;
            case 1:
                context.getMessage().getChannel().sendMessage("REMATCH NOW!\n" +
                        "You filth " + (context.getMember().getRoles().size() > 0 ? context.getMember().getRoles().get(RANDOM.nextInt(context.getMember().getRoles().size())).getName() : "normie") + "!").queue();
                break;
            case 2:
                context.reply(", are you gay?");
                break;
        }
    }

    private static void tieEvent(Context context) {
        context.reply("Looks like its a tie.");
        if (Math.random() < 0.2) {
            context.getMessage().getChannel().sendMessage("gay...");
        }
    }

    private static String getPlay(int play) {
        return play == 1 ? "Rock" : play == 2 ? "Paper" : play == 3 ? "Scissor" : null;
    }

    @CommandHelp("rps")
    public static void rockPaperScissorHelp(Context context){
        EmbedBuilder embed = new EmbedBuilder();
        MessageBuilder msg = new MessageBuilder();
        embed.setColor((int) (Math.random() * 16777215)); // now can be red and white, thanks to SpicyFerret
        embed.setDescription("You know, I'm awesome at Rock Paper Scissor and I can play with you\n" +
                "Use !rpc <your choice>\n" +
                "Rock: `rock` or `r`\n" +
                "Paper: `paper` or `p`\n" +
                "Scissor: `scissor` or `s`\n" +
                (Math.random() < 0.2 ? MessageHelper.makeSpoiler("I'll read only the first letter anyway...") : ""));
        context.send(msg.setEmbed(embed.build()).build());
    }


}
