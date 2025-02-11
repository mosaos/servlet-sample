package mosaos.servlet_sample.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
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

@WebServlet("/error")
@Slf4j
public class ErrorServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        templateEngine = (TemplateEngine) getServletContext().getAttribute(ThymeleafConfig.TEMPLATE_ENGINE_ATTR);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebContext context = new WebContext(JakartaServletWebApplication
                .buildApplication(getServletContext()).buildExchange(req, resp));

        Object statusCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = (String) req.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Exception exception = (Exception) req.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String requestUri = (String) req.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);

        logException(exception);

        req.setAttribute("statusCode", statusCode);
        if ((message == null || message.isEmpty()) && exception != null) {
            message = exception.getLocalizedMessage();
        }
        req.setAttribute("message", message);
        req.setAttribute("requestUri", requestUri);

        resp.setContentType("text/html;charset=UTF-8");
        templateEngine.process("error", context, resp.getWriter());
    }
    
    private void logException(Exception e) {
        if (e != null) {
            if (log.isDebugEnabled()) {
                log.debug(e.getMessage(), e);
            } else {
                log.info(e.getMessage());
                if (e.getCause() != null) {
                    log.info(e.getCause().getMessage());
                }
            }
        }
    }
}
