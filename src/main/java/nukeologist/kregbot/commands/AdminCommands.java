package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.api.ContextType;
import nukeologist.kregbot.data.MessageValues;
import nukeologist.kregbot.listeners.GuildListener;
import nukeologist.kregbot.util.SaveHelper;

import java.util.Map;

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

    @Command(value = "printtable", type = ContextType.GUILD)
    public static void print(Context ctx) {
        if (VALUES == null) VALUES = INCREMENTSSAVER.fromJson("increments");
        Member member = ctx.getMember();
        //if (member.hasPermission(Permission.ADMINISTRATOR) && VALUES != null) { //TODO: add optional permission for admins to give to non admins
        if (VALUES != null) {
            EmbedBuilder eb = new EmbedBuilder();
            StringBuilder label = new StringBuilder();
            StringBuilder numbers = new StringBuilder();
            eb.setTitle("Table of Increments");
            int part = 1;
            long guild = ctx.getMember().getGuild().getIdLong();
            Map<String, Integer> map = VALUES.getMapOfGuild(guild);
            if (map != null) {
                for (Map.Entry<String, Integer> entry : map.entrySet()) {
                    label.append(entry.getKey()).append("\n\n");
                    numbers.append(entry.getValue()).append("\n\n");
                    if (label.length() > 950) { //Messages over 1024 characters get rekt by discord
                        eb.addField("Message", label.toString(), true);
                        eb.addField("Value", numbers.toString(), true);
                        MessageBuilder msg = new MessageBuilder();
                        msg.setEmbed(eb.build());
                        ctx.send(msg.build());
                        label = new StringBuilder();
                        numbers = new StringBuilder();
                        eb = new EmbedBuilder();
                        eb.setTitle("Table of Increments Part " + ++part);
                    }
                }
                eb.addField("Message", label.toString(), true);
                eb.addField("Value", numbers.toString(), true);
                MessageBuilder msg = new MessageBuilder();
                msg.setEmbed(eb.build());
                ctx.send(msg.build());
            }
        }

    }

    @CommandHelp("printtable")
    public static void helpPrint(Context ctx) {
        ctx.send("Command that prints the table of message values.\nWorks only with admins and on servers.");
    }
}
