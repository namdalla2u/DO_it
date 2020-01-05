package net.plang.HoWooAccount.company.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.company.to.WorkplaceBean;

public interface WorkplaceDAO {
	public void updateWorkplaceAccount(String code,String status); //사업장 승인상태 업데이트
	
    public WorkplaceBean selectWorkplace(String workplaceCode); //사업장조회
	
	public void insertWorkplace(WorkplaceBean workplaceBean); //사업장추가
	
	public void deleteWorkplace(String code); //사업장삭제
	
	public ArrayList<WorkplaceBean> selectAllWorkplaceList(); //모든사업장조회
}
