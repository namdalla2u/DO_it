package net.plang.HoWooAccount.company.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.company.to.BusinessBean;


public interface BusinessDAO {

    public ArrayList<BusinessBean> selectBusinessList();

}
