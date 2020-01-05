package net.plang.HoWooAccount.common.servlet.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.plang.HoWooAccount.common.servlet.ModelAndView;

public class MultiActionController extends AbstractController {
	
    @SuppressWarnings("deprecation")
    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("		@ MultiActionController접근");

        String methodName = request.getParameter("method");
        System.out.println("		@ 메소드명: " + methodName);


        Class[] parameters = new Class[]{HttpServletRequest.class, HttpServletResponse.class};
        Class<?> cls = this.getClass();
        System.out.println("멀티액션 컨트롤러 겟네임 :" + cls.getName());

        ModelAndView modelAndView = null;
        try {
            modelAndView = (ModelAndView) cls.getMethod(methodName, parameters).invoke(cls.newInstance(), request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelAndView;
    }

}
