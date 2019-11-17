package mnm.nightconfig.loader;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.UnmodifiableCommentedConfig;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

class ConfigLoaderImpl<T extends Config> implements ConfigLoader<T> {

    private final ConfigFormat<T> format;
    @Nullable
    private final StreamOpener<Reader> input;
    @Nullable
    private final StreamOpener<Writer> output;
    private final UnmodifiableCommentedConfig comments;

    ConfigLoaderImpl(ConfigFormat<T> format,
                     @Nullable StreamOpener<Reader> input,
                     @Nullable StreamOpener<Writer> output,
                     UnmodifiableCommentedConfig comments) {
        this.format = format;
        this.input = input;
        this.output = output;
        this.comments = comments;
    }

    @Override
    public ConfigFormat<T> getFormat() {
        return format;
    }

    public UnmodifiableCommentedConfig getComments() {
        return comments;
    }

    @Override
    public Reader openReader() throws IOException {
        Preconditions.checkState(input != null);
        return input.openStream();
    }

    @Override
    public Writer openWriter() throws IOException {
        Preconditions.checkState(output != null);
        return output.openStream();
    }
}
