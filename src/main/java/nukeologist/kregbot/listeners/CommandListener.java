package nukeologist.kregbot.listeners;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nukeologist.kregbot.BotManager;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.api.CommandContainer;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.api.ContextType;
import nukeologist.kregbot.impl.GuildContext;
import nukeologist.kregbot.impl.PrivateContext;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;

/**
 * @author Nukeologist
 */
public class CommandListener extends ListenerAdapter {

    private static final String PREFIX = "!";

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        Context ctx = new GuildContext(event);
        this.handleEvent(ctx);
    }

    @Override
    public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
        Context ctx = new PrivateContext(event);
        this.handleEvent(ctx);
    }

    public static void init() {
        BotManager.getCommands();
    }

    private void handleEvent(Context ctx) {
        String rawMessage = ctx.getMessage().getContentDisplay();
        if (rawMessage.startsWith(PREFIX)) {
            StringBuilder builder = new StringBuilder(rawMessage);
            builder.delete(0, PREFIX.length());
            String msg = MessageHelper.sanitizeEveryone(builder.toString());
            String[] arr = msg.split(" ", 2);
            String firstWord = arr[0];
            BotManager.getCommandFromLabel(firstWord).ifPresent(possible -> {
                ContextType type = possible.getContextType();
                if ((type == ContextType.GUILD && ctx.isGuild()) || (type == ContextType.EITHER) || (type == ContextType.PRIVATE && !ctx.isGuild())) {
                    if (possible.canBeCalledByBot() || (!possible.canBeCalledByBot() && !ctx.getAuthor().isBot())) {
                        boolean correct = executeCommand(possible, ctx);
                        if (!correct) ctx.send("A problem occurred when executing command. Contact the dev");
                    }
                }
            });
        }
    }

    private boolean executeCommand(CommandContainer command, Context ctx) {
        Method method = command.getCommand();
        try {
            method.invoke(null, ctx);
        } catch (Exception e) {
            System.out.println("Failed to invoke command: " + command.getLabel());
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
