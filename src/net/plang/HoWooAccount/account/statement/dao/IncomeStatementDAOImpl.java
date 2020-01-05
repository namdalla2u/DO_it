package net.plang.HoWooAccount.account.statement.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.to.IncomeStatementBean;
import net.plang.HoWooAccount.base.to.IncomeStatementResultBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import oracle.jdbc.internal.OracleTypes;

public class IncomeStatementDAOImpl implements IncomeStatementDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    private static IncomeStatementDAO instacne;

    private IncomeStatementDAOImpl() {
    }

    public static IncomeStatementDAO getInstance() {
        if (instacne == null) {
            instacne = new IncomeStatementDAOImpl();
            System.out.println("      @ IncomeStatementDAOImpl에 접근");
        }
        return instacne;

    }

    @Override
    public HashMap<String, Object> callIncomeStatement(String toDate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" IncomeStatementDAOImpl : callIncomeStatement 시작 ");
        }
        Connection conn;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<IncomeStatementBean> incomeStatement = new ArrayList<>();

        System.out.println("      @ " + toDate + " 까지 ");
        try {
            conn = dataSourceTransactionManager.getConnection();
            cstmt = conn.prepareCall("{call P_INCOME_STATEMENT(?, ?, ?, ?)}");
            System.out.println("      @ 프로시저 호출");
            cstmt.setString(1, toDate);
            cstmt.registerOutParameter(2, OracleTypes.NUMBER);  // ERROR_CODE
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR); // ERROR_MSG
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);  // RESULT CURSOR
            cstmt.execute();

            rs = (ResultSet) cstmt.getObject(4);

            while (rs.next()) {
                IncomeStatementBean incomeStatementBean = new IncomeStatementBean();
                incomeStatementBean.setAccountInnerCode(rs.getString("ACCOUNT_INNER_CODE"));
                incomeStatementBean.setAccountName(rs.getString("ACCOUNT_INNER_CODE")+"."+rs.getString("ACCOUNT_NAME"));
                incomeStatementBean.setParentAccountCode(rs.getString("PARENT_ACCOUNT_CODE"));
                incomeStatementBean.setIncome(rs.getString("INCOME"));
                incomeStatementBean.setIncomeSummary(rs.getString("INCOME_SUMMARY"));
                incomeStatementBean.setEarlyIncome(rs.getString("EARLY_INCOME"));
                incomeStatementBean.setEarlyIncomeSummary(rs.getString("EARLY_INCOME_SUMMARY"));
                incomeStatement.add(incomeStatementBean);              
            }

            result.put("incomeStatement", incomeStatement);
            result.put("errorCode", cstmt.getString(2));
            result.put("errorMsg", cstmt.getString(3));
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(cstmt, rs);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" IncomeStatementDAOImpl : callIncomeStatement 종료 ");
        }
        return result;
    }

}