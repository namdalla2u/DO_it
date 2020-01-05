package net.plang.HoWooAccount.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.company.to.DetailBusinessBean;

public class DetailBusinessDAOImpl implements DetailBusinessDAO{
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
    private static DetailBusinessDAO instance;

    public static DetailBusinessDAO getInstance() {
        if (instance == null) instance = new DetailBusinessDAOImpl();
        return instance;
    }
	 //업태소분류리스트 조회
    @Override
    public ArrayList<DetailBusinessBean> selectDetailBusinessList(String businessCode) {
        if (logger.isDebugEnabled()) {
            logger.debug(" DetailBusinessDAOImpl : selectDetailBusinessList 시작 ");
        }
        ArrayList<DetailBusinessBean> detailBusinessList = new ArrayList<DetailBusinessBean>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("select * from DETAIL_BUSINESS where CLASSIFICATION_CODE=?");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, businessCode);            
            rs = pstmt.executeQuery();

            DetailBusinessBean detailBusinessBean = null;

            while (rs.next()) {

            	detailBusinessBean = new DetailBusinessBean();

            	detailBusinessBean.setDetailBusinessName(rs.getString("DETAIL_BUSINESS_NAME"));
            	detailBusinessBean.setClassificationCode(rs.getString("CLASSIFICATION_CODE"));
            	detailBusinessBean.setRemarks(rs.getString("REMARKS"));
            	detailBusinessList.add(detailBusinessBean);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(" DetailBusinessDAOImpl : selectDetailBusinessList 종료 ");
            }
            return detailBusinessList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }
}
