package net.plang.HoWooAccount.account.slip.applicationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.slip.dao.JournalDetailDAO;
import net.plang.HoWooAccount.account.slip.dao.JournalDetailDAOImpl;
import net.plang.HoWooAccount.account.slip.to.JournalDetailBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;

import java.util.ArrayList;

public class JournalDetailApplicationServiceImpl implements JournalDetailApplicationService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private static JournalDetailDAO journalDetailDAO = JournalDetailDAOImpl.getInstance();

    @Override
    public ArrayList<JournalDetailBean> getJournalDetailList(String journalNo) {
        if (logger.isDebugEnabled()) {
            logger.debug("getJournalDetailList 시작 ");
        }
        ArrayList<JournalDetailBean> journalDetailBeans = null;

        try {
            journalDetailBeans = journalDetailDAO.selectJournalDetailList(journalNo);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getJournalDetailList 종료 ");
        }

        return journalDetailBeans;
    }

    @Override
    public void addJournalDetailList(String journalNo) {
        if (logger.isDebugEnabled()) {
            logger.debug("addJournalDetailList 시작 ");
        }

        try {
            journalDetailDAO.insertJournalDetailList(journalNo);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("addJournalDetailList 종료 ");
        }
    }

    @Override
    public void editJournalDetail(JournalDetailBean journalDetailBean) {
        if (logger.isDebugEnabled()) {
            logger.debug("editJournalDetail 시작 ");
        }

        try {
            journalDetailDAO.updateJournalDetail(journalDetailBean);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("editJournalDetail 종료 ");
        }
    }

    private static JournalDetailApplicationServiceImpl ourInstance = new JournalDetailApplicationServiceImpl();

    public static JournalDetailApplicationServiceImpl getInstance() {
        return ourInstance;
    }

    private JournalDetailApplicationServiceImpl() {
    }
}
