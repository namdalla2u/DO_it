package net.plang.HoWooAccount.company.applicationService;


import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.company.dao.WorkplaceDAO;
import net.plang.HoWooAccount.company.dao.WorkplaceDAOImpl;
import net.plang.HoWooAccount.company.to.WorkplaceBean;


public class WorkplaceApplicationServiceImpl implements WorkplaceApplicationService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private static WorkplaceApplicationServiceImpl instance = new WorkplaceApplicationServiceImpl();

    private WorkplaceApplicationServiceImpl() {
    }

    public static WorkplaceApplicationService getInstance() {

        System.out.println("		@ WorkplaceApplicationServiceImpl 객체접근");
        return instance;
    }

    

    private WorkplaceDAO workplaceDAO = WorkplaceDAOImpl.getInstance();
    

 
    // 사업장조회
    @Override
    public WorkplaceBean getWorkplace(String workplaceCode) {
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : getWorkplace 시작 ");
        }
        	WorkplaceBean workplaceBean =null;
        try {
        	workplaceBean = new WorkplaceBean();
        	workplaceBean = workplaceDAO.selectWorkplace(workplaceCode);
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : getWorkplace 종료 ");
        }
        return workplaceBean;
    }
    
    // 승인상태 업뎃
    @Override
    public void updateApprovalStatus(ArrayList<String> getCodes,String status) {
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : updateApprovalStatus 시작 ");
        }
        try {
        	for(String code : getCodes) {
        		
        		workplaceDAO.updateWorkplaceAccount(code, status);
        		System.out.println(code+"업뎃완료");
        	}
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw new DataAccessException(e.getMessage());

        }
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : updateApprovalStatus 종료 ");
        }
    }
   
    //사업장추가
    @Override
    public void workPlaceAdd(WorkplaceBean workplaceBean) {
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : getWorkplace 시작 ");
        }
        try {
        	workplaceDAO.insertWorkplace(workplaceBean);


        } catch (DataAccessException e) {
        	System.out.println(e.getMessage());
        	throw new DataAccessException(e.getMessage());
            

        }
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : getWorkplace 종료 ");
        }
    }
    
    //사업장삭제
    @Override
    public void eliminationWorkplace(ArrayList<String> getCodes) {
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : eliminationWorkplace 시작 ");
        }
        try {      
        	for(String code : getCodes) {
        		workplaceDAO.deleteWorkplace(code);
        		System.out.println("사업장삭제완료:"+code);
        	}
        } catch (DataAccessException e) {
        	System.out.println(e.getMessage());
        	throw new DataAccessException(e.getMessage());
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : eliminationWorkplace 종료 ");
        }
    }
    
    // 사업장 승인상태 조회
    @Override
    public ArrayList<WorkplaceBean> getAllWorkplaceList() {
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : getAllWorkplaceList 시작 ");
        }
        ArrayList<WorkplaceBean> allworkplaceList= null;
        try {
        	allworkplaceList = new ArrayList<>();
        	
        	allworkplaceList = workplaceDAO.selectAllWorkplaceList();


        } catch (DataAccessException e) {
        	throw new DataAccessException(e.getMessage());
            

        }
        if (logger.isDebugEnabled()) {
            logger.debug(" WorkplaceApplicationServiceImpl : getAllWorkplaceList 종료 ");
        }
		return allworkplaceList;
    }
}
