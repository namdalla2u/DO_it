package net.plang.HoWooAccount.base.controller;

import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.plang.HoWooAccount.base.to.OpenApiBean;
import net.plang.HoWooAccount.base.to.OpenApiBean2;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.AbstractController;
import net.sf.json.JSONObject;

public class OpenApiController extends AbstractController {
	
    protected final Log logger = LogFactory.getLog(this.getClass());
    
	// tag값의 정보를 가져오는 메소드
	private static String getTagValue(String tag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);
		if (nValue == null)
			return null;

		return nValue.getNodeValue();
	}

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {

		if (logger.isDebugEnabled()) {
            logger.debug(" OpenApiController : OpenApiList 시작 ");
        }
		
		JSONObject json = new JSONObject();
		ArrayList<OpenApiBean2> array= new ArrayList<>(); 
		OpenApiBean2 openApiBean2 = null;
		PrintWriter out = null;
		 
		
		try {
			openApiBean2 = new OpenApiBean2();
			out = response.getWriter();
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			
			System.out.println("요청연도 : "+year);
			System.out.println("요청월 : "+month);
			
			String url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?solYear="+year+"&solMonth="+month+"&ServiceKey="
					+"Pdni0WEWWEuBXB7jeyiQ4SR13hAIYmU6XUrWJuu7VejgoJErAhJB241HQ2kHYlDCs2%2F1iKUDKGVZx64QYMmvdw%3D%3D&_type=xml";

			DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
			Document doc = dBuilder.parse(url);

			// root tag
			doc.getDocumentElement().normalize();
			// System.out.println("Root element :" +
			// doc.getDocumentElement().getNodeName()); 최상위노드 = RESPONSE

			// 파싱할 tag
			NodeList nList = doc.getElementsByTagName("item");
			Element eElement = (Element) nList.item(0);
			
			openApiBean2.setDateName(getTagValue("dateName", eElement));
			openApiBean2.setLocdate(getTagValue("locdate", eElement));
	
			array.add(openApiBean2);
			
			json.put("OpenAPI" , array);

		} catch (Exception e) {
			e.printStackTrace();
		}
		out.print(json);

		return null;
	}
}
