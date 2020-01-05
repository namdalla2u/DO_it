package net.plang.HoWooAccount.company.applicationService;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.company.dao.BusinessDAO;
import net.plang.HoWooAccount.company.dao.BusinessDAOImpl;
import net.plang.HoWooAccount.company.dao.DetailBusinessDAO;
import net.plang.HoWooAccount.company.dao.DetailBusinessDAOImpl;
import net.plang.HoWooAccount.company.to.BusinessBean;
import net.plang.HoWooAccount.company.to.DetailBusinessBean;

public class BusinessApplicationServiceImpl implements BusinessApplicationService{

	
	 protected final Log logger = LogFactory.getLog(this.getClass());

	    private static BusinessApplicationServiceImpl instance = new BusinessApplicationServiceImpl();

	    private BusinessApplicationServiceImpl() {
	    }

	    public static BusinessApplicationServiceImpl getInstance() {

	        System.out.println("		@ BusinessApplicationServiceImpl 객체접근");
	        return instance;
	    }
	    
	    private BusinessDAO businessDAO = BusinessDAOImpl.getInstance();
	    private DetailBusinessDAO detailbusinessDAO = DetailBusinessDAOImpl.getInstance();
	    
	   //업태리스트조회 
	   @Override
	    public ArrayList<BusinessBean> getBusinessList() {

	        if (logger.isDebugEnabled()) {
	            logger.debug(" BusinessApplicationServiceImpl : getBusinessList 시작 ");
	        }
	        ArrayList<BusinessBean> businessList = null;
	        try {
	        	businessList = new ArrayList<BusinessBean>();
	        	businessList = businessDAO.selectBusinessList(); // 1:M    1 


	        } catch (DataAccessException e) {
	            logger.fatal(e.getMessage());
	            throw e;

	        }
	        if (logger.isDebugEnabled()) {
	            logger.debug(" BusinessApplicationServiceImpl : getBusinessList 종료 ");
	        }
	        return businessList;
	    }
	    
	    // 자식 업태 리스트
	    @Override
	    public ArrayList<DetailBusinessBean> getDetailBusiness(String businessCode) {
	        if (logger.isDebugEnabled()) {
	            logger.debug(" BusinessApplicationServiceImpl : getDetailBusiness 시작 ");
	        }
	        ArrayList<DetailBusinessBean> detailBusinessBean = null;
	        try {
	        	detailBusinessBean = new ArrayList<DetailBusinessBean>();
	        	detailBusinessBean = detailbusinessDAO.selectDetailBusinessList(businessCode); //1:m  다중


	        } catch (DataAccessException e) {
	            logger.fatal(e.getMessage());
	            throw e;

	        }
	        if (logger.isDebugEnabled()) {
	            logger.debug(" BusinessApplicationServiceImpl : getDetailBusiness 종료 ");
	        }
	        return detailBusinessBean;
	    }
	    

}
