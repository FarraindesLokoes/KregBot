package nukeologist.kregbot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import nukeologist.kregbot.data.MessageReplacer;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.util.SaveHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

/**
 * @author SpicyFerret
 * */
public class ReplacerListener implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger("Chat");
    public static final SaveHelper<MessageReplacer> SAVER = new SaveHelper<>(MessageReplacer.class);
    private static final Random RANDOM = new Random();
    public static MessageReplacer VALUES = SAVER.fromJson("replacers");


    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildMessageReceivedEvent)
            onGuildMessage((GuildMessageReceivedEvent) event);
    }

    private void onGuildMessage(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().getContentRaw().charAt(0) == '!') return;
        if (VALUES == null) VALUES = new MessageReplacer();
        String[] msg = event.getMessage().getContentRaw().split(" ");
        Map<String, String> map = VALUES.getMapOfGuild(event.getGuild().getIdLong());
        boolean replaced = false;
        for (int i = 0; i < msg.length; i++) {
            String str = msg[i];
            if (map.containsKey(str)) {
                replaced = true;
                msg[i] = map.get(str);
            }
        }
        if (!replaced) return;
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(event.getAuthor().getAsTag() + " says: " + MessageHelper.sanitizeEveryone(MessageHelper.collapse(msg))).queue();
    }

}
