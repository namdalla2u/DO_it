package net.plang.HoWooAccount.hr.serviceFacade;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.hr.applicationservice.HrApplicationService;
import net.plang.HoWooAccount.hr.applicationservice.HrApplicationServiceImpl;
import net.plang.HoWooAccount.hr.to.EmployeeBean;

public class HRServiceFacadeImpl implements HRServiceFacade {

    protected final Log logger = LogFactory.getLog(this.getClass());
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    HrApplicationService hrApplicationService = HrApplicationServiceImpl.getInstance();

    private static HRServiceFacade instance;

    private HRServiceFacadeImpl() {
    }

    public static HRServiceFacade getInstance() {

        if (instance == null) {
            instance = new HRServiceFacadeImpl();
        }
        return instance;
    }

    @Override
    public EmployeeBean findEmployee(String empCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : findEmployee 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        EmployeeBean employeeBean = null;
        try {
            employeeBean = hrApplicationService.findEmployee(empCode);
            System.out.println("		@ 사원코드 : " + empCode);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : findEmployee 종료 ");
        }
        return employeeBean;
    }

    @Override
    public ArrayList<EmployeeBean> findEmployeeList(String deptCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : findEmployee 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        ArrayList<EmployeeBean> empList = null;
        try {
            empList = hrApplicationService.findEmployeeList(deptCode);
            System.out.println("		@ 조회된 부서코드 : " + deptCode);
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : findEmployeeList 종료 ");
        }
        return empList;
    }

    @Override
    public void batchEmployeeInfo(EmployeeBean employeeBean) {


        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : batchEmployeeInfo 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        try {
            hrApplicationService.batchEmployeeInfo(employeeBean);
            System.out.println("		@ 조회된 사원명 : " + employeeBean.getEmpName());
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : batchEmployeeInfo 종료 ");
        }
    }

    @Override
    public void batchEmployee(ArrayList<EmployeeBean> empList) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : batchEmployee 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        try {
            hrApplicationService.batchEmployee(empList);
            for (int a = 0; a < empList.size(); a++) {
                System.out.println("		@ 사원리스트 : " + empList.get(a).getEmpName());
            }
            dataSourceTransactionManager.commitTransaction();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : batchEmployee 종료 ");
        }
    }

    @Override
    public void registerEmployee(EmployeeBean employeeBean) {

        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : registerEmployee 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        try {
            hrApplicationService.registerEmployee(employeeBean);
            System.out.println("		@ 등록된 사원명 : " + employeeBean.getEmpName());
            dataSourceTransactionManager.commitTransaction();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" HRServiceFacadeImpl : registerEmployee 종료 ");
        }
    }

	@Override
	public void removeEmployee(EmployeeBean employeeBean) {
		// TODO Auto-generated method stub
		 if (logger.isDebugEnabled()) {
	            logger.debug(" HRServiceFacadeImpl : removeEmployee 시작 ");
	        }
	        dataSourceTransactionManager.beginTransaction();
	        try {
	            hrApplicationService.removeEmployee(employeeBean);
	            System.out.println("		@ 삭제된 사원명 : " + employeeBean.getEmpName());
	            dataSourceTransactionManager.commitTransaction();

	        } catch (DataAccessException e) {
	            logger.fatal(e.getMessage());
	            dataSourceTransactionManager.rollbackTransaction();
	            throw e;
	        }
	        if (logger.isDebugEnabled()) {
	            logger.debug(" HRServiceFacadeImpl : removeEmployee 종료 ");
	        }
	}

}
