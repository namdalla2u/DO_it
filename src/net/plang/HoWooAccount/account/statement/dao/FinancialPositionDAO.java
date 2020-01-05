package net.plang.HoWooAccount.account.statement.dao;

import java.util.HashMap;

public interface FinancialPositionDAO {
    public HashMap<String, Object> callFinancialPosition(String toDate);
}
