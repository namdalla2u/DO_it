package net.plang.HoWooAccount.company.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.db.DataSourceTransactionManager;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.company.to.WorkplaceBean;


public class WorkplaceDAOImpl implements WorkplaceDAO {
	    protected final Log logger = LogFactory.getLog(this.getClass());
	    private DataSourceTransactionManager dataSourceTransactionManager = DataSourceTransactionManager.getInstance();
	    private static WorkplaceDAO instance;

	    public static WorkplaceDAO getInstance() {
	        if (instance == null) instance = new WorkplaceDAOImpl();
	        return instance;
	    }
	    
	    //사업장 조회
	    @Override
	    public WorkplaceBean selectWorkplace(String workplaceCode) {
	        if (logger.isDebugEnabled()) {
	            logger.debug(" WorkplaceDAOImpl : selectWorkplace 시작 ");
	        }
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try {
	            StringBuffer query = new StringBuffer();
	            query.append("select * from workplace where workplace_code=?");
	            con = dataSourceTransactionManager.getConnection();
	            pstmt = con.prepareStatement(query.toString());
	            pstmt.setString(1,"BRC-"+workplaceCode);
	            rs = pstmt.executeQuery();

	            WorkplaceBean workplaceBean = null;
	            while (rs.next()) {
	            	workplaceBean = new WorkplaceBean();
	            	workplaceBean.setWorkplaceCode(rs.getString("WORKPLACE_CODE")); //사업장코드
	            	workplaceBean.setCompanyCode(rs.getString("COMPANY_CODE")); //회사코드
	            	workplaceBean.setWorkplaceName(rs.getString("WORKPLACE_NAME")); //사업자명
	            	workplaceBean.setBusinessLicense(rs.getString("BUSINESS_LICENSE_NUMBER")); //사업자등록번호
	            	workplaceBean.setCorporationLicence(rs.getString("CORPORATION_LICENSE_NUMBER")); //법인등록번호
	            	workplaceBean.setWorkplaceCeoName(rs.getString("WORKPLACE_CEO_NAME")); //대표자명
	            	workplaceBean.setBusinessConditions(rs.getString("WORKPLACE_BUSINESS_CONDITIONS")); //업태
	            	workplaceBean.setBusinessItems(rs.getString("WORKPLACE_BUSINESS_ITEMS")); //종목
	            	workplaceBean.setWorkplaceTelNumber(rs.getString("WORKPLACE_TEL_NUMBER")); //사업자전화번호
	            	workplaceBean.setWorkplaceFaxNumber(rs.getString("WORKPLACE_FAX_NUMBER")); //사업장팩스번호
	            	workplaceBean.setWorkplaceBasicAddress(rs.getString("WORKPLACE_BASIC_ADDRESS")); //사업장주소
	            	workplaceBean.setApprovalStatus(rs.getString("APPROVAL_STATUS")); //미등록
	            }

	            if (logger.isDebugEnabled()) {
	                logger.debug(" WorkplaceDAOImpl : selectWorkplace 종료 ");
	            }
	            return workplaceBean;
	        } catch (Exception sqle) {
	            logger.fatal(sqle.getMessage());
	            throw new DataAccessException(sqle.getMessage());
	        } finally {
	            dataSourceTransactionManager.close(pstmt, rs);
	        }
	    }

		@Override //사업장추가
		public void insertWorkplace(WorkplaceBean workplaceBean) {
	        if (logger.isDebugEnabled()) {
	            logger.debug(" WorkplaceDAOImpl : insertWorkplace 시작 ");
	        }
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        try {
	            StringBuffer query = new StringBuffer();
	            query.append(" INSERT INTO WORKPLACE ");
	            query.append("(WORKPLACE_CODE,COMPANY_CODE,WORKPLACE_NAME, BUSINESS_LICENSE_NUMBER, ");
	            query.append("CORPORATION_LICENSE_NUMBER, WORKPLACE_CEO_NAME, WORKPLACE_BUSINESS_CONDITIONS, ");
	            query.append("WORKPLACE_BUSINESS_ITEMS, WORKPLACE_TEL_NUMBER, WORKPLACE_FAX_NUMBER, ");
	            query.append("WORKPLACE_BASIC_ADDRESS, APPROVAL_STATUS) ");
	            query.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
	            con = dataSourceTransactionManager.getConnection();
	            pstmt = con.prepareStatement(query.toString());
	            pstmt.setString(1, workplaceBean.getWorkplaceCode());
	            pstmt.setString(2, workplaceBean.getCompanyCode());
	            pstmt.setString(3, workplaceBean.getWorkplaceName());
	            pstmt.setString(4, workplaceBean.getBusinessLicense());
	            pstmt.setString(5, workplaceBean.getCorporationLicence());
	            pstmt.setString(6, workplaceBean.getWorkplaceCeoName());
	            pstmt.setString(7, workplaceBean.getBusinessConditions());
	            pstmt.setString(8, workplaceBean.getBusinessItems());
	            pstmt.setString(9, workplaceBean.getWorkplaceTelNumber());
	            pstmt.setString(10, workplaceBean.getWorkplaceFaxNumber());
	            pstmt.setString(11, workplaceBean.getWorkplaceBasicAddress());
	            pstmt.setString(12, workplaceBean.getApprovalStatus());

	            System.out.println("		@ 가입할 사업장코드" + workplaceBean.getWorkplaceCode());
	            System.out.println("		@ 가입할 사원대표자명" + workplaceBean.getWorkplaceCeoName());
	            System.out.println("		@ 가입할 거래 승인상태" + workplaceBean.getApprovalStatus());
	            pstmt.executeUpdate();
	            if (logger.isDebugEnabled()) {
	                logger.debug(" WorkplaceDAOImpl : insertWorkplace 종료 ");
	            }
	        } catch (Exception sqle) {
	            logger.fatal(sqle.getMessage());
	            throw new DataAccessException(sqle.getMessage());
	        } finally {
	            dataSourceTransactionManager.close(pstmt);
	        }
			
		}
		
	    @Override // 거래처 미등록 사업장 조회
	    public ArrayList<WorkplaceBean> selectAllWorkplaceList() {
	        if (logger.isDebugEnabled()) {
	            logger.debug(" WorkplaceDAOImpl : selectAllWorkplaceList 시작 ");
	        }
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
	        try {
	            StringBuffer query = new StringBuffer();
	            query.append("select * from workplace");
	            con = dataSourceTransactionManager.getConnection();
	            pstmt = con.prepareStatement(query.toString());
	            rs = pstmt.executeQuery();
	            ArrayList<WorkplaceBean> allworkplaceList= new ArrayList<>();
	            WorkplaceBean workplaceBean = null;
	            
	            while (rs.next()) {
	            	workplaceBean = new WorkplaceBean();
	            	workplaceBean.setWorkplaceCode(rs.getString("WORKPLACE_CODE")); //사업장코드
	            	workplaceBean.setCompanyCode(rs.getString("COMPANY_CODE")); //회사코드
	            	workplaceBean.setWorkplaceName(rs.getString("WORKPLACE_NAME")); //사업장명
	            	workplaceBean.setBusinessLicense(rs.getString("BUSINESS_LICENSE_NUMBER")); //사업자등록번호
	            	workplaceBean.setCorporationLicence(rs.getString("CORPORATION_LICENSE_NUMBER")); //법인등록번호
	            	workplaceBean.setWorkplaceCeoName(rs.getString("WORKPLACE_CEO_NAME")); //대표자명
	            	workplaceBean.setBusinessConditions(rs.getString("WORKPLACE_BUSINESS_CONDITIONS")); //업태
	            	workplaceBean.setBusinessItems(rs.getString("WORKPLACE_BUSINESS_ITEMS")); //종목
	            	workplaceBean.setWorkplaceTelNumber(rs.getString("WORKPLACE_TEL_NUMBER")); //사업자전화번호
	            	workplaceBean.setWorkplaceFaxNumber(rs.getString("WORKPLACE_FAX_NUMBER")); //사업장팩스번호
	            	workplaceBean.setWorkplaceBasicAddress(rs.getString("WORKPLACE_BASIC_ADDRESS")); //사업장주소
	            	workplaceBean.setApprovalStatus(rs.getString("APPROVAL_STATUS")); //미등록
	            	allworkplaceList.add(workplaceBean);
	            }

	            if (logger.isDebugEnabled()) {
	                logger.debug(" WorkplaceDAOImpl : selectAllWorkplaceList 종료 ");
	            }
	            return allworkplaceList;
	        } catch (Exception sqle) {
	            logger.fatal(sqle.getMessage());
	            throw new DataAccessException(sqle.getMessage());
	        } finally {
	            dataSourceTransactionManager.close(pstmt, rs);
	        }
	    }
	    
	    @Override // 승인상태 업데이트
	    public void updateWorkplaceAccount(String code,String status) {
	        if (logger.isDebugEnabled()) {
	            logger.debug(" WorkplaceDAOImpl : selectWorkplace 시작 ");
	        }
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;

	        try {

	            StringBuffer query = new StringBuffer();
	            query.append("update workplace set APPROVAL_STATUS=? where WORKPLACE_code=?");
	            con = dataSourceTransactionManager.getConnection();
	            pstmt = con.prepareStatement(query.toString());

	            pstmt.setString(1,status);
	            pstmt.setString(2,code);

	            pstmt.executeUpdate();

	            if (logger.isDebugEnabled()) {
	                logger.debug(" WorkplaceDAOImpl : selectAccountUnRegisteredList 종료 ");
	            }
	        } catch (Exception sqle) {
	            logger.fatal(sqle.getMessage());
	            throw new DataAccessException(sqle.getMessage());
	        } finally {
	            dataSourceTransactionManager.close(pstmt, rs);
	        }
	    }
	    
	    @Override // 사업장삭제
	    public void deleteWorkplace(String code) {
	        if (logger.isDebugEnabled()) {
	            logger.debug(" WorkplaceDAOImpl : deleteWorkplace 시작 ");
	        }
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;

	        try {

	            StringBuffer query = new StringBuffer();
	            query.append("DELETE FROM WORKPLACE WHERE workplace_code=?");
	            con = dataSourceTransactionManager.getConnection();
	            pstmt = con.prepareStatement(query.toString());

	            pstmt.setString(1,code);


	            pstmt.executeUpdate();
	            
	            if (logger.isDebugEnabled()) {
	                logger.debug(" WorkplaceDAOImpl : deleteWorkplace 종료 ");
	            }
	        } catch (Exception sqle) {
	            logger.fatal(sqle.getMessage());
	            throw new DataAccessException(sqle.getMessage());
	        } finally {
	            dataSourceTransactionManager.close(pstmt, rs);
	        }
	    }

}
