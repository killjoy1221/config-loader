package mnm.nightconfig.loader.test;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.conversion.Path;
import com.electronwill.nightconfig.toml.TomlFormat;
import mnm.nightconfig.loader.Comment;
import mnm.nightconfig.loader.ConfigLoader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CommentTest {

    @Test
    public void testCommentAnnotations() throws IOException {

        StringWriter sw = new StringWriter();

        ConfigLoader<CommentedConfig> loader = ConfigLoader.builder(TomlFormat.instance())
                .withOutput(() -> sw)
                .withComments(AnnotatedCommentedConfig.class)
                .build();

        loader.save(new AnnotatedCommentedConfig());
        String expectedConfig = "" +
                "#it's a hello\n" +
                "foo_bar = \"Hello world\"\n" +
                "\n" +
                "#It's a nested config comment\n" +
                "[nested]\n" +
                "\t#Yes!\n" +
                "\tbar = \"noooo!\"\n\n";
        assertEquals(expectedConfig, sw.toString().replace("\r\n", "\n"));
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
