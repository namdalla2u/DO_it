package net.plang.HoWooAccount.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.to.AddressBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;


public class AddressDAOImpl implements AddressDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
    private static AddressDAO instance;

    public static AddressDAO getInstance() {
        if (instance == null) instance = new AddressDAOImpl();
        return instance;
    }

    @Override
    public ArrayList<AddressBean> selectRoadList(String sido, String sigunguname, String roadname) {


        if (logger.isDebugEnabled()) {
            logger.debug(" AddressDAOImpl : selectRoadList 시작 ");
        }
        ArrayList<AddressBean> postRoadList = new ArrayList<AddressBean>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sidoTable = "road_post_" + sido;
            StringBuffer query = new StringBuffer();
            query.append("SELECT zipcode, road_name, building_code1, building_code2 ");
            query.append(" FROM " + sidoTable + " where sigungu='" + sigunguname + "' and road_name like '%" + roadname + "%'");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AddressBean postBean = new AddressBean();
                postBean.setZipcode(rs.getString("zipcode"));
                postBean.setRoadname(rs.getString("road_name"));
                postBean.setBuildingcode1(rs.getString("building_code1"));
                postBean.setBuildingcode2(rs.getString("building_code2"));

                postRoadList.add(postBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" AddressDAOImpl : selectRoadList 종료 ");
            }
            return postRoadList;

        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }


    @Override
    public ArrayList<AddressBean> selectSigunguList(String sido) {


        if (logger.isDebugEnabled()) {
            logger.debug(" AddressDAOImpl : selectSigunguList 시작 ");
        }
        ArrayList<AddressBean> postSigunguList = new ArrayList<AddressBean>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT value FROM post_sigungu where code=? group by value");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, sido);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AddressBean postBean = new AddressBean();
                postBean.setSidoname(rs.getString("value"));
                postSigunguList.add(postBean);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(" AddressDAOImpl : selectSigunguList 종료 ");
            }
            return postSigunguList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public ArrayList<AddressBean> selectSidoList() {

        if (logger.isDebugEnabled()) {
            logger.debug(" AddressDAOImpl : selectSidoList 시작 ");
        }
        ArrayList<AddressBean> postSidoList = new ArrayList<AddressBean>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT * FROM post_si order by value");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                AddressBean postBean = new AddressBean();
                postBean.setSido(rs.getString("code"));
                postBean.setSidoname(rs.getString("value"));
                postSidoList.add(postBean);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(" AddressDAOImpl : selectSidoList 종료 ");
            }
            return postSidoList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public ArrayList<AddressBean> selectPostList(String dong) {

        if (logger.isDebugEnabled()) {
            logger.debug(" AddressDAOImpl : selectPostList 시작 ");
        }
        ArrayList<AddressBean> postList = new ArrayList<AddressBean>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT sido, gugun, dong, ri, zip_no FROM S_ZIPCODE WHERE dong LIKE '%" + dong + "%'");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {

                AddressBean postBean = new AddressBean();
                postBean.setDong(rs.getString("dong"));
                postBean.setRi(rs.getString("ri"));
                postBean.setSido(rs.getString("sido"));
                postBean.setSigungu(rs.getString("gugun"));
                postBean.setZipNo(rs.getString("zip_no"));
                postList.add(postBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" AddressDAOImpl : selectPostList 종료 ");
            }
            return postList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

}
