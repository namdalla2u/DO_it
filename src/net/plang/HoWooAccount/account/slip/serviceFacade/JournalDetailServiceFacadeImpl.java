package net.plang.HoWooAccount.account.slip.serviceFacade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.slip.applicationService.JournalDetailApplicationService;
import net.plang.HoWooAccount.account.slip.applicationService.JournalDetailApplicationServiceImpl;
import net.plang.HoWooAccount.account.slip.to.JournalDetailBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

import java.util.ArrayList;

public class JournalDetailServiceFacadeImpl implements JournalDetailServiceFacade {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
    private JournalDetailApplicationService journalDetailApplicationService = JournalDetailApplicationServiceImpl.getInstance();

    @Override
    public void addJournalDetailList(String journalNo) {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " 시작 ");
        }

        dataSourceTransactionManager.beginTransaction();
        try {
            journalDetailApplicationService.addJournalDetailList(journalNo);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " 종료 ");
        }
    }

    @Override
    public void editJournalDetail(JournalDetailBean journalDetailBean) {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " 시작 ");
        }

        dataSourceTransactionManager.beginTransaction();
        try {
            journalDetailApplicationService.editJournalDetail(journalDetailBean);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " 종료 ");
        }
    }

    @Override
    public ArrayList<JournalDetailBean> getJournalDetailList(String journalNo) {
        if (logger.isDebugEnabled()) {
            logger.debug("getJournalDetailList 시작 ");
        }
        ArrayList<JournalDetailBean> journalDetailBeans = null;
        dataSourceTransactionManager.beginTransaction();
        try {
            journalDetailBeans = journalDetailApplicationService.getJournalDetailList(journalNo);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" getJournalDetailList 종료 ");
        }
        return journalDetailBeans;
    }

    private static JournalDetailServiceFacadeImpl ourInstance = new JournalDetailServiceFacadeImpl();

    public static JournalDetailServiceFacadeImpl getInstance() {
        return ourInstance;
    }

    private JournalDetailServiceFacadeImpl() {
    }
}
