package net.plang.HoWooAccount.account.statement.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.account.statement.to.CashJournalBean;

public interface CashJournalDAO {


    public ArrayList<CashJournalBean> selectCashJournalList(String fromDate, String toDate);

}
