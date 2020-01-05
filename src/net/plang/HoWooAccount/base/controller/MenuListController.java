package net.plang.HoWooAccount.base.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.serviceFacade.BaseServiceFacade;
import net.plang.HoWooAccount.base.serviceFacade.BaseServiceFacadeImpl;
import net.plang.HoWooAccount.base.to.MenuBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.sf.json.JSONObject;

public class MenuListController extends MultiActionController {  // 하나의 컨트롤러에서 여러개의 요청처리 지원

    private BaseServiceFacade baseServiceFacade = BaseServiceFacadeImpl.getInstance();
    protected final Log logger = LogFactory.getLog(this.getClass());

    public ModelAndView findUserMenuList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" MenuListController : findUserMenuList 시작 ");
        }
        JSONObject json = new JSONObject();
        System.out.println("		@ JSON객체 생성");
        PrintWriter out = null;
        System.out.println("		@ out 변수 선언");

        try {

            out = response.getWriter();
            System.out.println("		@ out 변수 초기화");
            String empCode = request.getParameter("empCode");   // 1) empCode 들고왔고
            System.out.println("		@ 로그인폼에서 받아온 파라메터: " + empCode);
            ArrayList<MenuBean> menuList = baseServiceFacade.findUserMenuList(empCode);   // 2) 그 empCode에 대해 메뉴리스트 뽑아옴
            System.out.println("		@ 권한: " + menuList.get(0).getPositionCode());
            json.put("userMenuList", menuList);
            json.put("errorCode", 1);
            json.put("errorMsg", "메뉴를 가져 왔습니다");


        } catch (IOException e) {
            logger.fatal(e.getMessage());

            json.put("errorCode", -1);
            json.put("errorMsg", "출력오류");
            e.printStackTrace();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());

            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        out.print(json);
        System.out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" MenuListController : findUserMenuList 종료 ");
        }
        return null;


    }

}
