package net.plang.HoWooAccount.account.statement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.account.statement.to.DetailTrialBalanceBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class DetailTrialBalanceDAOImpl implements DetailTrialBalanceDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    private static DetailTrialBalanceDAOImpl instacne;

    private DetailTrialBalanceDAOImpl() {
    }

    public static DetailTrialBalanceDAOImpl getInstance() {
        if (instacne == null) {
            instacne = new DetailTrialBalanceDAOImpl();
            System.out.println("		@ DetailTrialBalanceDAOImpl에 접근");
        }
        return instacne;

    }

    @Override
    public ArrayList<DetailTrialBalanceBean> selectDetailTrialBalance(String fromDate, String toDate) {
        if (logger.isDebugEnabled()) {
            logger.debug(" DetailTrialBalanceDAOImpl : callDetailTrialBalance 시작 ");
        }
        Connection conn;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<DetailTrialBalanceBean> detailTrialBalance = new ArrayList<>();

        System.out.println("		@ 조회일자: "+fromDate+"부터 ");
        System.out.println(toDate+"까지");
        try {
        	StringBuffer query = new StringBuffer();
        	
        	// 제일 하단에 쿼리문 원본 써놨습니다.
        	
        	query.append("SELECT LEV, CODE, DEBITS_SUM, EXCEPT_CASH_DEBITS, CASH_DEBITS, LPAD(' ', ( LEV - 1 ) * 4) || NVL(A1.ACCOUNT_NAME, '합계') AS ACCOUNT_NAME, ");
        	query.append("CASH_CREDITS, EXCEPT_CASH_CREDITS, CREDITS_SUM FROM ACCOUNT A1, (SELECT CASE WHEN ACCOUNT_CODE IS NOT NULL THEN 3 WHEN APPEND_CODE IS NOT NULL THEN 2 ");
        	query.append("WHEN PARENT_CODE IS NOT NULL THEN 1 END AS LEV, CASE WHEN ACCOUNT_CODE IS NOT NULL THEN ACCOUNT_CODE ");
        	query.append("WHEN APPEND_CODE IS NOT NULL THEN APPEND_CODE WHEN PARENT_CODE IS NOT NULL THEN PARENT_CODE ");
        	query.append("END AS CODE, PARENT_CODE, APPEND_CODE, ACCOUNT_CODE, SUM(DEBITS_SUM) AS DEBITS_SUM, SUM(EXCEPT_CASH_DEBITS) AS EXCEPT_CASH_DEBITS, ");
        	query.append("SUM(CASH_DEBITS) AS CASH_DEBITS, SUM(CASH_CREDITS) AS CASH_CREDITS, SUM(EXCEPT_CASH_CREDITS) AS EXCEPT_CASH_CREDITS, ");
        	query.append("SUM(CREDITS_SUM) AS CREDITS_SUM FROM (SELECT A1.ACCOUNT_INNER_CODE AS PARENT_CODE, A2.ACCOUNT_INNER_CODE AS APPEND_CODE, ");
        	query.append("A3.ACCOUNT_CODE AS ACCOUNT_CODE, J1.DEBITS AS DEBITS_SUM, CASE WHEN J1.DEBITS = 0 THEN 0 ELSE J1.DEBITS - J2.CASH_DEBITS ");
        	query.append("END AS EXCEPT_CASH_DEBITS, CASE WHEN J1.DEBITS = 0 THEN 0 ELSE J2.CASH_DEBITS END AS CASH_DEBITS, ");
        	query.append("CASE WHEN J1.CREDITS = 0 THEN 0 ELSE J2.CASH_CREDITS END AS CASH_CREDITS, CASE WHEN J1.CREDITS = 0 THEN 0 ");
        	query.append("ELSE J1.CREDITS - J2.CASH_CREDITS END AS EXCEPT_CASH_CREDITS, J1.CREDITS AS CREDITS_SUM ");
        	query.append("FROM ACCOUNT A1, ACCOUNT A2, ACCOUNT A3, (SELECT J.SLIP_NO AS SLIP_NO, J.ACCOUNT_INNER_CODE  AS ACCOUNT_CODE, ");
        	query.append("J.LEFT_DEBTOR_PRICE   AS DEBITS, J.RIGHT_CREDITS_PRICE AS CREDITS ");
        	query.append("FROM JOURNAL J, ACCOUNT A, SLIP S WHERE J.SLIP_NO = S.SLIP_NO AND J.ACCOUNT_INNER_CODE = A.ACCOUNT_INNER_CODE ");
        	query.append("AND S.SLIP_STATUS = '승인' AND S.REPORTING_DATE BETWEEN ? AND ? ");
        	query.append("AND J.ACCOUNT_INNER_CODE != '0101') J1, (SELECT SLIP_NO, LEFT_DEBTOR_PRICE AS CASH_CREDITS, RIGHT_CREDITS_PRICE AS CASH_DEBITS ");
        	query.append("FROM JOURNAL WHERE  ACCOUNT_INNER_CODE = '0101') J2 ");
        	query.append("WHERE J1.SLIP_NO = J2.SLIP_NO AND J1.ACCOUNT_CODE = A3.ACCOUNT_INNER_CODE AND A3.PARENT_ACCOUNT_INNER_CODE = A2.ACCOUNT_INNER_CODE(+) ");
        	query.append("AND A2.PARENT_ACCOUNT_INNER_CODE = A1.ACCOUNT_INNER_CODE(+)) GROUP  BY ROLLUP ( PARENT_CODE, APPEND_CODE, ACCOUNT_CODE )) A2 ");
        	query.append("WHERE  A1.ACCOUNT_INNER_CODE(+) = A2.CODE ORDER  BY SUBSTR(CODE, 1, 4), SUBSTR(CODE, 6) DESC NULLS LAST");
   	
            conn = dataSourceTransactionManager.getConnection();
            pstmt =  conn.prepareStatement(query.toString());
            pstmt.setString(1, fromDate);
            pstmt.setString(2, toDate);
            
            rs = pstmt.executeQuery();

            while (rs.next()) {
            	DetailTrialBalanceBean detailTrialBalanceBean = new DetailTrialBalanceBean();
                detailTrialBalanceBean.setLev(rs.getInt("LEV"));
                detailTrialBalanceBean.setAccountInnerCode(rs.getString("CODE"));
                detailTrialBalanceBean.setDebitsSum(rs.getInt("DEBITS_SUM"));
                detailTrialBalanceBean.setExceptCashDebits(rs.getInt("EXCEPT_CASH_DEBITS"));
                detailTrialBalanceBean.setCashDebits(rs.getInt("CASH_DEBITS"));
                detailTrialBalanceBean.setAccountName(rs.getString("ACCOUNT_NAME"));
                detailTrialBalanceBean.setCashCredits(rs.getInt("CASH_CREDITS"));
                detailTrialBalanceBean.setExceptCashCredits(rs.getInt("EXCEPT_CASH_CREDITS"));
                detailTrialBalanceBean.setCreditsSum(rs.getInt("CREDITS_SUM"));
                detailTrialBalance.add(detailTrialBalanceBean);
            }

            
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" DetailTrialBalanceDAOImpl : callDetailTrialBalance 종료 ");
        }
        return detailTrialBalance;
    }

}

/*
SELECT LEV, 
CODE, 
DEBITS_SUM, 
EXCEPT_CASH_DEBITS, 
CASH_DEBITS, 
LPAD(' ', ( LEV - 1 ) * 4) || NVL(A1.ACCOUNT_NAME, '합계') AS ACCOUNT_NAME, 
CASH_CREDITS, 
EXCEPT_CASH_CREDITS, 
CREDITS_SUM 
FROM   ACCOUNT A1, 
(SELECT 
        CASE 
          WHEN ACCOUNT_CODE IS NOT NULL THEN 3 
          WHEN APPEND_CODE IS NOT NULL THEN 2 
          WHEN PARENT_CODE IS NOT NULL THEN 1 
        END AS LEV, 
		 
        CASE 
          WHEN ACCOUNT_CODE IS NOT NULL THEN ACCOUNT_CODE 
          WHEN APPEND_CODE IS NOT NULL THEN APPEND_CODE 
          WHEN PARENT_CODE IS NOT NULL THEN PARENT_CODE 
        END AS CODE, 
		 
        PARENT_CODE, 
        APPEND_CODE, 
        ACCOUNT_CODE, 
        SUM(DEBITS_SUM)           AS DEBITS_SUM, 
        SUM(EXCEPT_CASH_DEBITS)   AS EXCEPT_CASH_DEBITS, 
        SUM(CASH_DEBITS)          AS CASH_DEBITS, 
        SUM(CASH_CREDITS)         AS CASH_CREDITS, 
        SUM(EXCEPT_CASH_CREDITS)  AS EXCEPT_CASH_CREDITS, 
        SUM(CREDITS_SUM)          AS CREDITS_SUM 
 FROM   (SELECT 
				A1.ACCOUNT_INNER_CODE AS PARENT_CODE, 
                A2.ACCOUNT_INNER_CODE AS APPEND_CODE, 
                A3.ACCOUNT_CODE       AS ACCOUNT_CODE, 
                J1.DEBITS             AS DEBITS_SUM, 
				 
                CASE 
                  WHEN J1.DEBITS = 0 
                  THEN 0 
                  ELSE J1.DEBITS - J2.CASH_DEBITS 
                END                   AS EXCEPT_CASH_DEBITS, 
				 
                CASE 
                  WHEN J1.DEBITS = 0 
                  THEN 0 
                  ELSE J2.CASH_DEBITS 
                END                   AS CASH_DEBITS, 
				 
                CASE 
                  WHEN J1.CREDITS = 0 
                  THEN 0 
                  ELSE J2.CASH_CREDITS 
                END                   AS CASH_CREDITS, 
				 
                CASE 
                  WHEN J1.CREDITS = 0 
                  THEN 0 
                  ELSE J1.CREDITS - J2.CASH_CREDITS 
                END                   AS EXCEPT_CASH_CREDITS, 
				 
                J1.CREDITS            AS CREDITS_SUM 
         FROM   ACCOUNT A1, 
                ACCOUNT A2, 
                ACCOUNT A3, 
                (SELECT J.SLIP_NO             AS SLIP_NO, 
                        J.ACCOUNT_INNER_CODE  AS ACCOUNT_CODE, 
                        J.LEFT_DEBTOR_PRICE   AS DEBITS, 
                        J.RIGHT_CREDITS_PRICE AS CREDITS 
                 FROM   JOURNAL J, 
                        ACCOUNT A, 
                        SLIP S 
                 WHERE  J.SLIP_NO = S.SLIP_NO 
                        AND J.ACCOUNT_INNER_CODE = A.ACCOUNT_INNER_CODE 
                        AND S.SLIP_STATUS = '승인' 
                        AND S.REPORTING_DATE BETWEEN ? AND ? 
                        AND J.ACCOUNT_INNER_CODE != '0101') J1, 
                (SELECT SLIP_NO, 
                        LEFT_DEBTOR_PRICE   AS CASH_CREDITS, 
                        RIGHT_CREDITS_PRICE AS CASH_DEBITS 
                 FROM   JOURNAL 
                 WHERE  ACCOUNT_INNER_CODE = '0101') J2 
         WHERE  J1.SLIP_NO = J2.SLIP_NO 
                AND J1.ACCOUNT_CODE = A3.ACCOUNT_INNER_CODE 
                AND A3.PARENT_ACCOUNT_INNER_CODE = A2.ACCOUNT_INNER_CODE(+) 
                AND A2.PARENT_ACCOUNT_INNER_CODE = A1.ACCOUNT_INNER_CODE(+)
         ) 
 GROUP  BY ROLLUP ( PARENT_CODE, APPEND_CODE, ACCOUNT_CODE )) A2 
WHERE  A1.ACCOUNT_INNER_CODE(+) = A2.CODE 
ORDER  BY SUBSTR(CODE, 1, 4), 
   SUBSTR(CODE, 6) DESC NULLS LAST;
*/