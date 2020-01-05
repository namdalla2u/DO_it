package net.plang.HoWooAccount.account.base.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.account.base.to.AccountBean;
import net.plang.HoWooAccount.account.base.to.AccountControlBean;

public interface AccountDAO {

    public AccountBean selectAccount(String accountCode);

    public ArrayList<AccountBean> selectDetailAccountList(String code);

    public ArrayList<AccountBean> selectParentAccountList();

    public void updateAccount(AccountBean accountBean);

    ArrayList<AccountBean> selectAccountListByName(String accountName);

    ArrayList<AccountControlBean> selectAccountControlList(String accountCode);
}
