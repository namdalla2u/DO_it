package net.plang.HoWooAccount.account.slip.applicationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.slip.dao.JournalDAO;
import net.plang.HoWooAccount.account.slip.dao.JournalDAOImpl;
import net.plang.HoWooAccount.account.slip.dao.JournalDetailDAO;
import net.plang.HoWooAccount.account.slip.dao.JournalDetailDAOImpl;
import net.plang.HoWooAccount.account.slip.to.JournalBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;

import java.util.ArrayList;

public class JournalApplicationServiceImpl implements JournalApplicationService {
    private static JournalApplicationServiceImpl instance = new JournalApplicationServiceImpl();

    public static JournalApplicationServiceImpl getInstance() {
        return instance;
    }

    private JournalApplicationServiceImpl() {
    }

    protected final Log logger = LogFactory.getLog(this.getClass());
    private static JournalDAO journalDAO = JournalDAOImpl.getInstance();
    private static JournalDetailDAO journalDetailDAO = JournalDetailDAOImpl.getInstance();

    @Override
    public ArrayList<JournalBean> findRangedJournalList(String fromDate, String toDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findRangedJournalList 시작 ");
        }
        ArrayList<JournalBean> journalList = null;
        try {
            journalList = journalDAO.selectRangedJournalList(fromDate, toDate);

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findRangedSlipList 종료 ");
        }
        return journalList;
    }

    @Override
    public void editJournal(String slipNo, ArrayList<JournalBean> journalBeanList) {
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : editJournal 시작 ");
        }

        try {
            for (JournalBean journalBean : journalBeanList) {
                if (journalBean.getStatus().equals("insert"))
                    journalDAO.insertJournal(slipNo, journalBean);

                else if (journalBean.getStatus().equals("update")) {
                    boolean isChangeAccountCode = journalDAO.updateJournal(journalBean);

                    if (isChangeAccountCode) {
                        journalDetailDAO.deleteJournalDetailByJournalNo(journalBean.getJournalNo());
                        journalDetailDAO.insertJournalDetailList(journalBean.getJournalNo());
                    }
                }
            }
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : editJournal 종료 ");
        }
    }

    @Override
    public ArrayList<JournalBean> findSingleJournalList(String slipNo) {
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findRangedJournalList 시작 ");
        }
        ArrayList<JournalBean> journalList = null;

        try {
            journalList = journalDAO.selectJournalList(slipNo);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findRangedSlipList 종료 ");
        }
        return journalList;
    }


}
