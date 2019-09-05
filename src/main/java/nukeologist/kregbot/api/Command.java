package nukeologist.kregbot.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to subscribe a method with {@link Context} as first param.
 *
 * @author Nukeologist
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /**
     * if the command matches given name this will be called.
     *
     * @return the command name/label
     */
    String value();

    /**
     * You can exclude types here.
     *
     * @return the context for the command
     */
    ContextType type() default ContextType.EITHER;

    /**
     * If the command can be called by a bot, return true here.
     *
     * @return true if command accepts a bot doing it
     */
    boolean acceptsBots() default false;

}
