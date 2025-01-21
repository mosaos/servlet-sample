package mosaos.controller.admin.user;

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
import mosaos.model.entity.User;
import mosaos.service.UserService;
import mosaos.util.UUIDUtil;

@WebServlet("/admin/users/*")
public class UserDetailServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService = new UserService();

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = (WebContext) req.getAttribute("webContext");

        String uuid = UUIDUtil.getUuid(req);
        User user = userService.getUserByUuid(uuid);
        if (user != null) {
            context.setVariable("user", user);
            templateEngine.process("user/userdetail", context, resp.getWriter());
        } else {
            throw new ApplicationException("Not Found");
        }
    }
}
