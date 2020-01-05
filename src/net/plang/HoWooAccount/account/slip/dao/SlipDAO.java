package net.plang.HoWooAccount.account.slip.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.account.slip.to.SlipBean;

public interface SlipDAO {

    public ArrayList<SlipBean> selectSlipDataList(String slipDate);

    public void deleteSlip(String slipNo);

    public void updateSlip(SlipBean slipBean);

    public void insertSlip(SlipBean slipBean);

    void approveSlip(SlipBean slipBean);

    public ArrayList<SlipBean> selectRangedSlipList(String fromDate, String toDate);

    public ArrayList<SlipBean> selectDisApprovalSlipList();

    public int selectSlipCount(String today);
}
