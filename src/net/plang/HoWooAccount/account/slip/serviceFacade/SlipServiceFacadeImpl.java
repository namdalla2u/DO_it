package net.plang.HoWooAccount.account.slip.serviceFacade;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.slip.applicationService.SlipApplicationService;
import net.plang.HoWooAccount.account.slip.applicationService.SlipApplicationServiceImpl;
import net.plang.HoWooAccount.account.slip.to.JournalBean;
import net.plang.HoWooAccount.account.slip.to.SlipBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class SlipServiceFacadeImpl implements SlipServiceFacade {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
    SlipApplicationService slipApplicationService = SlipApplicationServiceImpl.getInstance();


    private static SlipServiceFacade instance;

    private SlipServiceFacadeImpl() {
    }

    public static SlipServiceFacade getInstance() {

        if (instance == null) {
            instance = new SlipServiceFacadeImpl();
            System.out.println("		@ SlipServiceFacadeImpl에 접근");
        }
        return instance;
    }

    @Override
    public String addSlip(SlipBean slipBean, ArrayList<JournalBean> journalBeans) {
        String slipNo;

        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : addSlip 시작 ");
        }
        ArrayList<SlipBean> slipList = null;
        dataSourceTransactionManager.beginTransaction();
        try {
            slipNo = slipApplicationService.addSlip(slipBean, journalBeans);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : addSlip 종료 ");
        }

        return slipNo;
    }

    @Override
    public void deleteSlip(String slipNo) {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : deleteSlip 시작 ");
        }
        ArrayList<SlipBean> slipList = null;
        try {
            slipApplicationService.deleteSlip(slipNo);
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
        ArrayList<SlipBean> slipList = null;
        dataSourceTransactionManager.beginTransaction();
        try {
            slipApplicationService.updateSlip(slipBean);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : updateSlip 종료 ");
        }
    }

    @Override
    public void approveSlip(ArrayList<SlipBean> slipBeans) {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : approveSlip 시작 ");
        }
        ArrayList<SlipBean> slipList = null;
        dataSourceTransactionManager.beginTransaction(); // 오토커밋을 안하기 위해서 호출
        try {
            slipApplicationService.approveSlip(slipBeans);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : approveSlip 종료 ");
        }
    }

    @Override
    public ArrayList<SlipBean> findSlipDataList(String slipDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findSlipDataList 시작 ");
        }
        ArrayList<SlipBean> slipList = null;
        try {
            slipList = slipApplicationService.findSlipDataList(slipDate);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findSlipDataList 종료 ");
        }
        return slipList;
    }

    @Override
    public ArrayList<SlipBean> findRangedSlipList(String fromDate, String toDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findRangedSlipList 시작 ");
        }
        ArrayList<SlipBean> slipList = null;
        try {
            slipList = slipApplicationService.findRangedSlipList(fromDate, toDate);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findRangedSlipList 종료 ");
        }
        return slipList;
    }

    @Override
    public ArrayList<SlipBean> findDisApprovalSlipList() {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findDisApprovalSlipList 시작 ");
        }
        ArrayList<SlipBean> disApprovalSlipList = null;
        try {
            disApprovalSlipList = slipApplicationService.findDisApprovalSlipList();
        } catch (Exception error) {
            logger.fatal(error.getMessage());
            throw error;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipServiceFacadeImpl : findDisApprovalSlipList 종료 ");
        }
        return disApprovalSlipList;
    }
}
