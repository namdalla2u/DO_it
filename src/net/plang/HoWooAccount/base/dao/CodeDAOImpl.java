package net.plang.HoWooAccount.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.to.CodeBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;


public class CodeDAOImpl implements CodeDAO {
    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    private static CodeDAO instance;

    private CodeDAOImpl() {
    }

    ;

    public static CodeDAO getInstance() {

        if (instance == null) {
            instance = new CodeDAOImpl();
        }
        return instance;
    }

    @Override
    public ArrayList<CodeBean> selectCodeList() {

        if (logger.isDebugEnabled()) {
            logger.debug(" CodeDAOImpl : selectCustomerCodeList 시작 ");
        }
        ArrayList<CodeBean> codeList = new ArrayList<CodeBean>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT * FROM code where DIVISION_CODE_NO not in ('HR-02')  order by division_code_no");
            con = dataSourceTransactionManager.getConnection();

            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                CodeBean codeBean = new CodeBean();
                codeBean.setCodeType(rs.getString("code_type"));
                codeBean.setDivisionCodeName(rs.getString("DIVISION_CODE_name"));
                codeBean.setDivisionCodeNo(rs.getString("DIVISION_CODE_NO"));
                codeList.add(codeBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" CodeDAOImpl : selectCustomerCodeList 종료 ");
            }
            return codeList;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            logger.fatal(e.getMessage());
            throw new DataAccessException(e.getMessage());
        } finally {

            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public void insertCode(CodeBean codeBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" CodeDAOImpl : insertCode 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {

            StringBuffer query = new StringBuffer();
            query.append(" INSERT INTO CODE ");
            query.append("DIVISION_CODE_NO,CODE_TYPE,DIVISION_CODE_NAME  ");
            query.append(" VALUES(?,?,?) ");

            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            System.out.println(codeBean.getDivisionCodeNo());
            pstmt.setString(1, codeBean.getDivisionCodeNo());
            pstmt.setString(2, codeBean.getCodeType());
            pstmt.setString(3, codeBean.getDivisionCodeName());
            pstmt.executeUpdate();

        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeDAOImpl : insertCode 종료 ");
        }
    }

    @Override
    public void updateCode(CodeBean codeBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" CodeDAOImpl : updateCode 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("UPDATE CODE SET DIVISION_CODE_NO = ? , CODE_TYPE=?, ");
            query.append("DIVISION_CODE_NAME= ?");
            query.append("WHERE DIVISION_CODE_NO = ?");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, codeBean.getDivisionCodeNo());
            pstmt.setString(2, codeBean.getCodeType());
            pstmt.setString(3, codeBean.getDivisionCodeName());
            pstmt.setString(4, codeBean.getDivisionCodeNo());
            pstmt.executeUpdate();
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeDAOImpl : updateCode 종료 ");
        }
    }

    @Override
    public void deleteCode(String Code) {

        if (logger.isDebugEnabled()) {
            logger.debug(" CodeDAOImpl : deleteCode 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("DELETE CUSTOMER WHERE CUSTOMER_CODE = ?");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, Code);
            pstmt.executeUpdate();
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeDAOImpl : deleteCode 종료 ");
        }
    }


}
