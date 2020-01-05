package net.plang.HoWooAccount.base.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.exception.IdNotFoundException;
import net.plang.HoWooAccount.base.exception.PwMissmatchException;
import net.plang.HoWooAccount.base.serviceFacade.BaseServiceFacade;
import net.plang.HoWooAccount.base.serviceFacade.BaseServiceFacadeImpl;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.AbstractController;
import net.plang.HoWooAccount.hr.to.EmployeeBean;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.PrintWriter;
import java.util.HashMap;

public class MemberLoginController extends AbstractController {
    protected final Log logger = LogFactory.getLog(this.getClass());
   
    @SuppressWarnings("unchecked")
    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" MemberLoginController : handleRequestInternal 시작 ");
        }

        String viewName = null;
        HashMap<String, Object> model = new HashMap<>();
        try {
            BaseServiceFacade baseServiceFacade = BaseServiceFacadeImpl.getInstance();
            System.out.println("		@ BaseServiceFacade의 객체 주소를 가져옴");
            HttpSession session = request.getSession();
            System.out.println("		@ session 생성");
            String empCode = request.getParameter("empCode");
            System.out.println("		@ 로그인 폼에서 파라메터로 받아온 empCode: " + empCode);
            String userPw = request.getParameter("userPw");
            System.out.println("		@ 로그인 폼에서 파라메터로 받아온 userPw: " + userPw);
//			String deptCode = request.getParameter("deptCode").toUpperCase();
//			System.out.println("		@ 로그인 폼에서 파라메터로 받아온 deptCode: "+deptCode);


            EmployeeBean employeeBean = baseServiceFacade.getLoginData(empCode, userPw);
            System.out.println("		@ BaseServiceFacade에서 접근 권한을 얻어옴");

            if (employeeBean != null) {
                System.out.println("		@ 로그인 : " + employeeBean.getEmpName());

                session.setAttribute("empCode", employeeBean.getEmpCode());
                session.setAttribute("empName", employeeBean.getEmpName());
                session.setAttribute("deptCode", employeeBean.getDeptCode());
                session.setAttribute("deptName", employeeBean.getDeptName());

//				session.setAttribute("authority", ((ArrayList<MenuBean>)employeeBean.get("menuList")).get(0).getPositionCode());
                session.setAttribute("positionName", employeeBean.getPositionName());
//				session.setAttribute("masterMenuList", employeeBean.get("masterMenuList"));
                viewName = "redirect:hello.html";
            }
            System.out.println("		@ 뷰네임: " + viewName);
        } catch (IdNotFoundException e) {
            model.put("errorCode", -1);
            model.put("errorMsg", /*e.getMessage()*/ "존재하지 않는 계정입니다");
            viewName = "redirect:loginForm.html";

        } catch (PwMissmatchException e) {
            model.put("errorCode", -3);
            model.put("errorMsg", /*e.getMessage()*/ "비밀번호가 맞지 않습니다");
            viewName = "redirect:loginForm.html";

        } catch (Exception e) {
            e.printStackTrace();
            model.put("errorCode", -4);
            model.put("errorMsg", /*e.getMessage()*/ "예기치 못한 오류 발생");
            viewName = "redirect:loginForm.html";
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" MemberLoginController : handleRequestInternal 종료 ");
        }
        ModelAndView modelAndView = new ModelAndView(viewName, model);
        return modelAndView;
    }

}
