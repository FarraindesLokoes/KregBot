package com.farraindeslokoes.kregbot.commands;

import com.farraindeslokoes.kregbot.KregBot;
import com.farraindeslokoes.kregbot.util.DatabaseUtils;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TrackerChannelCommand implements ICommand {

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] toDo) {
        if (event.getAuthor().isBot()) return;
        List<Permission> perms = event.getMember().getPermissions();
        if (perms.contains(Permission.ADMINISTRATOR)) {
            if (toDo.length == 1) {
                event.getChannel().sendMessage("Tracking guild users and using this channel.").queue();
                startTrackingChannel(event.getChannel(), event.getGuild().getIdLong());
            }else if (toDo.length == 2) {
                if (toDo[1].equals("off") || toDo[1].equals("false")) {
                    event.getChannel().sendMessage("Stopping tracking with this channel.").queue();
                    updateTrackingChannel(event.getChannel(), event.getGuild().getIdLong(), false);
                } else if (toDo[1].equals("on") || toDo[1].equals("true")) {
                    event.getChannel().sendMessage("Tracking with this channel").queue();
                    updateTrackingChannel(event.getChannel(), event.getGuild().getIdLong(), true);
                }
            }
        } else {
            event.getChannel().sendMessage("You dont have admin privileges.").queue();
        }
    }

    @Override
    public void executePrivate(PrivateMessageReceivedEvent event, String[] toDo) {
        event.getChannel().sendMessage("Only works in servers.").queue();
    }

    @Override
    public String getHelpString() {
        return "Tracks the channel this is run at with member join/leave/etc, needs ADM permission";
    }

    private void startTrackingChannel(TextChannel channel, long guildId) {
        DatabaseUtils.createTableIfNotExists("guildtrack", "(guildid NUMERIC NOT NULL UNIQUE, channel NUMERIC, istracking boolean);");
        String SQL = "INSERT INTO guildtrack(guildid, channel, istracking) VALUES(?,?,?) ON CONFLICT DO NOTHING";

        try {
            Connection conn = KregBot.getSQLConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            pstmt.setBigDecimal(1, new BigDecimal(guildId));
            pstmt.setBigDecimal(2, new BigDecimal(channel.getIdLong()));
            pstmt.setBoolean(3, true);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            channel.sendMessage("SQL error: " + e.toString()).queue();
        }
    }

    private void updateTrackingChannel(TextChannel channel, long guildId, boolean isTracking) {
        String SQL = "UPDATE guildtrack " + "SET istracking = ? " + "WHERE guildid = ? AND channel = ?";

        try {
            Connection conn = KregBot.getSQLConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            pstmt.setBoolean(1, isTracking);
            pstmt.setBigDecimal(2, new BigDecimal(guildId));
            pstmt.setBigDecimal(3, new BigDecimal(channel.getIdLong()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQL error when updating tracking database: ");
            e.printStackTrace();
        }
    }
}
