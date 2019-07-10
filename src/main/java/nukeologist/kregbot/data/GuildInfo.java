package nukeologist.kregbot.data;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nukeologist
 */
public class GuildInfo {

    private Map<Long, Boolean> GUILDTRACK = new HashMap<>();
    private Map<Long, Boolean> CHANNELTRACK = new HashMap<>();

    public void addGuild(TextChannel channel) {
        GUILDTRACK.putIfAbsent(channel.getGuild().getIdLong(), true);
        CHANNELTRACK.putIfAbsent(channel.getIdLong(), true);
    }

    public void removeGuild(TextChannel channel) {
        if (GUILDTRACK.containsKey(channel.getGuild().getIdLong())) {
            GUILDTRACK.replace(channel.getGuild().getIdLong(), false);
            CHANNELTRACK.replace(channel.getIdLong(), false);
        }
    }

    public boolean isGuildTracked(long guild) {
        if (GUILDTRACK.containsKey(guild))
            return GUILDTRACK.get(guild);
        return false;
    }

    public boolean isChannelTracked(long channel) {
        if (CHANNELTRACK.containsKey(channel))
            return CHANNELTRACK.get(channel);
        return false;
    }

}
