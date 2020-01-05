package net.plang.HoWooAccount.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.company.to.BusinessBean;



public class BusinessDAOImpl implements BusinessDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
    private static BusinessDAO instance;

    public static BusinessDAO getInstance() {
        if (instance == null) instance = new BusinessDAOImpl();
        return instance;
    }
    //업태리스트 조회
    @Override
    public ArrayList<BusinessBean> selectBusinessList() {
        if (logger.isDebugEnabled()) {
            logger.debug(" AddressDAOImpl : selectSidoList 시작 ");
        }
        ArrayList<BusinessBean> businessList = new ArrayList<BusinessBean>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("select * from business order by CLASSIFICATION_CODE");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            BusinessBean businessbean = null;
            while (rs.next()) {
            	businessbean = new BusinessBean();
            	businessbean.setBusinessName(rs.getString("business_Name"));
            	businessbean.setClassificationCode(rs.getString("Classification_Code"));
            	businessbean.setRemarks(rs.getString("Remarks"));;
            	businessList.add(businessbean);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(" AddressDAOImpl : selectSidoList 종료 ");
            }
            return businessList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }
}
