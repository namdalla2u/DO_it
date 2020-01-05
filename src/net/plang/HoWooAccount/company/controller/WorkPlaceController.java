package net.plang.HoWooAccount.company.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.plang.HoWooAccount.common.util.BeanCreator;
import net.plang.HoWooAccount.company.serviceFacade.WorkplaceServiceFacade;
import net.plang.HoWooAccount.company.serviceFacade.WorkplaceServiceFacadeImpl;
import net.plang.HoWooAccount.company.to.WorkplaceBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WorkPlaceController extends MultiActionController {
	
    private WorkplaceServiceFacade workplaceServiceFacade = WorkplaceServiceFacadeImpl.getInstance();

    protected final Log logger = LogFactory.getLog(this.getClass());
    //사업장등록
    public ModelAndView workPlaceAdd(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" CompanyController : workPlaceAdd 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;

        try {
            out = response.getWriter();

            JSONObject workplaceAddItems = JSONObject.fromObject(request.getParameter("workplaceAddItems"));
            WorkplaceBean workplaceBean = BeanCreator.getInstance().create(workplaceAddItems, WorkplaceBean.class);
            
            workplaceServiceFacade.workplaceAdd(workplaceBean); //insert
            json.put("errorCode", 0);
            json.put("errorMsg","회사등록완료");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
        }
        out.print(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListController : getDetailCodeList 종료 ");
        }
        return null;
    }
    //회사삭제
    public void eliminationWorkplace(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" CompanyController : eliminationWorkplace 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        ArrayList<String> getCodes=null;
        try {
        	getCodes=new ArrayList<>();
            out = response.getWriter();
            
            String codes=request.getParameter("codes");
		
			JSONArray jsonArray=JSONArray.fromObject(codes);
			for(Object obj :jsonArray) {
				String code=(String)obj;
				getCodes.add(code);
			}
            
            workplaceServiceFacade.eliminationWorkplace(getCodes); //delete
            json.put("errorCode", 0);
            json.put("errorMsg","회사삭제완료");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
        }
        out.print(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListController : eliminationWorkplace 종료 ");
        }
    }
    
    //사업장 1개 조회
    public ModelAndView getWorkplace(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" CompanyController : getWorkplace 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        WorkplaceBean workplaceBean= null;
        try {
            out = response.getWriter();
            workplaceBean = new WorkplaceBean();
            WorkplaceBean emptyWorkplaceBean = new WorkplaceBean();
            
            String workplaceCode=request.getParameter("workplaceCode");
            workplaceBean=workplaceServiceFacade.getWorkplace(workplaceCode);
            
            json.put("workplaceBean", workplaceBean);
            json.put("emptyWorkplaceBean", emptyWorkplaceBean);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        out.print(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListController : getDetailCodeList 종료 ");
        }
        return null;
    }
    
	//모든사업장조회
	public ArrayList<WorkplaceBean> getAllWorkplaceList(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug(" AccountUnregisteredList : AccountUnRegisteredList 시작 ");
		}
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		ArrayList<WorkplaceBean> allWorkplaceList = null;
		try {

			out = response.getWriter();

			allWorkplaceList = new ArrayList<>();
			allWorkplaceList = workplaceServiceFacade.getAllWorkplaceList();
			
			json.put("allWorkplaceList",allWorkplaceList);
			json.put("errorCode",0);
			
		} catch (IOException e) {

			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			logger.fatal(e.getMessage());
			json.put("errorCode", -1);
			json.put("errorMsg", e.getMessage());
			e.printStackTrace();
		} catch (DataAccessException e) {
			System.out.println("에러"+e.getMessage());
			logger.fatal(e.getMessage());
			json.put("errorCode", -2);
			json.put("errorMsg", e.getMessage());
			e.printStackTrace();
		}
		out.print(json);
		out.close();
		if (logger.isDebugEnabled()) {
			logger.debug(" AccountUnregisteredList : AccountUnRegisteredList 종료 ");
		}
		return null;

	}
	
	// 승인상태 업뎃
	public void updateApprovalStatus(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug(" WorkPlaceController : updateApprovalStatus 시작 ");
		}
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		ArrayList<String> getCodes=null;
		try {
			getCodes=new ArrayList<>();
			out = response.getWriter();

			String status=request.getParameter("status");
			String codes=request.getParameter("codes");
			
			JSONArray jsonArray=JSONArray.fromObject(codes);
			for(Object obj :jsonArray) {
				String code=(String)obj;
				getCodes.add(code);
			}
	
			workplaceServiceFacade.updateApprovalStatus(getCodes,status);

			json.put("errorCode",0);
			json.put("errorMsg","거래처변경완료");
			
		} catch (IOException e) {

			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			logger.fatal(e.getMessage());
			json.put("errorCode", -1);
			json.put("errorMsg", e.getMessage());
			e.printStackTrace();
		} catch (DataAccessException e) {
			System.out.println("에러"+e.getMessage());
			logger.fatal(e.getMessage());
			json.put("errorCode", -2);
			json.put("errorMsg", e.getMessage());
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		out.print(json);
		out.close();
		if (logger.isDebugEnabled()) {
			logger.debug(" WorkPlaceController : updateApprovalStatus 종료 ");
		}

	}
}
