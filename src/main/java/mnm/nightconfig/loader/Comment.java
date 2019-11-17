package mnm.nightconfig.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a comment for a configuration. To make effective use of this, the comments need to be added via
 * {@link ConfigLoader.Builder#setComments(Class)}. The comments will be automatically added to the output when
 * {@link ConfigLoader#save(Object)} is called.
 *
 * @see ConfigLoader.Builder#setComments(Class)
 * @see ConfigLoader#save(Object)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Comment {
    /**
     * The comment of the config value. Each array element will be on a separate line.
     */
    String[] value();
}
