package nukeologist.kregbot.commands;

import net.dv8tion.jda.api.Permission;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandHelp;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.api.ContextType;
import nukeologist.kregbot.data.EchoMessage;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.util.SaveHelper;

import java.util.*;
import java.util.stream.Collectors;

public class EchoCommand {

    private static final Random RANDOM = new Random();

    private static SaveHelper<EchoStorage> SAVER = new SaveHelper<>(EchoStorage.class);
    private static final String ECHOMESSAGES = "echoMessages";
    private static EchoStorage echoStorage = SAVER.fromJson(ECHOMESSAGES);

    @Command(value = "echo", type = ContextType.GUILD)
    public static void echo(Context ctx) {
        if (echoStorage == null) echoStorage = new EchoStorage();
        final String[] params = ctx.getWords();
        if (params.length == 1) {
            ctx.send("Need some kind of identifier. You can use random with RAND as second param, or get a list with LIST as well.");
            return;
        }
        switch (params[1].toLowerCase(Locale.ROOT)) {
            case "rand":
            case "r":
            case "random":
                handleRandom(ctx);
                break;
            case "list":
            case "l":
                handleList(ctx);
                break;
            case "add":
            case "a":
                handleAdding(ctx);
                break;
            case "update":
            case "u":
                handleUpdate(ctx);
                break;
            case "label":
                handleLabelUpdate(ctx);
                break;
            default:
                handleDefault(ctx);

        }
    }

    @CommandHelp("echo")
    public static void helpEcho(Context ctx) {
        ctx.send("Make Kreg remember messages, quotes, whatever you want. Usage: \n" +
                "!echo ADD NAME MESSAGE to save message with label NAME or\n !echo RAND for a random quote or\n" +
                "!echo UPDATE NAME to update the message with label NAME or\n !echo NAME to see the message with label NAME\n" +
                "!echo label OLD NEW to update message with label OLD to label NEW");
    }

    private static void handleRandom(final Context ctx) {
        final long guild = ctx.getMember().getGuild().getIdLong();
        final List<EchoMessage> messages = echoStorage.ECHOS.get(guild);
        if (messages == null) {
            ctx.send("No echos/quotes here bro... Please add one using !echo add NAME MESSAGE.");
            return;
        }
        ctx.send(messages.get(RANDOM.nextInt(messages.size())).getMessageContent());
    }

    private static void handleList(final Context ctx) {
        final long guild = ctx.getMember().getGuild().getIdLong();
        final List<EchoMessage> messages = echoStorage.ECHOS.get(guild);
        if (messages != null) {
            messages.forEach(echo -> ctx.send(echo.getLabel() + MessageHelper.makeBold(" has owner " + echo.getOwner(ctx).getEffectiveName()) + "\n"));
        }
    }

    private static void handleAdding(final Context ctx) {
        final String[] msg = ctx.getWords();
        final long guild = ctx.getMember().getGuild().getIdLong();
        final long owner = ctx.getMember().getIdLong();
        final List<EchoMessage> messages = echoStorage.ECHOS.get(guild);
        final String label = msg[2];
        final String save = MessageHelper.sanitizeEveryone(MessageHelper.collapse(msg, 3, msg.length));
        if (messages == null) {
            final ArrayList<EchoMessage> echo = new ArrayList<>();
            echo.add(new EchoMessage(owner, label).updateContent(save));
            echoStorage.ECHOS.put(guild, echo);
            SAVER.saveJson(echoStorage, ECHOMESSAGES);
            ctx.send("Added.");
            return;
        }
        final Set<String> labels = messages.stream().map(EchoMessage::getLabel).collect(Collectors.toSet());
        if (labels.contains(label)) {
            ctx.send("There already exists one with the label you gave me. Do you want to UPDATE instead?");
            return;
        }
        messages.add(new EchoMessage(owner, label).updateContent(save));
        SAVER.saveJson(echoStorage, ECHOMESSAGES);
        ctx.send("Added.");
    }

    private static void handleUpdate(Context ctx) {
        final long guild = ctx.getMember().getGuild().getIdLong();
        final long person = ctx.getMember().getIdLong();
        final List<EchoMessage> messages = echoStorage.ECHOS.get(guild);
        if (messages == null) {
            ctx.send("There is nothing to update, wtf are you doing bro (NO MESSAGES IN THIS GUILD)");
            return;
        }
        final String[] params = ctx.getWords();
        if (params.length < 4) {
            ctx.send("Not enough to update, did you send the label and something to update to?");
            return;
        }
        final String label = params[2];
        final Optional<EchoMessage> echo = messages.stream().filter(e -> e.getLabel().equals(label)).findAny();
        if (echo.isEmpty()) {
            ctx.send("No message with given label. Please try again changing the second param.");
            return;
        }
        EchoMessage e = echo.get();

        if (ctx.getMember().hasPermission(Permission.ADMINISTRATOR) && e.getOwner(ctx).getIdLong() != person) {
            ctx.send("You are not the owner, but you have perms because admin.");
        } else if (e.getOwner(ctx).getIdLong() != person) {
            ctx.send("You cant update ones made by other people.");
            return;
        }
        final String save = MessageHelper.sanitizeEveryone(MessageHelper.collapse(params, 3, params.length));
        e.updateContent(save);
        SAVER.saveJson(echoStorage, ECHOMESSAGES);
        ctx.send("Updated.");
    }

    private static void handleDefault(Context ctx) {
        final long guild = ctx.getMember().getGuild().getIdLong();
        final List<EchoMessage> messages = echoStorage.ECHOS.get(guild);
        if (messages == null) {
            ctx.send("No messages to display in this guild bruh");
            return;
        }
        final String[] words = ctx.getWords();
        final String label = words[1];
        final Optional<EchoMessage> echo = messages.stream().filter(e -> e.getLabel().equals(label)).findAny();
        if (echo.isEmpty()) {
            ctx.send("No message was found with that second param bruh");
            return;
        }
        ctx.send(echo.get().getMessageContent());
    }

    private static void handleLabelUpdate(Context ctx) {
        final String[] words = ctx.getWords();
        if (words.length < 4) {
            ctx.send("What do you want to update the name with? Not enough params.");
            return;
        }
        final long guild = ctx.getMember().getGuild().getIdLong();
        final List<EchoMessage> messages = echoStorage.ECHOS.get(guild);
        if (messages == null) {
            ctx.send("No messages in this guild bruh");
            return;
        }
        final Optional<EchoMessage> echo = messages.stream().filter(e -> e.getLabel().equals(words[2])).findAny();
        if (echo.isEmpty()) {
            ctx.send("Message with given label [" + words[2] + "] was not found.");
            return;
        }
        final EchoMessage msg = echo.get();
        msg.setLabel(words[3]);
        SAVER.saveJson(echoStorage, ECHOMESSAGES);
        ctx.send("Updated " + words[2] + " to use " + words[3] + " instead.");
    }

    private static class EchoStorage {
        private Map<Long, List<EchoMessage>> ECHOS = new HashMap<>();
    }
}
