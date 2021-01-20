package nukeologist.kregbot;

import io.github.classgraph.*;
import nukeologist.kregbot.api.CommandContainer;
import nukeologist.kregbot.impl.CommandImpl;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Class that handles command registry, and
 * scanning of all classes.
 *
 * @author Nukeologist
 */
public class BotManager {

    private static final Set<CommandContainer> commands = new HashSet<>();
    private static final String ROUTE = "nukeologist.kregbot.api.Command";
    private static final String CONTEXT = "nukeologist.kregbot.api.Context";

    private static ScanResult result = new ClassGraph()
            .enableAllInfo()
            .scan();


    /**
     * Gets the scan of all classes.
     *
     * @return the scanresult
     */
    public static ScanResult getResult() {
        return result;
    }

    /**
     * Gets a list of all commands this bot has.
     *
     * @return list containing commands.
     */
    public static Set<CommandContainer> getCommands() {
        if (commands.size() == 0) {
            //initializes the list.
            for (ClassInfo routeClassInfo : result.getClassesWithMethodAnnotation(ROUTE)) {
                for (MethodInfo inf : routeClassInfo.getMethodInfo()) {
                    if (inf.hasAnnotation(ROUTE)) {
                        MethodParameterInfo[] parameters = inf.getParameterInfo();
                        if (inf.isPublic() && inf.isStatic() && parameters.length == 1) {
                            if (parameters[0].getTypeSignatureOrTypeDescriptor() instanceof ClassRefTypeSignature) {
                                ClassRefTypeSignature signature = (ClassRefTypeSignature) parameters[0].getTypeSignatureOrTypeDescriptor();
                                if (signature.getBaseClassName().equals(CONTEXT))
                                    commands.add(new CommandImpl(inf));
                            }
                        }
                    }
                }
            }
            LoggerFactory.getLogger("[BotManager]").info("Found {} commands.", commands.size());
        }
        return commands;
    }

    /**
     * Gets the command associated with given label.
     *
     * @param label the command caller.
     * @return an optional which may have the command.
     */
    public static Optional<CommandContainer> getCommandFromLabel(String label) {
        return getCommands().stream().filter(command -> command.getLabel().equalsIgnoreCase(label)).findAny();
    }

}
