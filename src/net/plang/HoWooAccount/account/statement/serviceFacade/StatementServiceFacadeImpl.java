package net.plang.HoWooAccount.account.statement.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.applicationService.StatementApplicationService;
import net.plang.HoWooAccount.account.statement.applicationService.StatementApplicationServiceImpl;
import net.plang.HoWooAccount.account.statement.to.CashJournalBean;
import net.plang.HoWooAccount.account.statement.to.DetailTrialBalanceBean;
import net.plang.HoWooAccount.account.statement.to.EarlyAssetBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;


public class StatementServiceFacadeImpl implements StatementServiceFacade {
    protected final Log logger = LogFactory.getLog(this.getClass());

    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
    StatementApplicationService statementApplicationService = StatementApplicationServiceImpl.getInstance();
    private static StatementServiceFacade instance;

    private StatementServiceFacadeImpl() {
    }

    public static StatementServiceFacade getInstance() {

        if (instance == null) {
            instance = new StatementServiceFacadeImpl();
            System.out.println("		@ StatementServiceFacadeImpl에 접근");
        }
        return instance;
    }


    @Override
    public HashMap<String, Object> getTotalTrialBalance(String toDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : getTotalTrialBalance 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        HashMap<String, Object> trialBalanceList = null;
        try {
            trialBalanceList = statementApplicationService.getTotalTrialBalance(toDate);
            System.out.println("		@ 조회일자 : " + toDate + " 까지");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }

        dataSourceTransactionManager.commitTransaction();

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : getTotalTrialBalance 종료 ");
        }
        return trialBalanceList;
    }


    @Override
    public HashMap<String, Object> getIncomeStatement(String toDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : getIncomeStatement   시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        HashMap<String, Object> incomeStatementList = null;
        try {
            incomeStatementList = statementApplicationService.getIncomeStatement(toDate);
            System.out.println("		@ 조회일자 : " + toDate + " 까지");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }

        dataSourceTransactionManager.commitTransaction();

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : getIncomeStatement   종료 ");
        }
        return incomeStatementList;

    }

    @Override
    public HashMap<String, Object> getFinancialPosition(String toDate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : getFinancialPosition 시작 ");
        }

        dataSourceTransactionManager.beginTransaction();
        HashMap<String, Object> financialPosition = null;
        try {
            financialPosition = statementApplicationService.getFinancialPosition(toDate);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }

        dataSourceTransactionManager.commitTransaction();

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : getFinancialPosition 종료 ");
        }
        return financialPosition;
    }

    @Override
    public ArrayList<EarlyAssetBean> findEarlyAssetlist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : findEarlyAssetlist   시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        ArrayList<EarlyAssetBean> earlyAssetlist = null;
        try {
            earlyAssetlist = statementApplicationService.findEarlyAssetlist();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        dataSourceTransactionManager.commitTransaction();

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : callIncomeStatement   종료 ");
        }
        return earlyAssetlist;
    }

    public ArrayList<EarlyAssetBean> findEarlyAssetStatelist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : findEarlyAssetlist   시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        ArrayList<EarlyAssetBean> earlyAssetStatelist = null;
        try {
            earlyAssetStatelist = statementApplicationService.findEarlyAssetStatelist();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        dataSourceTransactionManager.commitTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : callIncomeStatement   종료 ");
        }
        return earlyAssetStatelist;
    }

    public ArrayList<EarlyAssetBean> findEarlyAssetSummarylist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : earlyAssetSummarylist   시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        ArrayList<EarlyAssetBean> earlyAssetSummarylist = null;
        try {
            earlyAssetSummarylist = statementApplicationService.findEarlyAssetsummarylist();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        dataSourceTransactionManager.commitTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : earlyAssetSummarylist   종료 ");
        }
        return earlyAssetSummarylist;
    }

    public ArrayList<EarlyAssetBean> findEarlyStatelist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : earlyAssetSummarylist   시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        ArrayList<EarlyAssetBean> earlyState = null;
        try {
            earlyState = statementApplicationService.findEarlyStatelist();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        dataSourceTransactionManager.commitTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : earlyAssetSummarylist   종료 ");
        }
        return earlyState;
    }

    public ArrayList<EarlyAssetBean> findEarlyStateSummarylist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : earlyAssetSummarylist   시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        ArrayList<EarlyAssetBean> earlyStateSummarylist = null;
        try {
            earlyStateSummarylist = statementApplicationService.findEarlyStatesummarylist();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        dataSourceTransactionManager.commitTransaction();
        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : earlyAssetSummarylist   종료 ");
        }
        return earlyStateSummarylist;
    }

	@Override
	public ArrayList<DetailTrialBalanceBean> getDetailTrialBalance(String fromDate, String toDate) {
		 if (logger.isDebugEnabled()) {
	            logger.debug(" StatementServiceFacadeImpl : getDetailTrialBalance 시작 ");
	        }
	        dataSourceTransactionManager.beginTransaction();
	        ArrayList<DetailTrialBalanceBean> detailTrialBalanceList = null;
	        try {
	        	detailTrialBalanceList = statementApplicationService.getDetailTrialBalance(fromDate, toDate);
	        	System.out.println("		@ 조회일자: "+fromDate+"부터 ");
	            System.out.println(toDate+"까지");
	        } catch (DataAccessException e) {
	            logger.fatal(e.getMessage());
	            dataSourceTransactionManager.rollbackTransaction();
	            throw e;
	        }

	        dataSourceTransactionManager.commitTransaction();

	        if (logger.isDebugEnabled()) {
	            logger.debug(" StatementServiceFacadeImpl : getDetailTrialBalance 종료 ");
	        }
	        return detailTrialBalanceList;
	}

	@Override
	public ArrayList<CashJournalBean> getCashJournal(String fromDate, String toDate) {
		if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : getCashJournal 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        ArrayList<CashJournalBean> cashJournalList = null;
        try {
        	cashJournalList = statementApplicationService.getCashJournal(fromDate, toDate);
        	System.out.print("		@ 조회일자: "+fromDate+"부터 ");
            System.out.println(toDate+"까지");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }

        dataSourceTransactionManager.commitTransaction();

        if (logger.isDebugEnabled()) {
            logger.debug(" StatementServiceFacadeImpl : getCashJournal 종료 ");
        }
        return cashJournalList;
	}
    
}
