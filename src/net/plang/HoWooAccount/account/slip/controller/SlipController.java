package net.plang.HoWooAccount.account.slip.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.plang.HoWooAccount.account.slip.serviceFacade.SlipServiceFacade;
import net.plang.HoWooAccount.account.slip.serviceFacade.SlipServiceFacadeImpl;
import net.plang.HoWooAccount.account.slip.to.JournalBean;
import net.plang.HoWooAccount.account.slip.to.SlipBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.plang.HoWooAccount.common.util.BeanCreator;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

public class SlipController extends MultiActionController {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private SlipServiceFacade slipServiceFacade = SlipServiceFacadeImpl.getInstance();

    public ModelAndView updateSlip(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 시작");

        JSONObject json = new JSONObject();

        try {
            SlipBean slipBean = new SlipBean();

            slipBean.setSlipNo(request.getParameter("slipNo"));
            slipBean.setSlipType(request.getParameter("slipType"));
            slipBean.setExpenseReport(request.getParameter("expenseReport"));

            slipServiceFacade.updateSlip(slipBean);

            json.put("errorCode", 0);
            json.put("errorMsg", "데이터 입력 성공");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } finally {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 종료");

        return null;
    }

    public ModelAndView addSlip(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 시작");

        JSONObject json = new JSONObject();

        try {
            JSONObject slipObj = JSONObject.fromObject(request.getParameter("slipObj"));
            JSONArray journalObjs = JSONArray.fromObject(request.getParameter("journalObj"));

            SlipBean slipBean = BeanCreator.getInstance().create(slipObj, SlipBean.class);
            slipBean.setReportingEmpCode(request.getSession().getAttribute("empCode").toString());
            slipBean.setDeptCode(request.getSession().getAttribute("deptCode").toString());

            ArrayList<JournalBean> journalBeans = new ArrayList<>();

            for (Object journalObj : journalObjs) {
                JournalBean journalBean = BeanCreator.getInstance().create(JSONObject.fromObject(journalObj), JournalBean.class);
                journalBean.setSlipNo(slipBean.getSlipNo());
                journalBeans.add(journalBean);
            }
            String slipNo = slipServiceFacade.addSlip(slipBean, journalBeans);

            json.put("slipNo", slipNo);
            json.put("errorCode", 0);
            json.put("errorMsg", "데이터 입력 성공");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } finally {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 종료");

        return null;
    }

    public ModelAndView deleteSlip(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 시작");

        JSONObject json = new JSONObject();

        try {
            String slipNo = request.getParameter("slipNo");

            slipServiceFacade.deleteSlip(slipNo);

            json.put("slipNo", slipNo);
            json.put("errorCode", 0);
            json.put("errorMsg", "데이터 제거 성공");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } finally {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 종료");

        return null;
    }


    public ModelAndView approveSlip(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 시작");

        JSONObject json = new JSONObject();

        try {
            JSONArray approveSlipList = JSONArray.fromObject(request.getParameter("approveSlipList"));
            String slipStatus = request.getParameter("isApprove"); // true
            ArrayList<SlipBean> slipBeans = new ArrayList<>();

            for (Object approveSlip : approveSlipList) { // 승인일자를 자바로 만든다
                Calendar calendar = Calendar.getInstance(); 
                String year = calendar.get(Calendar.YEAR) + "";
                String month = "0" + (calendar.get(Calendar.MONTH) + 1);
                String date = "0" + calendar.get(Calendar.DATE);
                String today = year + "-" + month.substring(month.length() - 2) + "-" + date.substring(date.length() - 2);
                //2019-07-20

                SlipBean slipBean = new SlipBean();
                slipBean.setSlipNo(approveSlip.toString());
                slipBean.setApprovalDate(today);
                slipBean.setSlipStatus(slipStatus);
                slipBean.setApprovalEmpCode(request.getSession().getAttribute("empCode").toString());
                slipBeans.add(slipBean);
            }

            slipServiceFacade.approveSlip(slipBeans);

            json.put("errorCode", 0);
            json.put("errorMsg", "전표 승인 변경");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } finally {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 종료");
        return null;
    }

    public ModelAndView findRangedSlipList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 시작");

        JSONObject json = new JSONObject();

        try {
            String fromDate = request.getParameter("from");
            String toDate = request.getParameter("to");
            ArrayList<SlipBean> slipList = slipServiceFacade.findRangedSlipList(fromDate, toDate);
            json.put("slipList", slipList);
            json.put("errorCode", 0);
            json.put("errorMsg", "데이터 조회 성공");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } finally {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 종료");

        return null;
    }


    public ModelAndView findDisApprovalSlipList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 시작");

        JSONObject json = new JSONObject();

        try {
            ArrayList<SlipBean> disApprovalSlipList = slipServiceFacade.findDisApprovalSlipList();
            json.put("disApprovalSlipList", disApprovalSlipList);
            json.put("errorCode", 0);
            json.put("errorMsg", "데이터 조회 성공");

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } finally {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 시작");

        return null;
    }
}
