package net.plang.HoWooAccount.common.servlet.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.plang.HoWooAccount.common.servlet.ModelAndView;

public abstract class AbstractController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("		@ AbstractController접근");
        String controllerFullName = this.getClass().getName();
        String controllerShortName = controllerFullName.substring(controllerFullName.lastIndexOf(".") + 1);
        System.out.println("		@ 컨트롤러 풀네임: " + controllerFullName);
        System.out.println("		@ 컨트롤러 숏네임: " + controllerShortName);


        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "no-store");

        ModelAndView modelAndView = handleRequestInternal(request, response);


        return modelAndView;
    }

    public abstract ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response);
}
