package net.plang.HoWooAccount.company.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.plang.HoWooAccount.company.serviceFacade.BusinessServiceFacade;
import net.plang.HoWooAccount.company.serviceFacade.BusinessServiceFacadeImpl;
import net.plang.HoWooAccount.company.to.BusinessBean;
import net.plang.HoWooAccount.company.to.DetailBusinessBean;
import net.sf.json.JSONObject;

public class BusinessController extends MultiActionController {

	private BusinessServiceFacade businessServiceFacade = BusinessServiceFacadeImpl.getInstance();

	protected final Log logger = LogFactory.getLog(this.getClass());

	// 업태리스트조회
	public ArrayList<BusinessBean> getBusinessList(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug(" BusinessListController : getBusinessList 시작 ");
		}
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		ArrayList<BusinessBean> businessList = null;
		try {
			out = response.getWriter();

			businessList = new ArrayList<>();

			businessList = businessServiceFacade.getBusinessList();
		
			json.put("businessList",businessList);
		} catch (IOException e) {
			System.out.println(e.getMessage());
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
	
	// 자식 업태 조회
	public ArrayList<DetailBusinessBean> getDetailBusiness(HttpServletRequest request, HttpServletResponse response) {
		if (logger.isDebugEnabled()) {
			logger.debug(" BusinessListController : getBusinessList 시작 ");
		}
		JSONObject json = new JSONObject();
		PrintWriter out = null;
		ArrayList<DetailBusinessBean> detailBusinessList = null;
		try {
			out = response.getWriter();
			String businessCode=request.getParameter("businessCode");
			detailBusinessList = new ArrayList<>();

			detailBusinessList = businessServiceFacade.getDetailBusiness(businessCode);

			json.put("detailBusinessList",detailBusinessList);
			json.put("errorCode", 0);
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

}
