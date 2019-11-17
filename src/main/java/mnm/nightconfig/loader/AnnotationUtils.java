package mnm.nightconfig.loader;

import com.electronwill.nightconfig.core.conversion.AdvancedPath;
import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.core.utils.StringUtils;

import javax.annotation.Nullable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class AnnotationUtils {

    private AnnotationUtils() {
    }

    static List<String> getPath(Field field) {
        List<String> annotatedPath = getPath((AnnotatedElement) field);
        return annotatedPath == null ? Collections.singletonList(field.getName()) : annotatedPath;
    }

    private static List<String> getPath(AnnotatedElement annotatedElement) {
        Path path = annotatedElement.getDeclaredAnnotation(Path.class);
        if (path != null) {
            return StringUtils.split(path.value(), '.');
        }
        AdvancedPath advancedPath = annotatedElement.getDeclaredAnnotation(AdvancedPath.class);
        if (advancedPath != null) {
            return Arrays.asList(advancedPath.value());
        }
        return null;
    }

    @Nullable
    static String getComment(AnnotatedElement annotatedElement) {
        Comment comment = annotatedElement.getDeclaredAnnotation(Comment.class);
        return comment != null ? String.join("\n", comment.value()) : null;
    }

}
