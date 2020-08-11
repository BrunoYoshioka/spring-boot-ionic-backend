package com.bruno.cursomc;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class HeaderExposureFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response; // receber um casting de ServletResponse
        res.addHeader("access-control-expose-headers", "location"); // expor o cabeçalho location na resposta de forma explicita no back end
        chain.doFilter(request, response); // encaminha a requisição para o ciclo normal.
    }

    @Override
    public void destroy() {
    }
}
