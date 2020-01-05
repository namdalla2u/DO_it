package net.plang.HoWooAccount.base.dao;

import java.util.ArrayList;
import java.util.HashMap;

import net.plang.HoWooAccount.base.to.DetailCodeBean;

public interface DetailCodeDAO {

    ArrayList<DetailCodeBean> selectDetailCodeList(HashMap<String, String> param);

    void insertDetailCode(DetailCodeBean codeDetailBean);

    void updateDetailCode(DetailCodeBean codeDetailBean);

    void deleteDetailCode(String codeNo);

}
