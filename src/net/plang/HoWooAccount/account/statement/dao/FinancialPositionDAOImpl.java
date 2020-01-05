package net.plang.HoWooAccount.account.statement.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.to.FinancialPositionBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import oracle.jdbc.internal.OracleTypes;

public class FinancialPositionDAOImpl implements FinancialPositionDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    private static FinancialPositionDAO instacne;

    private FinancialPositionDAOImpl() {
    }

    public static FinancialPositionDAO getInstance() {

        if (instacne == null) {
            instacne = new FinancialPositionDAOImpl();
            System.out.println("		@ FinancialPositionDAOImpl 접근");
        }
        return instacne;

    }

    @Override
    public HashMap<String, Object> callFinancialPosition(String toDate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" FinancialPositionDAOImpl : callFinancialPosition 시작 ");
        }
        Connection conn;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        HashMap<String, Object> result = new HashMap<>();
        ArrayList<FinancialPositionBean> financialPosition = new ArrayList<>();

        System.out.println("		@ " + toDate + " 까지 ");
        try {
            conn = dataSourceTransactionManager.getConnection();
            cstmt = conn.prepareCall("{call P_FINANCIAL_POSITION(?, ?, ?, ?)}");
            System.out.println("		@ 프로시저 호출");

            cstmt.setString(1, toDate);
            cstmt.registerOutParameter(2, OracleTypes.NUMBER);  // ERROR_CODE
            cstmt.registerOutParameter(3, OracleTypes.VARCHAR); // ERROR_MSG
            cstmt.registerOutParameter(4, OracleTypes.CURSOR);  // RESULT CURSOR
            cstmt.execute();

            rs = (ResultSet) cstmt.getObject(4);

            while (rs.next()) {
                FinancialPositionBean financialPositionBean = new FinancialPositionBean();

                financialPositionBean.setLev(rs.getInt("LEV"));
                financialPositionBean.setCategory(rs.getString("CATEGORY"));
                financialPositionBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                financialPositionBean.setAccountCode(rs.getString("ACCOUNT_CODE"));
                financialPositionBean.setBalanceDetail(rs.getInt("BALANCE_DETAIL"));
                financialPositionBean.setBalanceSummary(rs.getInt("BALANCE_SUMMARY"));
                financialPositionBean.setPreBalanceDetail(rs.getInt("PRE_BALANCE_DETAIL"));
                financialPositionBean.setPreBalanceSummary(rs.getInt("PRE_BALANCE_SUMMARY"));

                financialPosition.add(financialPositionBean);
            }

            result.put("financialPosition", financialPosition);
            result.put("errorCode", cstmt.getString(2));
            result.put("errorMsg", cstmt.getString(3));

        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(cstmt, rs);
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" FinancialPositionDAOImpl : callFinancialPosition 종료 ");
        }
        return result;
    }

}
