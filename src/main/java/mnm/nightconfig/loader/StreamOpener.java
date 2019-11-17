package mnm.nightconfig.loader;

import java.io.IOException;

public interface StreamOpener<T> {

    T openStream() throws IOException;
}
