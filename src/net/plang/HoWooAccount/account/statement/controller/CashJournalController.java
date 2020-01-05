package net.plang.HoWooAccount.account.statement.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.serviceFacade.StatementServiceFacade;
import net.plang.HoWooAccount.account.statement.serviceFacade.StatementServiceFacadeImpl;
import net.plang.HoWooAccount.account.statement.to.CashJournalBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.AbstractController;
import net.sf.json.JSONObject;

public class CashJournalController extends AbstractController {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private StatementServiceFacade statementServiceFacade = StatementServiceFacadeImpl.getInstance();

    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" CashJournalController : handleRequestInternal 시작 ");
        }

        JSONObject json = new JSONObject();
        try {

        	String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            
            System.out.println(fromDate+"부터");
            System.out.println(toDate+"까지");
            
            ArrayList<CashJournalBean> cashJournalList = statementServiceFacade.getCashJournal(fromDate, toDate);
           
            json.put("cashJournalList", cashJournalList);
            json.put("errorCode", 1);
            json.put("errorMsg", "현금출납금 성공");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } catch (Exception error) {
            logger.fatal(error.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", error.getMessage());
            error.printStackTrace();
        } finally {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" CashJournalController : handleRequestInternal 종료 ");
        }
        return null;
    }

}
