package nukeologist.kregbot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nukeologist
 */
public class MessageListener implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger("Chat");

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof MessageReceivedEvent)
            onMessage((MessageReceivedEvent) event);
    }

    // called by onEvent
    private void onMessage(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        LOG.info("#{} #{} < {} >", event.getChannel(), event.getAuthor(), event.getMessage());
    }
}
