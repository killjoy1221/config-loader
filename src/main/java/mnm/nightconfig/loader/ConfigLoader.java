package mnm.nightconfig.loader;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.UnmodifiableCommentedConfig;
import com.electronwill.nightconfig.core.conversion.ObjectConverter;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.function.Supplier;

public interface ConfigLoader<T extends Config> {

    ConfigFormat<T> getFormat();

    UnmodifiableCommentedConfig getComments();

    Reader openReader() throws IOException;

    Writer openWriter() throws IOException;

    default T load() throws IOException {
        try (Reader r = openReader()) {
            return getFormat().createParser().parse(r);
        }
    }

    default <C> C load(Supplier<C> func) throws IOException {
        ObjectConverter converter = new ObjectConverter();
        return converter.toObject(load(), func);
    }

    default void save(T config) throws IOException {
        if (getFormat().supportsComments()) {
            ((CommentedConfig) config).putAllComments(getComments());
        }
        try (Writer w = openWriter()) {
            getFormat().createWriter().write(config, w);
        }
    }

    default <C> void save(C object) throws IOException {
        ObjectConverter converter = new ObjectConverter();
        save(converter.toConfig(object, getFormat()::createConfig));
    }

    static <C extends Config> Builder<C> builder(ConfigFormat<C> format) {
        return new ConfigLoaderBuilder<>(format);
    }

    interface Builder<T extends Config> {

        Builder<T> withInput(StreamOpener<Reader> input);

        Builder<T> withOutput(StreamOpener<Writer> output);

        Builder<T> withComments(Class<?> classs);

        ConfigLoader<T> build();

        default Builder<T> loadFrom(String configString) {
            return withInput(() -> new StringReader(configString));
        }

        default Builder<T> loadFrom(URL url, Charset charset) {
            return withInput(() -> new InputStreamReader(url.openStream(), charset));
        }

        default Builder<T> loadFrom(Path path, Charset charset) {
            return withInput(() -> Files.newBufferedReader(path, charset));
        }

        default Builder<T> writeTo(Path path, Charset charset, OpenOption... options) {
            return withOutput(() -> Files.newBufferedWriter(path, charset, options));
        }

        default Builder<T> withPath(Path path, Charset charset, OpenOption... options) {
            return loadFrom(path, charset).writeTo(path, charset, options);
        }
    }
}
