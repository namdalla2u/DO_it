package net.plang.HoWooAccount.account.statement.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import net.plang.HoWooAccount.account.statement.to.CashJournalBean;
import net.plang.HoWooAccount.account.statement.to.DetailTrialBalanceBean;
import net.plang.HoWooAccount.account.statement.to.EarlyAssetBean;

public interface StatementApplicationService {

    HashMap<String, Object> getTotalTrialBalance(String toDate);

    HashMap<String, Object> getIncomeStatement(String toDate);

    HashMap<String, Object> getFinancialPosition(String toDate);

    ArrayList<EarlyAssetBean> findEarlyAssetlist();

    ArrayList<EarlyAssetBean> findEarlyAssetStatelist();

    ArrayList<EarlyAssetBean> findEarlyAssetsummarylist();

    ArrayList<EarlyAssetBean> findEarlyStatelist();

    ArrayList<EarlyAssetBean> findEarlyStatesummarylist();
    
    ArrayList<DetailTrialBalanceBean> getDetailTrialBalance(String fromDate, String toDate);
    
    ArrayList<CashJournalBean> getCashJournal(String fromDate, String toDate);
}
