package mosaos.servlet_sample.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import lombok.extern.slf4j.Slf4j;
import mosaos.servlet_sample.conf.ThymeleafConfig;
import mosaos.servlet_sample.model.entity.User;
import mosaos.servlet_sample.service.UserService;
import mosaos.servlet_sample.util.MessageUtil;

/**
 * Login Servlet.
 */
@WebServlet("/login")
@Slf4j
public class LoginServlet extends HttpServlet {

    private UserService userService = new UserService();
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = (WebContext) req.getAttribute("webContext");

        context.setVariable("errorMessage", req.getAttribute("errorMessage"));

        // processing the thymeleaf template
        templateEngine.process("login", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");
        String password = req.getParameter("password");

        WebContext context = (WebContext) req.getAttribute("webContext");

        User user = userService.authenticate(id, password);
        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);

            // Redirect to access destination, if via LoginCheckFilter
            String originalUrl = req.getParameter("originalUrl");
            if (originalUrl != null && !originalUrl.isEmpty()) {
                session.removeAttribute("originalUrl");
                resp.sendRedirect(originalUrl);
            } else {
                resp.sendRedirect(req.getContextPath() + "/");
            }
        } else {
            context.setVariable("errorMessage", MessageUtil.getMessage("login.fail", req.getLocale()));
            templateEngine.process("login", context, resp.getWriter());
        }
    }
}