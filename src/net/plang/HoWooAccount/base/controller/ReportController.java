package net.plang.HoWooAccount.base.controller;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;

import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.plang.HoWooAccount.base.serviceFacade.BaseServiceFacade;
import net.plang.HoWooAccount.base.serviceFacade.BaseServiceFacadeImpl;
import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.MultiActionController;
import net.plang.HoWooAccount.common.sl.ServiceLocator;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class ReportController extends MultiActionController {
	private BaseServiceFacade baseServiceFacade = BaseServiceFacadeImpl.getInstance();

    protected final Log logger = LogFactory.getLog(this.getClass());

    private ModelAndView modelAndView = null;

    public ModelAndView FinancialPosition(HttpServletRequest request, HttpServletResponse response) {
    	System.out.println("전표 아이리포트 시작");
    	//SMTPAppender log4j
        if (logger.isDebugEnabled()) {
            logger.debug(" ReportController : FinancialPosition 시작 ");
        }
        HashMap<String, Object> parameters = new HashMap<>();
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        try {
            String slipNo = request.getParameter("slipNo");
            parameters.put("slip_no", slipNo);
            
            baseServiceFacade.getIreportData(slipNo);
            
            DataSource dataSource = ServiceLocator.getInstance().getDataSource("jdbc/ac2");
            Connection conn = dataSource.getConnection();
            
            String path = "/resources/reportform/report11.jasper";
            String rPath = request.getServletContext().getRealPath(path);
            System.out.println(rPath);
            
                InputStream inputStream = new FileInputStream(rPath);
                
                JasperReport jasperReport =(JasperReport)JRLoader.loadObject(inputStream);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, conn);
                ServletOutputStream out = response.getOutputStream();
                response.setContentType("application/pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                out.println();
                out.flush();

        } catch (Exception error) {
            logger.fatal(error.getMessage());
            error.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug(" ReportController : FinancialPosition 종료 ");
        }
        return modelAndView;
    }
}
