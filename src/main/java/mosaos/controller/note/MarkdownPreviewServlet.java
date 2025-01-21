package mosaos.controller.note;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * Currently, this is not used.
 * This app is using `marked.js` to convert markdown to html.
 */
@WebServlet("/notes/preview")
public class MarkdownPreviewServlet extends HttpServlet {

    private Parser parser = null;
    HtmlRenderer renderer = null;

    @Override
    public void init() throws ServletException {
        List<Extension> extensions = List.of(TablesExtension.create());
        parser = Parser.builder()
                .extensions(extensions)
                .build();
        renderer = HtmlRenderer.builder()
                .extensions(extensions)
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String markdownText = req.getParameter("markdown");
        if (markdownText == null) {
            markdownText = "";
        }

        String htmlContent = renderer.render(parser.parse(markdownText));

        // send converted content to Response
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write(htmlContent);
    }
}