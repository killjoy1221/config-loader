package mnm.nightconfig.loader.test;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import mnm.nightconfig.loader.ConfigLoader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NestedConfigTest {

    @Test
    public void testNestedConfigs() throws IOException {

        StringWriter sw = new StringWriter();
        ConfigLoader<CommentedConfig> loader = ConfigLoader.builder(TomlFormat.instance())
                .withOutput(() -> sw)
                .loadFrom("[nested]\n    nestedValue=\"hello\"\n\n")
                .build();

        NestedConfig config = loader.load(NestedConfig::new);
        assertEquals("hello", config.nested.nestedValue);
    }

    static class NestedConfig {

        NestedConfig2 nested;

        static class NestedConfig2 {
            String nestedValue;
        }
    }

}
