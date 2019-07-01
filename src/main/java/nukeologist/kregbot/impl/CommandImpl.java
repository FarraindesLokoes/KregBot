package nukeologist.kregbot.impl;

import io.github.classgraph.AnnotationEnumValue;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.MethodInfo;
import nukeologist.kregbot.api.CommandContainer;
import nukeologist.kregbot.api.ContextType;

import java.lang.reflect.Method;

/**
 * @author Nukeologist
 */
public class CommandImpl implements CommandContainer {

    private static final String ROUTE = "nukeologist.kregbot.api.Command";

    private final String label;
    private final Method command;
    private final ContextType type;
    private final boolean canBeCalledByBot;

    public CommandImpl(String label, Method command, ContextType type, boolean canBeCalledByBot) {
        this.command = command;
        this.label = label;
        this.type = type;
        this.canBeCalledByBot = canBeCalledByBot;
    }

    public CommandImpl(MethodInfo info) {
        AnnotationInfo anno = info.getAnnotationInfo(ROUTE);
        if (anno == null) throw new RuntimeException("Invalid command registered...");
        this.command = info.loadClassAndGetMethod();
        AnnotationParameterValueList list = anno.getParameterValues();
        this.label = (String) list.get(0).getValue();
        AnnotationEnumValue en = (AnnotationEnumValue) list.get(1).getValue();
        this.type = (ContextType) en.loadClassAndReturnEnumValue();
        this.canBeCalledByBot = (boolean) list.get(2).getValue();
    }

    @Override
    public Method getCommand() {
        return command;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public ContextType getContextType() {
        return type;
    }

    @Override
    public boolean canBeCalledByBot() {
        return canBeCalledByBot;
    }
}
