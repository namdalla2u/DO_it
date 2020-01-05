package net.plang.HoWooAccount.base.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.base.to.MenuBean;

public interface MenuDAO {
    ArrayList<MenuBean> selectMenuList(String empCode);

    ArrayList<MenuBean> selectAllMenuList();

}
