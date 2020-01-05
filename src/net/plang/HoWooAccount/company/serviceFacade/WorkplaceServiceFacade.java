package net.plang.HoWooAccount.company.serviceFacade;

import java.util.ArrayList;

import net.plang.HoWooAccount.company.to.WorkplaceBean;



public interface WorkplaceServiceFacade {
	
	public WorkplaceBean getWorkplace(String workplaceCode); // 1개사업장 조회
	
	public void workplaceAdd(WorkplaceBean workplaceBean); //사업장추가
	
	public void eliminationWorkplace(ArrayList<String> getCodes); //사업장삭제 //arraylist로 바꿀꺼임

	public void updateApprovalStatus(ArrayList<String> getCodes,String status); //사업장 승인상태 업데이트
		
	public ArrayList<WorkplaceBean> getAllWorkplaceList(); //모든사업장조회

}
