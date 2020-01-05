package net.plang.HoWooAccount.base.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.plang.HoWooAccount.base.serviceFacade.BaseServiceFacade;
import net.plang.HoWooAccount.base.serviceFacade.BaseServiceFacadeImpl;
import net.plang.HoWooAccount.base.to.CodeBean;
import net.plang.HoWooAccount.base.to.DetailCodeBean;
import net.plang.HoWooAccount.common.exception.DataAccessException;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.sf.json.JSONObject;

public class CodeListController extends MultiActionController {
    private BaseServiceFacade baseServiceFacade = BaseServiceFacadeImpl.getInstance();

    protected final Log logger = LogFactory.getLog(this.getClass());

    public ModelAndView getDetailCodeList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListController : getDetailCodeList 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;

        try {
            out = response.getWriter();

            HashMap<String, String> param = new HashMap<>();
            System.out.println(request.getParameter("detailCodeName"));
            param.put("divisionCodeNo", request.getParameter("divisionCodeNo"));
            if (request.getParameter("detailCodeName") != null)
                param.put("detailCodeName", request.getParameter("detailCodeName"));

            ArrayList<DetailCodeBean> detailCodeList = baseServiceFacade.getDetailCodeList(param);
            json.put("detailCodeList", detailCodeList);
            json.put("errorCode", 1);

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

    public ModelAndView findCodeList(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListController : findCodeList 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;

        try {
            out = response.getWriter();
            ArrayList<CodeBean> codeList = baseServiceFacade.findCodeList();

            json.put("codeList", codeList);
            json.put("errorCode", 1);
            json.put("errorMsg", "코드를 가져 왔습니다");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", "출력오류");
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", /* e.getMessage() */ "DB오류");
            e.printStackTrace();
        }
        out.print(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListController : findCodeList 종료 ");
        }
        return null;
    }

    public ModelAndView batchCodeProcess(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListController : batchCodeProcess 시작 ");
        }
        JSONObject json = new JSONObject();
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String list = request.getParameter("batchList");
            String list2 = request.getParameter("batchList2");
            // String list3 = request.getParameter("batchlist3");
            // System.out.println(list3);
            ObjectMapper mapper = new ObjectMapper();

            ArrayList<CodeBean> codeList = mapper.readValue(list, new TypeReference<ArrayList<CodeBean>>() {
            });

            ArrayList<DetailCodeBean> codeList2 = mapper.readValue(list2,
                    new TypeReference<ArrayList<DetailCodeBean>>() {
                    });

            // ArrayList<CodeBean> codeList3 = mapper.readValue(list, newTypeReference<ArrayList<CodeBean>>(){});
            // System.out.println("@@@@"+codeList3);

            // JSONArray jsonArrayList = JSONArray.fromObject(list3);
            /*
             * for(int a=0;a<jsonArrayList.size();a++){ System.out.println(a); Object[]
             * key=jsonArrayList.getJSONObject(a).keySet().toArray(); for(Object k:key) {
             * System.out.println(k);
             *
             * ArrayList<DetailCodeBean> detailcode=new ArrayList<DetailCodeBean>();
             * CodeBean codebean=new CodeBean();
             *
             * if(k.toString().equals("codeDetailObj")) { System.out.println("b");
             * JSONArray json4=(JSONArray)jsonArrayList.getJSONObject(a).get(k);
             * System.out.println(json4.size()); System.out.println("***"+json4.get(0));
             *
             * for(int b=0; b<json4.size(); b++) {
             *
             * JSONObject json5=(JSONObject)json4.getJSONObject(b);
             * System.out.println("뭉"+json5); DetailCodeBean bean =
             * (DetailCodeBean)JSONObject.toBean(json5 ,DetailCodeBean.class);
             * detailcode.add(bean); System.out.println(bean.getDivisionCode()); } }else {
             *
             * codebean.setCodeName((String)jsonArrayList.getJSONObject(a).get(k));
             * System.out.println(codebean.getCodeName()); }
             * codebean.setCodeDetailList(detailcode); }
             *
             *
             *
             * }
             *
             *
             */


            baseServiceFacade.batchCodeProcess(codeList, codeList2);

            json.put("errorCode", 1);
            json.put("errorMsg", "데이터를 가져 왔습니다");
        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -1);
            json.put("errorMsg", "출력오류");
            e.printStackTrace();
        } catch (DataAccessException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());
            e.printStackTrace();
        }
        out.println(json);
        out.close();
        if (logger.isDebugEnabled()) {
            logger.debug(" CodeListController : batchCodeProcess 종료 ");
        }
        return null;
    }

}
