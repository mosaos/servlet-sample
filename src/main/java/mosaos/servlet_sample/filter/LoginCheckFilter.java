package mosaos.servlet_sample.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import mosaos.servlet_sample.util.MessageUtil;

/**
 * Login Check Filter.
 */
@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] END_PATTERN = { "/login", "/logout" };
    private static final String[] CONTAIN_PATTERN = { "/css/", "/img/" };

    /**
     * If login is required. forward to Login page.
     * @param req ServletRequest
     * @param resp ServletResponse
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpSession session = httpReq.getSession(false);

        if (isPublic(httpReq)) {
            chain.doFilter(req, resp);
            return;
        }

        boolean isLogin = (session != null && session.getAttribute("user") != null);

        if (isLogin) {
            chain.doFilter(req, resp);
        } else {
            req.setAttribute("errorMessage", MessageUtil.getMessage("login.required", req.getLocale()));
            log.debug("Login required.");
            // store originalUrl
            String originalUrl = httpReq.getRequestURI();
            String query = httpReq.getQueryString();
            if (query != null) {
                originalUrl += "?" + query;
            }
            req.setAttribute("originalUrl", originalUrl);
            req.getRequestDispatcher("/login").forward(req, resp);
        }
    }

    /**
     * Is public page ?
     * @param req
     * @return If the page is public, return true
     */
    private boolean isPublic(HttpServletRequest req) {
        String uri = req.getRequestURI();
        for (String pattern: END_PATTERN) {
            if (uri.endsWith(pattern)) {
                return true;
            }
        }
        for (String pattern: CONTAIN_PATTERN) {
            if (uri.contains(pattern)) {
                return true;
            }
        }
        return false;
    }
}
