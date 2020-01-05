package net.plang.HoWooAccount.account.statement.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.serviceFacade.StatementServiceFacade;
import net.plang.HoWooAccount.account.statement.serviceFacade.StatementServiceFacadeImpl;
import net.plang.HoWooAccount.account.statement.to.EarlyAssetBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.sf.json.JSONObject;

public class EarlyAssetController extends MultiActionController {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private StatementServiceFacade statementServiceFacade = StatementServiceFacadeImpl.getInstance();

    public ModelAndView findEarlyAsset(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : findEarlyAsset 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {

            out = response.getWriter();
            ArrayList<EarlyAssetBean> earlyAssetlist = statementServiceFacade.findEarlyAssetlist();
            json.put("earlyAssetlist", earlyAssetlist);

            json.put("errorCode", 1);
            json.put("errorMsg", "오류발생");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : findEarlyAsset 종료 ");
        }
        return null;
    }


    public ModelAndView findEarlyAssetStatelist(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : earlyAssetStatelist 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {

            out = response.getWriter();
            ArrayList<EarlyAssetBean> earlyAssetStatelist = statementServiceFacade.findEarlyAssetStatelist();
            json.put("earlyAssetStatelist", earlyAssetStatelist);

            json.put("errorCode", 1);
            json.put("errorMsg", "오류발생");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : earlyAssetStatelist 종료 ");
        }
        return null;
    }

    public ModelAndView findEarlyAssetSummarylist(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : earlyAssetsummarylist 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {

            out = response.getWriter();
            ArrayList<EarlyAssetBean> earlyAssetsummarylist = statementServiceFacade.findEarlyAssetSummarylist();
            json.put("earlyAssetsummarylist", earlyAssetsummarylist);

            json.put("errorCode", 1);
            json.put("errorMsg", "오류발생");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : earlyAssetsummarylist 종료 ");
        }
        return null;
    }

    public ModelAndView findEarlyState(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : findEarlyAsset 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ArrayList<EarlyAssetBean> earlyState = statementServiceFacade.findEarlyStatelist();
            json.put("earlyState", earlyState);
            json.put("errorCode", 1);
            json.put("errorMsg", "오류발생");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : findEarlyAsset 종료 ");
        }
        return null;
    }

    public ModelAndView findEarlyStateSummarylist(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : findEarlyAsset 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            ArrayList<EarlyAssetBean> earlyStateSummarylist = statementServiceFacade.findEarlyStateSummarylist();
            json.put("earlyStateSummarylist", earlyStateSummarylist);
            json.put("errorCode", 1);
            json.put("errorMsg", "오류발생");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetController : findEarlyAsset 종료 ");
        }
        return null;
    }
}
