package net.plang.HoWooAccount.account.slip.controller;

import net.plang.HoWooAccount.account.slip.serviceFacade.JournalServiceFacade;
import net.plang.HoWooAccount.account.slip.serviceFacade.JournalServiceFacadeImpl;
import net.plang.HoWooAccount.account.slip.to.JournalBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.plang.HoWooAccount.common.util.BeanCreator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class JournalController extends MultiActionController {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private JournalServiceFacade journalServiceFacade = JournalServiceFacadeImpl.getInstance();

    public ModelAndView findRangedJournalList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipController : findRangedJournalList 시작 ");
        }
        JSONObject json = new JSONObject();
        try {
            String fromDate = request.getParameter("from");
            String toDate = request.getParameter("to");


            ArrayList<JournalBean> journalList = journalServiceFacade.findRangedJournalList(fromDate, toDate);
            json.put("journalList", journalList);

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
            logger.debug(" SlipController : findRangedJournalList 종료 ");
        }
        return null;
    }

    public ModelAndView findSingleJournalList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipController : findSlipDataList 시작 ");    
        }

        ArrayList<JournalBean> journalList = null;
        JSONObject json = new JSONObject();

        try {
            String slipNo = request.getParameter("slipNo");
            journalList = journalServiceFacade.findSingleJournalList(slipNo);
            json.put("journalList", journalList);
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
            logger.debug(" SlipController : findSlipDataList 종료 ");
        }
        return null;
    }


    public ModelAndView editJournal(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled())
            logger.debug(this.getClass().getName() + "(" + Thread.currentThread().getStackTrace()[1].getMethodName() + ") 시작");

        JSONObject json = new JSONObject();

        try {
            String slipNo = request.getParameter("slipNo");
            JSONArray journalObjs = JSONArray.fromObject(request.getParameter("journalObj"));

            ArrayList<JournalBean> journalBeanList = new ArrayList<>();

            for (Object journalObj : journalObjs) {
                JournalBean journalBean = BeanCreator.getInstance().create(JSONObject.fromObject(journalObj), JournalBean.class);
                journalBean.setSlipNo(slipNo);

                journalBeanList.add(journalBean);
            }

            journalServiceFacade.editJournal(slipNo, journalBeanList);

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
}
