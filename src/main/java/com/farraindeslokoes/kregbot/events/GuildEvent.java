package com.farraindeslokoes.kregbot.events;

import com.farraindeslokoes.kregbot.KregBot;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class GuildEvent extends ListenerAdapter {

    private static EmbedBuilder eb;
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        Optional<TextChannel> optionalChannel = event.getGuild().getTextChannels().stream()
                .filter(channel -> isThisChannelTracked(channel, event.getGuild().getIdLong()))
                .findFirst();

        if (optionalChannel.isPresent()) {
            TextChannel textChannel = optionalChannel.get();
            eb = new EmbedBuilder();
            eb.setTitle("User joined:");
            eb.setAuthor(event.getMember().getUser().getName());
            eb.setTimestamp(Instant.now());
            eb.setDescription(event.getMember().getEffectiveName() + " has joined the server.");

            MessageBuilder message = new MessageBuilder();
            message.setEmbed(eb.build());

            textChannel.sendMessage(message.build()).queue();
        }

    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        Optional<TextChannel> optionalChannel = event.getGuild().getTextChannels().stream()
                .filter(channel -> isThisChannelTracked(channel, event.getGuild().getIdLong()))
                .findFirst();

        if (optionalChannel.isPresent()) {
            TextChannel textChannel = optionalChannel.get();
            eb = new EmbedBuilder();
            eb.setTitle("User left:");
            eb.setAuthor(event.getMember().getUser().getName());
            eb.setTimestamp(Instant.now());
            eb.setDescription(event.getMember().getEffectiveName() + " has left the server.");

            MessageBuilder message = new MessageBuilder();
            message.setEmbed(eb.build());

            textChannel.sendMessage(message.build()).queue();
        }
    }

    @Override
    public void onGuildMemberNickChange(GuildMemberNickChangeEvent event) {
        Optional<TextChannel> optionalChannel = event.getGuild().getTextChannels().stream()
                .filter(channel -> isThisChannelTracked(channel, event.getGuild().getIdLong()))
                .findFirst();

        if (optionalChannel.isPresent()) {
            TextChannel textChannel = optionalChannel.get();
            eb = new EmbedBuilder();
            eb.setTitle("User changed nick");
            eb.setAuthor(event.getMember().getEffectiveName());
            eb.setTimestamp(Instant.now());
            eb.setDescription(event.getMember().getUser().getName() + " has changed his/her nickname : \n" + event.getPrevNick() +
                    "---> " + event.getNewNick());

            MessageBuilder message = new MessageBuilder();
            message.setEmbed(eb.build());

            textChannel.sendMessage(message.build()).queue();
        }
    }

    @Override
    public void onUserUpdateName(UserUpdateNameEvent event) {
        List<Guild> guilds = event.getUser().getMutualGuilds();

        Optional<Guild> optionalGuild = guilds.stream().filter(guild -> isGuildTracked(guild.getIdLong()))
                .findFirst();

        if (optionalGuild.isPresent()) {
            Guild guild = optionalGuild.get();
            Optional<TextChannel> optionalChannel = guild.getTextChannels().stream()
                    .filter(channel -> isThisChannelTracked(channel, guild.getIdLong()))
                    .findFirst();

            if (optionalChannel.isPresent()) {
                TextChannel textChannel = optionalChannel.get();
                eb = new EmbedBuilder();
                eb.setTitle("User changed Name");
                eb.setAuthor(event.getUser().getName());
                eb.setTimestamp(Instant.now());
                eb.setDescription(event.getOldName() + " has changed his/her name to: " + event.getNewName());

                MessageBuilder message = new MessageBuilder();
                message.setEmbed(eb.build());

                textChannel.sendMessage(message.build()).queue();
            }
        }

    }

    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent event) {
        Optional<TextChannel> optionalChannel = event.getGuild().getTextChannels().stream()
                .filter(channel -> isThisChannelTracked(channel, event.getGuild().getIdLong()))
                .findFirst();

        if (optionalChannel.isPresent()) {
            TextChannel textChannel = optionalChannel.get();
            eb = new EmbedBuilder();
            eb.setTitle("Server updated name");
            eb.setAuthor(event.getGuild().getOwner().getEffectiveName());
            eb.setTimestamp(Instant.now());
            eb.setDescription(event.getOldName() + " has changed the name to : \n" + event.getNewName());

            MessageBuilder message = new MessageBuilder();
            message.setEmbed(eb.build());

            textChannel.sendMessage(message.build()).queue();
        }
    }

    private boolean isThisChannelTracked(TextChannel channel, long guildId) {
        String SQL = "SELECT guildid, channel, istracking FROM guildtrack";

        try {
            Connection conn = KregBot.getSQLConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                BigDecimal id = rs.getBigDecimal("guildid");
                BigDecimal channelDecimal = rs.getBigDecimal("channel");
                boolean tracking = rs.getBoolean("istracking");

                if (channel.getIdLong() == channelDecimal.longValueExact()) {
                    System.out.println("channel id: " + channel.getIdLong());
                    if (guildId == id.longValueExact()) {
                        System.out.println("guild: " + guildId);
                        if (tracking) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (SQLException ex) {
            System.out.println("failed to query if channel was being tracked, returning false");
            ex.printStackTrace();
            return false;
        }
    }

    private boolean isGuildTracked(long guildId) {
        String SQL = "SELECT guildid, channel, istracking FROM guildtrack";

        try {
            Connection conn = KregBot.getSQLConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                BigDecimal id = rs.getBigDecimal("guildid");
                boolean tracking = rs.getBoolean("istracking");

                if (guildId == id.longValueExact()) {
                    if (tracking) {
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException ex) {
            System.out.println("failed to query if guild was being tracked, returning false");
            ex.printStackTrace();
            return false;
        }
    }


}
