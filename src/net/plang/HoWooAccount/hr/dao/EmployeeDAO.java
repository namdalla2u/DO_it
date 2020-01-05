package net.plang.HoWooAccount.hr.dao;

import java.util.ArrayList;

import net.plang.HoWooAccount.hr.to.EmployeeBean;

public interface EmployeeDAO {

    public ArrayList<EmployeeBean> selectEmployeeList(String deptCode);

    public void updateEmployeeInfo(EmployeeBean employeeBean);

    public void updateEmployee(EmployeeBean employBean);

    public void deleteEmployee(String empCode);

    public void insertEmployee(EmployeeBean employeeBean);

    public EmployeeBean selectEmployee(String EmpCode);
}
