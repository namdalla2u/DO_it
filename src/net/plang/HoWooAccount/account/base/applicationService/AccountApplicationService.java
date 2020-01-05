package net.plang.HoWooAccount.account.base.applicationService;

import java.util.ArrayList;

import net.plang.HoWooAccount.account.base.to.AccountBean;
import net.plang.HoWooAccount.account.base.to.AccountControlBean;

public interface AccountApplicationService {

    public AccountBean getAccount(String code);

    public ArrayList<AccountBean> findParentAccountList();

    public ArrayList<AccountBean> findDetailAccountList(String code);

    public void updateAccount(AccountBean accountBean);

    ArrayList<AccountBean> getAccountListByName(String accountName);

    ArrayList<AccountControlBean> getAccountControlList(String accountCode);
}
