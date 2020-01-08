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
    	/*FilterConfig의 메서드 getServletContext()의 리턴타입 ServletContext
    	 * 변수 contextPath는 프로젝트 경로(/HoWooAccount)만 담겨있다. 
    	 * */
        String contextPath = filterConfig.getServletContext().getContextPath();
//        excludePathList = filterConfig.getInitParameter("excludeLoginFilter");
        loginPage = filterConfig.getInitParameter("loginPage"); //  /loginForm.html
        loginPage = loginPage.startsWith("/") ? contextPath + loginPage : contextPath + "/" + loginPage;

        movePage = filterConfig.getInitParameter("movePage"); // /hello.html
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

            boolean isLoginPage = request.getRequestURI().equals(loginPage);  //사용자가 요청한 URI가 /loginForm.html로 끝나면 true반환 
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
        String uri = request.getRequestURI().toString().trim(); //  request.getRequestURI()는 프로젝트와 파일경로까지 가져온다. 
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
