package net.plang.HoWooAccount.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.AbstractController;

public class MemberLogoutController extends AbstractController {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {

        if (logger.isDebugEnabled()) {
            logger.debug(" MemberLogoutController : handleRequestInternal 시작 ");
        }
        ModelAndView modelAndView = new ModelAndView("loginForm", null);
        HttpSession session = request.getSession();
        session.invalidate();
        System.out.println("		@ :session 종료: 사용자가 로그 아웃 하였습니다");
        if (logger.isDebugEnabled()) {
            logger.debug(" MemberLogoutController : handleRequestInternal 종료 ");
        }
        return modelAndView;
    }

}
