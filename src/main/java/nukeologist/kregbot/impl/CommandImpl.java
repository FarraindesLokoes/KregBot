package nukeologist.kregbot.impl;

import io.github.classgraph.AnnotationEnumValue;
import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValueList;
import io.github.classgraph.MethodInfo;
import nukeologist.kregbot.api.CommandContainer;
import nukeologist.kregbot.api.Context;
import nukeologist.kregbot.api.ContextType;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.function.Consumer;

/**
 * @author Nukeologist
 */
public class CommandImpl implements CommandContainer {

    private static final String ROUTE = "nukeologist.kregbot.api.Command";
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final MethodType consumerType = MethodType.methodType(void.class, Object.class);
    private static final MethodType mt = MethodType.methodType(Consumer.class);


    private final String label;
    private final Consumer<Context> command;
    private final ContextType type;
    private final boolean canBeCalledByBot;

    public CommandImpl(String label, Method command, ContextType type, boolean canBeCalledByBot) {
        this.command = handleMetaFactory(command);
        this.label = label;
        this.type = type;
        this.canBeCalledByBot = canBeCalledByBot;
    }

    public CommandImpl(MethodInfo info) {
        AnnotationInfo anno = info.getAnnotationInfo(ROUTE);
        if (anno == null) throw new RuntimeException("Invalid command registered...");
        this.command = handleMetaFactory(info.loadClassAndGetMethod());
        AnnotationParameterValueList list = anno.getParameterValues();
        this.label = (String) list.get(0).getValue();
        AnnotationEnumValue en = (AnnotationEnumValue) list.get(1).getValue();
        this.type = (ContextType) en.loadClassAndReturnEnumValue();
        this.canBeCalledByBot = (boolean) list.get(2).getValue();
    }

    @SuppressWarnings("unchecked")
    public static Consumer<Context> handleMetaFactory(Method method) {
        try {
            final MethodHandle mh = lookup.unreflect(method);
            final CallSite site = LambdaMetafactory.metafactory(lookup, "accept", mt, consumerType, mh, mh.type());
            return (Consumer<Context>) site.getTarget().invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to make a lambda out of the command, ", e);
        }

    }

    @Override
    public Consumer<Context> getCommand() {
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
