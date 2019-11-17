package mnm.nightconfig.loader.test;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.toml.TomlFormat;
import mnm.nightconfig.loader.ConfigLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PathAnnotatedTest {

    @Test
    public void testPathAnnotations() throws IOException {

        ConfigLoader<CommentedConfig> loader = ConfigLoader.builder(TomlFormat.instance())
                .loadFrom("string_value = \"hello\"\nint_value = 3\n\n")
                .build();

        AnnotatedConfig config = loader.load(AnnotatedConfig::new);
        assertEquals("hello", config.value1);
        assertEquals(3, config.value2);
    }

    static class AnnotatedConfig {
        @Path("string_value")
        String value1;
        @Path("int_value")
        int value2;
    }

}
