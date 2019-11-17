package mnm.nightconfig.loader.test;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import mnm.nightconfig.loader.ConfigLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class LoaderTest {

    @Test
    public void testConversion() throws IOException {

        ConfigLoader<CommentedConfig> loader = ConfigLoader.builder(TomlFormat.instance())
                .loadFrom("value1 = \"hello\"\nvalue2 = 3\n")
                .build();

        MyConfig config = loader.load(MyConfig::new);

        assertEquals("hello", config.value1);
        assertEquals(3, config.value2);
    }

    static class MyConfig {
        String value1;
        int value2;
    }
}
