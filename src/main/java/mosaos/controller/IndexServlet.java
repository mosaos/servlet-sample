package mosaos.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import mosaos.conf.ThymeleafConfig;
import mosaos.model.dto.NoteDto;
import mosaos.service.NoteService;

@WebServlet("/index.html")
public class IndexServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private NoteService noteService = new NoteService();

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = (WebContext) req.getAttribute("webContext");

        int page = 1;

        List<NoteDto> noteList = noteService.getNotes(page);

        noteList = noteService.summarize(noteList);
        long total = noteService.getTotalCount();
        int totalPage = (int) Math.ceil((double) total / NoteService.PAGE_SIZE);

        int current = page;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, totalPage);

        context.setVariable("list", noteList);
        context.setVariable("beginIndex", begin);
        context.setVariable("endIndex", end);
        context.setVariable("currentIndex", current);

        templateEngine.process("index", context, resp.getWriter());
    }
}
