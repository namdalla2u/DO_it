package net.plang.HoWooAccount.hr.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.plang.HoWooAccount.hr.serviceFacade.HRServiceFacade;
import net.plang.HoWooAccount.hr.serviceFacade.HRServiceFacadeImpl;
import net.plang.HoWooAccount.hr.to.EmployeeBean;
import net.sf.json.JSONObject;

public class HRController extends MultiActionController {
    protected final Log logger = LogFactory.getLog(this.getClass());


    private static HRServiceFacade hrServiceFacade = HRServiceFacadeImpl.getInstance();

    public ModelAndView findEmployeeList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : findEmployeeList 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            String deptCode = request.getParameter("deptCode");
            System.out.println("		@ 부서 코드 : " + deptCode);
            ArrayList<EmployeeBean> empList = hrServiceFacade.findEmployeeList(deptCode);
            out = response.getWriter();
            json.put("empList", empList);
            json.put("errorCode", 1);
            json.put("errorMsg", "성공");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", "실패");
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            //json.put("errorMes",e.getMessage());
            json.put("errorMes", "DB오류");
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : findEmployeeList 종료 ");
        }
        return null;
    }

    public ModelAndView findEmployee(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : findEmployee 시작 ");
        }

        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            String empCode = request.getParameter("empCode");
            System.out.println("		@ 사원 코드 : " + empCode);
            EmployeeBean employeeBean = hrServiceFacade.findEmployee(empCode);
            out = response.getWriter();
            json.put("employeeInfo", employeeBean);
            json.put("errorCode", 1);
            json.put("errorMsg", "성공");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", "실패");
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            //json.put("errorMes",e.getMessage());
            json.put("errorMes", "DB오류");
            e.printStackTrace();
        }
        out.println(json);
        out.close();

        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : findEmployee 종료 ");
        }
        return null;
    }


    public ModelAndView batchEmpInfo(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : batchEmpInfo 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            JSONObject jsonObject = JSONObject.fromObject(request.getParameter("employeeInfo"));
            String image = request.getParameter("image");
            EmployeeBean employeeBean = (EmployeeBean) JSONObject.toBean(jsonObject, EmployeeBean.class);
            employeeBean.setImage(image);
            hrServiceFacade.batchEmployeeInfo(employeeBean);
            System.out.println("		@ Emp: " + employeeBean.getEmpCode());

            json.put("errorCode", 1);
            json.put("errorMsg", "성공");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", "실패");
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            //json.put("errorMsg", e.getMessage());
            json.put("errorMsg", "DB오류");
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : batchEmpInfo 종료 ");
        }
        return null;
    }

    public ModelAndView EmptyEmpBean(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : EmptyEmpBean 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            json.put("EmptyEmpBean", new EmployeeBean());
            json.put("errorCode", 1);
            json.put("errorMsg", "성공");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", "실패");
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            //json.put("errorMsg", e.getMessage());
            json.put("errorMsg", "DB오류");
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : EmptyEmpBean 종료 ");
        }
        return null;
    }

    public ModelAndView batchEmp(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : batchEmp 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            JSONObject jsonObject = JSONObject.fromObject(request.getParameter("JoinEmployee"));
            
            EmployeeBean employeeBean = (EmployeeBean) JSONObject.toBean(jsonObject, EmployeeBean.class);

            hrServiceFacade.registerEmployee(employeeBean);
            System.out.println("		@ Emp: " + employeeBean.getEmpCode());
            System.out.println("		@ Emp: " + employeeBean.getImage());

            json.put("errorCode", 1);
            json.put("errorMsg", "성공");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", "실패");
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            // json.put("errorMsg", e.getMessage());
            json.put("errorMsg", "DB오류");
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : batchEmp 종료 ");
        }
        return null;
    }

    
    public ModelAndView deleteEmployee(HttpServletRequest request, HttpServletResponse response) {
    	if (logger.isDebugEnabled()) {
            logger.debug(" HRController : deleteEmployee 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String empCode = request.getParameter("empCode");
            EmployeeBean employeeBean = new EmployeeBean();
            employeeBean.setEmpCode(empCode);

            hrServiceFacade.removeEmployee(employeeBean);
            json.put("errorCode", 1);
            json.put("errorMsg", "성공");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", "실패");
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            // json.put("errorMsg", e.getMessage());
            json.put("errorMsg", "DB오류");
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" HRController : batchEmp 종료 ");
        }
        return null;
    }
}
