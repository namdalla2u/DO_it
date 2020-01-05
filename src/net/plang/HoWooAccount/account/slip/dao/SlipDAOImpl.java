package net.plang.HoWooAccount.account.slip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.slip.to.SlipBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class SlipDAOImpl implements SlipDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    private static SlipDAO instace;

    private SlipDAOImpl() {
    }

    public static SlipDAO getInstance() {

        if (instace == null) {
            instace = new SlipDAOImpl();
            System.out.println("		@ SlipDAOImpl에 접근");
        }
        return instace;
    }


    @Override
    public ArrayList<SlipBean> selectSlipDataList(String slipDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : selectSlipDataList 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<SlipBean> slipList = new ArrayList<>();
        try {
            StringBuffer query = new StringBuffer();
            query.append(" SELECT * FROM SLIP WHERE REPORTING_DATE =? ORDER BY SLIP_NO ");
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setString(1, slipDate);
            System.out.println("		@ 조회할 일자: " + slipDate);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SlipBean slipBean = new SlipBean();
                slipBean.setSlipNo(rs.getString("SLIP_NO"));
                slipBean.setAccountPeriodNo(rs.getString("ACCOUNT_PERIOD_NO"));
                slipBean.setApprovalDate(rs.getString("Approval_date"));
                slipBean.setApprovalEmpCode(rs.getString("approval_emp_code"));
                slipBean.setAuthorizationStatus(rs.getString("Authorization_status"));
                slipBean.setDeptCode(rs.getString("Dept_code"));
                slipBean.setExpenseReport(rs.getString("Expense_report"));
                slipBean.setReportingDate(rs.getString("Reporting_date"));
                slipBean.setReportingEmpCode(rs.getString("Reporting_emp_code"));
                slipBean.setSlipStatus(rs.getString("Slip_status"));
                slipBean.setSlipType(rs.getString("Slip_type"));

                slipList.add(slipBean);
                System.out.println("		@ 전표 조회됨");
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" SlipDAOImpl : selectSlipDataList 종료 ");
            }
            return slipList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public void deleteSlip(String slipNo) {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : deleteSlip 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        dataSourceTransactionManager.beginTransaction();

        try {
            StringBuffer query = new StringBuffer();
            query.append("DELETE FROM SLIP WHERE SLIP_NO = ?");
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setString(1, slipNo);
            System.out.println("		@ 전표번호: " + slipNo);
            pstmt.executeUpdate();
            dataSourceTransactionManager.commitTransaction();
            System.out.println("		@ 전표 삭제됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : deleteSlip 종료 ");
        }
    }


    @Override
    public void updateSlip(SlipBean slipBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : updateSlip 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String query = "UPDATE SLIP SET SLIP_TYPE=?, EXPENSE_REPORT=? WHERE SLIP_NO=?";

            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, slipBean.getSlipType());
            pstmt.setString(2, slipBean.getExpenseReport());
            pstmt.setString(3, slipBean.getSlipNo());

            pstmt.executeUpdate();
            System.out.println("		@ 전표번호: " + slipBean.getSlipNo());
            System.out.println("		@ 전표 수정됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : updateSlip 종료 ");
        }


    }

    @Override
    public void insertSlip(SlipBean slipBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : insertSlip 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append(" INSERT INTO SLIP ");
            query.append(" (SLIP_NO,ACCOUNT_PERIOD_NO,DEPT_CODE,SLIP_TYPE,EXPENSE_REPORT,REPORTING_EMP_CODE,REPORTING_DATE,SLIP_STATUS,Approval_emp_code,APPROVAL_DATE)");
            query.append(" VALUES (?,?,?,?,?,?,?,?,?,?) ");
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setString(1, slipBean.getSlipNo());
            pstmt.setString(2, slipBean.getAccountPeriodNo());
            pstmt.setString(3, slipBean.getDeptCode());
            pstmt.setString(4, slipBean.getSlipType());
            pstmt.setString(5, slipBean.getExpenseReport());
            pstmt.setString(6, slipBean.getReportingEmpCode());
            pstmt.setString(7, slipBean.getReportingDate());
            pstmt.setString(8, slipBean.getSlipStatus());
            pstmt.setString(9, slipBean.getApprovalEmpCode());
            pstmt.setString(10, slipBean.getApprovalDate());
            pstmt.executeUpdate();
            System.out.println("		@ 전표번호: " + slipBean.getSlipNo());
            System.out.println("		@ 전표 추가됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : insertSlip 종료 ");
        }

    }

    @Override
    public void approveSlip(SlipBean slipBean) {
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : approveSlip 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            String query = "UPDATE SLIP SET APPROVAL_DATE=? ,SLIP_STATUS=? ,APPROVAL_EMP_CODE=? WHERE SLIP_NO=?";

            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, slipBean.getApprovalDate());
            pstmt.setString(2, slipBean.getSlipStatus());
            pstmt.setString(3, slipBean.getApprovalEmpCode());
            pstmt.setString(4, slipBean.getSlipNo());
            pstmt.executeUpdate();

            System.out.println("		@ 전표번호: " + slipBean.getSlipNo());
            System.out.println("		@ 전표 수정됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : approveSlip 종료 ");
        }
    }

    @Override
    public ArrayList<SlipBean> selectRangedSlipList(String fromDate, String toDate) {    //분개장 보기

        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : selectRangedSlipList 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<SlipBean> slipList = new ArrayList<>();
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT distinct s.SLIP_NO,s.REPORTING_EMP_CODE, ");
            query.append(" s.APPROVAL_EMP_CODE,s.EXPENSE_REPORT, s.SLIP_TYPE, ");
            query.append(" s.SLIP_STATUS, s.APPROVAL_DATE,s.REPORTING_DATE, ");
            query.append(" s.Dept_code, s.account_period_no ");
            query.append(" FROM slip s,journal j ");
            query.append(" WHERE s.slip_no=j.SLIP_NO ");
            query.append(" AND s.REPORTING_DATE BETWEEN  ? and ?");
            query.append(" ORDER BY s.REPORTING_DATE ");
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query.toString());
            System.out.println("		@ 조회 일자");
            System.out.println("		==========" + fromDate + "부터");
            System.out.println("		==========" + toDate + "까지");

            pstmt.setString(1, fromDate);
            pstmt.setString(2, toDate);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SlipBean slipBean = new SlipBean();
                slipBean.setSlipNo(rs.getString("SLIP_NO"));
                slipBean.setReportingEmpCode(rs.getString("REPORTING_EMP_CODE"));
                slipBean.setApprovalEmpCode(rs.getString("APPROVAL_EMP_CODE"));
                slipBean.setExpenseReport(rs.getString("EXPENSE_REPORT"));
                slipBean.setDeptCode(rs.getString("Dept_code"));
                slipBean.setAccountPeriodNo(rs.getString("account_period_no"));
                slipBean.setSlipType(rs.getString("SLIP_TYPE"));
                //slipBean.setBalanceDivision(rs.getString("BALANCE_DIVISION"));
                slipBean.setSlipStatus(rs.getString("SLIP_STATUS"));
                slipBean.setApprovalDate(rs.getString("APPROVAL_DATE"));
                slipBean.setReportingDate(rs.getString("REPORTING_DATE"));
                slipList.add(slipBean);
            }
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : selectRangedSlipList 종료 ");
        }
        return slipList;
    }

    @Override
    public ArrayList<SlipBean> selectDisApprovalSlipList() {    //미승인,승인 전표 조회
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : selectDisApprovalSlipList 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<SlipBean> disApprovalSlipList = new ArrayList<>();
        try {

            String query = "SELECT * FROM SLIP WHERE SLIP_STATUS='미승인' OR APPROVAL_DATE IS NULL";
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            int cnt = 0;
            while (rs.next()) {
                SlipBean slipBean = new SlipBean();
                slipBean.setAccountPeriodNo(rs.getString("ACCOUNT_PERIOD_NO"));
                slipBean.setApprovalDate(rs.getString("Approval_date"));
                slipBean.setApprovalEmpCode(rs.getString("approval_emp_code"));
                slipBean.setAuthorizationStatus(rs.getString("Authorization_status"));
                slipBean.setDeptCode(rs.getString("Dept_code"));
                slipBean.setExpenseReport(rs.getString("Expense_report"));
                slipBean.setReportingDate(rs.getString("Reporting_date"));
                slipBean.setReportingEmpCode(rs.getString("Reporting_emp_code"));
                slipBean.setSlipStatus(rs.getString("Slip_status"));
                slipBean.setSlipType(rs.getString("Slip_type"));
                slipBean.setSlipNo(rs.getString("Slip_NO"));

                disApprovalSlipList.add(slipBean);
                cnt += 1;
            }
            System.out.println("미승인 전표 조회 " + cnt + "건");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" SlipDAOImpl : selectDisApprovalSlipList 종료 ");
        }
        return disApprovalSlipList;
    }

    @Override
    public int selectSlipCount(String today) {
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : selectSlipCount 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int resultCount = 0;
        try {
            String query = "SELECT COUNT(*) FROM SLIP WHERE SLIP_NO LIKE '" + today + "%'";
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();
            if (rs.next())
                resultCount = rs.getInt(1);

        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(this.getClass().getName() + " : selectSlipCount 종료 ");
        }
        return resultCount;
    }
}
