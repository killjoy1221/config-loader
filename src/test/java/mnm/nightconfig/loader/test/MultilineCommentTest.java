package mnm.nightconfig.loader.test;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import mnm.nightconfig.loader.Comment;
import mnm.nightconfig.loader.ConfigLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class MultilineCommentTest {

    @Test
    public void testMultiLineComment() throws IOException {

        ConfigLoader<CommentedConfig> loader = ConfigLoader.builder(TomlFormat.instance())
                .setComments(MultiLineCommentConfig.class)
                .build();

        String config = loader.dump(new MultiLineCommentConfig());
        String expected = "#With great power,\n" +
                "#comes great responsitility\n" +
                "superhero = \"Spider-Man\"\n" +
                "\n";

        assertEquals(expected, config.replace("\r\n", "\n"));
    }

    public static class MultiLineCommentConfig {
        @Comment({
                "With great power,",
                "comes great responsitility"
        })
        public String superhero = "Spider-Man";
    }
}
