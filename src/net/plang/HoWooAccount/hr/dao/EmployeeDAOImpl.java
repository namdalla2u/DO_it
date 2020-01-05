package net.plang.HoWooAccount.hr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.hr.to.EmployeeBean;

public class EmployeeDAOImpl implements EmployeeDAO {
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    protected final Log logger = LogFactory.getLog(this.getClass());

    private static EmployeeDAO instance;

    private EmployeeDAOImpl() {
    }

    public static EmployeeDAO getInstance() {
        if (instance == null)
            instance = new EmployeeDAOImpl();
        return instance;
    }

    @Override
    public EmployeeBean selectEmployee(String empCode) {


        if (logger.isDebugEnabled()) {
            logger.debug(" EmployeeDAOImpl : selectEmployee 시작 ");
        }

        Connection conn;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM EMPLOYEE WHERE EMP_CODE = ?";
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, empCode);
            rs = pstmt.executeQuery();
            EmployeeBean employeeBean = null;
            if (rs.next()) {

                employeeBean = new EmployeeBean();
                employeeBean.setEmpCode(rs.getString("EMP_CODE"));
                employeeBean.setEmpName(rs.getString("EMP_NAME"));
                employeeBean.setUserPw(rs.getString("USER_PW"));
                employeeBean.setPositionCode(rs.getString("POSITION_CODE"));
                employeeBean.setPositionName(rs.getString("POSITION_NAME"));
                employeeBean.setDeptCode(rs.getString("DEPT_CODE"));
                employeeBean.seteMail(rs.getString("EMAIL"));
                employeeBean.setGender(rs.getString("GENDER"));
                employeeBean.setSocialSecurityNumber(rs.getString("SOCIAL_SECURITY_NUMBER"));
                employeeBean.setPhoneNumber(rs.getString("PHONE_NUMBER"));

                employeeBean.setCompanyCode(rs.getString("COMPANY_CODE"));
                employeeBean.setBirthDate(rs.getString("BIRTH_DATE"));

                employeeBean.setZipCode(rs.getString("ZIP_CODE"));
                employeeBean.setBasicAddress(rs.getString("BASIC_ADDRESS"));
                employeeBean.setDetailAddress(rs.getString("DETAIL_ADDRESS"));
                employeeBean.setImage(rs.getString("IMAGE"));

            }
            if (logger.isDebugEnabled()) {
                logger.debug(" EmployeeDAOImpl : selectEmployee 종료 ");
            }
            return employeeBean;

        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }

    }

    @Override
    public ArrayList<EmployeeBean> selectEmployeeList(String deptCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" EmployeeDAOImpl : selectEmployeeList 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<EmployeeBean> empList = new ArrayList<EmployeeBean>();
        try {
            StringBuffer query = new StringBuffer();
            query.append(" SELECT * FROM EMPLOYEE where DEPT_CODE = ? ");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, deptCode);
            rs = pstmt.executeQuery();
            System.out.println("		@ 선택된 부서: " + deptCode);
            while (rs.next()) {
                EmployeeBean employeeBean = new EmployeeBean();
                employeeBean.setEmpCode(rs.getString("EMP_CODE"));
                employeeBean.setEmpName(rs.getString("EMP_NAME"));
                employeeBean.setUserPw(rs.getString("USER_PW"));
                employeeBean.setPositionCode(rs.getString("POSITION_CODE"));
                employeeBean.setDeptCode(rs.getString("DEPT_CODE"));
                employeeBean.seteMail(rs.getString("EMAIL"));
                employeeBean.setGender(rs.getString("GENDER"));
                employeeBean.setPhoneNumber(rs.getString("PHONE_NUMBER"));
                employeeBean.setBirthDate(rs.getString("BIRTH_DATE"));
                employeeBean.setSocialSecurityNumber(rs.getString("SOCIAL_SECURITY_NUMBER"));
                employeeBean.setZipCode(rs.getString("ZIP_CODE"));
                employeeBean.setBasicAddress(rs.getString("BASIC_ADDRESS"));
                employeeBean.setDetailAddress(rs.getString("DETAIL_ADDRESS"));
                employeeBean.setImage(rs.getString("IMAGE"));
                empList.add(employeeBean);
            }
            if (logger.isDebugEnabled()) {
                logger.debug(" EmployeeDAOImpl : selectEmployeeList 종료 ");
            }
            return empList;
        } catch (Exception sqle) {
            // TODO Auto-generated catch block
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());

        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }

    }

    @Override
    public void updateEmployeeInfo(EmployeeBean employeeBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" EmployeeDAOImpl : updateEmployeeInfo 시작 ");
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        System.out.println("		@ 수정할 사원코드: " + employeeBean.getEmpCode());
        System.out.println("		@ 수정할 사원이름: " + employeeBean.getEmpName());
        try {
            StringBuffer query = new StringBuffer();
            query.append("UPDATE EMPLOYEE SET ");
            query.append("USER_PW = ?, EMP_NAME = ?, ");
            query.append("POSITION_CODE = ?, DEPT_CODE = ?, DEPT_NAME = ?, ");
            query.append("PHONE_NUMBER = ?, ZIP_CODE = ?, BASIC_ADDRESS = ?, ");
            query.append("DETAIL_ADDRESS = ?, EMAIL = ?, IMAGE = ?, ");
            query.append("SOCIAL_SECURITY_NUMBER = ? ");
            query.append("WHERE EMP_CODE = ?");
            conn = dataSourceTransactionManager.getConnection();
            pstmt = conn.prepareStatement(query.toString());

            pstmt.setString(1, employeeBean.getUserPw());
            pstmt.setString(2, employeeBean.getEmpName());
            pstmt.setString(3, employeeBean.getPositionCode());
            pstmt.setString(4, employeeBean.getDeptCode());
            pstmt.setString(5, employeeBean.getDeptName());
            pstmt.setString(6, employeeBean.getPhoneNumber());
            pstmt.setString(7, employeeBean.getZipCode());
            pstmt.setString(8, employeeBean.getBasicAddress());
            pstmt.setString(9, employeeBean.getDetailAddress());
            pstmt.setString(10, employeeBean.geteMail());
            pstmt.setString(11, employeeBean.getImage());
            System.out.print(employeeBean.getImage());
            pstmt.setString(12, employeeBean.getSocialSecurityNumber());
            pstmt.setString(13, employeeBean.getEmpCode());

            pstmt.executeUpdate();
            if (logger.isDebugEnabled()) {
                logger.debug(" EmployeeDAOImpl : updateEmployeeInfo 종료 ");
            }
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
    }


    @Override
    public void updateEmployee(EmployeeBean bean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" EmployeeDAOImpl : updateEmployee 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        System.out.println("		@ 수정할 사원코드: " + bean.getEmpCode());
        System.out.println("		@ 수정할 사원이름: " + bean.getEmpName());
        try {
            StringBuffer query = new StringBuffer();
            query.append("UPDATE EMPLOYEE ");
            query.append("SET DEPT_CODE=?, ");
            query.append("POSITION_CODE=? ");
            query.append("WHERE EMP_CODE=? ");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, bean.getDeptCode());
            pstmt.setString(2, bean.getPositionCode());
            pstmt.setString(3, bean.getEmpCode());
            pstmt.executeUpdate();
            if (logger.isDebugEnabled()) {
                logger.debug(" EmployeeDAOImpl : updateEmployee 종료 ");
            }
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());

        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
    }

    @Override
    public void deleteEmployee(String empCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" EmployeeDAOImpl : deleteEmployee 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        System.out.println("		@ 삭제할 사원코드: " + empCode);

        try {
            StringBuffer query = new StringBuffer();
            query.append(" DELETE FROM EMPLOYEE ");
            query.append(" WHERE EMP_CODE=? ");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, empCode);
            pstmt.executeUpdate();
            if (logger.isDebugEnabled()) {
                logger.debug(" EmployeeDAOImpl : deleteEmployee 종료 ");
            }
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
    }

    public void insertEmployee(EmployeeBean employeeBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" EmployeeDAOImpl : insertEmployee 시작 ");
        }
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append(" INSERT INTO EMPLOYEE ");
            query.append("(EMP_CODE,USER_PW,EMP_NAME,DEPT_CODE, ");
            query.append("GENDER,SOCIAL_SECURITY_NUMBER,PHONE_NUMBER, ");
            query.append("EMAIL,BIRTH_DATE,ZIP_CODE,BASIC_ADDRESS,DETAIL_ADDRESS) ");
            query.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
 
            pstmt.setString(1, employeeBean.getEmpCode());
            pstmt.setString(2, employeeBean.getUserPw());
            pstmt.setString(3, employeeBean.getEmpName());
            pstmt.setString(4, employeeBean.getDeptCode());
            pstmt.setString(5, employeeBean.getGender());
            pstmt.setString(6, employeeBean.getSocialSecurityNumber());
            pstmt.setString(7, employeeBean.getPhoneNumber());
            pstmt.setString(8, employeeBean.geteMail());
            pstmt.setString(9, employeeBean.getBirthDate());
            pstmt.setString(10, employeeBean.getZipCode());
            pstmt.setString(11, employeeBean.getBasicAddress());
            pstmt.setString(12, employeeBean.getDetailAddress());
            System.out.println("		@ 가입할 사원코드" + employeeBean.getEmpCode());
            System.out.println("		@ 가입할 사원이름" + employeeBean.getEmpName());
            pstmt.executeUpdate();
            if (logger.isDebugEnabled()) {
                logger.debug(" EmployeeDAOImpl : insertEmployee 종료 ");
            }
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt);
        }
    }

}