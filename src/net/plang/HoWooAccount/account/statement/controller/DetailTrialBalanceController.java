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
import net.plang.HoWooAccount.account.statement.to.DetailTrialBalanceBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.AbstractController;
import net.sf.json.JSONObject;

public class DetailTrialBalanceController extends AbstractController {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private StatementServiceFacade statementServiceFacade = StatementServiceFacadeImpl.getInstance();
    
    //일(월)계표
    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
    	System.out.println("일(월)계표  시작");
        if (logger.isDebugEnabled()) {
            logger.debug(" DetailTrialBalanceController : detailTrialBalance 시작 ");
        }

        JSONObject json = new JSONObject();
        try {
        	
            String fromDate = request.getParameter("fromDate");
            String toDate = request.getParameter("toDate");
            
            System.out.println(fromDate+"부터");
            System.out.println(toDate+"까지");

            ArrayList<DetailTrialBalanceBean> detailTrialBalanceList = statementServiceFacade.getDetailTrialBalance(fromDate, toDate);

            json.put("detailTrialBalanceList", detailTrialBalanceList);
            json.put("errorCode", 0);
            json.put("errorMsg", "일(월)계표 성공");
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
            logger.debug(" DetailTrialBalanceController : detailTrialBalance 종료 ");
        }
        return null;
    }

}
