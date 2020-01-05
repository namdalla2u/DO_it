package net.plang.HoWooAccount.company.applicationService;


import java.util.ArrayList;

import net.plang.HoWooAccount.company.to.WorkplaceBean;


public interface WorkplaceApplicationService {
	
	public void eliminationWorkplace(ArrayList<String> getCodes); //사업장 삭제

	public void workPlaceAdd(WorkplaceBean workplaceBean); //사업장추가
    
    public WorkplaceBean getWorkplace(String workplaceCode); //사업장조회
    
    public ArrayList<WorkplaceBean> getAllWorkplaceList(); //거래처 미등록 사업장조회
    
    public void updateApprovalStatus(ArrayList<String> getCodes,String status); //update 승인상태
}
