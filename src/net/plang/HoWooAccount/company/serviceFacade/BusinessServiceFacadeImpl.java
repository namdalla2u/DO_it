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
import net.plang.HoWooAccount.company.to.BusinessBean;
import net.plang.HoWooAccount.company.to.DetailBusinessBean;

public class BusinessServiceFacadeImpl implements BusinessServiceFacade{

	protected final Log logger = LogFactory.getLog(this.getClass());

	private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	WorkplaceApplicationService workplaceApplicationService = WorkplaceApplicationServiceImpl.getInstance();
	BusinessApplicationService businessApplicationService = BusinessApplicationServiceImpl.getInstance();

	private static BusinessServiceFacade instance = new BusinessServiceFacadeImpl();

	private BusinessServiceFacadeImpl() {
	}

	;

	public static BusinessServiceFacade getInstance() {

		return instance;
	}
	
	@Override
	public ArrayList<BusinessBean> getBusinessList() {
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : getBusinessList 시작 ");
		}
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : getBusinessList");
		ArrayList<BusinessBean> businessList = null;
		try {
			businessList = new ArrayList<BusinessBean>();
			businessList=businessApplicationService.getBusinessList();
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ 업태리스트 오류");
			System.out.println("		@ DB 롤백");
			// throw e;
		}
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : getBusinessList 종료 ");
		}
		return businessList;
	}
	
	@Override
	public ArrayList<DetailBusinessBean> getDetailBusiness(String businessCode) {
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : getDetailBusiness 시작 ");
		}
		dataSourceTransactionManager.beginTransaction();
		System.out.println("		@ DB 접근 : getDetailBusiness");
		ArrayList<DetailBusinessBean> detailBusinessList = null;
		try {
			detailBusinessList = new ArrayList<DetailBusinessBean>();
			detailBusinessList = businessApplicationService.getDetailBusiness(businessCode);
		} catch (DataAccessException e) {
			logger.fatal(e.getMessage());
			dataSourceTransactionManager.rollbackTransaction();
			System.out.println("		@ 업태소분류리스트 오류");
			System.out.println("		@ DB 롤백");
			// throw e;
		}
		if (logger.isDebugEnabled()) {
			logger.debug(" workplaceServiceFacadeImpl : getDetailBusiness 종료 ");
		}
		return detailBusinessList;
	}
}
