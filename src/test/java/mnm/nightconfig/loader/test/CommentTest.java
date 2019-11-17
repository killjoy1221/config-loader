package mnm.nightconfig.loader.test;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.toml.TomlFormat;
import mnm.nightconfig.loader.Comment;
import mnm.nightconfig.loader.ConfigLoader;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CommentTest {

    @Test
    public void testCommentAnnotations() throws IOException {

        ConfigLoader<CommentedConfig> loader = ConfigLoader.builder(TomlFormat.instance())
                .setComments(AnnotatedCommentedConfig.class)
                .build();

        String config = loader.dump(new AnnotatedCommentedConfig());
        String expectedConfig = "" +
                "#it's a hello\n" +
                "foo_bar = \"Hello world\"\n" +
                "\n" +
                "#It's a nested config comment\n" +
                "[nested]\n" +
                "\t#Yes!\n" +
                "\tbar = \"noooo!\"\n\n";
        assertEquals(expectedConfig, config.replace("\r\n", "\n"));
    }

    public static class AnnotatedCommentedConfig {

        @Path("foo_bar")
        @Comment("it's a hello")
        public String foobar = "Hello world";

        @Comment("It's a nested config comment")
        NestedCommentConfig nested = new NestedCommentConfig();

        static class NestedCommentConfig {
            @Comment("Yes!")
            String bar = "noooo!";
        }
    }
}
