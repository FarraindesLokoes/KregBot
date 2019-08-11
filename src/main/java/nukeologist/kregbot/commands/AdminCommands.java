package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.api.ContextType;
import nukeologist.kregbot.data.MessageValues;
import nukeologist.kregbot.listeners.GuildListener;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.util.SaveHelper;

import java.util.Map;
import java.util.Set;

/**
 * @author Nukeologist
 */
public class AdminCommands {

    private static final SaveHelper<MessageValues> INCREMENTSSAVER = new SaveHelper<>(MessageValues.class);
    private static MessageValues VALUES = INCREMENTSSAVER.fromJson("increments");

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

    @Command(value = "delete", type = ContextType.GUILD)
    public static void deleteIncrement(Context ctx) {
        if (ctx.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            String[] words = ctx.getWords();
            if (words.length == 1) {
                ctx.send("Need one more parameter.");
            } else {
                VALUES.getMapOfGuild(ctx.getMember().getGuild().getIdLong()).remove(words[1]);
                INCREMENTSSAVER.saveJson(VALUES, "increments");
                ctx.send("Deleted from table.");
            }
        } else {
            ctx.send("Only admins can use this command");
        }
    }

    @CommandHelp("delete")
    public static void helpDelete(Context ctx) {
        ctx.send("Admin only, deletes something from the increments table.");
    }

    @Command(value = "printtable", type = ContextType.GUILD)
    public static void print(Context ctx) {
        VALUES = INCREMENTSSAVER.fromJson("increments");
        Member member = ctx.getMember();
        //if (member.hasPermission(Permission.ADMINISTRATOR) && VALUES != null) { //TODO: add optional permission for admins to give to non admins
        if (VALUES != null) {
            StringBuilder label = new StringBuilder("Table of Increments\nMessage : Value\n");
            int part = 1;
            long guild = ctx.getMember().getGuild().getIdLong();
            Map<String, Integer> map = VALUES.getMapOfGuild(guild);
            int longest = MessageHelper.getLongestString(map.keySet()).length();
            if (map != null) {
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    String key = entry.getKey();
                    label.append(key).append(" ".repeat(longest + 1 - key.length())).append(entry.getValue()).append("\n");
                    if (label.length() > 1600) { //Embeds over 1024 characters get rekt by discord, and messages > 2000
                        //Message shall format itself, but code blocks actually look worse, f u discord!
                        ctx.send(label.toString());
                        part++;
                        label = new StringBuilder("Part " + part + "\n");
                    }
                }
                ctx.send(label.toString());
            }
        }
    }

    @CommandHelp("printtable")
    public static void helpPrint(Context ctx) {
        ctx.send("Command that prints the table of message values.\nWorks only with admins and on servers.");
    }
}
