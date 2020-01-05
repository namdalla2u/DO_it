package net.plang.HoWooAccount.account.base.serviceFacade;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.base.applicationService.AccountApplicationService;
import net.plang.HoWooAccount.account.base.applicationService.AccountApplicationServiceImpl;
import net.plang.HoWooAccount.account.base.to.AccountBean;
import net.plang.HoWooAccount.account.base.to.AccountControlBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class AccountServiceFacadeImpl implements AccountServiceFacade {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
    AccountApplicationService accountApplicationService = AccountApplicationServiceImpl.getInstance();

    private static AccountServiceFacade instance;

    private AccountServiceFacadeImpl() {
    }

    public static AccountServiceFacade getInstance() {

        if (instance == null) {
            instance = new AccountServiceFacadeImpl();
        }
        return instance;
    }

    @Override
    public AccountBean getAccount(String accountCode) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : getAccount 시작 ");
        }

        dataSourceTransactionManager.beginTransaction();
        AccountBean accountBean = null;

        try {
            accountBean = accountApplicationService.getAccount(accountCode);
            dataSourceTransactionManager.commitTransaction();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : getAccount 종료 ");
        }
        return accountBean;
    }

    @Override
    public ArrayList<AccountBean> findParentAccountList() {

        dataSourceTransactionManager.beginTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : findParentAccountList 시작 ");
        }
        ArrayList<AccountBean> accountList = null;

        try {

            accountList = accountApplicationService.findParentAccountList();
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : getAccount 종료 ");
        }
        return accountList;
    }

    @Override
    public ArrayList<AccountBean> findDetailAccountList(String code) {

        dataSourceTransactionManager.beginTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : finddetaccountList 시작 ");
        }
        ArrayList<AccountBean> accountList = null;

        try {

            accountList = accountApplicationService.findDetailAccountList(code);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : finddetuntList 종료 ");
        }
        return accountList;
    }

    @Override
    public void updateAccount(AccountBean accountBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : updateAccount 시작 ");
        }

        dataSourceTransactionManager.beginTransaction();

        try {
            accountApplicationService.updateAccount(accountBean);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : updateAccount 종료 ");
        }

    }

    @Override
    public ArrayList<AccountBean> getAccountListByName(String accountName) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : getAccountListByName 시작 ");
        }

        dataSourceTransactionManager.beginTransaction();
        ArrayList<AccountBean> accountList;

        try {
            accountList = accountApplicationService.getAccountListByName(accountName);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : getAccountListByName 종료 ");
        }
        return accountList;
    }

    @Override
    public ArrayList<AccountControlBean> getAccountControlList(String accountCode) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : getAccountControlList 시작 ");
        }

        dataSourceTransactionManager.beginTransaction();
        ArrayList<AccountControlBean> accountControlList;

        try {
            accountControlList = accountApplicationService.getAccountControlList(accountCode);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountServiceFacadeImpl : getAccountControlList 종료 ");
        }
        return accountControlList;
    }

}
