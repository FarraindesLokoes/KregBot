package nukeologist.kregbot.listeners;

import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import nukeologist.kregbot.data.MessageReplacer;
import nukeologist.kregbot.util.Constants;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.util.SaveHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author SpicyFerret
 * */
public class ReplacerListener implements EventListener {
    private static final Logger LOG = LoggerFactory.getLogger("Chat");
    public static final SaveHelper<MessageReplacer> SAVER = new SaveHelper<>(MessageReplacer.class);
    public static MessageReplacer VALUES = SAVER.fromJson("replacers");


    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof GuildMessageReceivedEvent)
            onGuildMessage((GuildMessageReceivedEvent) event);
    }

    private void onGuildMessage(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().getContentRaw().charAt(0) == '!') return;
        if (VALUES == null) VALUES = new MessageReplacer();
        String msgFull = event.getMessage().getContentRaw();
        Map<String, String> map = VALUES.getMapOfGuild(event.getGuild().getIdLong());
        boolean replaced = false;

        // Split the maps
        Map<String, String> mapRegex = map.entrySet().stream()
            .filter(e -> e.getKey().startsWith("regex:"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        Map<String, String> mapNormal = map.entrySet().stream()
            .filter(e -> !e.getKey().startsWith("regex:"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Check for in regex map for a match and replace
        for (Map.Entry<String, String> entry : mapRegex.entrySet()) {
            String key = entry.getKey().substring(6);
            String value = entry.getValue();
            if (msgFull.contains(key)) {
                replaced = true;
                msgFull = msgFull.replace(key, value);
            }
        }
        String[] msg = msgFull.split(" ");

        // Check for in normal map for a match
        for (int i = 0; i < msg.length; i++) {
            String str = msg[i];
            String c = String.valueOf(str.charAt(str.length() - 1));
            str = str.replaceAll("[!,.*]", "");
            if (mapNormal.containsKey(str)) {
                replaced = true;
                String fstr = mapNormal.get(str);
                if (fstr.endsWith(" ")) {
                    fstr = fstr.substring(0, fstr.length() - 1);
                }
                msg[i] = fstr + (Constants.PUNCTUATION.matcher(c).matches() ? c : "");
            }
        }
        if (!replaced) return;
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(event.getAuthor().getAsTag() + " says: " + MessageHelper.collapse(msg)).queue();
    }


}
