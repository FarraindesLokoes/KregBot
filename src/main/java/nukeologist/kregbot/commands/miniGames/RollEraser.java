package nukeologist.kregbot.commands.miniGames;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.util.CommonMessagesReplays;
import nukeologist.kregbot.util.Constants;
import nukeologist.kregbot.util.MessageHelper;

public class RollEraser {

    @Command("rollEraser")
    public static void rollEraser(Context context) {
        double roll = (Math.random() * 25);

        if (roll < 12) { // Yes
            context.reply(MessageHelper.makeCodeBlock("\n" +
                    Constants.Symbols.DOUBLE_TOP_LEFT_CORNER + String.valueOf(Constants.Symbols.DOUBLE_HORIZONTAL_BAR).repeat(15) + Constants.Symbols.DOUBLE_TOP_RIGHT_CORNER +"\n" +
                    Constants.Symbols.DOUBLE_VERTICAL_BAR + "               " + Constants.Symbols.DOUBLE_VERTICAL_BAR + "\n" +
                    Constants.Symbols.DOUBLE_VERTICAL_BAR + "      YES      " + Constants.Symbols.DOUBLE_VERTICAL_BAR + "\n" +
                    Constants.Symbols.DOUBLE_VERTICAL_BAR + "               " + Constants.Symbols.DOUBLE_VERTICAL_BAR + "\n" +
                    Constants.Symbols.DOUBLE_BOTTOM_LEFT_CORNER + String.valueOf(Constants.Symbols.DOUBLE_HORIZONTAL_BAR).repeat(15) + Constants.Symbols.DOUBLE_BOTTOM_RIGHT_CORNER));
        } else if (roll < 24) { // No
            context.reply(MessageHelper.makeCodeBlock("\n" +
                    Constants.Symbols.DOUBLE_TOP_LEFT_CORNER + String.valueOf(Constants.Symbols.DOUBLE_HORIZONTAL_BAR).repeat(15) + Constants.Symbols.DOUBLE_TOP_RIGHT_CORNER +"\n" +
                    Constants.Symbols.DOUBLE_VERTICAL_BAR + "               " + Constants.Symbols.DOUBLE_VERTICAL_BAR + "\n" +
                    Constants.Symbols.DOUBLE_VERTICAL_BAR + "      NO       " + Constants.Symbols.DOUBLE_VERTICAL_BAR + "\n" +
                    Constants.Symbols.DOUBLE_VERTICAL_BAR + "               " + Constants.Symbols.DOUBLE_VERTICAL_BAR + "\n" +
                    Constants.Symbols.DOUBLE_BOTTOM_LEFT_CORNER + String.valueOf(Constants.Symbols.DOUBLE_HORIZONTAL_BAR).repeat(15) + Constants.Symbols.DOUBLE_BOTTOM_RIGHT_CORNER));
        } else { // Dont know
            context.reply(MessageHelper.makeCodeBlock("\n" +
                    Constants.Symbols.DOUBLE_TOP_LEFT_CORNER + String.valueOf(Constants.Symbols.DOUBLE_HORIZONTAL_BAR).repeat(15) + Constants.Symbols.DOUBLE_TOP_RIGHT_CORNER +"\n" +
                    Constants.Symbols.DOUBLE_VERTICAL_BAR + "     DONNO     " + Constants.Symbols.DOUBLE_VERTICAL_BAR + "\n" +
                    Constants.Symbols.DOUBLE_BOTTOM_LEFT_CORNER + String.valueOf(Constants.Symbols.DOUBLE_HORIZONTAL_BAR).repeat(15) + Constants.Symbols.DOUBLE_BOTTOM_RIGHT_CORNER));
        }
    }

    @CommandHelp("rollEraser")
    public static void rollEraserHelp(Context context){
        CommonMessagesReplays.embedMessage(context, "Use !rollEraser to roll a YES or NO eraser.");
    }
}
