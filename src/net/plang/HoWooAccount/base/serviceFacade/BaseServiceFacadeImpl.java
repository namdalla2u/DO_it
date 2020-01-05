package net.plang.HoWooAccount.base.serviceFacade;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.applicationService.BaseApplicationService;
import net.plang.HoWooAccount.base.applicationService.BaseApplicationServiceImpl;
import net.plang.HoWooAccount.base.applicationService.CodeListApplicationService;
import net.plang.HoWooAccount.base.applicationService.CodeListApplicationServiceImpl;
import net.plang.HoWooAccount.base.exception.DeptCodeNotFoundException;
import net.plang.HoWooAccount.base.exception.IdNotFoundException;
import net.plang.HoWooAccount.base.exception.PwMissmatchException;
import net.plang.HoWooAccount.base.to.CodeBean;
import net.plang.HoWooAccount.base.to.DetailCodeBean;
import net.plang.HoWooAccount.base.to.IreportBean;
import net.plang.HoWooAccount.base.to.MenuBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.hr.to.EmployeeBean;


public class BaseServiceFacadeImpl implements BaseServiceFacade {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
    BaseApplicationService baseCodeApplicationService = BaseApplicationServiceImpl.getInstance();

    CodeListApplicationService codeListApplicationService = CodeListApplicationServiceImpl.getInstance();

    private static BaseServiceFacade instance = new BaseServiceFacadeImpl();

    private BaseServiceFacadeImpl() {
    }

    ;

    public static BaseServiceFacade getInstance() {

        return instance;
    }
    //리포트 테스트중
    @Override
    public ArrayList<IreportBean> getIreportData(String slipNo) {

        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : getReportData 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        ArrayList<IreportBean> reportDataList = null;
        System.out.println("		@ DB 접근 : getReportData");
        try {
            System.out.println("Ireport 시작");
            reportDataList= baseCodeApplicationService.getIreportData(slipNo);      
            dataSourceTransactionManager.commitTransaction();
            System.out.println("		@ DB 커밋");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            //throw e;
            System.out.println("		@ DB 롤백");
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : getIreportData 종료 ");
        }
		return reportDataList;
    }

    @Override
    public EmployeeBean getLoginData(String empCode, String userPw) throws IdNotFoundException, DeptCodeNotFoundException, PwMissmatchException {

        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : getLoginData 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        System.out.println("		@ DB 접근 : getLoginData");
        EmployeeBean result = null;
        try {
            System.out.println("베이스서비스 파사드 트라이");
            result = baseCodeApplicationService.getLoginData(empCode, userPw);
            dataSourceTransactionManager.commitTransaction();
            System.out.println("		@ DB 커밋");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            //throw e;
            System.out.println("		@ DB 롤백");
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : getLoginData 종료 ");
        }
        return result;
    }

    @Override
    public ArrayList<MenuBean> findUserMenuList(String empCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : findUserMenuList 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        System.out.println("		@ DB 접근 : findUserMenuList");
        ArrayList<MenuBean> menuList = null;
        try {
            menuList = baseCodeApplicationService.findMenuCodeList(empCode);
            dataSourceTransactionManager.commitTransaction();
            System.out.println("		@ DB 커밋");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            System.out.println("		@ DB 롤백");
            //throw e;

        }
        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : findUserMenuList 종료 ");
        }
        return menuList;
    }

    @Override
    public ArrayList<DetailCodeBean> getDetailCodeList(HashMap<String, String> param) {

        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : getDetailCodeList 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        System.out.println("		@ DB 접근 : getDetailCodeList");
        ArrayList<DetailCodeBean> datailCondeList = null;
        try {
            datailCondeList = codeListApplicationService.getDetailCodeList(param);

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : getDetailCodeList 종료 ");
        }
        return datailCondeList;
    }

    @Override
    public ArrayList<CodeBean> findCodeList() {

        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : findCodeList 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        System.out.println("		@ DB 접근 : findCodeList");
        ArrayList<CodeBean> codeList = null;
        try {
            codeList = codeListApplicationService.findCodeList();

        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            dataSourceTransactionManager.rollbackTransaction();
            System.out.println("		@ 베이스 퍼사드 조회 오류");
            System.out.println("		@ DB 롤백");
            //throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : findCodeList 종료 ");
        }
        return codeList;
    }

    @Override
    public void batchCodeProcess(ArrayList<CodeBean> codeList, ArrayList<DetailCodeBean> codeList2) {

        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : batchCodeProcess 시작 ");
        }
        dataSourceTransactionManager.beginTransaction();
        System.out.println("		@ DB 접근 : batchCodeProcess");
        try {

            codeListApplicationService.batchCodeProcess(codeList, codeList2);

            dataSourceTransactionManager.commitTransaction();
            System.out.println("		@ DB 커밋");
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());

            dataSourceTransactionManager.rollbackTransaction();
            System.out.println("		@ DB 롤백");
            //throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" BaseServiceFacadeImpl : batchCodeProcess 종료 ");
        }
    }


}
