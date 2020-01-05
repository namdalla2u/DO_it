package net.plang.HoWooAccount.account.statement.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.dao.*;
import net.plang.HoWooAccount.account.statement.to.CashJournalBean;
import net.plang.HoWooAccount.account.statement.to.DetailTrialBalanceBean;
import net.plang.HoWooAccount.account.statement.to.EarlyAssetBean;


public class StatementApplicationServiceImpl implements StatementApplicationService {
    protected final Log logger = LogFactory.getLog(this.getClass());

    private static TotalTrialBalanceDAO totalTrialBalanceDAO = TotalTrialBalanceDAOImpl.getInstance();
    private static FinancialPositionDAO financialPositionDAO = FinancialPositionDAOImpl.getInstance();
    private static IncomeStatementDAO IncomeStatementDAO = IncomeStatementDAOImpl.getInstance();
    private static EarlyAssetDAO EarlyAssetDAO = EarlyAssetDAOimpl.getInstance();
    private static DetailTrialBalanceDAO detailTrialBalanceDAO = DetailTrialBalanceDAOImpl.getInstance();
    private static CashJournalDAO cashJournalDAO = CashJournalDAOImpl.getInstance();

    private static StatementApplicationService instance;

    private StatementApplicationServiceImpl() {
    }

    public static StatementApplicationService getInstance() {

        if (instance == null) {
            instance = new StatementApplicationServiceImpl();
            System.out.println("		@ StatementApplicationServiceImpl에 접근");
        }
        return instance;
    }


    @Override
    public HashMap<String, Object> getTotalTrialBalance(String toDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : callTotalTrialBalance 시작 ");
        }
        HashMap<String, Object> trialBalanceList = null;
        try {
            trialBalanceList = totalTrialBalanceDAO.callTotalTrialBalance(toDate);

        } catch (Exception error) {
            logger.fatal(error.getMessage());
            throw error;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : callTotalTrialBalance 종료 ");
        }
        return trialBalanceList;
    }

    @Override
    public HashMap<String, Object> getIncomeStatement(String toDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : callIncomeStatement 시작 ");
        }
        HashMap<String, Object> incomeStatementList = null;
        try {
            incomeStatementList = IncomeStatementDAO.callIncomeStatement(toDate);

        } catch (Exception error) {
            logger.fatal(error.getMessage());
            throw error;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : callIncomeStatement 종료 ");
        }
        return incomeStatementList;
    }

    @Override
    public HashMap<String, Object> getFinancialPosition(String toDate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : callTotalTrialBalance 시작 ");
        }
        HashMap<String, Object> financialPosition;
        try {
            financialPosition = financialPositionDAO.callFinancialPosition(toDate);
        } catch (Exception error) {
            logger.fatal(error.getMessage());
            throw error;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : callTotalTrialBalance 종료 ");
        }
        return financialPosition;
    }

    @Override
    public ArrayList<EarlyAssetBean> findEarlyAssetlist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : findEarlyAssetlist 시작 ");
        }
        ArrayList<EarlyAssetBean> earlyAssetlist = null;
        try {
            earlyAssetlist = EarlyAssetDAO.findEarlyAssetlist();

        } catch (Exception error) {
            logger.fatal(error.getMessage());
            throw error;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : findEarlyAssetlist 종료 ");
        }
        return earlyAssetlist;
    }

    @Override
    public ArrayList<EarlyAssetBean> findEarlyAssetStatelist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : findEarlyAssetlist 시작 ");
        }
        ArrayList<EarlyAssetBean> earlyAssetStatelist = null;
        try {
            earlyAssetStatelist = EarlyAssetDAO.findEarlyStatementslist();

        } catch (Exception error) {
            logger.fatal(error.getMessage());
            throw error;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : findEarlyAssetlist 종료 ");
        }
        return earlyAssetStatelist;
    }

    @Override
    public ArrayList<EarlyAssetBean> findEarlyAssetsummarylist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : findEarlyAssetlist 시작 ");
        }
        ArrayList<EarlyAssetBean> earlyAssetsummarylist = null;
        try {
            earlyAssetsummarylist = EarlyAssetDAO.earlyAssetsummarylist();

        } catch (Exception error) {
            logger.fatal(error.getMessage());
            throw error;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : findEarlyAssetlist 종료 ");
        }
        return earlyAssetsummarylist;
    }

    @Override
    public ArrayList<EarlyAssetBean> findEarlyStatelist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : findEarlyAssetlist 시작 ");
        }
        ArrayList<EarlyAssetBean> earlyState = null;
        try {
            earlyState = EarlyAssetDAO.earlyStatelist();

        } catch (Exception error) {
            logger.fatal(error.getMessage());
            throw error;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : findEarlyAssetlist 종료 ");
        }
        return earlyState;
    }

    @Override
    public ArrayList<EarlyAssetBean> findEarlyStatesummarylist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : earlyStatesummarylist 시작 ");
        }
        ArrayList<EarlyAssetBean> earlyStateSummary = null;
        try {
            earlyStateSummary = EarlyAssetDAO.earlyStateSummarylist();

        } catch (Exception error) {
            logger.fatal(error.getMessage());
            throw error;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementApplicationServiceImpl : earlyStatesummarylist 종료 ");
        }
        return earlyStateSummary;
    }

	@Override
	public ArrayList<DetailTrialBalanceBean> getDetailTrialBalance(String fromDate, String toDate) {
		 if (logger.isDebugEnabled()) {
	            logger.debug(" StatementApplicationServiceImpl : callDetailTrialBalance 시작 ");
	        }
		 ArrayList<DetailTrialBalanceBean> detailTrialBalanceList = null;
	        try {
	        	detailTrialBalanceList = detailTrialBalanceDAO.selectDetailTrialBalance(fromDate, toDate);

	        } catch (Exception error) {
	            logger.fatal(error.getMessage());
	            throw error;
	        }
	        if (logger.isDebugEnabled()) {
	            logger.debug(" StatementApplicationServiceImpl : callDetailTrialBalance 종료 ");
	        }
	        return detailTrialBalanceList;
	}

	@Override
	public ArrayList<CashJournalBean> getCashJournal(String fromDate, String toDate) {
		 if (logger.isDebugEnabled()) {
	            logger.debug(" StatementApplicationServiceImpl : getCashJournal 시작 ");
	        }
		 ArrayList<CashJournalBean> cashJournalList = null;
	        try {
	        	cashJournalList = cashJournalDAO.selectCashJournalList(fromDate, toDate);

	        } catch (Exception error) {
	            logger.fatal(error.getMessage());
	            throw error;
	        }
	        if (logger.isDebugEnabled()) {
	            logger.debug(" StatementApplicationServiceImpl : getCashJournal 종료 ");
	        }
	        return cashJournalList;
	}
	
	
}
