package nukeologist.kregbot.api;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

/**
 * Command context, first param to method {@link Command}s.
 *
 * @author Nukeologist
 */
public interface Context {

    /**
     * Gets if is in guild context.
     *
     * @return true if on a guild, false otherwise
     */
    boolean isGuild();

    /**
     * Either a TextChannel (guild), PrivateChannel or Group...
     *
     * @return the message channel which the command was used.
     */
    MessageChannel getChannel();

    /**
     * The user that sent the message.
     *
     * @return the author that called the command.
     */
    User getAuthor();

    /**
     * The guild member that called the command.
     *
     * @return the author or null if not in guild context.
     */
    Member getMember();

    /**
     * Gets the raw message, including the command itself.
     *
     * @return the message received.
     */
    Message getMessage();

    /**
     * Separates the message into words in an array
     *
     * @return the message stripped of spaces.
     */
    String[] getWords();

    /**
     * Replies to an user, @them.
     *
     * @param replyString the reply after @user.
     */
    void reply(String replyString);

    /**
     * Replies to an user, @them
     *
     * @param message the reply message after @user.
     */
    void reply(Message message);

    /**
     * Sends a message to the channel.
     *
     * @param message the given string.
     */
    void send(String message);

    /**
     * Sends a message to the channel.
     *
     * @param message the given message.
     */
    void send(Message message);

    /**
     * Utility to send a string as a txt file, to this context's channel.
     *
     * @param string   the string containing everything in the file.
     * @param fileName name of the file to send (will add txt to it later)
     */
    void sendFile(String string, String fileName);

    /**
     * Sends a direct message to the command caller.
     *
     * @param message the given message.
     */
    void dm(String message);

    /**
     * Sends a direct message to the command caller.
     *
     * @param message the given message.
     */
    void dm(Message message);

    /**
     * Gets a list of the users mentioned in given command,
     * in the order that they appeared. After that, the mentions will be
     * sanitized in order to prevent echo mentioning.
     *
     * @return a list containing mentioned users.
     */
    List<User> getMentioned();

}
