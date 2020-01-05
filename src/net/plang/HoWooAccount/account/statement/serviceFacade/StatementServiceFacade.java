package net.plang.HoWooAccount.account.statement.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import net.plang.HoWooAccount.account.statement.to.CashJournalBean;
import net.plang.HoWooAccount.account.statement.to.DetailTrialBalanceBean;
import net.plang.HoWooAccount.account.statement.to.EarlyAssetBean;

public interface StatementServiceFacade {

    public HashMap<String, Object> getTotalTrialBalance(String toDate);

    public HashMap<String, Object> getFinancialPosition(String toDate);

    public HashMap<String, Object> getIncomeStatement(String toDate);
    
    public ArrayList<DetailTrialBalanceBean> getDetailTrialBalance(String fromDate, String toDate);

    public ArrayList<EarlyAssetBean> findEarlyAssetlist();

    public ArrayList<EarlyAssetBean> findEarlyAssetStatelist();

    public ArrayList<EarlyAssetBean> findEarlyAssetSummarylist();

    public ArrayList<EarlyAssetBean> findEarlyStatelist();

    public ArrayList<EarlyAssetBean> findEarlyStateSummarylist();

    public ArrayList<CashJournalBean> getCashJournal(String fromDate, String toDate);

}

