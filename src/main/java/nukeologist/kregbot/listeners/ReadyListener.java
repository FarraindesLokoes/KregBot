package nukeologist.kregbot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.slf4j.LoggerFactory;

/**
 * @author Nukeologist
 */
public class ReadyListener implements EventListener {
    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof ReadyEvent)
            LoggerFactory.getLogger("KregBot").info("Bot Ready!");
    }
}
