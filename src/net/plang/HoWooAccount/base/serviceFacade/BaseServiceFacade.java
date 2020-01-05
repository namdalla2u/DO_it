package net.plang.HoWooAccount.base.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import net.plang.HoWooAccount.base.exception.DeptCodeNotFoundException;
import net.plang.HoWooAccount.base.exception.IdNotFoundException;
import net.plang.HoWooAccount.base.exception.PwMissmatchException;
import net.plang.HoWooAccount.base.to.CodeBean;
import net.plang.HoWooAccount.base.to.DetailCodeBean;
import net.plang.HoWooAccount.base.to.IreportBean;
import net.plang.HoWooAccount.base.to.MenuBean;
import net.plang.HoWooAccount.hr.to.EmployeeBean;

public interface BaseServiceFacade {

    public EmployeeBean getLoginData(String empCode, String userPw) throws IdNotFoundException, PwMissmatchException, DeptCodeNotFoundException;

    public ArrayList<MenuBean> findUserMenuList(String empCode);

    public ArrayList<DetailCodeBean> getDetailCodeList(HashMap<String, String> param);

    public ArrayList<CodeBean> findCodeList();

    public ArrayList<IreportBean> getIreportData(String slipNo);

    public void batchCodeProcess(ArrayList<CodeBean> codeList, ArrayList<DetailCodeBean> codeList2);


}
