package nukeologist.kregbot.api;

/**
 * Context for {@link Command} method listeners.
 * @author Nukeologist
 */
public enum ContextType {

    /*Guilds only*/
    GUILD,
    /*Direct Messages only*/
    PRIVATE,
    /*Common to Direct Messages and Guilds*/
    EITHER;
}
