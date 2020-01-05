package net.plang.HoWooAccount.common.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class loginFilter implements Filter {

    private String loginPage;
    private String movePage;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String contextPath = filterConfig.getServletContext().getContextPath();

//        excludePathList = filterConfig.getInitParameter("excludeLoginFilter");
        loginPage = filterConfig.getInitParameter("loginPage");
        loginPage = loginPage.startsWith("/") ? contextPath + loginPage : contextPath + "/" + loginPage;

        movePage = filterConfig.getInitParameter("movePage");
        movePage = movePage.startsWith("/") ? contextPath + movePage : contextPath + "/" + movePage;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // .html 파일과 welcome page만 필터링
        if(excludeUrl(request)) {
        	  filterChain.doFilter(request, response);
        }else {
        if (request.getRequestURI().equals("/") || request.getRequestURI().endsWith(".html")) {
            System.out.println("로그인 필터 Start");

            boolean isLoginPage = request.getRequestURI().equals(loginPage);
            System.out.println("RequestURI : " + request.getRequestURI());
            System.out.println("loginPage : " + loginPage);

            if (isLoginPage && request.getSession().getAttribute("empCode") != null) {
                response.sendRedirect(movePage);
                System.out.println(movePage + " 페이지 이동");
                return;
            }
            
            if (!isLoginPage && request.getRequestURI().endsWith("empinsertForm.html")) {
                response.sendRedirect(movePage);
                System.out.println("페이지 이동");
                return;
            }

            if (!isLoginPage && request.getSession().getAttribute("empCode") == null) {
                response.sendRedirect(loginPage);
                System.out.println("페이지 이동");
                return;
            }

            System.out.println("로그인 필터 End");
        }
        filterChain.doFilter(servletRequest, response);
    }
    }
    
    private boolean excludeUrl(HttpServletRequest request) {
        String uri = request.getRequestURI().toString().trim();
        if(uri.contains(request.getContextPath()+"/hr/empinsertForm.html")){
            return true;
        }else{
            return false;
        }

  }

    @Override
    public void destroy() {

    }
}
