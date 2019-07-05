package nukeologist.kregbot.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used in a method that should be called whenever
 * someone !help a given command. First param in method should
 * be a {@link Context} or this will not be called. The method
 * MUST be static and return void.
 *
 * @author Nukeologist
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandHelp {

    /**
     * If the param in [!help param] matches this value, this will be called
     *
     * @return the String which represents the command
     */
    String value();
}
