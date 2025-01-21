package mosaos.controller.admin.user;

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
import mosaos.model.entity.User;
import mosaos.service.UserService;

@WebServlet("/admin/users")
public class UserListServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService = new UserService();

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = (WebContext) req.getAttribute("webContext");

        String p = req.getParameter("p");
        int page = (p != null)? Integer.valueOf(p) : 0;
        page = (page > 0)? page : 1;

        List<User> userList = userService.getUsers(page);
        long total = userService.getTotalCount();
        int totalPage = (int) Math.ceil((double) total / UserService.PAGE_SIZE);

        int current = page;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, totalPage);

        context.setVariable("list", userList);
        context.setVariable("beginIndex", begin);
        context.setVariable("endIndex", end);
        context.setVariable("currentIndex", current);

        templateEngine.process("user/userlist", context, resp.getWriter());

    }
}
