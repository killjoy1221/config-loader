package mnm.nightconfig.loader.test;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import mnm.nightconfig.loader.ConfigLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NestedConfigTest {

    @Test
    public void testNestedConfigs() throws IOException {

        ConfigLoader<CommentedConfig> loader = ConfigLoader.builder(TomlFormat.instance())
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
