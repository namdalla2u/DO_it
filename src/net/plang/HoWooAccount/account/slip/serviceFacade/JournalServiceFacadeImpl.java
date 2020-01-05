package net.plang.HoWooAccount.account.slip.serviceFacade;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.slip.applicationService.JournalApplicationService;
import net.plang.HoWooAccount.account.slip.applicationService.JournalApplicationServiceImpl;
import net.plang.HoWooAccount.account.slip.to.JournalBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

import java.util.ArrayList;

public class JournalServiceFacadeImpl implements JournalServiceFacade {
    private static JournalServiceFacadeImpl instance = new JournalServiceFacadeImpl();

    public static JournalServiceFacadeImpl getInstance() {
        return instance;
    }

    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    JournalApplicationService journalApplicationService = JournalApplicationServiceImpl.getInstance();

    @Override
    public ArrayList<JournalBean> findRangedJournalList(String fromDate, String toDate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findRangedSlipList 시작 ");
        }
        ArrayList<JournalBean> journalList = null;
        dataSourceTransactionManager.beginTransaction();
        try {
            journalList = journalApplicationService.findRangedJournalList(fromDate, toDate);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findRangedSlipList 종료 ");
        }
        return journalList;
    }

    @Override
    public ArrayList<JournalBean> findSingleJournalList(String slipNo) {
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findRangedSlipList 시작 ");
        }
        ArrayList<JournalBean> journalList = null;
        dataSourceTransactionManager.beginTransaction();
        try {
            journalList = journalApplicationService.findSingleJournalList(slipNo);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findRangedSlipList 종료 ");
        }
        return journalList;
    }

    @Override
    public void editJournal(String slipNo, ArrayList<JournalBean> journalBeanList) {
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : editJournal 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        try {
            journalApplicationService.editJournal(slipNo, journalBeanList);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : editJournal 종료 ");
        }
        return;
    }

    private JournalServiceFacadeImpl() {
    }
}
