package net.plang.HoWooAccount.account.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.base.to.AccountBean;
import net.plang.HoWooAccount.account.base.to.AccountControlBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class AccountDAOImpl implements AccountDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());

    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();


    private static AccountDAO instance;

    private AccountDAOImpl() {
    }

    public static AccountDAO getInstance() {

        if (instance == null) {
            instance = new AccountDAOImpl();
        }
        return instance;
    }


    public AccountBean selectAccount(String accountCode) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountDAOImpl : selectAccount 시작 ");
        }

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        AccountBean accountBean = new AccountBean();
        try {
//            StringBuffer query = new StringBuffer();
//
//            query.append(" SELECT * from ACCOUNT ");
//            query.append(" WHERE ");
//            query.append(" AND PARENT_ACCOUNT_INNER_CODE IS not NULL ");
//            query.append(" AND ACCOUNT_INNER_CODE = ? ");
//            query.append(" FOR UPDATE ");

            String query = "SELECT * from ACCOUNT "
                    + " WHERE PARENT_ACCOUNT_INNER_CODE IS not NULL"
                    + " AND ACCOUNT_INNER_CODE = ?";

            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, accountCode);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                accountBean.setAccountInnerCode(rs.getString("ACCOUNT_INNER_CODE"));
                accountBean.setAccountCharacter(rs.getString("ACCOUNT_CHARACTER"));
                accountBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                accountBean.setAccountDescription(rs.getString("ACCOUNT_DESCRIPTION"));
                accountBean.setParentAccountInnercode(rs.getString("PARENT_ACCOUNT_INNER_CODE"));
            }
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountDAOImpl : selectAccount 종료 ");
        }
        return accountBean;
    }

    @Override
    public ArrayList<AccountBean> selectParentAccountList() {

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountDAOImpl : selectParentAccountList 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<AccountBean> accountList = new ArrayList<>();
        try {
            StringBuffer query = new StringBuffer();

            query.append("SELECT * FROM ACCOUNT ");
            query.append("WHERE ACCOUNT_INNER_CODE LIKE '%-%'");
            query.append(" AND ACCOUNT_INNER_CODE not IN ");
            query.append("  ('0101-0175','0176-0250') ");
            query.append("AND PARENT_ACCOUNT_INNER_CODE IS not NULL ");
            query.append(" ORDER BY ACCOUNT_INNER_CODE ");

            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AccountBean accountBean = new AccountBean();
                accountBean.setAccountInnerCode(rs.getString("ACCOUNT_INNER_CODE"));
                accountBean.setAccountCharacter(rs.getString("ACCOUNT_CHARACTER"));
                accountBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                accountBean.setAccountUseCheck(rs.getString("ACCOUNT_USE_CHECK"));
//                accountBean.setGroupCode(rs.getString("GROUP_CODE"));
                accountBean.setParentAccountInnercode(rs.getString("PARENT_ACCOUNT_INNER_CODE"));
                accountBean.setStatus("사용");
                accountList.add(accountBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" AccountDAOImpl : selectParentAccountList 종료 ");
            }
            return accountList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }

    }

    @Override
    public ArrayList<AccountBean> selectDetailAccountList(String code) {

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountDAOImpl : selectDetailAccountList 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<AccountBean> accountList = new ArrayList<>();
        try {
            StringBuffer query = new StringBuffer();

            query.append("SELECT * from ACCOUNT");
            query.append(" WHERE ACCOUNT_INNER_CODE not LIKE '%-%'");
            query.append(" AND PARENT_ACCOUNT_INNER_CODE = ?");
            query.append(" ORDER BY ACCOUNT_INNER_CODE ");

            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, code);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AccountBean accountBean = new AccountBean();
                accountBean.setAccountInnerCode(rs.getString("ACCOUNT_INNER_CODE"));
                accountBean.setAccountName(rs.getString("ACCOUNT_NAME")); 
                accountBean.setParentAccountInnercode(rs.getString("PARENT_ACCOUNT_INNER_CODE"));
                accountBean.setEditable(rs.getString("EDITABLE")); 
                accountBean.setStatus("사용");
                accountList.add(accountBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" AccountDAOImpl : selectDetailAccountList 종료 ");
            }
            return accountList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public void updateAccount(AccountBean accountBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountDAOImpl : updateAccount 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            StringBuffer query = new StringBuffer();

            query.append("UPDATE ACCOUNT SET");
            query.append(" ACCOUNT_NAME= ?");
            query.append(" WHERE ACCOUNT_INNER_CODE = ? ");


            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, accountBean.getAccountName());
            pstmt.setString(2, accountBean.getAccountInnerCode());

            rs = pstmt.executeQuery();

            if (logger.isDebugEnabled()) {
                logger.debug(" AccountDAOImpl : selectAccount 종료 ");
            }

        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public ArrayList<AccountBean> selectAccountListByName(String accountName) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountDAOImpl : selectAccountListByName 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<AccountBean> accountList = new ArrayList<>();
        try {
            String query = "SELECT * FROM ACCOUNT WHERE ACCOUNT_NAME LIKE ? AND ACCOUNT_CODE NOT LIKE '%-%'";

            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, "%" + accountName + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AccountBean accountBean = new AccountBean();

                accountBean.setAccountInnerCode(rs.getString("ACCOUNT_INNER_CODE"));
                accountBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                accountBean.setParentAccountInnercode(rs.getString("PARENT_ACCOUNT_INNER_CODE"));
                accountBean.setEditable(rs.getString("EDITABLE"));
                accountBean.setStatus(rs.getString("ACCOUNT_USE_CHECK"));

                accountList.add(accountBean);
            }
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountDAOImpl : selectAccountListByName 종료 ");
        }

        return accountList;
    }

    @Override
    public ArrayList<AccountControlBean> selectAccountControlList(String accountCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountDAOImpl : selectAccountControlList 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<AccountControlBean> accountControlDetailList = new ArrayList<>();
        try {
            String query = "SELECT D.*" +
                    "FROM ACCOUNT_CONTROL_CODE C," +
                    "     ACCOUNT_CONTROL_DETAIL D " +
                    "WHERE C.ACCOUNT_CODE = ?" +
                    "  AND C.ACCOUNT_CONTROL_CODE = D.ACCOUNT_CONTROL_CODE";

            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, accountCode);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AccountControlBean accountControlBean = new AccountControlBean();

                accountControlBean.setAccountControlCode(rs.getString("ACCOUNT_CONTROL_CODE"));
                accountControlBean.setAccountControlName(rs.getString("ACCOUNT_CONTROL_NAME"));
                accountControlBean.setAccountControlType(rs.getString("ACCOUNT_CONTROL_TYPE"));

                accountControlDetailList.add(accountControlBean);
            }
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountDAOImpl : selectAccountControlList 종료 ");
        }
        return accountControlDetailList;
    }
}
