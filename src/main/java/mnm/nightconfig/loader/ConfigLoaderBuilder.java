package mnm.nightconfig.loader;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigFormat;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ConfigLoaderBuilder<T extends Config> implements ConfigLoader.Builder<T> {

    private final ConfigFormat<T> format;
    private StreamOpener<Reader> reader;
    private StreamOpener<Writer> writer;

    private CommentedConfig comments = CommentedConfig.inMemory();

    ConfigLoaderBuilder(ConfigFormat<T> format) {
        this.format = format;
    }

    public ConfigLoader.Builder<T> withReader(StreamOpener<Reader> reader) {
        this.reader = reader;
        return this;
    }

    public ConfigLoader.Builder<T> withWriter(StreamOpener<Writer> writer) {
        this.writer = writer;
        return this;
    }

    @Override
    public ConfigLoader.Builder<T> withComments(Class<?> classs) {
        addComments(Collections.emptyList(), classs);
        return this;
    }

    private void addComments(List<String> parentPath, Class<?> classs) {
        for (Field f : classs.getDeclaredFields()) {
            if (!f.isSynthetic() && !Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
                List<String> path = new ArrayList<>(parentPath);
                path.addAll(AnnotationUtils.getPath(f));
                String comment = AnnotationUtils.getComment(f);
                if (comment != null) {
                    comments.setComment(path, comment);
                    Class<?> type = f.getType();
                    if (format.supportsType(type)) {
                        comments.set(path, "dummy");
                    } else {
                        addComments(path, type);
                    }
                }
            }
        }
    }

    public ConfigLoader<T> build() {
        return new ConfigLoaderImpl<>(format, reader, writer, comments.unmodifiable());
    }
}
