package net.plang.HoWooAccount.account.slip.applicationService;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.slip.dao.JournalDAO;
import net.plang.HoWooAccount.account.slip.dao.JournalDAOImpl;
import net.plang.HoWooAccount.account.slip.dao.JournalDetailDAO;
import net.plang.HoWooAccount.account.slip.dao.JournalDetailDAOImpl;
import net.plang.HoWooAccount.account.slip.dao.SlipDAO;
import net.plang.HoWooAccount.account.slip.dao.SlipDAOImpl;
import net.plang.HoWooAccount.account.slip.to.JournalBean;
import net.plang.HoWooAccount.account.slip.to.SlipBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.hr.dao.EmployeeDAO;
import net.plang.HoWooAccount.hr.dao.EmployeeDAOImpl;

public class SlipApplicationServiceImpl implements SlipApplicationService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private static SlipDAO slipDAO = SlipDAOImpl.getInstance();
    private static EmployeeDAO employeeDAO = EmployeeDAOImpl.getInstance();  /*DAO */
    private static JournalDAO journalDAO = JournalDAOImpl.getInstance();
    private static JournalDetailDAO journalDetailDAO = JournalDetailDAOImpl.getInstance();


    private static SlipApplicationService instance;

    private SlipApplicationServiceImpl() {
    }

    public static SlipApplicationService getInstance() {

        if (instance == null) {
            instance = new SlipApplicationServiceImpl();
        }
        return instance;
    }


    @Override
    public ArrayList<SlipBean> findSlipDataList(String slipDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findSlipDataList 시작 ");
        }
        ArrayList<SlipBean> slipList = null;
        try {
            slipList = slipDAO.selectSlipDataList(slipDate);
//            for (SlipBean slip : slipList) {
//
//                ArrayList<JournalBean> journalList = journalDAO.selectJournalList(slip.getSlipNo());
//                for (JournalBean jb : journalList) {
//                    jb.setJournalDetailBean(journalDetailDAO.selectJournalDetailList(jb.getJournalNo()));
//                }
//                slip.setJournalBean(journalList);
//            }
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findSlipDataList 종료 ");
        }
        return slipList;
    }

    @Override
    public String addSlip(SlipBean slipBean, ArrayList<JournalBean> journalBeans) {
        StringBuffer slipNo = new StringBuffer();

        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : addSlip 시작 ");
        }

        try { 
            String slipNoDate = slipBean.getReportingDate().replace("-", ""); // 20190717
            slipNo.append(slipNoDate);
            slipNo.append("SLIP"); // 20190717SLIP

            String code = "0000" + (slipDAO.selectSlipCount(slipNoDate) + 1) + ""; // 00003
            slipNo.append(code.substring(code.length() - 5)); // 00003

            slipBean.setSlipNo(slipNo.toString()); // 20190717SLIP00003

            slipDAO.insertSlip(slipBean);

            for (JournalBean journalBean : journalBeans) {
                String journalNo = journalDAO.insertJournal(slipBean.getSlipNo(), journalBean);
                System.out.println("###########" + journalNo);
                journalDetailDAO.insertJournalDetailList(journalNo);
            }

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : addSlip 종료 ");
        }

        return slipNo.toString();
    }

    @Override
    public void deleteSlip(String slipNo) {

        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : deleteSlip 시작 ");
        }

        try {
            slipDAO.deleteSlip(slipNo);

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : deleteSlip 종료 ");
        }
    }

    @Override
    public void updateSlip(SlipBean slipBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : updateSlip 시작 ");
        }

        try {
            slipDAO.updateSlip(slipBean);

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : updateSlip 종료 ");
        }
    }

    @Override
    public void approveSlip(ArrayList<SlipBean> slipBeans) {

        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : approveSlip 시작 ");
        }

        try {
            for (SlipBean slipBean : slipBeans) {
                slipBean.setSlipStatus(slipBean.getSlipStatus().equals("true") ? "승인" : "반려"); 
                slipDAO.approveSlip(slipBean);
            }

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : approveeSlip 종료 ");
        }
    }

    @Override
    public ArrayList<SlipBean> findRangedSlipList(String fromDate, String toDate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findRangedSlipList 시작 ");
        }
        ArrayList<SlipBean> slipList = null;
        try {
            slipList = slipDAO.selectRangedSlipList(fromDate, toDate);

				/* if (slip.getApprovalEmpCode() != null) {
					slip.setApprovalEmpName(employeeDAO.selectEmployee(slip.getApprovalEmpCode()).getEmpName());
				}*/
//            for (SlipBean slip : slipList) {
//
//                ArrayList<JournalBean> journalList = journalDAO.selectJournalList(slip.getSlipNo());
//                for (JournalBean jb : journalList) {
//                    jb.setJournalDetailBean(journalDetailDAO.selectJournalDetailList(jb.getJournalNo()));
//                }
//                slip.setJournalBean(journalList);
//            }
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findRangedSlipList 종료 ");
        }
        return slipList;
    }

    @Override
    public ArrayList<SlipBean> findDisApprovalSlipList() {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findDisApprovalSlipList 시작 ");
        }
        ArrayList<SlipBean> disApprovalSlipList = null;
        disApprovalSlipList = slipDAO.selectDisApprovalSlipList();

//        for (SlipBean slip : disApprovalSlipList) {
//            //slip.setReportingEmpCode(employeeDAO.selectEmployee(slip.getReportingEmpCode()).getEmpName());
//            System.out.println("슬립넘버" + slip.getSlipNo());
//            ArrayList<JournalBean> journalList = journalDAO.selectJournalList(slip.getSlipNo());
//            for (JournalBean jb : journalList) {
//                System.out.println(jb.getJournalNo());
//            }
//            slip.setJournalBean(journalList);
//        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipApplicationServiceImpl : findDisApprovalSlipList 종료 ");
        }
        return disApprovalSlipList;
    }


}


