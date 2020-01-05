package net.plang.HoWooAccount.common.servlet.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.plang.HoWooAccount.common.servlet.ModelAndView;

public class UrlFilenameViewController extends AbstractController {

    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("		@ UrlFilenameViewController 접근");
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        int sIndex = contextPath.length() + 1;
        int eIndex = uri.lastIndexOf(".");
        String viewName = uri.substring(sIndex, eIndex);
        System.out.println("		@ 뷰네임: " + viewName);
        ModelAndView modelAndView = new ModelAndView(viewName, null);

        return modelAndView;
    }

}
