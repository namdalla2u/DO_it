package net.plang.HoWooAccount.common.controller;

import net.plang.HoWooAccount.common.servlet.ModelAndView;
import net.plang.HoWooAccount.common.servlet.controller.AbstractController;
import net.plang.HoWooAccount.common.util.FileUploader;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ImgFileController extends AbstractController {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : getAccount 시작 ");
        }

        JSONObject json = new JSONObject();
        try {
            // Multipart Contnet 인지 확인
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                json.put("errorCode", -1);
                json.put("errorMsg", "정상적인 경로로 접근해주세요.");

                return null;
            }

            // Create a factory for disk-based file items
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();

            // Create a new file upload handler
            ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);

            String empCode = null;
            String imgUrl = null;

            List<FileItem> fileItemList = servletFileUpload.parseRequest(request);

            for (FileItem fileItem : fileItemList) {
                if (!fileItem.isFormField()) {
//                    if ("empCode".equals(fileItem.getFieldName())) {
//                        empCode = fileItem.getString();
//                        System.out.println(fileItem.getFieldName() + "=" + fileItem.getString());
//                    }
//                } else {
                    String fileName = fileItem.getName();
                    System.out.println(fileItem.getFieldName() + "=" + fileItem.getName());
                    if ((fileName != null) && (fileItem.getSize() > 0)) {
                        imgUrl = FileUploader.doFileUpload(fileItem, empCode);
                    }
                }
            }
            json.put("url", imgUrl);
            json.put("errorCode", 1);
            json.put("errorMsg", "저장성공");

        } catch (IOException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -2);
            json.put("errorMsg", e.getMessage());

        } catch (FileUploadException e) {
            logger.fatal(e.getMessage());
            json.put("errorCode", -3);
            json.put("errorMsg", e.getMessage());

        } finally {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug(" AccountController : getAccount 종료 ");
        }

        return null;
    }

}
