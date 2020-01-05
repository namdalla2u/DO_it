package net.plang.HoWooAccount.base.applicationService;

import java.util.ArrayList;

import net.plang.HoWooAccount.base.exception.DeptCodeNotFoundException;
import net.plang.HoWooAccount.base.exception.IdNotFoundException;
import net.plang.HoWooAccount.base.exception.PwMissmatchException;
import net.plang.HoWooAccount.base.to.IreportBean;
import net.plang.HoWooAccount.base.to.MenuBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.hr.to.EmployeeBean;

public interface BaseApplicationService {

    public EmployeeBean getLoginData(String empCode, String userPw) throws IdNotFoundException, DeptCodeNotFoundException, PwMissmatchException, DataAccessException;

    public ArrayList<MenuBean> findMenuCodeList(String empCode);

    public ArrayList<IreportBean> getIreportData(String slipNo);
}
