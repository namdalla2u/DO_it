package net.plang.HoWooAccount.company.applicationService;

import java.util.ArrayList;

import net.plang.HoWooAccount.company.to.BusinessBean;
import net.plang.HoWooAccount.company.to.DetailBusinessBean;


public interface BusinessApplicationService {

    public ArrayList<BusinessBean> getBusinessList(); //parent 부모
	
    public ArrayList<DetailBusinessBean> getDetailBusiness(String businessName); // 1:M detail
}
