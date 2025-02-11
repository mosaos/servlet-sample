package mosaos.servlet_sample.controller.note;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import lombok.extern.slf4j.Slf4j;
import mosaos.servlet_sample.conf.ThymeleafConfig;
import mosaos.servlet_sample.exception.ApplicationException;
import mosaos.servlet_sample.model.entity.Note;
import mosaos.servlet_sample.model.entity.User;
import mosaos.servlet_sample.service.NoteService;
import mosaos.servlet_sample.util.PermissionUtil;

@WebServlet("/notes/create")
@Slf4j
public class NoteCreateServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private NoteService noteService = new NoteService();

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    /**
     * Render Note Create Page.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (PermissionUtil.getCurrentUser(req) == null) {
            throw new ApplicationException("Authentication Required.");
        }

        WebContext context = (WebContext) req.getAttribute("webContext");

        Note note = new Note();
        context.setVariable("note", note);

        templateEngine.process("note/notecreate", context, resp.getWriter());
    }

    /**
     * Create Note.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = PermissionUtil.getCurrentUser(req);
        if (user == null) {
            throw new ApplicationException("Authentication Required.");
        }

        WebContext context = (WebContext) req.getAttribute("webContext");

        Note note = new Note();
        // set form values
        note.setTitle(req.getParameter("title"));
        note.setContent(req.getParameter("content"));
        note.setTags(req.getParameter("tags"));
        note.setUserId(user.getId());

        int count = noteService.create(note);
        if (count > 0) {
            log.info("{} note crated by {}", note.getTitle(), PermissionUtil.getCurrentUser(req).getUserId());
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            templateEngine.process("note/notecreate", context, resp.getWriter());
        }
    }
}
