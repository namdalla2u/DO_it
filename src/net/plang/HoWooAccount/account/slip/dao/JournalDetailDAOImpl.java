package net.plang.HoWooAccount.account.slip.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.slip.to.JournalDetailBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class JournalDetailDAOImpl implements JournalDetailDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager
            = DataSourceTransactionManager.getInstance();

    private static JournalDetailDAO instance;

    private JournalDetailDAOImpl() {
    }

    public static JournalDetailDAO getInstance() {
        if (instance == null) {
            instance = new JournalDetailDAOImpl();
            System.out.println("		@ JournalDetailDAOImpl에 접근");
        }
        return instance;
    }

    @Override
    public ArrayList<JournalDetailBean> selectJournalDetailList(String journalNo) {
        if (logger.isDebugEnabled()) {
            logger.debug("selectJournalDetailList 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<JournalDetailBean> journalDetailBeans = new ArrayList<>();
        try {
            String query = "SELECT J.JOURNAL_DETAIL_NO, " +
                    "J.DESCRIPTION AS JOURNAL_DESCRIPTION, " +
                    "A.ACCOUNT_CONTROL_TYPE, " +
                    "A.ACCOUNT_CONTROL_NAME, " +
                    "A.DESCRIPTION AS ACCOUNT_CONTROL_DESCRIPTION " +
                    "FROM JOURNAL_DETAIL J, ACCOUNT_CONTROL_DETAIL A " +
                    "WHERE JOURNAL_NO = ?" +
                    "  AND J.ACCOUNT_CONTROL_CODE = A.ACCOUNT_CONTROL_CODE";

            System.out.println("journalNo : " + journalNo);

            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, journalNo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                JournalDetailBean journalDetailBean = new JournalDetailBean();
                journalDetailBean.setJournalDetailNo(rs.getString("JOURNAL_DETAIL_NO"));
                journalDetailBean.setAccountControlName(rs.getString("ACCOUNT_CONTROL_NAME"));
                journalDetailBean.setAccountControlType(rs.getString("ACCOUNT_CONTROL_TYPE"));
                journalDetailBean.setJournalDescription(rs.getString("JOURNAL_DESCRIPTION"));
                journalDetailBean.setAccountControlDescription(rs.getString("ACCOUNT_CONTROL_DESCRIPTION"));

                journalDetailBeans.add(journalDetailBean);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("selectJournalDetailList 종료 ");
            }

            return journalDetailBeans;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public void deleteJournalDetailByJournalNo(String journalNo) {
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDetailDAOImpl : deleteJournalDetail 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        dataSourceTransactionManager.beginTransaction();
        try {
            String query = "DELETE FROM JOURNAL_DETAIL WHERE JOURNAL_NO = ?";

            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query);

            pstmt.setString(1, journalNo);
            pstmt.executeUpdate();
            dataSourceTransactionManager.commitTransaction();
            System.out.println("		@ 분개 세부 사항 삭제됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDetailDAOImpl : deleteJournalDetail 종료 ");
        }
    }

    @Override
    public void updateJournalDetail(JournalDetailBean journalDetailBean) {
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDetailDAOImpl : updateJournalDetail 시작 ");
        }
        Connection conn;
        PreparedStatement pstmt = null;
        try {
            String query = "UPDATE JOURNAL_DETAIL SET DESCRIPTION = ? WHERE JOURNAL_DETAIL_NO = ?";
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, journalDetailBean.getJournalDescription());
            pstmt.setString(2, journalDetailBean.getJournalDetailNo());

            pstmt.executeUpdate();
            System.out.println("		@ 분개 세부 사항 수정됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDetailDAOImpl : updateJournalDetail 종료 ");
        }

    }

    @Override
    public void insertJournalDetailList(String journalNo) {
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDetailDAOImpl : insertJournalDetailList 시작 ");
        }
        Connection conn;
        PreparedStatement pstmt = null;
        try {
            String query = "INSERT INTO JOURNAL_DETAIL (" +
                    "SELECT J.JOURNAL_NO, JOURNAL_DETAIL_NO_SEQUENCE.NEXTVAL, ACC.ACCOUNT_CONTROL_CODE, NULL " +
                    "FROM JOURNAL J," +
                    "     ACCOUNT_CONTROL_CODE ACC," +
                    "     ACCOUNT_CONTROL_DETAIL ACD " +
                    "WHERE J.JOURNAL_NO = ?" +
                    "  AND J.ACCOUNT_INNER_CODE = ACC.ACCOUNT_CODE" +
                    "  AND ACC.ACCOUNT_CONTROL_CODE = ACD.ACCOUNT_CONTROL_CODE)";

            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, journalNo);
            pstmt.executeUpdate();
            System.out.println("		@ 분개 세부 사항 추가됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" JournalDetailDAOImpl : insertJournalDetailList 종료 ");
        }
    }

}
