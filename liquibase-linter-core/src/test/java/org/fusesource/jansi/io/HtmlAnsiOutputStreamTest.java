package org.fusesource.jansi.io;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="http://code.dblock.org">Daniel Doubrovkine</a>
 */
class HtmlAnsiOutputStreamTest {

    @Test
    void testNoMarkup() throws IOException {
        assertThat(colorize("line")).isEqualTo("line");
    }

    @Test
    void testClear() throws IOException {
        assertThat(colorize("[0m[K")).isEmpty();
        assertThat(colorize("[0mhello world")).isEqualTo("hello world");
    }

    @Test
    void testBold() throws IOException {
        assertThat(colorize("[1mhello world")).isEqualTo("<b>hello world</b>");
    }

    @Test
    void testGreen() throws IOException {
        assertThat(colorize("[32mhello world")).isEqualTo("<span style=\"color: green;\">hello world</span>");
    }

    @Test
    void testGreenOnWhite() throws IOException {
        assertThat(colorize("[47;32mhello world")).isEqualTo(
            "<span style=\"background-color: white;\"><span style=\"color: green;\">hello world</span></span>"
        );
    }

    @Test
    void testEscapeHtml() throws IOException {
        assertThat(colorize("\"")).isEqualTo("&quot;");
        assertThat(colorize("&")).isEqualTo("&amp;");
        assertThat(colorize("<")).isEqualTo("&lt;");
        assertThat(colorize(">")).isEqualTo("&gt;");
        assertThat(colorize("\"&<>")).isEqualTo("&quot;&amp;&lt;&gt;");
    }

    @Test
    void testResetOnOpen() throws IOException {
        assertThat(colorize("[0;31;49mred[0m")).isEqualTo("<span style=\"color: red;\">red</span>");
    }

    @Test
    void testUTF8Character() throws IOException {
        assertThat(colorize("[1m\u3053\u3093\u306b\u3061\u306f")).isEqualTo("<b>\u3053\u3093\u306b\u3061\u306f</b>");
    }

    @Test
    void testResetCharacterSet() throws IOException {
        assertThat(colorize("(\033(0)")).isEqualTo("()");
        assertThat(colorize("(\033)0)")).isEqualTo("()");
    }

    private String colorize(String text) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (HtmlAnsiOutputStream hos = new HtmlAnsiOutputStream(os)) {
            hos.write(text.getBytes(StandardCharsets.UTF_8));
        }
        os.close();
        return os.toString();
    }
}
