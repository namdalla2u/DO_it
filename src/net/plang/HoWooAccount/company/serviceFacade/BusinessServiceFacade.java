package net.plang.HoWooAccount.company.serviceFacade;

import java.util.ArrayList;

import net.plang.HoWooAccount.company.to.BusinessBean;
import net.plang.HoWooAccount.company.to.DetailBusinessBean;

public interface BusinessServiceFacade {
	public ArrayList<BusinessBean> getBusinessList(); //업태종목 전부조회
	
	public ArrayList<DetailBusinessBean> getDetailBusiness(String businessName); // 업태종목 소분류 전부조회
}
