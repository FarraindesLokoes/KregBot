package nukeologist.kregbot.commands;

import io.github.classgraph.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import nukeologist.kregbot.BotManager;
import nukeologist.kregbot.impl.CommandImpl;
import nukeologist.kregbot.util.MessageHelper;
import nukeologist.kregbot.api.Command;
import nukeologist.kregbot.api.CommandContainer;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.KregBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Nukeologist
 */
public class HelpCommands {

    private static final String ROUTE = "nukeologist.kregbot.api.CommandHelp";
    private static final String CONTEXT = "nukeologist.kregbot.api.Context";

    private static List<CommandContainer> commands;
    private static Map<String, Consumer<Context>> helpMethods;

    @Command("about")
    public static void about(Context ctx) {
        ctx.send(MessageHelper.makeBold("KregBot | Reborn") + "\nWritten by: Nukeologist and SpicyFerret\nVersion: " + KregBot.INSTANCE.getVersion());
    }

    @Command("help")
    public static void help(Context ctx) {
        var params = ctx.getWords();
        if (params.length == 1 || params[1].equals("help")) {
            ctx.send("Get a list of commands with !commands, to get help with a specific one use !help COMMANDHERE");
            return;
        }
        var map = getHelpMethods();
        try {
            map.getOrDefault(params[1], c -> c.send("Given command has no help for it. Either it is self explanatory, or we actually forgot to implement."))
                    .accept(ctx);
        } catch (Exception e) {
            ctx.send("Problem when executing help command, contact author.");
            KregBot.LOG.info("Failed to execute help command with label {}", params[1]);
            e.printStackTrace();
        }
    }

    @Command("commands")
    public static void displayCommands(Context ctx) {
        ctx.send("Displaying all commands:\n");
        EmbedBuilder eb = new EmbedBuilder();
        StringBuilder label = new StringBuilder();
        StringBuilder context = new StringBuilder();
        StringBuilder bot = new StringBuilder();
        eb.setTitle("Command List");
        for (CommandContainer command : getCommands()) {
            label.append("!").append(command.getLabel()).append("\n");
            context.append(command.getContextType().toString()).append("\n");
            bot.append(command.canBeCalledByBot()).append("\n");
        }
        eb.addField("Command", label.toString(), true);
        eb.addField("Guild or Private", context.toString(), true);
        eb.addField("Bots can call this", bot.toString(), true);
        MessageBuilder msg = new MessageBuilder();
        msg.setEmbed(eb.build());
        ctx.send(msg.build());
    }

    /*Lazy initializes*/
    private static List<CommandContainer> getCommands() {
        if (commands == null) commands = BotManager.getCommands();
        return commands;
    }

    private static Map<String, Consumer<Context>> getHelpMethods() {
        if (helpMethods == null) {
            helpMethods = new HashMap<>();
            ScanResult result = BotManager.getResult();
            for (ClassInfo routeClassInfo : result.getClassesWithMethodAnnotation(ROUTE)) {
                for (MethodInfo inf : routeClassInfo.getMethodInfo()) {
                    if (inf.hasAnnotation(ROUTE)) {
                        MethodParameterInfo[] parameters = inf.getParameterInfo();
                        if (inf.isPublic() && inf.isStatic() && parameters.length == 1) {
                            if (parameters[0].getTypeSignatureOrTypeDescriptor() instanceof ClassRefTypeSignature) {
                                ClassRefTypeSignature signature = (ClassRefTypeSignature) parameters[0].getTypeSignatureOrTypeDescriptor();
                                if (signature.getBaseClassName().equals(CONTEXT)) {
                                    AnnotationInfo anno = inf.getAnnotationInfo(ROUTE);
                                    AnnotationParameterValueList list = anno.getParameterValues();
                                    String label = (String) list.get(0).getValue();
                                    helpMethods.putIfAbsent(label, CommandImpl.handleMetaFactory(inf.loadClassAndGetMethod()));
                                }
                            }
                        }
                    }
                }
            }
        }
        return helpMethods;
    }

}
