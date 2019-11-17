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

/**
 * A configuration loader for quickly loading and saving a config from a consistent location or source.
 *
 * <p>
 * To use, create a new loader from a builder. Then you can load or save whenever.
 * </p>
 * <pre>
 *      ConfigLoader&lt;CommentedConfig&gt; loader = ConfigLoader.builder(TomlFormat.instance())
 *              .withPath(Paths.get("config.cfg"), StandardCharsets.UTF_8)
 *              .build();
 *
 *      try {
 *          CommentedConfig config = loader.load();
 *          String hello = config.getString("hello");
 *          config.set("hello", "goodbye");
 *          loader.save(config);
 *      } catch (IOException e) {
 *          logger.warn("config loading or saving failed", e);
 *      }
 * </pre>
 *
 * @param <T> The config element type
 */
public interface ConfigLoader<T extends Config> {

    /**
     * Gets the {@link ConfigFormat} used to read, write, and create the config.
     *
     * @return The config format
     */
    ConfigFormat<T> getFormat();

    /**
     * Gets the {@link UnmodifiableCommentedConfig} containing the comments for the output. The values in the config
     * are dummy values.
     *
     * @return The config comments
     */
    UnmodifiableCommentedConfig getComments();

    /**
     * Opens the reader for loading from disk.
     *
     * @return The reader
     * @throws IllegalStateException If the loader does not support reading
     * @throws IOException           If the reader cannot be opened
     */
    Reader openReader() throws IOException;

    /**
     * Opens the writer for saving to disk.
     *
     * @return The writer
     * @throws IllegalStateException If the loader does not support writing
     * @throws IOException           If the writer cannot be opened
     */
    Writer openWriter() throws IOException;

    /**
     * Loads the configuration and returns the {@link Config} object.
     *
     * @return The config object
     * @throws IOException
     */
    default T load() throws IOException {
        try (Reader r = openReader()) {
            return getFormat().createParser().parse(r);
        }
    }

    /**
     * Loads the configuration and inserts it into a new object {@code C}
     *
     * @param func The factory supplier of the object
     * @param <C>  The type of object to return
     * @return The object
     * @throws IOException
     */
    default <C> C load(Supplier<C> func) throws IOException {
        return new ObjectConverter().toObject(load(), func);
    }

    /**
     * Saves the configuration using the output writer.
     *
     * @param config The config object
     * @throws IOException
     */
    default void save(T config) throws IOException {
        if (getFormat().supportsComments()) {
            ((CommentedConfig) config).putAllComments(getComments());
        }
        try (Writer w = openWriter()) {
            getFormat().createWriter().write(config, w);
        }
    }

    /**
     * Saves the object after converting it to {@code T}.
     *
     * @param object The object to save
     * @param <C>    The type of the object.
     * @throws IOException
     */
    default <C> void save(C object) throws IOException {
        save(new ObjectConverter().toConfig(object, getFormat()::createConfig));
    }

    /**
     * Creates a new builder using the specified format. e.g.
     *
     * <pre>
     * ConfigLoader.Builder&lt;CommentedConfig&gt; builder = ConfigLoader.builder(TomlFormat.instance());
     * </pre>
     *
     * @param format The config format
     * @param <C>    The config type (Config or CommentedConfig)
     * @return The loader builder
     */
    static <C extends Config> Builder<C> builder(ConfigFormat<C> format) {
        return new ConfigLoaderBuilder<>(format);
    }

    /**
     * A builder for a {@link ConfigLoader}
     *
     * @param <T> The config type (Config or CommentedConfig)
     */
    interface Builder<T extends Config> {

        /**
         * Sets the given supplier output to be used to read the config when loading.
         *
         * @param input A supplier of a Reader
         * @return This object
         */
        Builder<T> withInput(StreamOpener<Reader> input);

        /**
         * Sets the given supplier output to be used to write the config when saving.
         *
         * @param output A supplier of a Writer.
         * @return This object
         */
        Builder<T> withOutput(StreamOpener<Writer> output);

        /**
         * Adds comments to the output of the loader. Subsequent calls may overwrite previous values or throw exceptions
         * if there are conflicting types.
         * <p>
         * The class given should have non-static fields annotated with {@link Comment}.
         *
         * @param classs The class containing comment annotated fields
         * @return This instance
         */
        Builder<T> withComments(Class<?> classs);

        /**
         * Builds the {@link ConfigLoader}.
         *
         * @return The loader
         */
        ConfigLoader<T> build();

        /**
         * Uses the given constant string as the config. This effectively makes the config static.
         * <p>
         * Useful for testing.
         *
         * @param configString The config string
         * @return This instance
         */
        default Builder<T> loadFrom(String configString) {
            return withInput(() -> new StringReader(configString));
        }

        /**
         * Sets the given url to be used for reading
         *
         * @param url     The load url
         * @param charset The charset
         * @return This instance
         */
        default Builder<T> readFrom(URL url, Charset charset) {
            return withInput(() -> new InputStreamReader(url.openStream(), charset));
        }

        /**
         * Sets the given path to be used for reading
         *
         * @param path    The load path
         * @param charset The charset
         * @return This instance
         */
        default Builder<T> readFrom(Path path, Charset charset) {
            return withInput(() -> Files.newBufferedReader(path, charset));
        }

        /**
         * Sets the given path to be used for writing
         *
         * @param path    The save path
         * @param charset The charset
         * @param options The open options
         * @return This instance
         */
        default Builder<T> writeTo(Path path, Charset charset, OpenOption... options) {
            return withOutput(() -> Files.newBufferedWriter(path, charset, options));
        }

        /**
         * Sets the given path to be used for both reading and writing.
         *
         * @param path    The save and load path
         * @param charset The charset
         * @param options The open options
         * @return This instance
         */
        default Builder<T> withPath(Path path, Charset charset, OpenOption... options) {
            return readFrom(path, charset).writeTo(path, charset, options);
        }
    }
}
