package nukeologist.kregbot.api;

import java.lang.reflect.Method;

/**
 * A command annotated with {@link Command} will get associated with this interface.
 * Use {@link nukeologist.kregbot.BotManager} to get an object associated with the command.
 *
 * @author Nukeologist
 */
public interface CommandContainer {

    /**
     * Gets the method executor of given command.
     *
     * @return the static method.
     */
    Method getCommand();

    /**
     * Returns the label of the command.
     *
     * @return the label which calls this command.
     */
    String getLabel();

    /**
     * Gets if the command environments.
     *
     * @return the context type of command.
     */
    ContextType getContextType();

    /**
     * Gets if the command can be called by a bot.
     *
     * @return true if can be called by a bot user.
     */
    boolean canBeCalledByBot();
}
