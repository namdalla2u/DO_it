package net.plang.HoWooAccount.company.serviceFacade;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.company.applicationService.BusinessApplicationService;
import net.plang.HoWooAccount.company.applicationService.BusinessApplicationServiceImpl;
import net.plang.HoWooAccount.company.applicationService.WorkplaceApplicationService;
import net.plang.HoWooAccount.company.applicationService.WorkplaceApplicationServiceImpl;
import net.plang.HoWooAccount.company.to.WorkplaceBean;

public class WorkplaceServiceFacadeImpl implements WorkplaceServiceFacade {

	protected final Log logger = LogFactory.getLog(this.getClass());

	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	WorkplaceApplicationService workplaceApplicationService = WorkplaceApplicationServiceImpl.getInstance();
	BusinessApplicationService businessApplicationService = BusinessApplicationServiceImpl.getInstance();

	private static WorkplaceServiceFacade instance = new WorkplaceServiceFacadeImpl();

	private WorkplaceServiceFacadeImpl() {
	}

	;

	public static WorkplaceServiceFacade getInstance() {

		return instance;
	}

	@Override
	public void workplaceAdd(WorkplaceBean workplaceBean) {
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : workplaceAdd 시작 ");
		}
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : workplaceAdd");
		try {
			WorkplaceBean workplaceCodeCheck = workplaceApplicationService.getWorkplace(workplaceBean.getWorkplaceCode());
			if(workplaceCodeCheck==null) {
			workplaceApplicationService.workPlaceAdd(workplaceBean);
			}
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ 회사 등록 오류");
			System.out.println("		@ DB 롤백");
			throw new DataAccessException(e.getMessage());
		}
		
		dataSourceTransactionManager.commitTransaction();
		
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : workplaceAdd 종료 ");
		}
	}
	
	@Override
	public void eliminationWorkplace(ArrayList<String> getCodes) {
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : deleteWorkplace 시작 ");
		}
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : workplaceAdd");
		try {
			workplaceApplicationService.eliminationWorkplace(getCodes);

		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ 회사 삭제 오류");
			System.out.println("		@ DB 롤백");
			throw new DataAccessException(e.getMessage());
		}
		
		dataSourceTransactionManager.commitTransaction();
		
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : deleteWorkplace 종료 ");
		}
	}
	
	@Override
	public void updateApprovalStatus(ArrayList<String> getCodes,String status) {
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : updateWorkplaceAccount 시작 ");
		}
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : workplaceAdd");
		try {
			workplaceApplicationService.updateApprovalStatus(getCodes,status);

		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ 거래 승인상태 처리 오류");
			System.out.println("		@ DB 롤백");
			throw new DataAccessException(e.getMessage());
		}
		
		dataSourceTransactionManager.commitTransaction();
		
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : updateWorkplaceAccount 종료 ");
		}
	}
	
	@Override
	public WorkplaceBean getWorkplace(String workplaceCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : getWorkplace 시작 ");
		}

		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : getWorkplace");
		
		WorkplaceBean workplaceBean =null;
		try {
			workplaceBean=workplaceApplicationService.getWorkplace(workplaceCode);
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println(e.getMessage());
			System.out.println("		@ 사업장조회 오류");
			System.out.println("		@ DB 롤백");
			// throw e;
		}
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : getWorkplace 종료 ");
		}
		return workplaceBean;
	}
	
	
	@Override
	public ArrayList<WorkplaceBean> getAllWorkplaceList () {
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : getAllWorkplaceList 시작 ");
		}
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : getAllWorkplaceList");
		ArrayList<WorkplaceBean> allworkplaceList = null;
		try {
			allworkplaceList = new ArrayList<>();
			allworkplaceList=workplaceApplicationService.getAllWorkplaceList();
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ 사업장 전체리스트 오류");
			System.out.println("		@ DB 롤백");
			// throw e;
		}
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : getAllWorkplaceList 종료 ");
		}
		return allworkplaceList;
	}
	
}