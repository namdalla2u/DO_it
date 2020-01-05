package net.plang.HoWooAccount.base.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.to.MenuBean;
import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;

public class MenuDAOImpl implements MenuDAO {
    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();

    protected final Log logger = LogFactory.getLog(this.getClass());

    private static MenuDAO instance = new MenuDAOImpl();

    private MenuDAOImpl() {
    }

    public static MenuDAO getInstance() {
        return instance;
    }

    @Override
    public ArrayList<MenuBean> selectMenuList(String empCode) {

        if (logger.isDebugEnabled()) {
            logger.debug(" MenuDAOImpl : selectMenuList 시작");
        }
        System.out.println("		@ 넘겨받은 empCode: " + empCode);
        ArrayList<MenuBean> menuList = new ArrayList<MenuBean>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("select m.menu_code,m.MENU_NAME,m.PARENT_MENU_CODE,m.URL,e.POSITION_CODE ");
            query.append(" FROM menu m,EMPLOYEE e,MENU_AVAILABLE_BY_POSITION mv ");
            query.append(" WHERE e.DEPT_CODE=mv.DEPT_CODE ");
            query.append("  AND e.POSITION_CODE=mv.POSITION_CODE ");
            query.append("  AND mv.MENU_CODE=m.menu_code ");
            query.append("  AND e.EMP_CODE=? ORDER BY M.MENU_CODE ");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            pstmt.setString(1, empCode);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MenuBean menuBean = new MenuBean();
                menuBean.setMenuCode(rs.getString("MENU_CODE"));
                menuBean.setMenuName(rs.getString("MENU_NAME"));
                menuBean.setPositionCode(rs.getString("POSITION_CODE"));
                menuBean.setParentMenuCode(rs.getString("PARENT_MENU_CODE"));
                menuList.add(menuBean);

            }


            System.out.println("		@ 메뉴 권한: " + menuList.get(0).getPositionCode());


            if (logger.isDebugEnabled()) {
                logger.debug(" MenuDAOImpl : selectMenuList 종료 ");
            }
            return menuList;


        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

    @Override
    public ArrayList<MenuBean> selectAllMenuList() {

        if (logger.isDebugEnabled()) {
            logger.debug(" MenuDAOImpl : selectAllMenuList 시작 ");
        }
        ArrayList<MenuBean> menuList = new ArrayList<MenuBean>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append(" SELECT * FROM MENU ORDER BY MENU_CODE");
            con = dataSourceTransactionManager.getConnection();
            pstmt = con.prepareStatement(query.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                MenuBean menuBean = new MenuBean();
                menuBean.setMenuCode(rs.getString("MENU_CODE"));
                menuBean.setMenuName(rs.getString("MENU_NAME"));

                menuBean.setParentMenuCode(rs.getString("PARENT_MENU_CODE"));
                menuBean.setUrl(rs.getString("URL"));
                menuList.add(menuBean);
            }
            System.out.println("		@ 메뉴경로 : " + menuList.get(0).getUrl());
            if (logger.isDebugEnabled()) {
                logger.debug(" MenuDAOImpl : selectAllMenuList 종료 ");
            }
            return menuList;
        } catch (Exception sqle) {
            logger.fatal(sqle.getMessage());
            throw new DataAccessException(sqle.getMessage());
        } finally {
            dataSourceTransactionManager.close(pstmt, rs);
        }
    }

}
