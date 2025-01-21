package mosaos.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import mosaos.conf.ThymeleafConfig;
import mosaos.model.entity.User;

@WebServlet("/admin/userprofile")
public class UserProfileServlet extends HttpServlet {
    
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = (WebContext) req.getAttribute("webContext");

        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("user");

        req.setAttribute("user", user);
        resp.setContentType("text/html;charset=UTF-8");
        templateEngine.process("user/userprofile", context, resp.getWriter());
    }
}