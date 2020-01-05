package net.plang.HoWooAccount.account.statement.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.to.EarlyAssetBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import oracle.jdbc.OracleTypes;

public class EarlyAssetDAOimpl implements EarlyAssetDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());

    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();


    private static EarlyAssetDAO instance;

    private EarlyAssetDAOimpl() {
    }

    public static EarlyAssetDAO getInstance() {

        if (instance == null) {
            instance = new EarlyAssetDAOimpl();
        }
        return instance;
    }

    @Override
    public ArrayList<EarlyAssetBean> findEarlyAssetlist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetDAOimpl : findEarlyAssetlist 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<EarlyAssetBean> earlyAssetlist = new ArrayList<>();
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT NVL(ACCOUNT_NAME,'총합') AS ACCOUNT_NAME,");
            query.append("SUM(DECODE(ACCOUNT_NAME,'대손충당금',-PRICE,'감가상각누계액',-PRICE,PRICE)) AS PRICE ");
            query.append("FROM  ");
            query.append("(SELECT GROUP_CODE,ACCOUNT_NAME,PRICE FROM EARLY_ASSETS) ");
            query.append("GROUP BY ROLLUP((GROUP_CODE,ACCOUNT_NAME))  ");

            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            // pstmt.setString(1, code);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                EarlyAssetBean earlyAssetBean = new EarlyAssetBean();
                earlyAssetBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                earlyAssetBean.setPrice(rs.getString("PRICE"));
                earlyAssetlist.add(earlyAssetBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" AccountDAOImpl : selectAccount 종료 ");
            }
            return earlyAssetlist;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public ArrayList<EarlyAssetBean> findEarlyStatementslist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetDAOimpl : findEarlyStatementslist 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<EarlyAssetBean> earlyAssetlist = new ArrayList<>();
        try {
            StringBuffer query = new StringBuffer();
            query.append(" SELECT  * from EARLY_STATEMENTS ");
            query.append(" WHERE STATEMENTS_division='재무' ");
            query.append(" AND BALANCE_DIVISION='대변' ");
            query.append(" AND ACCOUNT_NAME NOT IN ('대손충당금','감가상각누계액') ");

            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            // pstmt.setString(1, code);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                EarlyAssetBean earlyAssetBean = new EarlyAssetBean();

                earlyAssetBean.setAccountInnerCode(rs.getString("ACCOUNT_INNER_CODE"));
                earlyAssetBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                earlyAssetBean.setRightCreditsPrice(rs.getString("RIGHT_CREDITS_PRICE"));
                earlyAssetlist.add(earlyAssetBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" AccountDAOImpl : findEarlyStatementslist 종료 ");
            }
            return earlyAssetlist;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    public ArrayList<EarlyAssetBean> earlyAssetsummarylist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetDAOimpl : earlyAssetsummarylist 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<EarlyAssetBean> earlyAssetsummarylist = new ArrayList<>();
        try {
            StringBuffer query = new StringBuffer();
            query.append(" SELECT  * from EARLY_VIEW ");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            // pstmt.setString(1, code);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                EarlyAssetBean earlyAssetBean = new EarlyAssetBean();
                earlyAssetBean.setGropuCode(rs.getString("GROUP_CODE"));
                earlyAssetBean.setPrice(rs.getString("PRICE"));
                earlyAssetsummarylist.add(earlyAssetBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" AccountDAOImpl : earlyAssetsummarylist 종료 ");
            }
            return earlyAssetsummarylist;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public ArrayList<EarlyAssetBean> earlyStatelist() {

        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetDAOimpl : earlyStatelist 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<EarlyAssetBean> earlyState = new ArrayList<>();
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT ACCOUNT_NAME,(NVL(LEFT_DEBTOR_PRICE,0)+NVL(RIGHT_CREDITS_PRICE,0)) AS PRICE");
            query.append(" FROM EARLY_STATEMENTS  ");
            query.append(" WHERE STATEMENTS_DIVISION='손익' ");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                EarlyAssetBean earlyAssetBean = new EarlyAssetBean();
                earlyAssetBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                //System.out.println(""+earlyAssetBean.getAccountName());
                earlyAssetBean.setPrice(rs.getString("PRICE"));
                earlyState.add(earlyAssetBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" AccountDAOImpl : earlyStatelist 종료 ");
            }
            return earlyState;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public ArrayList<EarlyAssetBean> earlyStateSummarylist() {


        if (logger.isDebugEnabled()) {
            logger.debug(" EarlyAssetDAOimpl : earlyStateSummarylist 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        ArrayList<EarlyAssetBean> earlyState = new ArrayList<>();
        try {
            /*StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM EARLY_STATE_VIEW ");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();*/
            con = dataSourceTransactionManager.getConnection();
            cstmt = con.prepareCall("{call EX_INCOME_STATE(?,?,?)}");
            cstmt.registerOutParameter(1, OracleTypes.NUMBER);  /*에러코드*/
            cstmt.registerOutParameter(2, OracleTypes.VARCHAR);  /*에러메세지*/
            cstmt.registerOutParameter(3, OracleTypes.CURSOR);   /*프로시저에서 result였던것!*/
            cstmt.execute();
            rs = (ResultSet) cstmt.getObject(3);
            while (rs.next()) {
                EarlyAssetBean earlyAssetBean = new EarlyAssetBean();
                earlyAssetBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                //System.out.println("AAAAAAAAAA"+earlyAssetBean.getAccountName());
                earlyAssetBean.setPrice(rs.getString("PRICE"));
                earlyState.add(earlyAssetBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" AccountDAOImpl : earlyStateSummarylist 종료 ");
            }
            return earlyState;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }
}

