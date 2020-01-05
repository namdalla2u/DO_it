package net.plang.HoWooAccount.account.slip.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.account.slip.to.JournalBean;

public interface JournalDAO {

    public ArrayList<JournalBean> selectRangedJournalList(String fromDate, String toDate);

    public ArrayList<JournalBean> selectJournalList(String slipNo);

    public JournalBean selectJournal(String journalNo);

    public String insertJournal(String slipNo, JournalBean journalBean);

    public void deleteJournal(JournalBean journalBean);

    public boolean updateJournal(JournalBean journalBean);
}
