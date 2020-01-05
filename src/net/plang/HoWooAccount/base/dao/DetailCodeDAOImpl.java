package net.plang.HoWooAccount.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.to.DetailCodeBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class DetailCodeDAOImpl implements DetailCodeDAO {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    private static DetailCodeDAO instance;

    private DetailCodeDAOImpl() {
    }

    ;

    public static DetailCodeDAO getinstance() {

        if (instance == null) {
            instance = new DetailCodeDAOImpl();
            System.out.println("		@ DetailCodeDAOImpl에 접근");
        }
        return instance;
    }

    @Override
    public ArrayList<DetailCodeBean> selectDetailCodeList(HashMap<String, String> param) {

        if (logger.isDebugEnabled()) {
            logger.debug(" DetailCodeDAOImpl : selectDetailCodeList 시작 ");
        }

        Connection con;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<DetailCodeBean> DetailCodeList = new ArrayList<DetailCodeBean>();
        System.out.println("		@ selectDetailCodeList에서 받은 코드: '" + param.get("divisionCodeNo") + "'");
        try {
            String query = "SELECT * FROM CODE_DETAIL WHERE DIVISION_CODE_NO = ?";
            if (param.get("detailCodeName") != null)
                query += "AND DETAIL_CODE_NAME LIKE ?";

            System.out.println(query);
            con = dataSourceTransactionManager.getConnection();

            pstmt = con.prepareStatement(query);
            pstmt.setString(1, param.get("divisionCodeNo"));
            if (param.get("detailCodeName") != null)
                pstmt.setString(2, "%" + param.get("detailCodeName") + "%");

            rs = pstmt.executeQuery();
            while (rs.next()) {
                DetailCodeBean detailcodeBean = new DetailCodeBean();
                detailcodeBean.setDivisionCodeNo(rs.getString("DIVISION_CODE_NO"));
                detailcodeBean.setDetailCode(rs.getString("DETAIL_CODE"));
                detailcodeBean.setDetailCodeName(rs.getString("DETAIL_CODE_NAME"));
                detailcodeBean.setDescription(rs.getString("DESCRIPTION"));

                DetailCodeList.add(detailcodeBean);

            }

            if (logger.isDebugEnabled()) {
                logger.debug(" DetailCodeDAOImpl : selectDetailCodeList 종료 ");
            }
            return DetailCodeList;
        } catch (Exception sqle) {
            // TODO Auto-generated catch block
            System.out.println("		@ 코드 조회 에러 ");
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public void insertDetailCode(DetailCodeBean codeDetailBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" DetailCodeDAOImpl : insertDetailCode 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            StringBuffer query = new StringBuffer();
            query.append("INSERT INTO CODE_DETAIL  ");
            query.append(" (DIVISION_CODE_NO,DETAIL_CODE,DETAIL_CODE_NAME) ");
            query.append(" VALUES(?, ?, ?) ");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());

            pstmt.setString(1, codeDetailBean.getDivisionCodeNo());
            pstmt.setString(2, codeDetailBean.getDetailCode());
            pstmt.setString(3, codeDetailBean.getDetailCodeName());


            pstmt.executeUpdate();

        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" DetailCodeDAOImpl : insertDetailCode 종료 ");
        }

    }

    @Override
    public void updateDetailCode(DetailCodeBean codeDetailBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" DetailCodeDAOImpl : updateDetailCode 시작 ");
        }

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            StringBuffer query = new StringBuffer();
            query.append("UPDATE CODE_DETAIL SET DETAIL_CODE_NAME = ? ,DETAIL_CODE = ? WHERE DIVISION_CODE_NO = ? AND DETAIL_CODE = ?");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, codeDetailBean.getDetailCodeName());

            pstmt.setString(2, codeDetailBean.getDetailCode());
            pstmt.setString(3, codeDetailBean.getDivisionCodeNo());
            pstmt.setString(4, codeDetailBean.getDetailCode());
            pstmt.executeUpdate();

        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" DetailCodeDAOImpl : updateDetailCode 종료 ");
        }
    }

    @Override
    public void deleteDetailCode(String codeNo) {

        if (logger.isDebugEnabled()) {
            logger.debug(" DetailCodeDAOImpl : deleteDetailCode 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        System.out.println("		@ 삭제할 코드: '" + codeNo + "'");
        try {
            StringBuffer query = new StringBuffer();
            query.append("DELETE FROM CODE_DETAIL WHERE DETAIL_CODE = ?");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, codeNo);
            pstmt.executeUpdate();
            System.out.println("		@ 코드: '" + codeNo + "' 삭제됨");
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" DetailCodeDAOImpl : deleteDetailCode 종료 ");
        }
    }


}
