package nukeologist.kregbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import nukeologist.kregbot.listeners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

/**
 * @author Nukeologist
 */
public enum KregBot {

    INSTANCE;

    private static final String VERSION = "1.9.3";

    public static final Logger LOG = LoggerFactory.getLogger("[KregBot|Reborn]");

    private static JDA JDA;

    public void init() throws LoginException {

        CommandListener.init();
        LOG.info("Finished initializing commands.");
        JDA = JDABuilder.createDefault(System.getenv("BOT_TOKEN"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .addEventListeners(new ReadyListener(), new MessageListener(), new CommandListener(), new GuildListener(), new ReplacerListener())
                .setActivity(Activity.playing("with fire"))
                .build();
    }

    public String getVersion() {
        return VERSION;
    }

    public JDA getJDA() {
        return JDA;
    }
}
