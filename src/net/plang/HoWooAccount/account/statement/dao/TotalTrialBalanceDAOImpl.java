package net.plang.HoWooAccount.account.statement.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.to.TotalTrialBalanceBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import oracle.jdbc.internal.OracleTypes;

public class TotalTrialBalanceDAOImpl implements TotalTrialBalanceDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    private static TotalTrialBalanceDAO instacne;

    private TotalTrialBalanceDAOImpl() {
    }

    public static TotalTrialBalanceDAO getInstance() {
        if (instacne == null) {
            instacne = new TotalTrialBalanceDAOImpl();
            System.out.println("		@ TrialBalanceDAOImpl에 접근");
        }
        return instacne;

    }

    @Override
    public HashMap<String, Object> callTotalTrialBalance(String toDate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" TotalTrialBalanceDAOImpl : callTotalTrialBalance 시작 ");
        }
        Connection conn;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<TotalTrialBalanceBean> totalTrialBalance = new ArrayList<>();

        System.out.println("		@ " + toDate + " 까지 ");
        try {
            conn = dataSourceTransactionManager.getConnection();
            cstmt = conn.prepareCall("{call P_TOTAL_TRIAL_BALANCE(?, ?, ?, ?)}");
            System.out.println("		@ 프로시저 호출");
            cstmt.setString(1, toDate);

            cstmt.registerOutParameter(2, OracleTypes.NUMBER);  // ERROR_CODE
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR); // ERROR_MSG
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);  // RESULT CURSOR
            cstmt.execute();

            rs = (ResultSet) cstmt.getObject(4);

            while (rs.next()) {
                TotalTrialBalanceBean totalTrialBalanceBean = new TotalTrialBalanceBean();
                totalTrialBalanceBean.setLev(rs.getInt("LEV"));
                totalTrialBalanceBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                totalTrialBalanceBean.setAccountInnerCode(rs.getString("CODE"));
                totalTrialBalanceBean.setDebitsSumBalance(rs.getInt("DEBITS_SUM_BALANCE"));
                totalTrialBalanceBean.setDebitsSum(rs.getInt("DEBITS_SUM"));
                totalTrialBalanceBean.setCreditsSum(rs.getInt("CREDITS_SUM"));
                totalTrialBalanceBean.setCreditsSumBalance(rs.getInt("CREDITS_SUM_BALANCE"));

                totalTrialBalance.add(totalTrialBalanceBean);
            }

            result.put("totalTrialBalance", totalTrialBalance);
            result.put("errorCode", cstmt.getString(2));
            result.put("errorMsg", cstmt.getString(3));
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(cstmt, rs);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" TotalTrialBalanceDAOImpl : callTotalTrialBalance 종료 ");
        }
        return result;
    }

}
