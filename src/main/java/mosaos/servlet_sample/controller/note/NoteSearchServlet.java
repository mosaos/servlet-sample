package mosaos.servlet_sample.controller.note;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import mosaos.servlet_sample.conf.ThymeleafConfig;
import mosaos.servlet_sample.model.dto.NoteDto;
import mosaos.servlet_sample.service.NoteService;

/**
 * Show note detail.
 */
@WebServlet("/notes/search")
public class NoteSearchServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private NoteService noteService = new NoteService();

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }
    //    @Override
    //    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //        process(req, resp);
    //    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = (WebContext) req.getAttribute("webContext");

        String p = req.getParameter("p");
        int page = (p != null) ? Integer.valueOf(p) : 0;
        page = (page > 0) ? page : 1;

        String keyword = (req.getParameter("q") != null) ? req.getParameter("q") : "";
        String tag = (req.getParameter("tag") != null) ? req.getParameter("tag") : "";
        List<NoteDto> noteList = noteService.searchNotes(keyword, tag, page);

        noteList = noteService.summarize(noteList);
        long total = noteService.getTotalCount();
        int totalPage = (int) Math.ceil((double) total / NoteService.PAGE_SIZE);

        int current = page;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, totalPage);

        context.setVariable("q", keyword);
        context.setVariable("tag", tag);
        context.setVariable("list", noteList);
        context.setVariable("beginIndex", begin);
        context.setVariable("endIndex", end);
        context.setVariable("currentIndex", current);

        templateEngine.process("note/notesearch", context, resp.getWriter());

    }
}
