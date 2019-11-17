package mnm.nightconfig.loader.test;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import mnm.nightconfig.loader.Comment;
import mnm.nightconfig.loader.ConfigLoader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class MultilineCommentTest {

    @Test
    public void testMultiLineComment() throws IOException {

        StringWriter sw = new StringWriter();

        ConfigLoader<CommentedConfig> loader = ConfigLoader.builder(TomlFormat.instance())
                .withOutput(() -> sw)
                .withComments(MultiLineCommentConfig.class)
                .build();

        loader.save(new MultiLineCommentConfig());

        String expected = "#With great power,\n" +
                "#comes great responsitility\n" +
                "superhero = \"Spider-Man\"\n" +
                "\n";

        assertEquals(expected, sw.toString().replace("\r\n", "\n"));
    }

    public static class MultiLineCommentConfig {
        @Comment({
                "With great power,",
                "comes great responsitility"
        })
        public String superhero = "Spider-Man";
    }
}
