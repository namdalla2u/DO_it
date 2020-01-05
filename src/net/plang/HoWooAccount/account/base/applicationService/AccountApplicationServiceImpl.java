package net.plang.HoWooAccount.account.base.applicationService;


import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.base.dao.AccountDAO;
import net.plang.HoWooAccount.account.base.dao.AccountDAOImpl;
import net.plang.HoWooAccount.account.base.to.AccountBean;
import net.plang.HoWooAccount.account.base.to.AccountControlBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;

@SuppressWarnings("unused")
public class AccountApplicationServiceImpl implements AccountApplicationService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private AccountDAO accountDAO = AccountDAOImpl.getInstance();

    private static AccountApplicationService instance;

    private AccountApplicationServiceImpl() {
    }

    public static AccountApplicationService getInstance() {

        if (instance == null) {
            instance = new AccountApplicationServiceImpl();
        }
        return instance;
    }

    public AccountBean getAccount(String accountCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : getAccount 시작 ");
        }
        AccountBean accountBean = null;

        try {
            accountBean = accountDAO.selectAccount(accountCode);

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : getAccount 종료 ");
        }
        return accountBean;
    }

    @Override
    public ArrayList<AccountBean> findParentAccountList() {

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : findParentAccountList 시작 ");
        }
        ArrayList<AccountBean> accountList = null;

        try {
            accountList = accountDAO.selectParentAccountList();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : findParentAccountList 종료 ");
        }
        return accountList;
    }


    public ArrayList<AccountBean> findDetailAccountList(String code) {

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : getAccount 시작 ");
        }
        ArrayList<AccountBean> accountList = null;

        try {
            accountList = accountDAO.selectDetailAccountList(code);

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : getAccount 종료 ");
        }
        return accountList;
    }

    @Override
    public void updateAccount(AccountBean accountBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : updateAccount 시작 ");
        }
        ArrayList<AccountBean> accountList = null;

        try {
            accountDAO.updateAccount(accountBean);

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : updateAccount 종료 ");
        }

    }

    @Override
    public ArrayList<AccountBean> getAccountListByName(String accountName) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : getAccountListByName 시작 ");
        }
        ArrayList<AccountBean> accountList = null;

        try {
            accountList = accountDAO.selectAccountListByName(accountName);

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : getAccountListByName 종료 ");
        }
        return accountList;
    }

    @Override
    public ArrayList<AccountControlBean> getAccountControlList(String accountCode) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : getAccountControlList 시작 ");
        }
        ArrayList<AccountControlBean> accountControlList = null;

        try {
            accountControlList = accountDAO.selectAccountControlList(accountCode);

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountApplicationServiceImpl : getAccountControlList 종료 ");
        }
        return accountControlList;
    }


}
