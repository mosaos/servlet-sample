package mosaos.controller.note;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import mosaos.conf.ThymeleafConfig;
import mosaos.exception.ApplicationException;
import mosaos.model.dto.NoteDto;
import mosaos.service.NoteService;
import mosaos.util.UUIDUtil;

/**
 * Show note detail.
 */
@WebServlet("/notes/*")
public class NoteDetailServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private NoteService noteService = new NoteService();

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = (WebContext) req.getAttribute("webContext");

        String uuid = UUIDUtil.getUuid(req);
        NoteDto noteDto = noteService.getNoteByUuid(uuid);
        if (noteDto != null) {
            context.setVariable("note", noteDto);
            templateEngine.process("note/notedetail", context, resp.getWriter());
        } else {
            throw new ApplicationException("Not Found");
        }
    }
}
