package net.plang.HoWooAccount.account.slip.controller;

import net.plang.HoWooAccount.account.slip.serviceFacade.JournalDetailServiceFacade;
import net.plang.HoWooAccount.account.slip.serviceFacade.JournalDetailServiceFacadeImpl;
import net.plang.HoWooAccount.account.slip.to.JournalDetailBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class JournalDetailController extends MultiActionController {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private JournalDetailServiceFacade journalDetailServiceFacade = JournalDetailServiceFacadeImpl.getInstance();

    public ModelAndView getJournalDetailList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDetailController : getJournalDetailList 시작 ");
        }
        JSONObject json = new JSONObject();

        try {
            String journalNo = request.getParameter("journalNo");
            System.out.println(journalNo);

            ArrayList<JournalDetailBean> journalDetailList = journalDetailServiceFacade.getJournalDetailList(journalNo);
            for(JournalDetailBean ss:journalDetailList){
            	System.out.println("분개상세조회 : "+ss.getAccountControlName());
            }
            json.put("journalDetailList", journalDetailList);

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

        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDetailController : getJournalDetailList 종료 ");
        }
        return null;
    }

    public ModelAndView editJournalDetail(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " 시작 ");
        }
        JSONObject json = new JSONObject();

        try {
            JournalDetailBean journalDetailBean = new JournalDetailBean();
//            journalDetailBean.setJournalNo(request.getParameter("journalNo"));
            journalDetailBean.setJournalDetailNo(request.getParameter("journalDetailNo"));
//            journalDetailBean.setItem(request.getParameter("item"));
            journalDetailBean.setJournalDescription(request.getParameter("journalDescription"));

            journalDetailServiceFacade.editJournalDetail(journalDetailBean);
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

        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " 종료");
        }
        return null;
    }
}
