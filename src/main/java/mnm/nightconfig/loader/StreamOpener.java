package mnm.nightconfig.loader;

import java.io.IOException;

/**
 * A fancy supplier that throws {@link IOException}
 *
 * @param <T> The closeable to open
 */
public interface StreamOpener<T> {

    /**
     * Opens the stream for consumption
     *
     * @return The stream
     * @throws IOException
     */
    T openStream() throws IOException;
}
