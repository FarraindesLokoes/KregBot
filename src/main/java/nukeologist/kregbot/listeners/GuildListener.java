package nukeologist.kregbot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nukeologist.kregbot.data.GuildInfo;
import nukeologist.kregbot.util.SaveHelper;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * @author Nukeologist
 */
public class GuildListener extends ListenerAdapter {

    private static EmbedBuilder eb;

    private static final SaveHelper<GuildInfo> SAVE = new SaveHelper<>(GuildInfo.class);
    private static GuildInfo INFO = SAVE.fromJson("guildtracker");


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        Optional<TextChannel> optionalChannel = event.getGuild().getTextChannels().stream()
                .filter(this::isThisChannelTracked)
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
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Optional<TextChannel> optionalChannel = event.getGuild().getTextChannels().stream()
                .filter(this::isThisChannelTracked)
                .findFirst();

        if (optionalChannel.isPresent()) {
            TextChannel textChannel = optionalChannel.get();
            eb = new EmbedBuilder();
            eb.setTitle("User left:");
            eb.setAuthor(event.getUser().getName());
            eb.setTimestamp(Instant.now());
            eb.setDescription(event.getMember() == null ? event.getUser().getName() : event.getMember().getEffectiveName() + " has left the server.");

            MessageBuilder message = new MessageBuilder();
            message.setEmbed(eb.build());

            textChannel.sendMessage(message.build()).queue();
        }
    }

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        Optional<TextChannel> optionalChannel = event.getGuild().getTextChannels().stream()
                .filter(this::isThisChannelTracked)
                .findFirst();

        if (optionalChannel.isPresent()) {
            TextChannel textChannel = optionalChannel.get();
            eb = new EmbedBuilder();
            eb.setTitle("User changed nick");
            eb.setAuthor(event.getMember().getEffectiveName());
            eb.setTimestamp(Instant.now());
            eb.setDescription(event.getMember().getUser().getName() + " has changed his/her nickname : \n" + event.getOldNickname() +
                    "---> " + event.getNewNickname());

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
                    .filter(this::isThisChannelTracked)
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
                .filter(this::isThisChannelTracked)
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

    public static SaveHelper<GuildInfo> getSAVE() {
        return SAVE;
    }

    public static GuildInfo getINFO() {
        if (INFO == null)
            INFO = new GuildInfo();
        return INFO;
    }

    private boolean isThisChannelTracked(TextChannel channel) {
        if (INFO == null) {
            INFO = new GuildInfo();
            return false;
        }
        return INFO.isChannelTracked(channel.getIdLong());
    }

    private boolean isGuildTracked(long guildId) {
        if (INFO == null) {
            INFO = new GuildInfo();
            return false;
        }
        return INFO.isGuildTracked(guildId);
    }
}
