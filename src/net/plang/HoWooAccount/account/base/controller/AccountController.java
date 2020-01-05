package net.plang.HoWooAccount.account.base.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.base.serviceFacade.AccountServiceFacade;
import net.plang.HoWooAccount.account.base.serviceFacade.AccountServiceFacadeImpl;
import net.plang.HoWooAccount.account.base.to.AccountBean;
import net.plang.HoWooAccount.account.base.to.AccountControlBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.sf.json.JSONObject;

public class AccountController extends MultiActionController {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private AccountServiceFacade accountServiceFacade = AccountServiceFacadeImpl.getInstance();

    public ModelAndView getAccount(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : getAccount 시작 ");
        }
        JSONObject json = new JSONObject();

        try {
            String accountCode = request.getParameter("accountCode");

            AccountBean accountBean = accountServiceFacade.getAccount(accountCode);
            json.put("account", accountBean);

            json.put("errorCode", 0);
            json.put("errorMsg", "계정과목 조회 완료");

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
            logger.debug(" AccountController : getAccount 종료 ");
        }
        return null;
    }

    public ModelAndView getAccountControlList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : getAccountControlList 시작 ");
        }
        JSONObject json = new JSONObject();

        try {
            String accountCode = request.getParameter("accountCode");

            ArrayList<AccountControlBean> accountControlList = accountServiceFacade.getAccountControlList(accountCode);
            json.put("accountControl", accountControlList);

            json.put("errorCode", 0);
            json.put("errorMsg", "계정과목 조회 완료");

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
            logger.debug(" AccountController : getAccountControlList 종료 ");
        }
        return null;
    }

    public ModelAndView getAccountListByName(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : getAccountList 시작 ");
        }
        JSONObject json = new JSONObject();

        try {
            String accountName = request.getParameter("accountName");
            ArrayList<AccountBean> accountList;

            accountList = accountServiceFacade.getAccountListByName(accountName);

            json.put("accountList", accountList);
            json.put("errorCode", 0);
            json.put("errorMsg", "계정과목 조회 완료");

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
//            e.printStackTrace();

        } finally {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : getAccountList 종료 ");
        }
        return null;
    }

    public ModelAndView findParentAccountList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : findParentAccountList 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();

            ArrayList<AccountBean> accountList = accountServiceFacade.findParentAccountList();
            json.put("accountList", accountList);

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
            logger.debug(" AccountController : getAccount 종료 ");
        }
        return null;
    }

    public ModelAndView findDetailAccountList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : findParentAccountList 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String code = request.getParameter("code");
            ArrayList<AccountBean> accountList = accountServiceFacade.findDetailAccountList(code);
            json.put("detailAccountList", accountList);
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
        System.out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : getAccount 종료 ");
        }
        return null;
    }

    public ModelAndView editAccount(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : editAccount 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            String accountInnerCode = request.getParameter("accountInnerCode");
            String accountName = request.getParameter("accountName");
//            String accountCharacter = request.getParameter("accountCharacter");
//            String accountDescription = request.getParameter("accountDescription");

            AccountBean accountBean = new AccountBean();
//            accountBean.setAccountCharacter(accountCharacter);
//            accountBean.setAccountDescription(accountDescription);
            accountBean.setAccountInnerCode(accountInnerCode);
            accountBean.setAccountName(accountName);
            out = response.getWriter();
            accountServiceFacade.updateAccount(accountBean);


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
            logger.debug(" AccountController : editAccount 종료 ");
        }
        return null;
    }
}
