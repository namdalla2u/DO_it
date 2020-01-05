package net.plang.HoWooAccount.base.applicationService;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.dao.CodeDAO;
import net.plang.HoWooAccount.base.dao.CodeDAOImpl;
import net.plang.HoWooAccount.base.dao.DetailCodeDAO;
import net.plang.HoWooAccount.base.dao.DetailCodeDAOImpl;
import net.plang.HoWooAccount.base.to.CodeBean;
import net.plang.HoWooAccount.base.to.DetailCodeBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class CodeListApplicationServiceImpl implements CodeListApplicationService {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private CodeDAO codeDAO = CodeDAOImpl.getInstance();

    private DetailCodeDAO codeDetailDAO = DetailCodeDAOImpl.getinstance();

    private static CodeListApplicationService instance;

    private CodeListApplicationServiceImpl() {
    }

    ;

    public static CodeListApplicationService getInstance() {

        if (instance == null) {
            instance = new CodeListApplicationServiceImpl();
            System.out.println("		@ CodeListApplicationServiceImpl에 접근");
        }
        return instance;
    }


    private DetailCodeDAO detailCodeDAO = DetailCodeDAOImpl.getinstance();

    @Override
    public ArrayList<DetailCodeBean> getDetailCodeList(HashMap<String, String> param) {

        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListApplicationServiceImpl : getDetailCodeList 시작 ");
        }

        ArrayList<DetailCodeBean> detailCodeList = null;
        try {
            detailCodeList = detailCodeDAO.selectDetailCodeList(param);
        } catch (Exception e) {
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListApplicationServiceImpl : getDetailCodeList 종료 ");
        }
        return detailCodeList;
    }

    @Override
    public ArrayList<CodeBean> findCodeList() {

        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListApplicationServiceImpl : findCodeList 시작 ");
        }
        ArrayList<CodeBean> codeList = null;
        try {
            codeList = codeDAO.selectCodeList();

        } catch (DataAccessException e) {
            System.out.println("		@ 코드어플서비스 조회 오류");
            logger.fatal(e.getMessage());
            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListApplicationServiceImpl : findCodeList 종료 ");
        }
        return codeList;
    }

    @Override
    public void batchCodeProcess(ArrayList<CodeBean> codeList, ArrayList<DetailCodeBean> codeList2) {

        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListApplicationServiceImpl : batchCodeProcess 시작 ");
        }
        try {
            System.out.println(codeList);
            for (CodeBean code : codeList) {
                switch (code.getStatus()) {
                    case "insert":
                        codeDAO.insertCode(code);
                        break;
                    case "update":
                        codeDAO.updateCode(code);
                        break;
                    case "normal":
                        break;
                    case "delete":
                        codeDAO.deleteCode(code.getDivisionCodeNo());
                }
            }
            ArrayList<DetailCodeBean> DetailcodeList = codeList2;
            for (DetailCodeBean codeDetailBean : DetailcodeList) {
                switch (codeDetailBean.getStatus()) {
                    case "insert":
                        codeDetailDAO.insertDetailCode(codeDetailBean);
                        break;
                    case "update":
                        codeDetailDAO.updateDetailCode(codeDetailBean);
                        break;
                    case "delete":
                        codeDetailDAO.deleteDetailCode(codeDetailBean.getDetailCode());
                }
            }


        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());

            throw e;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListApplicationServiceImpl : batchCodeProcess 종료 ");
        }
    }


}
