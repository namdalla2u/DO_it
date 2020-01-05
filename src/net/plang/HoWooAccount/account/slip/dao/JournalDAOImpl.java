package net.plang.HoWooAccount.account.slip.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.slip.to.JournalBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class JournalDAOImpl implements JournalDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());

    private DataSourceTransactionManager dataSourceTransactionManager
            = DataSourceTransactionManager.getInstance();

    private static JournalDAO instance;

    private JournalDAOImpl() {
    }

    public static JournalDAO getInstance() {

        if (instance == null) {
            instance = new JournalDAOImpl();
            System.out.println("		@ JournalDAOImpl에 접근");
        }
        return instance;
    }

    @Override
    public JournalBean selectJournal(String journalNo) {
        return null;
    }

    public ArrayList<JournalBean> selectRangedJournalList(String fromDate, String toDate) {

        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDAOImpl : selectRangedJournalList 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<JournalBean> journalList = new ArrayList<>();
        try {
            String query = "SELECT J.*, A.ACCOUNT_NAME FROM SLIP S, JOURNAL J, ACCOUNT A" +
                    " WHERE J.SLIP_NO = S.SLIP_NO" +
                    "  AND J.ACCOUNT_INNER_CODE = A.ACCOUNT_INNER_CODE" +
                    "  AND S.REPORTING_DATE BETWEEN ? AND ?" +
                    "  AND S.SLIP_STATUS = '승인'" +
                    " ORDER BY S.REPORTING_DATE, S.SLIP_NO, J.JOURNAL_NO";

            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, fromDate);
            pstmt.setString(2, toDate);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                JournalBean journalBean = new JournalBean();
                journalBean.setJournalNo(rs.getString("JOURNAL_NO"));
                // System.out.println(rs.getString("ACCOUNT_INNER_CODE"));
                journalBean.setSlipNo(rs.getString("SLIP_NO"));
                journalBean.setBalanceDivision(rs.getString("BALANCE_DIVISION"));
                journalBean.setAccountCode(rs.getString("ACCOUNT_INNER_CODE"));
                journalBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                journalBean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
                journalBean.setLeftDebtorPrice(rs.getString("LEFT_DEBTOR_PRICE"));
                journalBean.setRightCreditsPrice(rs.getString("RIGHT_CREDITS_PRICE"));

                journalList.add(journalBean);
                //System.out.println("		@ 분개 조회됨");
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" JournalDAOImpl : selectRangedJournalList 종료 ");
            }
            return journalList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public ArrayList<JournalBean> selectJournalList(String slipNo) {

        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDAOImpl : selectJournalList 시작 ");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<JournalBean> journalList = new ArrayList<>();
        try {
            String query = "SELECT J.*, A.ACCOUNT_NAME FROM SLIP S, JOURNAL J, ACCOUNT A " +
                    "WHERE J.SLIP_NO = S.SLIP_NO" +
                    "  AND J.ACCOUNT_INNER_CODE = A.ACCOUNT_INNER_CODE" +
                    "  AND S.SLIP_NO = ?";
            conn = dataSourceTransactionManager.getConnection();

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, slipNo);
            logger.debug("쿼리 실행확인");
            rs = pstmt.executeQuery();
            logger.debug("쿼리 완료");
            while (rs.next()) {
                JournalBean journalBean = new JournalBean();
                journalBean.setJournalNo(rs.getString("JOURNAL_NO"));

                journalBean.setSlipNo(rs.getString("SLIP_NO"));
                journalBean.setBalanceDivision(rs.getString("BALANCE_DIVISION"));
                journalBean.setAccountCode(rs.getString("ACCOUNT_INNER_CODE"));
                journalBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                journalBean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
                journalBean.setLeftDebtorPrice(rs.getString("LEFT_DEBTOR_PRICE"));
                journalBean.setRightCreditsPrice(rs.getString("RIGHT_CREDITS_PRICE"));
                journalList.add(journalBean);
            }

            logger.debug("빈 생성 완료");
            if (logger.isDebugEnabled()) {
                logger.debug(" JournalDAOImpl : selectJournalList 종료 ");
            }
            return journalList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public String insertJournal(String slipNo, JournalBean journalBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDAOImpl : insertJournal 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        String journalNo = null;
        ResultSet rs;

        try {
            String query = "SELECT ? || 'JOURNAL' || " +
                    "NVL(SUBSTR(MAX(JOURNAL_NO), INSTR(MAX(JOURNAL_NO), 'JOURNAL') + 7) + 1, 0) " +
                    "FROM JOURNAL " +
                    "WHERE SLIP_NO = ?";

            conn = dataSourceTransactionManager.getConnection();

            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, slipNo);
            pstmt.setString(2, slipNo);
            rs = pstmt.executeQuery();

            if (rs.next())
                journalNo = rs.getString(1);

            System.out.println("		@ journalNo : " + journalNo);

            String query2 = "INSERT INTO JOURNAL" +
                    " (JOURNAL_NO, SLIP_NO, BALANCE_DIVISION, ACCOUNT_INNER_CODE, CUSTOMER_CODE, RIGHT_CREDITS_PRICE, LEFT_DEBTOR_PRICE)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(query2);
            pstmt.setString(1, journalNo);
            pstmt.setString(2, slipNo);
            pstmt.setString(3, journalBean.getBalanceDivision());
            pstmt.setString(4, journalBean.getAccountCode());
            pstmt.setString(5, journalBean.getCustomerCode());
            pstmt.setString(6, journalBean.getRightCreditsPrice());
            pstmt.setString(7, journalBean.getLeftDebtorPrice());
            pstmt.executeUpdate();
            System.out.println("		@ 분개 추가됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDAOImpl : insertJournal 종료 ");
        }

        return journalNo;
    }

    @Override
    public void deleteJournal(JournalBean journalBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDAOImpl : deleteJournal 시작 ");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        dataSourceTransactionManager.beginTransaction();
        try {
            StringBuffer query = new StringBuffer();
            query.append("DELETE FROM JOURNAL WHERE SLIP_NO =? AND JOURNAL_NO=?");
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query.toString());
            pstmt.setString(1, journalBean.getSlipNo());
            pstmt.setString(2, journalBean.getJournalNo());
            pstmt.executeUpdate();
            dataSourceTransactionManager.commitTransaction();
            System.out.println("		@ 분개 삭제됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDAOImpl : deleteJournal 종료 ");
        }

    }

    @Override
    public boolean updateJournal(JournalBean journalBean) {
    	System.out.println(journalBean.getLeftDebtorPrice());
    	System.out.println(journalBean.getRightCreditsPrice());

        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDAOImpl : updateJournal 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean isChangeAccountCode = false;

        try {
/*            String query = "SELECT DECODE(ACCOUNT_INNER_CODE, ?, 0, 1) FROM JOURNAL WHERE JOURNAL_NO = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, journalBean.getAccountCode());
            rs = pstmt.executeQuery();

            if (rs.next())
                isChangeAccountCode = rs.getInt(1) == 1;*/

            String updateQuery = "UPDATE JOURNAL SET " +
                    "BALANCE_DIVISION=?, ACCOUNT_INNER_CODE=?, CUSTOMER_CODE=?,RIGHT_CREDITS_PRICE=?, LEFT_DEBTOR_PRICE=? " +
                    "WHERE SLIP_NO =? AND JOURNAL_NO =?";

            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, journalBean.getBalanceDivision());
            pstmt.setString(2, journalBean.getAccountCode());
            pstmt.setString(3, journalBean.getCustomerCode());
            pstmt.setString(4, journalBean.getRightCreditsPrice());
            pstmt.setString(5, journalBean.getLeftDebtorPrice());
            pstmt.setString(6, journalBean.getSlipNo());
            pstmt.setString(7, journalBean.getJournalNo());

            pstmt.executeUpdate();
            System.out.println("		@ 분개 수정됨");
        } catch (Exception sqle) {
        	System.out.println(sqle.getMessage());
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDAOImpl : updateJournal 종료 ");
        }

        return isChangeAccountCode;
    }

}
