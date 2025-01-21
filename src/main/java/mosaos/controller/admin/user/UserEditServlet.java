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
import mosaos.util.BCryptUtil;
import mosaos.util.MessageUtil;
import mosaos.util.PermissionUtil;
import mosaos.util.UUIDUtil;

@WebServlet("/admin/users/edit/*")
@Slf4j
public class UserEditServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService = new UserService();

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = UUIDUtil.getUuid(req);
        User user = userService.getUserByUuid(uuid);
        if (!PermissionUtil.isPermit(req, user)) {
            throw new ApplicationException("Access Forbidden");
        }

        WebContext context = (WebContext) req.getAttribute("webContext");

        if (user != null) {
            if (!PermissionUtil.isPermit(req, user)) {
                throw new ApplicationException("Access Forbidden");
            }
            context.setVariable("user", user);
            templateEngine.process("user/useredit", context, resp.getWriter());
        } else {
            throw new ApplicationException("Not Found");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = UUIDUtil.getUuid(req);
        User user = userService.getUserByUuid(uuid);
        if (!PermissionUtil.isPermit(req, user)) {
            throw new ApplicationException("Access Forbidden");
        }

        WebContext context = (WebContext) req.getAttribute("webContext");

        if (user != null) {
            user.setUserName(req.getParameter("userName"));
            user.setEmail(req.getParameter("email"));
            try {
                user.setPassword(getPassword(req, user));
            } catch (ApplicationException e) {
                context.setVariable("user", user);
                context.setVariable("errorMessage", MessageUtil.getMessage("password.different", req.getLocale()));
                templateEngine.process("user/useredit", context, resp.getWriter());
            }
            if (PermissionUtil.isAdmin(req)) { // admin flag change is permitted only when user is admin.
                String admin = req.getParameter("isAdmin");
                boolean isAdmin = Boolean.valueOf(admin);
                user.setIsAdmin(isAdmin);
            }
            int count = userService.update(user);
            log.info("{} user edited by {}", user.getUuid(), PermissionUtil.getCurrentUser(req).getUserId());

            if (count > 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/users/" + user.getUuid());
            } else {
                context.setVariable("user", user);
                templateEngine.process("user/useredit", context, resp.getWriter());
            }
        } else {
            throw new ApplicationException("Not Found");
        }
    }

    /**
     * if the password input is empty. return the current password.
     * @param req HttpServletRequest
     * @param user User
     * @return password
     */
    private String getPassword(HttpServletRequest req, User user) {
        String input = req.getParameter("password");
        String input2 = req.getParameter("password2");
        if (input != null && !input.equals(input2)) {
            throw new ApplicationException("Two password inputs are different");
        }
        if (input == null || input.isEmpty()) {
            return user.getPassword();
        } else {
            return BCryptUtil.encode(input.trim());
        }
    }
}
