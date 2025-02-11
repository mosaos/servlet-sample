package mosaos.servlet_sample.controller.note;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import lombok.extern.slf4j.Slf4j;
import mosaos.servlet_sample.conf.ThymeleafConfig;
import mosaos.servlet_sample.exception.ApplicationException;
import mosaos.servlet_sample.model.dto.NoteDto;
import mosaos.servlet_sample.service.NoteService;
import mosaos.servlet_sample.util.PermissionUtil;
import mosaos.servlet_sample.util.UUIDUtil;

@WebServlet("/notes/edit/*")
@Slf4j
public class NoteEditServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private NoteService noteService = new NoteService();

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    /**
     * Render Create Page.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = UUIDUtil.getUuid(req);
        NoteDto noteDto = noteService.getNoteByUuid(uuid);
        if (!PermissionUtil.isPermit(req, noteDto)) {
            throw new ApplicationException("Access Forbidden");
        }

        WebContext context = new WebContext(JakartaServletWebApplication
                .buildApplication(getServletContext()).buildExchange(req, resp));

        if (noteDto != null) {
            if (!PermissionUtil.isPermit(req, noteDto)) {
                throw new ApplicationException("Access Forbidden");
            }
            context.setVariable("note", noteDto);
            templateEngine.process("note/noteedit", context, resp.getWriter());
        } else {
            throw new ApplicationException("Not Found");
        }
    }
    
    /**
     * Create Note
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = UUIDUtil.getUuid(req);
        NoteDto note = noteService.getNoteByUuid(uuid);
        if (!PermissionUtil.isPermit(req, note)) {
            throw new ApplicationException("Access Forbidden");
        }

        WebContext context = (WebContext) req.getAttribute("webContext");

        // set form values
        note.setTitle(req.getParameter("title"));
        note.setContent(req.getParameter("content"));
        note.setTags(req.getParameter("tags"));

        int count = noteService.update(note.toNote());
        log.debug("{} notes edited.", count);

        if (count > 0) {
            resp.sendRedirect(req.getContextPath() + "/");
        } else {
            templateEngine.process("note/notecreate", context, resp.getWriter());
        }
    }
}
