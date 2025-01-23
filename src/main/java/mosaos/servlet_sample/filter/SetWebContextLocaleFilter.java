package mosaos.servlet_sample.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SetWebContextLocaleFilter implements Filter {

    private ServletContext sc;
    private JakartaServletWebApplication webApplication;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.sc = filterConfig.getServletContext();
        this.webApplication = JakartaServletWebApplication.buildApplication(this.sc);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpResp = (HttpServletResponse) resp;

        WebContext context = new WebContext(webApplication.buildExchange(httpReq, httpResp));
        context.setLocale(req.getLocale());
        httpReq.setAttribute("webContext", context);
        log.debug("WebContext Locale = `{}`", context.getLocale());
        chain.doFilter(req, resp);
    }
}