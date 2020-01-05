package net.plang.HoWooAccount.base.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.base.to.CodeBean;

public interface CodeDAO {

    public ArrayList<CodeBean> selectCodeList();

    public void insertCode(CodeBean codeBean);

    public void updateCode(CodeBean codeBean);

    public void deleteCode(String Code);

}
