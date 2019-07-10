package nukeologist.kregbot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import nukeologist.kregbot.data.MessageValues;
import nukeologist.kregbot.util.Patterns;
import nukeologist.kregbot.util.SaveHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

/**
 * @author Nukeologist
 */
public class MessageListener implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger("Chat");
    private static final SaveHelper<MessageValues> SAVER = new SaveHelper<>(MessageValues.class);
    private static MessageValues VALUES = SAVER.fromJson("increments");

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof MessageReceivedEvent)
            onMessage((MessageReceivedEvent) event);
        if (event instanceof GuildMessageReceivedEvent)
            onGuildMessage((GuildMessageReceivedEvent) event);
    }

    // called by onEvents
    private void onMessage(MessageReceivedEvent event) {
        LOG.info("#{} #{} < {} >", event.getChannel(), event.getAuthor(), event.getMessage());
    }

    private void onGuildMessage(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (VALUES == null)
            VALUES = new MessageValues();
        String msg = event.getMessage().getContentRaw();
        Matcher matcher = Patterns.INCREMENT_DECREMENT.matcher(msg);
        long guild = event.getGuild().getIdLong();
        if (matcher.matches()) {
            String key = matcher.group(1);
            int incr = matcher.group(2).equals("++") ? 1 : -1;
            VALUES.add(guild, key, incr);
            SAVER.saveJson(VALUES, "increments");
            event.getChannel().sendMessage(key + " == " + VALUES.getMapOfGuild(guild).get(key)).queue();
        }
    }
}
