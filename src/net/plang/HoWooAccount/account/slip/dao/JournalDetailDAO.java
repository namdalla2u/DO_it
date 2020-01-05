package net.plang.HoWooAccount.account.slip.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.account.slip.to.JournalDetailBean;

public interface JournalDetailDAO {

    ArrayList<JournalDetailBean> selectJournalDetailList(String journalNo);

//	public JournalDetailBean selectJournalDetail(String journalDetailNo);

    void deleteJournalDetailByJournalNo(String journalNo);

    void updateJournalDetail(JournalDetailBean journalDetailBean);

    void insertJournalDetailList(String journalNo);

}
