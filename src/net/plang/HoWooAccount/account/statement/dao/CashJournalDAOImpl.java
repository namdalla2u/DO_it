package net.plang.HoWooAccount.account.statement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.to.CashJournalBean;
import net.plang.HoWooAccount.account.statement.to.DetailTrialBalanceBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class CashJournalDAOImpl implements CashJournalDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    private static CashJournalDAOImpl instacne;

    private CashJournalDAOImpl() {
    }

    public static CashJournalDAOImpl getInstance() {
        if (instacne == null) {
            instacne = new CashJournalDAOImpl();
            System.out.println("		@ CashJournalDAOImpl에 접근");
        }
        return instacne;

    }

    @Override
    public ArrayList<CashJournalBean> selectCashJournalList(String fromDate, String toDate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" CashJournalDAOImpl : selectCashJournalList 시작 ");
        }
        Connection conn;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<CashJournalBean> cashJournalList = new ArrayList<>();

        System.out.print("		@ 조회일자: "+fromDate+"부터 ");
        System.out.println(toDate+"까지");
        try {
        	StringBuffer query = new StringBuffer();
        	
        	// 제일 하단에 쿼리문 원본 써놨습니다.

        	query.append("SELECT CASE WHEN LEV IS NULL THEN '2' ELSE LEV END AS LEV, MONTH_REPORTING_DATE, CASE WHEN LEV = '1' THEN '[전 일 이 월]' ");
        	query.append("WHEN REPORTING_DATE IS NULL THEN '[월 계]' ELSE REPORTING_DATE END AS REPORTING_DATE, EXPENSE_REPORT, CUSTOMER_CODE, ");
        	query.append("CUSTOMER_NAME, SUM(DEPOSIT) AS DEPOSIT, SUM(WITHDRAWAL) AS WITHDRAWAL, BALANCE, SLIP_NO FROM (SELECT LEV, MONTH_REPORTING_DATE, ");
        	query.append("REPORTING_DATE, EXPENSE_REPORT, CUSTOMER_CODE, CUSTOMER_NAME, DEPOSIT, WITHDRAWAL, CASE WHEN LEV = '1' ");
        	query.append("THEN DEPOSIT - WITHDRAWAL WHEN LEAD(REPORTING_DATE) OVER(ORDER BY REPORTING_DATE) = REPORTING_DATE THEN NULL ELSE SUM(DEPOSIT) ");
        	query.append("OVER(ORDER BY LEV, MONTH_REPORTING_DATE, SLIP_NO ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) - SUM(WITHDRAWAL) ");
        	query.append("OVER(ORDER BY LEV, MONTH_REPORTING_DATE, SLIP_NO ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) ");
        	query.append("END AS BALANCE, SLIP_NO FROM (SELECT '2' AS LEV, MONTH_REPORTING_DATE, REPORTING_DATE REPORTING_DATE, EXPENSE_REPORT, CUSTOMER_CODE, ");
        	query.append("CUSTOMER_NAME, DEPOSIT AS DEPOSIT, WITHDRAWAL AS WITHDRAWAL, SLIP_NO FROM (SELECT SUBSTR(S.REPORTING_DATE, 0, 7) AS MONTH_REPORTING_DATE, ");
        	query.append("S.REPORTING_DATE AS REPORTING_DATE, S.EXPENSE_REPORT AS EXPENSE_REPORT, J.CUSTOMER_CODE AS CUSTOMER_CODE, C.CUSTOMER_NAME AS CUSTOMER_NAME, ");
        	query.append("J.LEFT_DEBTOR_PRICE AS DEPOSIT, J.RIGHT_CREDITS_PRICE AS WITHDRAWAL, S.SLIP_NO AS SLIP_NO FROM ACCOUNT A, SLIP S, JOURNAL J, ");
        	query.append("CUSTOMER C WHERE S.SLIP_NO = J.SLIP_NO AND J.ACCOUNT_INNER_CODE = A.ACCOUNT_INNER_CODE AND J.CUSTOMER_CODE = C.CUSTOMER_CODE(+) ");
        	query.append("AND J.ACCOUNT_INNER_CODE = '0101' AND S.SLIP_STATUS = '승인' AND S.REPORTING_DATE BETWEEN ? AND ?) UNION ALL SELECT '1' AS LEV, ");
        	query.append("NULL AS MONTH_REPORTING_DATE, NULL AS REPORTING_DATE, NULL AS EXPENSE_REPORT, NULL AS CUSTOMER_CODE, NULL AS CUSTOMER_NAME, ");
        	query.append("SUM(J.LEFT_DEBTOR_PRICE) AS DEPOSIT, SUM(J.RIGHT_CREDITS_PRICE) AS WITHDRAWAL, NULL AS SLIP_NO FROM SLIP S, JOURNAL J ");
        	query.append("WHERE S.SLIP_NO = J.SLIP_NO AND J.ACCOUNT_INNER_CODE = '0101' AND S.SLIP_STATUS = '승인' AND S.REPORTING_DATE < ?)) ");
        	query.append("GROUP  BY ROLLUP( MONTH_REPORTING_DATE, ( LEV, REPORTING_DATE, EXPENSE_REPORT, CUSTOMER_CODE, CUSTOMER_NAME, BALANCE, SLIP_NO ) ) ");
        	query.append("HAVING GROUPING(MONTH_REPORTING_DATE) < 1 AND LEV IN( 1, 2 ) OR MONTH_REPORTING_DATE IS NOT NULL UNION ALL SELECT '3' AS LEV, ");
        	query.append("'9999-12' AS MONTH_REPORTING_DATE, '[전 체 누 계]' AS REPORTING_DATE, NULL AS EXPENSE_REPORT, NULL AS CUSTOMER_CODE, NULL AS CUSTOMER_NAME, ");
        	query.append("SUM(J.LEFT_DEBTOR_PRICE) AS DEPOSIT, SUM(J.RIGHT_CREDITS_PRICE) AS WITHDRAWAL, NULL AS BALANCE, NULL AS SLIP_NO FROM SLIP S, ");
        	query.append("JOURNAL J WHERE S.SLIP_NO = J.SLIP_NO AND J.ACCOUNT_INNER_CODE = '0101' AND S.SLIP_STATUS = '승인' AND S.REPORTING_DATE <= ?");
        	query.append("ORDER  BY LEV, MONTH_REPORTING_DATE, SLIP_NO");

            conn = dataSourceTransactionManager.getConnection();
            pstmt =  conn.prepareStatement(query.toString());
            pstmt.setString(1, fromDate);
            pstmt.setString(2, toDate);
            pstmt.setString(3, fromDate);
            pstmt.setString(4, toDate);
            
            rs = pstmt.executeQuery();

            while (rs.next()) {
            	CashJournalBean cashJournalBean = new CashJournalBean();
            	cashJournalBean.setMonthReportingDate(rs.getString("MONTH_REPORTING_DATE"));
            	cashJournalBean.setReportingDate(rs.getString("REPORTING_DATE"));
            	cashJournalBean.setExpenseReport(rs.getString("EXPENSE_REPORT"));
            	cashJournalBean.setCustomerCode(rs.getString("CUSTOMER_CODE"));
            	cashJournalBean.setCustomerName(rs.getString("CUSTOMER_NAME"));
            	cashJournalBean.setDeposit(rs.getInt("DEPOSIT"));
            	cashJournalBean.setWithdrawal(rs.getInt("WITHDRAWAL"));
            	cashJournalBean.setBalance(rs.getString("BALANCE"));
                cashJournalList.add(cashJournalBean);
            }

            
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" CashJournalDAOImpl : selectCashJournalList 종료 ");
        }
        return cashJournalList;
    }

}
/*
SELECT CASE 
			WHEN LEV IS NULL THEN '2' 
			ELSE LEV 
       END             AS LEV, 
       MONTH_REPORTING_DATE, 
       CASE 
         WHEN LEV = '1' THEN '[전 일 이 월]' 
         WHEN REPORTING_DATE IS NULL THEN '[월 계]' 
         ELSE REPORTING_DATE 
       END             AS REPORTING_DATE, 
       EXPENSE_REPORT, 
       CUSTOMER_CODE, 
       CUSTOMER_NAME, 
       SUM(DEPOSIT)    AS DEPOSIT, 
       SUM(WITHDRAWAL) AS WITHDRAWAL, 
       BALANCE, 
       SLIP_NO 
FROM   (SELECT LEV, 
               MONTH_REPORTING_DATE, 
               REPORTING_DATE, 
               EXPENSE_REPORT, 
               CUSTOMER_CODE, 
               CUSTOMER_NAME, 
               DEPOSIT, 
               WITHDRAWAL, 
               CASE 
					WHEN LEV = '1' 
					THEN DEPOSIT - WITHDRAWAL 
					WHEN LEAD(REPORTING_DATE) OVER(ORDER BY REPORTING_DATE) = REPORTING_DATE 
					THEN NULL 
					ELSE SUM(DEPOSIT) OVER(ORDER BY LEV, MONTH_REPORTING_DATE, SLIP_NO 
							ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) 
						- SUM(WITHDRAWAL) OVER(ORDER BY LEV, MONTH_REPORTING_DATE, SLIP_NO 
							ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) 
               END AS BALANCE, 
               SLIP_NO 
        FROM   (SELECT '2'            AS LEV, 
                       MONTH_REPORTING_DATE, 
                       REPORTING_DATE REPORTING_DATE, 
                       EXPENSE_REPORT, 
                       CUSTOMER_CODE, 
                       CUSTOMER_NAME, 
                       DEPOSIT        AS DEPOSIT, 
                       WITHDRAWAL     AS WITHDRAWAL, 
                       SLIP_NO 
                FROM   (SELECT SUBSTR(S.REPORTING_DATE, 0, 7) AS MONTH_REPORTING_DATE, 
                               S.REPORTING_DATE               AS REPORTING_DATE, 
                               S.EXPENSE_REPORT               AS EXPENSE_REPORT, 
                               J.CUSTOMER_CODE                AS CUSTOMER_CODE, 
                               C.CUSTOMER_NAME                AS CUSTOMER_NAME, 
                               J.LEFT_DEBTOR_PRICE            AS DEPOSIT, 
                               J.RIGHT_CREDITS_PRICE          AS WITHDRAWAL, 
                               S.SLIP_NO                      AS SLIP_NO 
                        FROM   ACCOUNT A, 
                               SLIP S, 
                               JOURNAL J, 
                               CUSTOMER C 
                        WHERE  S.SLIP_NO = J.SLIP_NO 
                               AND J.ACCOUNT_INNER_CODE = A.ACCOUNT_INNER_CODE 
                               AND J.CUSTOMER_CODE = C.CUSTOMER_CODE(+) 
                               AND J.ACCOUNT_INNER_CODE = '0101' 
                               AND S.SLIP_STATUS = '승인' 
                               AND S.REPORTING_DATE BETWEEN ? AND ?) 
                UNION ALL 
                SELECT '1'                        AS LEV, 
                       NULL                       AS MONTH_REPORTING_DATE, 
                       NULL                       AS REPORTING_DATE, 
                       NULL                       AS EXPENSE_REPORT, 
                       NULL                       AS CUSTOMER_CODE, 
                       NULL                       AS CUSTOMER_NAME, 
                       SUM(J.LEFT_DEBTOR_PRICE)   AS DEPOSIT, 
                       SUM(J.RIGHT_CREDITS_PRICE) AS WITHDRAWAL, 
                       NULL                       AS SLIP_NO 
                FROM   SLIP S, 
                       JOURNAL J 
                WHERE  S.SLIP_NO = J.SLIP_NO 
                       AND J.ACCOUNT_INNER_CODE = '0101' 
                       AND S.SLIP_STATUS = '승인' 
                       AND S.REPORTING_DATE < ?)) 
GROUP  BY ROLLUP( MONTH_REPORTING_DATE, ( LEV, REPORTING_DATE, EXPENSE_REPORT, 
                                          CUSTOMER_CODE, CUSTOMER_NAME, BALANCE, SLIP_NO ) ) 
HAVING GROUPING(MONTH_REPORTING_DATE) < 1 
       AND LEV IN( 1, 2 ) 
        OR MONTH_REPORTING_DATE IS NOT NULL 
UNION ALL 
SELECT '3'                        AS LEV, 
       '9999-12'                  AS MONTH_REPORTING_DATE, 
       '[전 체 누 계]'        AS REPORTING_DATE, 
       NULL                       AS EXPENSE_REPORT, 
       NULL                       AS CUSTOMER_CODE, 
       NULL                       AS CUSTOMER_NAME, 
       SUM(J.LEFT_DEBTOR_PRICE)   AS DEPOSIT, 
       SUM(J.RIGHT_CREDITS_PRICE) AS WITHDRAWAL, 
       NULL                       AS BALANCE, 
       NULL                       AS SLIP_NO 
FROM   SLIP S, 
       JOURNAL J 
WHERE  S.SLIP_NO = J.SLIP_NO 
       AND J.ACCOUNT_INNER_CODE = '0101' 
       AND S.SLIP_STATUS = '승인' 
       AND S.REPORTING_DATE <= ?
ORDER  BY LEV, 
          MONTH_REPORTING_DATE, 
          SLIP_NO 
*/