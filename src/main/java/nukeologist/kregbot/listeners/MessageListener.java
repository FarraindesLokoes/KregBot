package nukeologist.kregbot.listeners;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import nukeologist.kregbot.data.MessageValues;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.util.Constants;
import nukeologist.kregbot.util.SaveHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;

/**
 * @author Nukeologist
 */
public class MessageListener implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger("Chat");
    private static final SaveHelper<MessageValues> SAVER = new SaveHelper<>(MessageValues.class);
    private static final Random RANDOM = new Random();
    public static MessageValues VALUES = SAVER.fromJson("increments");

    private static final String[] MEMES = {
            "Foda-se",
            "Seu nazista",
            "Cala a boca",
            "fucking shitstain",
            "filha da puta",
            "faggot",
            "60?",
            "70?",
            "Verdade",
            "Concordo plenamente",
            "Discordo",
            "É... mais ou menos",
            "Certeza?"
    };

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof MessageReceivedEvent)
            onMessage((MessageReceivedEvent) event);
        if (event instanceof GuildMessageReceivedEvent)
            onGuildMessage((GuildMessageReceivedEvent) event);
    }

    private void onMessage(MessageReceivedEvent event) {
        LOG.info("#{} #{} < {} >", event.getChannel(), event.getAuthor(), event.getMessage().getContentRaw());
        if (RANDOM.nextInt(10000) == 69) event.getChannel().sendMessage(getRandomResponse()).queue();
        if (event.getMessage().getContentRaw().startsWith("60?"))
            event.getChannel().sendMessage("Pare com essa viadagem.").queue();
    }

    private void onGuildMessage(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (VALUES == null) VALUES = new MessageValues();
        String msg = event.getMessage().getContentRaw();
        msg = MessageHelper.sanitizeEveryone(msg); //fuck @everyone lmao
        Matcher matcher = Constants.INCREMENT_DECREMENT.matcher(msg);
        boolean matches = matcher.matches();
        if (!matches) {
            String[] words = msg.split("\\s+");
            if (words.length > 1) {
                matcher = Constants.INCREMENT_DECREMENT.matcher(MessageHelper.sanitizeEveryone(words[0] + words[1]));
                if (matcher.matches()) matches = true;
            }
        }
        long guild = event.getGuild().getIdLong();
        if (matches) {
            String key = matcher.group(1);
            int incr = matcher.group(2).equals("++") ? 1 : -1;
            VALUES.add(guild, key, incr);
            SAVER.saveJson(VALUES, "increments");
            event.getChannel().sendMessage(key + " == " + VALUES.getMapOfGuild(guild).get(key)).queue();
        }
        //If an admin wants to set a number in increments
        if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            String[] words = msg.split("\\s+");
            if (words.length > 2 && words[1].equals("==")) {
                if (isNumeric(words[2])) {
                    int num = Integer.parseInt(words[2]);
                    Map<String, Integer> guildMap = VALUES.getMapOfGuild(guild);
                    if (guildMap == null) {
                        VALUES.getMap().put(guild, new HashMap<>());
                        guildMap = VALUES.getMapOfGuild(guild);
                    }
                    if (guildMap.containsKey(words[0])) {
                        guildMap.replace(words[0], num);
                    } else {
                        guildMap.put(words[0], num);
                    }
                    SAVER.saveJson(VALUES, "increments");
                    event.getChannel().sendMessage(words[0] + " == " + words[2]).queue();
                }
            }
        }
    }

    private static String getRandomResponse() {
        return MEMES[RANDOM.nextInt(MEMES.length)];
    }

    private static boolean isNumeric(String str) {
        return str.chars().allMatch(Character::isDigit);
    }
}
