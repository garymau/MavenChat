package servlet.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Diman on 29.10.2016.
 */

@WebFilter(urlPatterns = {"/homepage.jsp", "/login"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        String nickName = req.getParameter("nickname");
        HttpSession session = req.getSession(false);
        if (nickName == null && session != null) {
            nickName = (String)session.getAttribute("nickname");
            if (nickName == null) {
                resp.setHeader("Cache-Control", "private, must-revalidate, max-age=0, no-store, " +
                        "no-cache, must-revalidate, post-check=0, pre-check=0"); // HTTP 1.1.
                resp.setHeader("Pragma", "no-cache"); //HTTP 1.0
                resp.setDateHeader("Expires", 0); // Proxies.
                req.getRequestDispatcher(req.getContextPath() + "/login.jsp").forward(servletRequest, servletResponse);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
