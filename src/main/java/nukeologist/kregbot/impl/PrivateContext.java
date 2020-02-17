package nukeologist.kregbot.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.util.Constants;

import java.util.List;

/**
 * @author Nukeologist
 */
public class PrivateContext implements Context {

    private final PrivateMessageReceivedEvent event;
    private final List<User> userList;

    public PrivateContext(PrivateMessageReceivedEvent event) {
        this.event = event;
        this.userList = event.getMessage().getMentionedUsers();
    }

    @Override
    public boolean isGuild() {
        return false;
    }

    @Override
    public MessageChannel getChannel() {
        return event.getChannel();
    }

    @Override
    public User getAuthor() {
        return event.getAuthor();
    }

    @Override
    public Member getMember() {
        return null;
    }

    @Override
    public Message getMessage() {
        return event.getMessage();
    }

    @Override
    public String[] getWords() {
        return Constants.SPACES.split(getMessage().getContentRaw()); //.split("\\s+");
    }

    @Override
    public void reply(String replyString) {
        event.getChannel().sendMessage(replyUser(event.getAuthor()) + " " + replyString).queue();
    }

    @Override
    public void reply(Message message) {
        event.getChannel().sendMessage(replyUser(event.getAuthor()) + " " + message).queue();
    }

    @Override
    public void send(String message) {
        event.getChannel().sendMessage(message).queue();
    }

    @Override
    public void send(Message message) {
        event.getChannel().sendMessage(message).queue();
    }

    @Override
    public void dm(String message) {
        this.send(message);
    }

    @Override
    public void dm(Message message) {
        this.send(message);
    }

    @Override
    public List<User> getMentioned() {
        return userList;
    }

    private static String replyUser(User user) {
        return user.getAsMention();
    }

}
