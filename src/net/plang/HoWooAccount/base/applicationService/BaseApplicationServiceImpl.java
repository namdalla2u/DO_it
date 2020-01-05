package net.plang.HoWooAccount.base.applicationService;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.dao.AddressDAO;
import net.plang.HoWooAccount.base.dao.AddressDAOImpl;
import net.plang.HoWooAccount.base.dao.MenuDAO;
import net.plang.HoWooAccount.base.dao.MenuDAOImpl;
import net.plang.HoWooAccount.base.exception.DeptCodeNotFoundException;
import net.plang.HoWooAccount.base.exception.IdNotFoundException;
import net.plang.HoWooAccount.base.exception.PwMissmatchException;
import net.plang.HoWooAccount.base.to.IreportBean;
import net.plang.HoWooAccount.base.to.MenuBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.hr.dao.EmployeeDAO;
import net.plang.HoWooAccount.hr.dao.EmployeeDAOImpl;
import net.plang.HoWooAccount.hr.to.EmployeeBean;

public class BaseApplicationServiceImpl implements BaseApplicationService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private static BaseApplicationServiceImpl instance = new BaseApplicationServiceImpl();

    private BaseApplicationServiceImpl() {
    }

    public static BaseApplicationService getInstance() {

        System.out.println("		@ BaseApplicationServiceImpl 객체접근");
        return instance;
    }

    private MenuDAO menuDAO = MenuDAOImpl.getInstance();
    private EmployeeDAO employeeDAO = EmployeeDAOImpl.getInstance();
    private AddressDAO addressDAO = AddressDAOImpl.getInstance();

    
    //아이리포트 수정중
    @Override
    public ArrayList<IreportBean> getIreportData(String slipNo) {

        if (logger.isDebugEnabled()) {
            logger.debug(" BaseApplicationServiceImpl : getIreportData 시작 ");
        }
        ArrayList<IreportBean> reportDataList = null;
        try {
        	System.out.println("아이리포트 어플리케이션");

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;

        }
        if (logger.isDebugEnabled()) {
            logger.debug(" BaseApplicationServiceImpl : getIreportData 종료 ");
        }
        return reportDataList;
    }


    @Override
    public EmployeeBean getLoginData(String empCode, String userPw) throws IdNotFoundException, DeptCodeNotFoundException, PwMissmatchException, DataAccessException {

        if (logger.isDebugEnabled()) {
            logger.debug(" BaseApplicationServiceImpl : getLoginData 시작 ");
        }

        EmployeeBean employeeBean;

        try {
            employeeBean = employeeDAO.selectEmployee(empCode);
        	
            if (employeeBean == null) 
                throw new IdNotFoundException("존재 하지 않는 계정입니다.");
            else {
                if (!employeeBean.getUserPw().equals(userPw)) 
                    throw new PwMissmatchException("비밀번호가 틀립니다.");
                
            }
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        return employeeBean;

    }

    @Override
    public ArrayList<MenuBean> findMenuCodeList(String empCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" BaseApplicationServiceImpl : findMenuCodeList 시작 ");
        }
        ArrayList<MenuBean> menuList = null;
        try {
            menuList = menuDAO.selectMenuList(empCode);


            System.out.println("		@ 메뉴코드 : " + menuList.get(0).getMenuCode());
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;

        }
        if (logger.isDebugEnabled()) {
            logger.debug(" BaseApplicationServiceImpl : findMenuCodeList 종료 ");
        }
        return menuList;
    }


}
