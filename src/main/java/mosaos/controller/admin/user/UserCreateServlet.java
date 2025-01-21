package mosaos.controller.admin.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import lombok.extern.slf4j.Slf4j;
import mosaos.conf.ThymeleafConfig;
import mosaos.exception.ApplicationException;
import mosaos.model.entity.User;
import mosaos.service.UserService;
import mosaos.util.PermissionUtil;

@WebServlet("/admin/users/create")
@Slf4j
public class UserCreateServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService = new UserService();

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!PermissionUtil.isAdmin(req)) { // Admin only
            throw new ApplicationException("Access Forbidden.");
        }

        WebContext context = (WebContext) req.getAttribute("webContext");

        User user = new User();
        context.setVariable("user", user);
        templateEngine.process("user/usercreate", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!PermissionUtil.isAdmin(req)) { // Admin only
            throw new ApplicationException("Access Forbidden.");
        }

        WebContext context = (WebContext) req.getAttribute("webContext");

        User user = new User();
        // set form values
        user.setUserId(req.getParameter("userId"));
        user.setUserName(req.getParameter("userName"));
        user.setEmail(req.getParameter("email"));
        user.setPassword(req.getParameter("password"));
        user.setIsAdmin(Boolean.valueOf(req.getParameter("isAdmin")));
 
        int count = userService.create(user);
        log.info("{} user created by {}", user.getUserId(), PermissionUtil.getCurrentUser(req).getUserId());

        if (count > 0) {
            resp.sendRedirect(req.getContextPath() + "/admin/users");
        } else {
            context.setVariable("user", user);
            templateEngine.process("user/usercreate", context, resp.getWriter());
        }
    }
}
