package nukeologist.kregbot;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import nukeologist.kregbot.listeners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

/**
 * @author Nukeologist
 */
public enum KregBot {

    INSTANCE;

    private static final String VERSION = "1.1.2";

    public static final Logger LOG = LoggerFactory.getLogger("[KregBot|Reborn]");

    private static JDA JDA;

    public void init() throws LoginException {

        CommandListener.init();
        LOG.info("Finished initializing commands.");
        JDA = new JDABuilder(AccountType.BOT)
                .setToken(System.getenv("BOT_TOKEN"))
                .addEventListeners(new ReadyListener(), new MessageListener(), new CommandListener(), new GuildListener(), new ReplacerListener())
                .setActivity(Activity.playing("comunistas do helicóptero"))
                .build();
    }

    public String getVersion() {
        return VERSION;
    }

    public JDA getJDA() {
        return JDA;
    }
}
