package net.plang.HoWooAccount.hr.applicationservice;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.dao.DetailCodeDAO;
import net.plang.HoWooAccount.base.dao.DetailCodeDAOImpl;
import net.plang.HoWooAccount.base.to.DetailCodeBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.hr.dao.EmployeeDAO;
import net.plang.HoWooAccount.hr.dao.EmployeeDAOImpl;
import net.plang.HoWooAccount.hr.to.EmployeeBean;

public class HrApplicationServiceImpl implements HrApplicationService {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private EmployeeDAO employeeDAO = EmployeeDAOImpl.getInstance();
    private DetailCodeDAO codeDetailDAO = DetailCodeDAOImpl.getinstance();
    private static HrApplicationService instance;

    private HrApplicationServiceImpl() {
    }

    public static HrApplicationService getInstance() {

        System.out.println("		@ HrApplicationService 객체접근");
        if (instance == null) {
            instance = new HrApplicationServiceImpl();
        }
        return instance;
    }


    @Override
    public ArrayList<EmployeeBean> findEmployeeList(String deptCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : findEmployeeList 시작 ");
        }
        ArrayList<EmployeeBean> empList = null;
        try {
            empList = employeeDAO.selectEmployeeList(deptCode);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : findEmployeeList 종료 ");
        }
        return empList;
    }

    @Override
    public EmployeeBean findEmployee(String empCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : findEmployee 시작 ");
        }
        EmployeeBean employeeBean = null;
        try {
            employeeBean = employeeDAO.selectEmployee(empCode);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : findEmployee 종료 ");
        }
        return employeeBean;
    }

    @Override
    public void batchEmployeeInfo(EmployeeBean employeeBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : batchEmployeeInfo 시작 ");
        }
        try {
            employeeDAO.updateEmployeeInfo(employeeBean);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : batchEmployeeInfo 종료 ");
        }
    }

    @Override
    public void batchEmployee(ArrayList<EmployeeBean> empList) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : batchEmployee 시작 ");
        }
        try {
            for (EmployeeBean employBean : empList) {
                String empStatus = employBean.getStatus();
                switch (empStatus) {
                    case "update":
                        modifyEmployee(employBean);
                        break;
                    case "delete":
                        removeEmployee(employBean);
                        break;
                }
            }
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : batchEmployee 종료 ");
        }
    }

    private void modifyEmployee(EmployeeBean employeeBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : modifyEmployee 시작 ");
        }
        try {
            employeeDAO.updateEmployee(employeeBean);
            String empCode = employeeBean.getEmpCode();
            String empName = employeeBean.getEmpName();
            DetailCodeBean detailCodeBean = new DetailCodeBean();
            detailCodeBean.setDivisionCodeNo("HR-02");
            detailCodeBean.setDetailCode(empCode);
            detailCodeBean.setDetailCodeName(empName);
            codeDetailDAO.updateDetailCode(detailCodeBean);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;

        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : modifyEmployee 종료 ");
        }
    }

    public void removeEmployee(EmployeeBean employBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : removeEmployee 시작 ");
        }
        try {
            employeeDAO.deleteEmployee(employBean.getEmpCode());
            codeDetailDAO.deleteDetailCode(employBean.getEmpCode());

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;

        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : removeEmployee 종료 ");
        }
    }

    @Override
    public void registerEmployee(EmployeeBean employeeBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : registerEmployee 시작 ");
        }
        try {
            employeeDAO.insertEmployee(employeeBean);
            String empCode = employeeBean.getEmpCode();
            String empName = employeeBean.getEmpName();
            DetailCodeBean detailCodeBean = new DetailCodeBean();
            detailCodeBean.setDivisionCodeNo("HR-02");
            detailCodeBean.setDetailCode(empCode);
            detailCodeBean.setDetailCodeName(empName);
            codeDetailDAO.insertDetailCode(detailCodeBean);
        } catch (Exception e) {
            logger.fatal(e.getMessage());
            throw e;

        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HrApplicationServiceImpl : registerEmployee 종료 ");
        }
    }

}
