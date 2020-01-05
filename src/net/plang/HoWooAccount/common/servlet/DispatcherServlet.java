package net.plang.HoWooAccount.common.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.plang.HoWooAccount.common.servlet.context.ApplicationContext;
import net.plang.HoWooAccount.common.servlet.controller.Controller;
import net.plang.HoWooAccount.common.servlet.mapper.SimpleUrlHandlerMapping;
import net.plang.HoWooAccount.common.servlet.view.InternalResourceViewResolver;

/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet extends HttpServlet {

    private ApplicationContext applicationContext;
    private SimpleUrlHandlerMapping simpleUrlHandlerMapping;
    private InternalResourceViewResolver internalResourceViewResolver;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);
        System.out.println("		@ init호출됨");
        ServletContext application = this.getServletContext();
        applicationContext = new ApplicationContext(config, application);
        simpleUrlHandlerMapping = SimpleUrlHandlerMapping.getInstance(application);
        internalResourceViewResolver = InternalResourceViewResolver.getInstance(application);
        System.out.println("		@ ServletContext, applicationContext");
        System.out.println("		@ simpleUrlHandlerMapping, internalResourceViewResolver");
        System.out.println("		@ 객체 생성됨");
    }

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DispatcherServlet() {

        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        Controller controller = simpleUrlHandlerMapping.getController(applicationContext, request);
        ModelAndView modelAndView = controller.handleRequest(request, response);
        if (modelAndView != null) {
            internalResourceViewResolver.resolverView(modelAndView, request, response);
        }
    }
}
