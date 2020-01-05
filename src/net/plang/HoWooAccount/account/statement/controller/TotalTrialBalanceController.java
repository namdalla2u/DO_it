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
import net.plang.HoWooAccount.account.statement.to.TotalTrialBalanceBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.AbstractController;
import net.sf.json.JSONObject;

public class TotalTrialBalanceController extends AbstractController {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private StatementServiceFacade statementServiceFacade = StatementServiceFacadeImpl.getInstance();

    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" TotalTrialBalanceController : handleRequestInternal 시작 ");
        }

        JSONObject json = new JSONObject();
        try {

            String toDate = request.getParameter("toDate");

            System.out.println("		@ 조회일자: " + toDate + " 까지");
            HashMap<String, Object> totalTrialBalanceMap = statementServiceFacade.getTotalTrialBalance(toDate);

            String errorCode = totalTrialBalanceMap.get("errorCode").toString();
            String errorMsg = totalTrialBalanceMap.get("errorMsg").toString();
            	
            if (!errorCode.equals("0"))
                throw new DataAccessException(errorMsg);
           
            json.put("totalTrialBalance", totalTrialBalanceMap.get("totalTrialBalance"));
            json.put("errorCode", errorCode);
            json.put("errorMsg", errorMsg);
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
            logger.debug(" TotalTrialBalanceController : handleRequestInternal 종료 ");
        }
        return null;
    }

}
