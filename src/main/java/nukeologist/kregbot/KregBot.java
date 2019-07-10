package nukeologist.kregbot;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import nukeologist.kregbot.listeners.CommandListener;
import nukeologist.kregbot.listeners.GuildListener;
import nukeologist.kregbot.listeners.MessageListener;
import nukeologist.kregbot.listeners.ReadyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;

/**
 * @author Nukeologist
 */
public enum KregBot {

    INSTANCE;

    private static final String version = "0.1";

    public static final Logger LOG = LoggerFactory.getLogger("[KregBot|Reborn]");

    public void init() throws LoginException, IOException {

        CommandListener.init();
        LOG.info("Finished initializing commands.");
        new JDABuilder(AccountType.BOT)
                .setToken(System.getenv("BOT_TOKEN"))
                .addEventListeners(new ReadyListener(), new MessageListener(), new CommandListener(), new GuildListener())
                .setActivity(Activity.playing("with your mother"))
                .build();
    }

    public String getVersion() {
        return version;
    }
}
