package mosaos.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // TODO 初期化処理
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {
        // 前処理
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        log.info("Request received for: {}", httpRequest.getRequestURI());

        // フィルタチェーンの次の処理へ
        chain.doFilter(req, resp);

        // 後処理
        log.info("Response sent for: {}", httpRequest.getRequestURI());
    }

    @Override
    public void destroy() {
        // TODO 破棄処理 ( リソースの解放など )
    }
}