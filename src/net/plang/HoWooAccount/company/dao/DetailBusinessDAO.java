package net.plang.HoWooAccount.company.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.company.to.DetailBusinessBean;

public interface DetailBusinessDAO {

	public ArrayList<DetailBusinessBean> selectDetailBusinessList(String businessCode);
}
