package net.ticket.controller.occasion.util;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/api/v1/occasions/*")
public class OccasionResponseHeaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        if (httpServletRequest.getQueryString() == null)
            httpServletResponse.setHeader("Request-path", httpServletRequest.getRequestURI());
        else
            httpServletResponse.setHeader("Request-path", httpServletRequest.getRequestURI() + "/" + httpServletRequest.getQueryString());
        httpServletResponse.setHeader(HttpHeaders.HOST, httpServletRequest.getServerName() + ":" + httpServletRequest.getLocalPort());
        chain.doFilter(request, response);
    }
}
