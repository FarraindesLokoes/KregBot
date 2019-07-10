package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.api.ContextType;
import nukeologist.kregbot.listeners.GuildListener;

/**
 * @author Nukeologist
 */
public class TrackCommands {

    @Command(value = "track", type = ContextType.GUILD)
    public static void track(Context ctx) {
        Member member = ctx.getMember();
        String[] params = ctx.getWords();
        if (member.hasPermission(Permission.ADMINISTRATOR)) {
            if (params.length == 1) {
                ctx.send("Use !track <on/off> to turn on or off tracking in this channel.");
                return;
            } else if (params.length == 2) {
                String capital = params[1].toLowerCase();
                switch (capital) {
                    case "on":
                    case "y":
                    case "yes":
                    case "true":
                        ctx.send("Turning tracking ON on this channel.");
                        GuildListener.getINFO().addGuild((TextChannel) ctx.getChannel());
                        GuildListener.getSAVE().saveJson(GuildListener.getINFO(), "guildtracker");
                        break;
                    case "off":
                    case "n":
                    case "no":
                    case "false":
                        ctx.send("Turning tracking OFF on this channel.");
                        GuildListener.getINFO().removeGuild((TextChannel) ctx.getChannel());
                        GuildListener.getSAVE().saveJson(GuildListener.getINFO(), "guildtracker");
                        break;

                    default:
                        ctx.send("I do not understand the param you gave me.");
                        break;
                }
            } else {
                ctx.send("Too many params. Please, only give one.");
            }
        }
    }

    @CommandHelp("track")
    public static void helpTrack(Context ctx) {
        ctx.send("Exclusive to guild admins, this makes a channel track changes in the server.");
    }
}
